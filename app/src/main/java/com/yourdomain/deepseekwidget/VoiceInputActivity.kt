package com.yourdomain.deepseekwidget

import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.yourdomain.deepseekwidget.Constants.DEEPSEEK_PACKAGE
import com.yourdomain.deepseekwidget.Constants.FILE_PROVIDER_AUTHORITY
import com.yourdomain.deepseekwidget.Constants.DEEPSEEK_WEB_URL
import com.yourdomain.deepseekwidget.Constants.EXTRA_LAUNCH_CAMERA
import com.yourdomain.deepseekwidget.Constants.EXTRA_SKIP_VOICE
import com.yourdomain.deepseekwidget.Constants.KEY_PHOTO_PATH
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Transparent trampoline [AppCompatActivity] launched by widget tap targets.
 *
 * Handles three flows:
 *  1. **Open** (`EXTRA_SKIP_VOICE = true`) — launches DeepSeek directly.
 *  2. **Voice** (default) — requests [RECORD_AUDIO], fires speech recognizer,
 *     forwards transcript to DeepSeek.
 *  3. **Camera** (`EXTRA_LAUNCH_CAMERA = true`) — requests [CAMERA], captures
 *     image via [FileProvider], shares to DeepSeek.
 *
 * Extends [AppCompatActivity] so [ActivityResultContracts] and [ContextCompat]
 * work correctly. The manifest applies [R.style.Theme_DeepSeekWidget_Transparent]
 * which is an AppCompat-compatible transparent theme.
 */
class VoiceInputActivity : AppCompatActivity() {

    // ── State ────────────────────────────────────────────────────────────────

    /** Absolute path of the image file created for camera capture. Persisted across process death. */
    private var currentPhotoPath: String? = null

    // ── Activity-result launchers ─────────────────────────────────────────────
    // Must be registered as properties (before onCreate) per ActivityResultContracts contract.

    private val voiceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ::onVoiceResult
    )

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ::onCameraResult
    )

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) dispatchTakePictureIntent()
        else {
            showToast(R.string.perm_camera_denied)
            finish()
        }
    }

    private val audioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startVoiceRecognition()
        else {
            showToast(R.string.perm_audio_denied)
            finish()
        }
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore photo path in case the process was killed while the camera was open.
        currentPhotoPath = savedInstanceState?.getString(KEY_PHOTO_PATH)

        when {
            intent.getBooleanExtra(EXTRA_LAUNCH_CAMERA, false) -> checkCameraPermission()
            intent.getBooleanExtra(EXTRA_SKIP_VOICE, false)    -> openDeepSeek(query = null)
            else                                                -> checkAudioPermission()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentPhotoPath?.let { outState.putString(KEY_PHOTO_PATH, it) }
    }

    // ── Permission gates ──────────────────────────────────────────────────────

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED)
            dispatchTakePictureIntent()
        else
            cameraPermissionLauncher.launch(CAMERA)
    }

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            startVoiceRecognition()
        else
            audioPermissionLauncher.launch(RECORD_AUDIO)
    }

    // ── Voice recognition ─────────────────────────────────────────────────────

    private fun startVoiceRecognition() {
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_prompt))
        }
        if (speechIntent.resolveActivity(packageManager) == null) {
            Log.w(TAG, "No speech recognizer available on this device")
            showToast(R.string.voice_unavailable)
            openDeepSeek(query = null)
            return
        }
        try {
            voiceLauncher.launch(speechIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Speech recognizer not found at launch time", e)
            showToast(R.string.voice_unavailable)
            openDeepSeek(query = null)
        }
    }

    private fun onVoiceResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            openDeepSeek(query = matches?.firstOrNull().orEmpty())
        } else {
            finish()
        }
    }

    // ── Camera capture ────────────────────────────────────────────────────────

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Verify a camera app exists before building the FileProvider URI.
        val resolvedCameraActivity = takePictureIntent.resolveActivity(packageManager)
        if (resolvedCameraActivity == null) {
            Log.w(TAG, "No camera app available on this device")
            showToast(R.string.camera_unavailable)
            finish()
            return
        }

        val photoFile: File? = try {
            createImageFile()
        } catch (e: IOException) {
            Log.e(TAG, "Failed to create temp image file", e)
            null
        }

        if (photoFile == null) {
            // Guard: no file = no camera launch; finish so transparent screen does not hang.
            showToast(R.string.camera_file_error)
            finish()
            return
        }

        val photoUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        // Grant URI permission only to the resolved camera activity (not every app).
        grantUriPermission(
            resolvedCameraActivity.packageName,
            photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        try {
            cameraLauncher.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Camera launch failed", e)
            showToast(R.string.camera_unavailable)
            finish()
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun onCameraResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            shareToDeepSeek(isImage = true, content = currentPhotoPath)
        } else {
            // Clean up the pre-created file if the user canceled.
            deleteTempFile()
            finish()
        }
    }

    // ── DeepSeek launch / share ───────────────────────────────────────────────

    /**
     * Opens the DeepSeek app (or web fallback if not installed).
     * If [query] is non-blank, attempts to share the text via [Intent.ACTION_SEND].
     */
    private fun openDeepSeek(query: String?) {
        if (!query.isNullOrBlank()) {
            shareToDeepSeek(isImage = false, content = query)
            return
        }
        try {
            val launchIntent = packageManager.getLaunchIntentForPackage(DEEPSEEK_PACKAGE)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(launchIntent)
            } else {
                // App not installed — open web chat.
                startActivity(
                    Intent(Intent.ACTION_VIEW, DEEPSEEK_WEB_URL.toUri()).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to open DeepSeek or browser", e)
            showToast(R.string.deepseek_open_error)
        }
        finish()
    }

    /**
     * Shares text or an image to DeepSeek via [Intent.ACTION_SEND].
     * Falls back to opening the web chat on failure (text-only; image sharing
     * cannot be done without the native app).
     */
    private fun shareToDeepSeek(isImage: Boolean, content: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (isImage) {
            if (content == null || !File(content).exists()) {
                Log.e(TAG, "Image file missing: $content")
                showToast(R.string.image_file_error)
                finish()
                return
            }
            val uri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, File(content))
            shareIntent.type = "image/jpeg"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        }

        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "DeepSeek app not found or does not handle ACTION_SEND", e)
            if (!isImage) {
                // Text fallback: open web chat. Pre-filling the query is not officially
                // supported by DeepSeek web — user will need to paste manually.
                try {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, DEEPSEEK_WEB_URL.toUri()).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    )
                    showToast(R.string.deepseek_web_fallback)
                } catch (e2: ActivityNotFoundException) {
                    Log.e(TAG, "No browser available for web fallback", e2)
                    showToast(R.string.deepseek_open_error)
                }
            } else {
                showToast(R.string.image_share_error)
            }
        } finally {
            // Always delete the temp image after sharing attempt to avoid storage bloat.
            if (isImage) deleteTempFile()
        }
        finish()
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun deleteTempFile() {
        currentPhotoPath?.let { path ->
            try {
                File(path).delete()
            } catch (e: SecurityException) {
                Log.w(TAG, "Could not delete temp file: $path", e)
            }
            currentPhotoPath = null
        }
    }

    private fun showToast(resId: Int) =
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

    companion object {
        private const val TAG = "VoiceInputActivity"
    }
}