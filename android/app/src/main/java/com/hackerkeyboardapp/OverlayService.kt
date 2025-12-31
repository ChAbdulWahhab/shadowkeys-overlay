package com.hackerkeyboardapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingButton: FrameLayout
    private lateinit var keyboardOverlay: ReactRootView
    private lateinit var buttonParams: WindowManager.LayoutParams
    private lateinit var keyboardParams: WindowManager.LayoutParams
    private var isExpanded = false

    override fun onBind(intent: Intent?): IBinder? = null

    private val toggleReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
            toggleKeyboard()
        }
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createFloatingButton()
        createKeyboardOverlay()
        
        val filter = android.content.IntentFilter("com.hackerkeyboardapp.TOGGLE_KEYBOARD")
        registerReceiver(toggleReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "OverlayServiceChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Overlay Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Shadow Keys")
            .setContentText("Overlay button is active")
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    private fun createFloatingButton() {
        floatingButton = FrameLayout(this)
        val button = View(this)
        button.background = getDrawable(R.drawable.assistive_touch_bg)
        
        val innerCircle = View(this)
        innerCircle.background = getDrawable(R.drawable.assistive_touch_bg)
        innerCircle.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#4DFFFFFF"))

        val buttonSize = 130 // Slightly smaller for premium feel
        val layoutParams = FrameLayout.LayoutParams(buttonSize, buttonSize)
        floatingButton.addView(button, layoutParams)
        
        val innerSize = 60
        val innerParams = FrameLayout.LayoutParams(innerSize, innerSize, Gravity.CENTER)
        floatingButton.addView(innerCircle, innerParams)

        buttonParams = WindowManager.LayoutParams(
            buttonSize,
            buttonSize,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )

        buttonParams.gravity = Gravity.TOP or Gravity.START
        buttonParams.x = 20
        buttonParams.y = 200

        floatingButton.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var isDragging = false

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = buttonParams.x
                        initialY = buttonParams.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDragging = false
                        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start()
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - initialTouchX).toInt()
                        val dy = (event.rawY - initialTouchY).toInt()
                        
                        if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                            isDragging = true
                            buttonParams.x = initialX + dx
                            buttonParams.y = initialY + dy
                            windowManager.updateViewLayout(floatingButton, buttonParams)
                        }
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                        if (!isDragging) {
                            toggleKeyboard()
                        }
                        return true
                    }
                }
                return false
            }
        })

        windowManager.addView(floatingButton, buttonParams)
    }

    private fun createKeyboardOverlay() {
        val reactInstanceManager = (application as MainApplication).reactNativeHost.reactInstanceManager
        keyboardOverlay = ReactRootView(this)
        
        // Pass initial props if needed
        val initialProps = Bundle()
        keyboardOverlay.startReactApplication(reactInstanceManager, "OverlayKeyboard", initialProps)

        keyboardParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or 
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        keyboardParams.gravity = Gravity.BOTTOM
    }

    private fun toggleKeyboard() {
        if (isExpanded) {
            windowManager.removeView(keyboardOverlay)
        } else {
            windowManager.addView(keyboardOverlay, keyboardParams)
        }
        isExpanded = !isExpanded
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(toggleReceiver)
        } catch (e: Exception) {}
        
        if (::floatingButton.isInitialized) {
            windowManager.removeView(floatingButton)
        }
        if (isExpanded) {
            windowManager.removeView(keyboardOverlay)
        }
    }
}
