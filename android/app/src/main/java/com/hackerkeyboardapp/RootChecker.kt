package com.hackerkeyboardapp

import android.os.Build
import java.io.File

object RootChecker {
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }
    
    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }
    
    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/data/local/su",
            "/system/sbin/su",
            "/usr/bin/su",
            "/vendor/bin/su"
        )
        return paths.any { File(it).exists() }
    }
    
    private fun checkRootMethod3(): Boolean {
        return try {
            Runtime.getRuntime().exec("su")
            true
        } catch (e: Exception) {
            false
        }
    }
}
