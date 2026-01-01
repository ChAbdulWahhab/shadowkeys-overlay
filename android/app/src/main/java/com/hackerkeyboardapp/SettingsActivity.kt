package com.hackerkeyboardapp

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Settings Activity for configuring cheat injection parameters.
 * 
 * Allows users to adjust:
 * - Keystroke delay timing (50-300ms)
 * - Test injection functionality
 */
class SettingsActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    companion object {
        private const val PREFS_NAME = "CheatSettings"
        private const val KEY_DELAY = "key_delay"
        private const val DEFAULT_DELAY = 100
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        setupTimingSettings()
        setupTestButton()
        setupInstructions()
    }
    
    /**
     * Sets up the timing adjustment SeekBar
     */
    private fun setupTimingSettings() {
        val seekBar = findViewById<SeekBar>(R.id.seekbar_timing)
        val textView = findViewById<TextView>(R.id.tv_timing_value)
        
        // Load saved timing (default: 100ms)
        val currentTiming = sharedPreferences.getInt(KEY_DELAY, DEFAULT_DELAY)
        seekBar.progress = currentTiming
        textView.text = "${currentTiming}ms"
        
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Range: 50ms to 300ms
                val timing = progress.coerceIn(50, 300)
                textView.text = "${timing}ms"
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val timing = seekBar?.progress?.coerceIn(50, 300) ?: DEFAULT_DELAY
                sharedPreferences.edit().putInt(KEY_DELAY, timing).apply()
                Toast.makeText(
                    this@SettingsActivity, 
                    "Timing saved: ${timing}ms", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    
    /**
     * Sets up the test injection button
     */
    private fun setupTestButton() {
        findViewById<Button>(R.id.btn_test_injection).setOnClickListener {
            testInjection()
        }
    }
    
    /**
     * Sets up instructions button
     */
    private fun setupInstructions() {
        findViewById<Button>(R.id.btn_instructions)?.setOnClickListener {
            showGameInstructions()
        }
    }
    
    /**
     * Tests injection with current settings
     */
    private fun testInjection() {
        val testDialog = AlertDialog.Builder(this)
            .setTitle("🧪 Test Injection")
            .setMessage("Open any text app (Notes, WhatsApp) and press OK. The word 'TEST' will be typed character by character.")
            .setPositiveButton("OK") { _, _ ->
                Handler(Looper.getMainLooper()).postDelayed({
                    val injector = ShellInputInjector(this)
                    injector.injectCheat("TEST") { success ->
                        Toast.makeText(
                            this, 
                            if (success) "✅ Test successful! Check the text app." else "❌ Test failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, 2000) // 2 second delay to switch apps
            }
            .setNegativeButton("Cancel", null)
            .create()
        
        testDialog.show()
    }
    
    /**
     * Shows game instructions for GTA SA
     */
    private fun showGameInstructions() {
        val instructions = """
            📋 HOW TO USE IN GTA SA:
            
            1️⃣ Load your game (be in GAMEPLAY)
            2️⃣ Make sure game is NOT PAUSED
            3️⃣ Tap game screen once (to focus)
            4️⃣ Press overlay button
            5️⃣ Tap cheat button
            6️⃣ Watch for on-screen message
            
            ⚠️ If cheat doesn't work:
            - Go to Settings
            - Increase keystroke delay to 150ms+
            - Try again
            
            ✅ You'll know it worked when:
            - "CHEAT ACTIVATED" appears on screen
            - Health/money increases immediately
        """.trimIndent()
        
        AlertDialog.Builder(this)
            .setTitle("🎮 GTA SA Instructions")
            .setMessage(instructions)
            .setPositiveButton("Got it!", null)
            .show()
    }
}

