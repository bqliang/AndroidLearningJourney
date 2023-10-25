package com.bqliang.customview.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class AlignCenterTextView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val auxiliaryPaint = Paint()
    private val textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
    private val textBounds = Rect()
    private val text = "bqliang"

    init {
        auxiliaryPaint.apply {
            color = Color.BLACK
            strokeWidth = 1f
        }

        textPaint.apply {
            textSize = 60.dp
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, auxiliaryPaint)
        canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), auxiliaryPaint)

        textPaint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(
            text,
            width / 2f,
            /* 由 height / 2 - bottom + (bottom - top) / 2 化简得到 */
            height / 2f - (textBounds.top + textBounds.bottom) / 2f,
            textPaint
        )

        // 这种利用 TextBounds 来测量文字将文字居中的方式只适合静态文字，
        // 即文字内容不会发生变化的情况
    }
}

class AlignCenterTextViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AlignCenterTextView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
