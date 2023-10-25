package com.bqliang.customview.practise

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import com.bqliang.customview.R
import com.bqliang.customview.utils.dp

class MaterialEditText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val floatLabelTextSize = 12.dp
    private val floatLabelPaddingTop = 11.dp
    private val floatLabelVerticalOffset = 20.dp
    private val floatLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = floatLabelTextSize
    }

    private var showingFloatLabel = false

    private var floatLabelFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    var enableFloatLabel = false
        set(enable) {
            if (field == enable) return
            field = enable
            if (enable) {
                setPadding(
                    /* left = */ paddingLeft,
                    /* top = */ (paddingTop + floatLabelTextSize + floatLabelPaddingTop).toInt(),
                    /* right = */ paddingRight,
                    /* bottom = */ paddingBottom
                )
            } else {
                setPadding(
                    /* left = */ paddingLeft,
                    /* top = */ (paddingTop - floatLabelTextSize - floatLabelPaddingTop).toInt(),
                    /* right = */ paddingRight,
                    /* bottom = */ paddingBottom
                )
            }
            invalidate()
        }

    init {
        val typedArray = context.obtainStyledAttributes(R.styleable.MaterialEditText)
        enableFloatLabel = typedArray.getBoolean(R.styleable.MaterialEditText_enableFloatLabel, true)
        typedArray.recycle()
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (showingFloatLabel && text.isNullOrEmpty()) {
            showingFloatLabel = false
            ObjectAnimator.ofFloat(this, "floatLabelFraction", 1f, 0f).apply {
                duration = 200
                interpolator = AccelerateInterpolator()
            }.start()
        } else if (!showingFloatLabel && !text.isNullOrEmpty()) {
            ObjectAnimator.ofFloat(this, "floatLabelFraction", 0f, 1f).apply {
                duration = 200
                interpolator = DecelerateInterpolator()
            }.start()
            showingFloatLabel = true
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!enableFloatLabel) return
        floatLabelPaint.alpha = (floatLabelFraction * 0xff).toInt()
        canvas.drawText(
            /* text = */ hint.toString(),
            /* x = */ paddingLeft.toFloat(),
            /* y = */ floatLabelPaddingTop + floatLabelTextSize + floatLabelVerticalOffset * (1 - floatLabelFraction),
            /* paint = */ floatLabelPaint
        )
    }
}


class MaterialEditTextPractiseFragment : Fragment() {

    private lateinit var materialEditText: MaterialEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val frameLayout1 = FrameLayout(requireContext()).apply {
            setBackgroundColor(Color.argb(50, 118, 209, 255))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }.also { root.addView(it) }

        val frameLayout2 = FrameLayout(requireContext()).apply {
            setBackgroundColor(Color.argb(50, 226, 64, 51))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }.also { root.addView(it) }

        AppCompatEditText(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }.also { frameLayout1.addView(it) }

        materialEditText = MaterialEditText(requireContext()).apply {
            hint = "Username"
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }.also { frameLayout2.addView(it) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // materialEditText.postDelayed(3000) {
        //     materialEditText.enableFloatLabel = false
        // }
    }
}
