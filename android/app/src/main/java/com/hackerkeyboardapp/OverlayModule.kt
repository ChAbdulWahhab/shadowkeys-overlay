package com.hackerkeyboardapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

class OverlayModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "OverlayModule"

    @ReactMethod
    fun canDrawOverlays(promise: Promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            promise.resolve(Settings.canDrawOverlays(reactApplicationContext))
        } else {
            promise.resolve(true)
        }
    }

    @ReactMethod
    fun openOverlaySettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${reactApplicationContext.packageName}")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            reactApplicationContext.startActivity(intent)
        }
    }

    @ReactMethod
    fun startService() {
        val intent = Intent(reactApplicationContext, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            reactApplicationContext.startForegroundService(intent)
        } else {
            reactApplicationContext.startService(intent)
        }
    }

    @ReactMethod
    fun stopService() {
        val intent = Intent(reactApplicationContext, OverlayService::class.java)
        reactApplicationContext.stopService(intent)
    }

    @ReactMethod
    fun isAccessibilityServiceEnabled(promise: Promise) {
        val service = "${reactApplicationContext.packageName}/${KeyboardAccessibilityService::class.java.canonicalName}"
        val enabled = try {
            val settingValue = Settings.Secure.getString(
                reactApplicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            settingValue?.contains(service) == true
        } catch (e: Exception) {
            false
        }
        promise.resolve(enabled)
    }

    @ReactMethod
    fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reactApplicationContext.startActivity(intent)
    }

    @ReactMethod
    fun sendInput(text: String?, command: String?) {
        val intent = Intent("com.hackerkeyboardapp.INJECT_INPUT")
        intent.putExtra("text", text)
        intent.putExtra("command", command)
        reactApplicationContext.sendBroadcast(intent)
    }

    @ReactMethod
    fun isKeyboardEnabled(promise: Promise) {
        val imm = reactApplicationContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val enabledMethods = imm.enabledInputMethodList
        val isEnabled = enabledMethods.any { it.packageName == reactApplicationContext.packageName }
        promise.resolve(isEnabled)
    }

    @ReactMethod
    fun openKeyboardSettings() {
        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reactApplicationContext.startActivity(intent)
    }

    @ReactMethod
    fun isServiceRunning(promise: Promise) {
        val manager = reactApplicationContext.getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val isRunning = manager.getRunningServices(Integer.MAX_VALUE).any { 
            it.service.className == OverlayService::class.java.name 
        }
        promise.resolve(isRunning)
    }

    @ReactMethod
    fun toggleKeyboard() {
        val intent = Intent("com.hackerkeyboardapp.TOGGLE_KEYBOARD")
        reactApplicationContext.sendBroadcast(intent)
    }
}
