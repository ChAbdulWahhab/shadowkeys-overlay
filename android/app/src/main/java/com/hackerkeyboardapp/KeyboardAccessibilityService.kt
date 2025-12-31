package com.hackerkeyboardapp

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build

class KeyboardAccessibilityService : AccessibilityService() {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val text = intent?.getStringExtra("text")
            val command = intent?.getStringExtra("command")
            
            if (text != null) {
                injectText(text)
            } else if (command != null) {
                handleCommand(command)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter("com.hackerkeyboardapp.INJECT_INPUT")
        registerReceiver(receiver, filter)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not used for now, but required
    }

    override fun onInterrupt() {
        // Not used
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun injectText(text: String) {
        val rootNode = rootInActiveWindow ?: return
        val targetNode = findTargetNode(rootNode)
        
        if (targetNode != null) {
            val currentText = targetNode.text ?: ""
            val hintText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) targetNode.hintText else null
            
            // If the current text is just the placeholder/hint, ignore it and start fresh
            val newText = if (currentText.isNotEmpty() && currentText != hintText) {
                currentText.toString() + text
            } else {
                text
            }
            
            val arguments = Bundle()
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, newText)
            targetNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            targetNode.recycle()
        }
    }

    private fun handleCommand(command: String) {
        when (command) {
            "ENTER" -> {
                // Try to perform search/action on the node if possible
                val rootNode = rootInActiveWindow ?: return
                val targetNode = findTargetNode(rootNode)
                if (targetNode != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        targetNode.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_IME_ENTER.id)
                    } else {
                        // Fallback for older versions if needed
                        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
                    }
                    targetNode.recycle()
                } else {
                    performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
                }
            }
            "DEL" -> {
                val rootNode = rootInActiveWindow ?: return
                val targetNode = findTargetNode(rootNode)
                if (targetNode != null) {
                    val currentText = targetNode.text ?: ""
                    if (currentText.isNotEmpty()) {
                        val newText = currentText.substring(0, currentText.length - 1)
                        val arguments = Bundle()
                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, newText)
                        targetNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
                    }
                    targetNode.recycle()
                }
            }
            "SPACE" -> injectText(" ")
            else -> {
                // For long codes like HESOYAM, inject char by char with small delays
                injectSequence(command)
            }
        }
    }

    private fun injectSequence(sequence: String) {
        // Multi-character injection for game cheats
        for (char in sequence) {
            injectText(char.toString())
            try { Thread.sleep(30) } catch (e: Exception) {}
        }
    }

    private fun findTargetNode(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (node.isFocused && node.isEditable) return node
        
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findTargetNode(child)
            if (found != null) return found
        }
        
        // Final fallback: just return the first editable node found
        return findFirstEditable(node)
    }

    private fun findFirstEditable(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (node.isEditable) return node
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findFirstEditable(child)
            if (found != null) return found
        }
        return null
    }
}
