package com.hackerkeyboardapp

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.KeyEvent
import android.widget.Button
import android.widget.Toast
import android.util.Log

/**
 * Cheat Keyboard Service for GTA San Andreas
 * 
 * Uses shell command injection to send key events directly to the game,
 * bypassing the standard InputConnection system which doesn't work with GTA SA.
 * 
 * Features:
 * - Fixed-size keyboard layout to prevent jerking/resizing
 * - Shell-based key event injection (no root required)
 * - Hardware acceleration for smooth rendering
 * - All GTA SA cheats pre-configured
 */
class CheatKeyboardService : InputMethodService() {

    private lateinit var shellInjector: ShellInputInjector
    private var keyboardView: View? = null

    companion object {
        private const val TAG = "CheatKeyboardService"
    }

    override fun onCreate() {
        super.onCreate()
        shellInjector = ShellInputInjector(this)
        Log.d(TAG, "CheatKeyboardService created")
    }

    override fun onCreateInputView(): View {
        // ✅ FIX: Use fixed XML layout instead of dynamic KeyboardView
        keyboardView = layoutInflater.inflate(R.layout.keyboard_layout, null)
        
        // ✅ FIX: Set FIXED layout parameters to prevent resizing
        keyboardView?.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dpToPx(240) // Fixed height in pixels
        )
        
        // ✅ FIX: Enable hardware acceleration for smooth rendering
        keyboardView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        
        setupAllCheatButtons(keyboardView!!)
        
        Log.d(TAG, "Keyboard view created with fixed dimensions")
        return keyboardView!!
    }

    override fun onComputeInsets(outInsets: Insets) {
        super.onComputeInsets(outInsets)
        // ✅ FIX: Force consistent insets to prevent layout shifts
        val keyboardHeight = dpToPx(240)
        outInsets.contentTopInsets = keyboardHeight
        outInsets.visibleTopInsets = keyboardHeight
    }

    override fun onEvaluateFullscreenMode(): Boolean {
        // Important: Return false to prevent the keyboard from taking over the screen
        // and causing the "jerking" effect in landscape games.
        return false
    }

    override fun onEvaluateInputViewShown(): Boolean {
        return true
    }

    /**
     * Converts dp to pixels for fixed dimensions
     */
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    /**
     * Sets up all cheat code buttons with their respective handlers
     */
    private fun setupAllCheatButtons(view: View) {
        // HESOYAM - Health, Armor, Money ($250,000)
        view.findViewById<Button>(R.id.btn_hesoyam)?.setOnClickListener {
            injectCheatWithFeedback("HESOYAM", "💰 Money cheat activated!")
        }

        // BAGUVIX - God Mode (Infinite Health)
        view.findViewById<Button>(R.id.btn_baguvix)?.setOnClickListener {
            injectCheatWithFeedback("BAGUVIX", "❤️ God mode activated!")
        }

        // LXGIWYL - Weapons Set 1
        view.findViewById<Button>(R.id.btn_lxgiwyl)?.setOnClickListener {
            injectCheatWithFeedback("LXGIWYL", "🔫 Weapon set 1 activated!")
        }

        // KJKSZPJ - Weapons Set 2
        view.findViewById<Button>(R.id.btn_kjkszpj)?.setOnClickListener {
            injectCheatWithFeedback("KJKSZPJ", "🔫 Weapon set 2 activated!")
        }

        // UZUMYMW - Weapons Set 3
        view.findViewById<Button>(R.id.btn_uzumymw)?.setOnClickListener {
            injectCheatWithFeedback("UZUMYMW", "🔫 Weapon set 3 activated!")
        }

        // ROCKETMAN - Jetpack
        view.findViewById<Button>(R.id.btn_rocketman)?.setOnClickListener {
            injectCheatWithFeedback("ROCKETMAN", "🚀 Jetpack activated!")
        }

        // JUMPJET - Hydra (fighter jet)
        view.findViewById<Button>(R.id.btn_jumpjet)?.setOnClickListener {
            injectCheatWithFeedback("JUMPJET", "✈️ Hydra spawned!")
        }

        // PANZER - Rhino Tank
        view.findViewById<Button>(R.id.btn_panzer)?.setOnClickListener {
            injectCheatWithFeedback("PANZER", "🚗 Tank spawned!")
        }

        // WANRLTW - Infinite Ammo
        view.findViewById<Button>(R.id.btn_wanrltw)?.setOnClickListener {
            injectCheatWithFeedback("WANRLTW", "🔫 Infinite ammo activated!")
        }

        // KANGAROO - Super Jump
        view.findViewById<Button>(R.id.btn_kangaroo)?.setOnClickListener {
            injectCheatWithFeedback("KANGAROO", "🦘 Super jump activated!")
        }

        // TURNDOWNTHEHEAT - Lower Wanted Level
        view.findViewById<Button>(R.id.btn_turndown)?.setOnClickListener {
            injectCheatWithFeedback("TURNDOWNTHEHEAT", "👮 Wanted level lowered!")
        }

        // TURNUPTHEHEAT - Raise Wanted Level
        view.findViewById<Button>(R.id.btn_turnup)?.setOnClickListener {
            injectCheatWithFeedback("TURNUPTHEHEAT", "👮 Wanted level raised!")
        }

        // Settings button - Open settings activity
        view.findViewById<Button>(R.id.btn_settings)?.setOnClickListener {
            openSettings()
        }

        // Close button - Hide keyboard
        view.findViewById<Button>(R.id.btn_close)?.setOnClickListener {
            requestHideSelf(0)
        }
    }

    /**
     * Injects a cheat code with user feedback via Toast messages
     * 
     * @param cheatCode The cheat code to inject (e.g., "HESOYAM")
     * @param successMessage Message to show on successful injection
     */
    private fun injectCheatWithFeedback(cheatCode: String, successMessage: String) {
        Log.d(TAG, "Injecting cheat code: $cheatCode")
        
        // Show loading indicator
        Toast.makeText(this, "⏳ Injecting $cheatCode...", Toast.LENGTH_SHORT).show()

        // Inject the cheat using shell command method
        shellInjector.injectCheat(cheatCode) { success ->
            if (success) {
                Toast.makeText(this, "✅ $successMessage", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Successfully injected: $cheatCode")
            } else {
                Toast.makeText(this, "❌ Failed to inject $cheatCode", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to inject: $cheatCode")
            }
        }
    }
    
    /**
     * Opens the Settings activity
     */
    private fun openSettings() {
        try {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open settings: ${e.message}", e)
            Toast.makeText(this, "Failed to open settings", Toast.LENGTH_SHORT).show()
        }
    }
}
