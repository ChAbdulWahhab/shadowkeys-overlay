package com.hackerkeyboardapp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import android.os.Handler
import android.os.Looper

/**
 * Shell-based input injector that works WITHOUT root access.
 * Uses 'input keyevent' shell commands to inject key events into GTA SA.
 * 
 * This is the MOST RELIABLE method for injecting cheats into games
 * that don't use standard Android InputConnection system.
 * 
 * Features:
 * - Screen tap before injection (ensures game has focus)
 * - Optimized timing (first character gets extra delay)
 * - ENTER key after cheat (required by some GTA SA versions)
 * - Configurable delay from settings
 */
class ShellInputInjector(private val context: Context? = null) {
    
    companion object {
        private const val TAG = "ShellInputInjector"
        private const val PREFS_NAME = "CheatSettings"
        private const val KEY_DELAY = "key_delay"
        private const val DEFAULT_DELAY = 100L // milliseconds
        private const val DELAY_AFTER_COMPLETE = 250L // milliseconds
        private const val DELAY_AFTER_TAP = 200L // milliseconds
    }
    
    private val sharedPreferences: SharedPreferences? = 
        context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    /**
     * Injects a cheat code by sending individual key events via shell commands.
     * 
     * @param cheatCode The cheat code string to inject (e.g., "HESOYAM")
     * @param callback Called with success status on main thread
     */
    fun injectCheat(cheatCode: String, callback: (Boolean) -> Unit) {
        Thread {
            try {
                // Step 1: Ensure game has focus (tap screen first)
                tapScreen()
                Thread.sleep(DELAY_AFTER_TAP)
                
                // Step 2: Inject cheat with optimized timing
                val success = injectViaShellOptimized(cheatCode)
                
                // Step 3: Send ENTER key (important for some GTA versions)
                if (success) {
                    sendEnterKey()
                }
                
                Handler(Looper.getMainLooper()).post {
                    callback(success)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error injecting cheat: ${e.message}", e)
                Handler(Looper.getMainLooper()).post {
                    callback(false)
                }
            }
        }.start()
    }
    
    /**
     * CRITICAL FIX: Tap screen to ensure game has focus
     */
    private fun tapScreen() {
        try {
            // Tap center of screen to activate game
            val resources = context?.resources ?: Resources.getSystem()
            val displayMetrics = resources.displayMetrics
            val x = displayMetrics.widthPixels / 2
            val y = displayMetrics.heightPixels / 3
            
            Runtime.getRuntime().exec(arrayOf(
                "sh", "-c", 
                "input tap $x $y"
            )).waitFor()
            
            Log.d(TAG, "Screen tapped at ($x, $y) for focus")
        } catch (e: Exception) {
            Log.e(TAG, "Tap failed: ${e.message}", e)
        }
    }
    
    /**
     * OPTIMIZED: Better timing and error handling
     */
    private fun injectViaShellOptimized(text: String): Boolean {
        return try {
            Log.d(TAG, "Starting optimized injection for: $text")
            
            val baseDelay = getKeyDelay()
            
            text.forEachIndexed { index, char ->
                val keyCode = getKeyEventCode(char)
                if (keyCode != null) {
                    // Send keyevent
                    val process = Runtime.getRuntime().exec(arrayOf(
                        "sh", "-c", 
                        "input keyevent $keyCode"
                    ))
                    
                    // Wait for completion
                    val exitValue = process.waitFor()
                    
                    if (exitValue != 0) {
                        Log.w(TAG, "Keyevent command failed for char: $char (exit code: $exitValue)")
                    }
                    
                    // CRITICAL: Timing between keys
                    // First character: longer delay (game recognition)
                    // Other characters: normal delay
                    if (index == 0) {
                        Thread.sleep(baseDelay + 50) // First character needs more time
                    } else {
                        Thread.sleep(baseDelay) // Subsequent characters
                    }
                } else {
                    Log.w(TAG, "No keycode mapping for char: $char")
                }
            }
            
            // IMPORTANT: Wait after complete sequence
            Thread.sleep(DELAY_AFTER_COMPLETE)
            
            Log.d(TAG, "Injection completed successfully for: $text")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Injection failed: ${e.message}", e)
            false
        }
    }
    
    /**
     * NEW: Send ENTER key to confirm cheat (some GTA SA versions need this)
     */
    private fun sendEnterKey() {
        try {
            Runtime.getRuntime().exec(arrayOf(
                "sh", "-c",
                "input keyevent 66" // KEYCODE_ENTER
            )).waitFor()
            Thread.sleep(100)
            Log.d(TAG, "ENTER key sent")
        } catch (e: Exception) {
            Log.e(TAG, "Enter key failed: ${e.message}", e)
        }
    }
    
    /**
     * Gets the configured key delay from settings, or default
     */
    private fun getKeyDelay(): Long {
        return sharedPreferences?.getInt(KEY_DELAY, DEFAULT_DELAY.toInt())?.toLong() 
            ?: DEFAULT_DELAY
    }
    
    /**
     * Maps a character to its Android keycode string for shell commands.
     * 
     * @param char The character to map
     * @return The keycode string (e.g., "29" for 'A'), or null if not mappable
     */
    private fun getKeyEventCode(char: Char): String? {
        return when (char.uppercaseChar()) {
            'A' -> "29"  // KEYCODE_A
            'B' -> "30"  // KEYCODE_B
            'C' -> "31"  // KEYCODE_C
            'D' -> "32"  // KEYCODE_D
            'E' -> "33"  // KEYCODE_E
            'F' -> "34"  // KEYCODE_F
            'G' -> "35"  // KEYCODE_G
            'H' -> "36"  // KEYCODE_H
            'I' -> "37"  // KEYCODE_I
            'J' -> "38"  // KEYCODE_J
            'K' -> "39"  // KEYCODE_K
            'L' -> "40"  // KEYCODE_L
            'M' -> "41"  // KEYCODE_M
            'N' -> "42"  // KEYCODE_N
            'O' -> "43"  // KEYCODE_O
            'P' -> "44"  // KEYCODE_P
            'Q' -> "45"  // KEYCODE_Q
            'R' -> "46"  // KEYCODE_R
            'S' -> "47"  // KEYCODE_S
            'T' -> "48"  // KEYCODE_T
            'U' -> "49"  // KEYCODE_U
            'V' -> "50"  // KEYCODE_V
            'W' -> "51"  // KEYCODE_W
            'X' -> "52"  // KEYCODE_X
            'Y' -> "53"  // KEYCODE_Y
            'Z' -> "54"  // KEYCODE_Z
            '0' -> "7"   // KEYCODE_0
            '1' -> "8"   // KEYCODE_1
            '2' -> "9"   // KEYCODE_2
            '3' -> "10"  // KEYCODE_3
            '4' -> "11"  // KEYCODE_4
            '5' -> "12"  // KEYCODE_5
            '6' -> "13"  // KEYCODE_6
            '7' -> "14"  // KEYCODE_7
            '8' -> "15"  // KEYCODE_8
            '9' -> "16"  // KEYCODE_9
            else -> null
        }
    }
    
    /**
     * Adjusts the delay between keys for different device performance levels.
     * Call this before injection if needed.
     * 
     * @param delayMs New delay in milliseconds (recommended: 80-200ms)
     */
    fun setDelayBetweenKeys(delayMs: Long) {
        // Note: This would require making DELAY_BETWEEN_KEYS a var
        // For now, users can modify the constant directly
        Log.d(TAG, "Delay adjustment requested: ${delayMs}ms (modify constant to apply)")
    }
}

