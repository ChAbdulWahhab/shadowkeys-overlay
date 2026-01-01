package com.hackerkeyboardapp

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Space

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var onKeyClickListener: ((String) -> Unit)? = null
    private var onDeleteClickListener: (() -> Unit)? = null
    private var onEnterClickListener: (() -> Unit)? = null
    private var onSwitchKeyboardListener: (() -> Unit)? = null

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#1A1A1A"))
        val padding = (8 * resources.displayMetrics.density).toInt()
        setPadding(padding, padding, padding, padding)
        
        setupUI()
    }

    fun setListeners(
        onKey: (String) -> Unit,
        onDelete: () -> Unit,
        onEnter: () -> Unit,
        onSwitch: () -> Unit
    ) {
        onKeyClickListener = onKey
        onDeleteClickListener = onDelete
        onEnterClickListener = onEnter
        onSwitchKeyboardListener = onSwitch
    }

    private fun setupUI() {
        // Quick Cheat Row
        val scrollCheats = HorizontalScrollView(context)
        val cheatRow = LinearLayout(context).apply {
            orientation = HORIZONTAL
        }
        
        GTASACheats.cheats.forEach { cheat ->
            val btn = createKeyButton(cheat.name, true)
            btn.setOnClickListener { onKeyClickListener?.invoke(cheat.code) }
            cheatRow.addView(btn)
        }
        scrollCheats.addView(cheatRow)
        addView(scrollCheats)

        // QWERTY Layout
        val rows = listOf(
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
        )

        rows.forEach { row ->
            val rowLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                gravity = Gravity.CENTER
            }
            row.forEach { char ->
                val btn = createKeyButton(char.toString())
                btn.setOnClickListener { onKeyClickListener?.invoke(char.toString()) }
                rowLayout.addView(btn)
            }
            addView(rowLayout)
        }

        // Bottom Row
        val bottomRow = LinearLayout(context).apply {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER
        }

        val switchBtn = createKeyButton("⌨", false, 0.15f)
        switchBtn.setOnClickListener { onSwitchKeyboardListener?.invoke() }
        bottomRow.addView(switchBtn)

        val spaceBtn = createKeyButton("SPACE", false, 0.5f)
        spaceBtn.setOnClickListener { onKeyClickListener?.invoke(" ") }
        bottomRow.addView(spaceBtn)

        val delBtn = createKeyButton("⌫", false, 0.15f)
        delBtn.setOnClickListener { onDeleteClickListener?.invoke() }
        bottomRow.addView(delBtn)

        val enterBtn = createKeyButton("⏎", false, 0.15f)
        enterBtn.setOnClickListener { onEnterClickListener?.invoke() }
        bottomRow.addView(enterBtn)

        addView(bottomRow)
    }

    private fun createKeyButton(text: String, isCheat: Boolean = false, weight: Float = 0f): Button {
        val btn = Button(context)
        btn.text = text
        btn.setTextColor(Color.WHITE)
        btn.isAllCaps = false
        btn.textSize = if (isCheat) 12f else 18f
        
        val density = resources.displayMetrics.density
        val params = LinearLayout.LayoutParams(
            if (weight > 0) 0 else LayoutParams.WRAP_CONTENT,
            (45 * density).toInt()
        )
        if (weight > 0) params.weight = weight
        params.setMargins((2 * density).toInt(), (2 * density).toInt(), (2 * density).toInt(), (2 * density).toInt())
        btn.layoutParams = params
        
        btn.setBackgroundColor(if (isCheat) Color.parseColor("#3D3D3D") else Color.parseColor("#2D2D2D"))
        
        return btn
    }
}
