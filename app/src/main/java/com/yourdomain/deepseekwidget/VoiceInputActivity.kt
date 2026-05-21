package com.yourdomain.deepseekwidget

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.RecognizerIntent
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VoiceInputActivity : Activity() {

    private companion object {
        const val REQUEST_SPEECH = 100
        const val REQUEST_CAMERA = 101
        const val DEEPSEEK_PACKAGE = "com.deepseek.chat"
    }

    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val skipVoice = intent.getBooleanExtra("SKIP_VOICE", false)
        val launchCamera = intent.getBooleanExtra("LAUNCH_CAMERA", false)

        when {
            launchCamera -> dispatchTakePictureIntent()
            skipVoice -> openDeepSeek(null)
            else -> startVoiceRecognition()
        }
    }

    private fun startVoiceRecognition() {
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speaking to DeepSeek...")
        }
        try {
            startActivityForResult(speechIntent, REQUEST_SPEECH)
        } catch (e: Exception) {
            openDeepSeek(null)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: Exception) {
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.yourdomain.deepseekwidget.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                // Grant permission to all apps that can handle the intent
                val resInfoList = packageManager.queryIntentActivities(takePictureIntent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivityForResult(takePictureIntent, REQUEST_CAMERA)
            }
        } else {
            openDeepSeek(null)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SPEECH -> {
                if (resultCode == RESULT_OK && data != null) {
                    val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenText = matches?.firstOrNull() ?: ""
                    openDeepSeek(spokenText)
                } else {
                    finish()
                }
            }
            REQUEST_CAMERA -> {
                if (resultCode == RESULT_OK) {
                    shareToDeepSeek(isImage = true, content = currentPhotoPath)
                } else {
                    finish()
                }
            }
            else -> finish()
        }
    }

    private fun openDeepSeek(query: String?) {
        if (!query.isNullOrBlank()) {
            shareToDeepSeek(isImage = false, content = query)
        } else {
            try {
                val launchIntent = packageManager.getLaunchIntentForPackage(DEEPSEEK_PACKAGE)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(launchIntent)
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.deepseek.com")))
                }
            } catch (e: Exception) {
                // ignore
            }
            finish()
        }
    }

    private fun shareToDeepSeek(isImage: Boolean, content: String?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            setPackage(DEEPSEEK_PACKAGE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (isImage) {
            val f = File(content ?: return)
            val uri = FileProvider.getUriForFile(this, "com.yourdomain.deepseekwidget.fileprovider", f)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, content)
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback: if DeepSeek doesn't handle ACTION_SEND well, try VIEW with query for text
            if (!isImage && content != null) {
                try {
                    val uri = Uri.parse("https://chat.deepseek.com/?query=${Uri.encode(content)}")
                    startActivity(Intent(Intent.ACTION_VIEW, uri).apply {
                        setPackage(DEEPSEEK_PACKAGE)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                } catch (e2: Exception) {
                    // Final fallback to web
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.deepseek.com/?query=${Uri.encode(content)}")))
                }
            }
        }
        finish()
    }
}