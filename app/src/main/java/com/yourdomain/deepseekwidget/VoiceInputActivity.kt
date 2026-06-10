package com.yourdomain.deepseekwidget

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yourdomain.deepseekwidget.Constants.DEEPSEEK_PACKAGE
import com.yourdomain.deepseekwidget.Constants.DEEPSEEK_WEB_URL
import com.yourdomain.deepseekwidget.Constants.EXTRA_LAUNCH_CAMERA
import com.yourdomain.deepseekwidget.Constants.EXTRA_LAUNCH_VOICE
import com.yourdomain.deepseekwidget.Constants.EXTRA_SKIP_VOICE

import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.speech.RecognizerIntent
import androidx.core.content.FileProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore photo path if we're coming back from a process death
        currentPhotoPath = savedInstanceState?.getString(Constants.KEY_PHOTO_PATH)

        val launchCamera = intent.getBooleanExtra(EXTRA_LAUNCH_CAMERA, false)
        val launchVoice = intent.getBooleanExtra(EXTRA_LAUNCH_VOICE, false)
        val skipVoice = intent.getBooleanExtra(EXTRA_SKIP_VOICE, false)

        when {
            launchCamera -> startCameraFlow()
            launchVoice  -> startVoiceFlow()
            skipVoice    -> routeToDeepSeekNative("chat")
            else         -> routeToDeepSeekNative("chat")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.KEY_PHOTO_PATH, currentPhotoPath)
    }

    private fun startCameraFlow() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: Exception) {
                Log.e(TAG, "Error creating image file", ex)
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun startVoiceFlow() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to DeepSeek")
        }
        try {
            startActivityForResult(intent, REQUEST_VOICE_RECOGNIZE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Voice recognition not supported", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    currentPhotoPath?.let { path ->
                        val file = File(path)
                        val uri = FileProvider.getUriForFile(
                            this,
                            "${packageName}.fileprovider",
                            file
                        )
                        shareToDeepSeek(uri, "image/jpeg")
                    } ?: finish()
                }
                REQUEST_VOICE_RECOGNIZE -> {
                    val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenText = results?.get(0)
                    if (spokenText != null) {
                        shareTextToDeepSeek(spokenText)
                    } else {
                        finish()
                    }
                }
            }
        } else {
            finish()
        }
    }

    private fun shareToDeepSeek(contentUri: Uri, mimeType: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(shareIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share to DeepSeek", e)
            Toast.makeText(this, "DeepSeek app not found", Toast.LENGTH_SHORT).show()
        } finally {
            finish()
        }
    }

    private fun shareTextToDeepSeek(text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(shareIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share text to DeepSeek", e)
            Toast.makeText(this, "DeepSeek app not found", Toast.LENGTH_SHORT).show()
        } finally {
            finish()
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(null)
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
        // DeepSeek app package ID
        val packageId = DEEPSEEK_PACKAGE

        // Define feature-specific URIs.
        // DeepSeek app registers for chat.deepseek.com as a verified host.
        val uri = when (feature) {
            "camera" -> Uri.parse("https://chat.deepseek.com/chat?action=camera")
            "voice"  -> Uri.parse("https://chat.deepseek.com/chat?action=voice")
            else     -> Uri.parse("https://chat.deepseek.com")
        }

        try {
            // 1. Attempt to find the launch intent for the package.
            val launchIntent = packageManager.getLaunchIntentForPackage(packageId)

            if (launchIntent != null) {
                // 2. Create a specific VIEW intent for the feature.
                val actionIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage(packageId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }

                try {
                    startActivity(actionIntent)
                } catch (e: ActivityNotFoundException) {
                    // 3. If deep link action fails, launch the app's main entry point.
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(launchIntent)
                }
            } else {
                // 4. Fallback to Web if app is not installed.
                launchWebFallback(uri)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Routing to DeepSeek failed", e)
            launchWebFallback(uri)
        } finally {
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
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val REQUEST_VOICE_RECOGNIZE = 1002
    }
}