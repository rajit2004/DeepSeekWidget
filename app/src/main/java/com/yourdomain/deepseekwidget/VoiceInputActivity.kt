package com.yourdomain.deepseekwidget

import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.yourdomain.deepseekwidget.Constants.DEEPSEEK_PACKAGE
import com.yourdomain.deepseekwidget.Constants.EXTRA_LAUNCH_CAMERA
import com.yourdomain.deepseekwidget.Constants.EXTRA_LAUNCH_VOICE
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Transparent trampoline [AppCompatActivity] that routes widget taps to the native DeepSeek app.
 *
 * This activity acts as a high-performance router to launch DeepSeek's internal
 * features (Camera, Voice, Chat) directly or via system capture-and-share flows.
 */
class VoiceInputActivity : AppCompatActivity() {

    private var currentPhotoPath: String? = null
    private lateinit var promptStore: PromptStore

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openCamera()
            } else {
                Toast.makeText(this, R.string.perm_camera_denied, Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    private val audioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                launchSpeechRecognizer()
            } else {
                Toast.makeText(this, R.string.perm_audio_denied, Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                currentPhotoPath?.let { path ->
                    val file = File(path)
                    val uri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.fileprovider",
                        file
                    )
                    shareToDeepSeek(uri, "image/jpeg")
                } ?: finish()
            } else {
                deleteCurrentPhoto()
                finish()
            }
        }

    private val recognizeSpeechLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val spokenText = result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.firstOrNull()
                if (spokenText != null) {
                    shareTextToDeepSeek(spokenText)
                } else {
                    finish()
                }
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        promptStore = PromptStore(this)
        currentPhotoPath = savedInstanceState?.getString(Constants.KEY_PHOTO_PATH)

        val launchCamera = intent.getBooleanExtra(EXTRA_LAUNCH_CAMERA, false)
        val launchVoice = intent.getBooleanExtra(EXTRA_LAUNCH_VOICE, false)

        when {
            launchCamera -> startCameraFlow()
            launchVoice  -> startVoiceFlow()
            else         -> routeToDeepSeekNative("chat")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.KEY_PHOTO_PATH, currentPhotoPath)
    }

    private fun startCameraFlow() {
        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            cameraPermissionLauncher.launch(CAMERA)
        }
    }

    private fun openCamera() {
        val photoFile = try {
            createImageFile()
        } catch (ex: Exception) {
            Log.e(TAG, "Error creating image file", ex)
            Toast.makeText(this, R.string.camera_file_error, Toast.LENGTH_SHORT).show()
            null
        }

        photoFile ?: return

        val photoURI = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }

        try {
            NotificationHelper.showProcessingNotification(this, getString(R.string.notification_camera_processing))
            window.decorView.announceForAccessibility(getString(R.string.a11y_camera_capturing))
            captureImageLauncher.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.camera_unavailable, Toast.LENGTH_SHORT).show()
            deleteCurrentPhoto()
            finish()
        }
    }

    private fun startVoiceFlow() {
        if (checkSelfPermission(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            launchSpeechRecognizer()
        } else {
            audioPermissionLauncher.launch(RECORD_AUDIO)
        }
    }

    private fun launchSpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_prompt))
        }
        try {
            NotificationHelper.showProcessingNotification(this, getString(R.string.notification_voice_processing))
            window.decorView.announceForAccessibility(getString(R.string.a11y_voice_listening))
            recognizeSpeechLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.voice_unavailable, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun shareToDeepSeek(contentUri: Uri, mimeType: String) {
        window.decorView.announceForAccessibility(getString(R.string.a11y_sending_to_deepseek))
        promptStore.savePrompt("[Photo shared]")
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to share to DeepSeek", e)
            Toast.makeText(this, R.string.image_share_error, Toast.LENGTH_SHORT).show()
        } finally {
            NotificationHelper.dismissNotification(this)
            deleteCurrentPhoto()
            finish()
        }
    }

    private fun shareTextToDeepSeek(text: String) {
        window.decorView.announceForAccessibility(getString(R.string.a11y_sending_to_deepseek))
        promptStore.savePrompt(text)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to share text to DeepSeek", e)
            Toast.makeText(this, R.string.deepseek_open_error, Toast.LENGTH_SHORT).show()
        } finally {
            NotificationHelper.dismissNotification(this)
            finish()
        }
    }

    private fun deleteCurrentPhoto() {
        currentPhotoPath?.let { path ->
            try {
                File(path).delete()
            } catch (e: Exception) {
                Log.w(TAG, "Failed to delete temp photo: $path", e)
            }
            currentPhotoPath = null
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(null) ?: filesDir
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    /**
     * Routes the user to a specific feature within the DeepSeek app.
     * Uses a combination of custom URI schemes and Package Manager launch intents.
     */
    private fun routeToDeepSeekNative(feature: String) {
        window.decorView.announceForAccessibility(getString(R.string.a11y_opening_deepseek))
        val uri = when (feature) {
            "camera" -> Uri.parse("https://chat.deepseek.com/chat?action=camera")
            "voice"  -> Uri.parse("https://chat.deepseek.com/chat?action=voice")
            else     -> Uri.parse(Constants.DEEPSEEK_WEB_URL)
        }

        try {
            val launchIntent = packageManager.getLaunchIntentForPackage(DEEPSEEK_PACKAGE)

            if (launchIntent != null) {
                val actionIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage(DEEPSEEK_PACKAGE)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }

                try {
                    startActivity(actionIntent)
                } catch (e: ActivityNotFoundException) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(launchIntent)
                }
            } else {
                launchWebFallback(uri)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Routing to DeepSeek failed", e)
            launchWebFallback(uri)
        } finally {
            NotificationHelper.dismissNotification(this)
            finish()
        }
    }

    private fun launchWebFallback(uri: Uri) {
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(webIntent)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.deepseek_open_error, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "VoiceInputActivity"
    }
}
