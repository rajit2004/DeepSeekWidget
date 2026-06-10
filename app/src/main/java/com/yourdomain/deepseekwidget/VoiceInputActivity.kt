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

/**
 * Transparent trampoline [Activity] that routes widget taps to the native DeepSeek app.
 *
 * This activity acts as a high-performance router to launch DeepSeek's internal
 * features (Camera, Voice, Chat) directly via Intent schemes.
 */
class VoiceInputActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launchCamera = intent.getBooleanExtra(EXTRA_LAUNCH_CAMERA, false)
        val launchVoice = intent.getBooleanExtra(EXTRA_LAUNCH_VOICE, false)
        val skipVoice = intent.getBooleanExtra(EXTRA_SKIP_VOICE, false)

        when {
            launchCamera -> routeToDeepSeekNative("camera")
            launchVoice  -> routeToDeepSeekNative("voice")
            skipVoice    -> routeToDeepSeekNative("chat")
            else         -> routeToDeepSeekNative("chat")
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
    }
}