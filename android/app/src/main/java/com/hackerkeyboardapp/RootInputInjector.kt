package com.hackerkeyboardapp

import android.util.Log
import java.io.DataOutputStream

/**
 * Root-based input injector for devices with root access.
 * Uses 'su' commands to inject key events via shell.
 * 
 * Note: This requires root access. For non-root devices, use ShellInputInjector instead.
 */
class RootInputInjector {
    
    companion object {
        private const val TAG = "RootInputInjector"
        private const val DELAY_BETWEEN_KEYS = 100L // milliseconds
    }
    
    /**
     * Injects a cheat code using root shell commands.
     * 
     * @param cheatCode The cheat code string to inject (e.g., "HESOYAM")
     * @return true if successful, false otherwise
     */
    fun injectCheat(cheatCode: String): Boolean {
        return try {
            Log.d(TAG, "Starting root injection for: $cheatCode")
            
            val process = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(process.outputStream)
            
            // ✅ FIX: Use keyevent instead of text input (works with GTA SA)
            cheatCode.forEach { char ->
                val keyCode = getKeyEventCode(char)
                if (keyCode != null) {
                    outputStream.writeBytes("input keyevent $keyCode\n")
                    outputStream.flush()
                    Thread.sleep(DELAY_BETWEEN_KEYS) // Delay between characters
                }
            }
            
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            process.waitFor()
            
            Log.d(TAG, "Root injection completed for: $cheatCode")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to inject cheat: ${e.message}", e)
            false
        }
    }
    
    /**
     * Maps a character to its Android keycode for shell commands.
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
}
