package com.bqliang.customview.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class AlignEdgeTextView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val text = "bqliang"
    private val textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)

    init {
        textPaint.apply {
            color = Color.BLACK
            textSize = 50.dp
        }
    }

    override fun onDraw(canvas: Canvas) {
        // 左上
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText(
            text,
            0f,
            -textPaint.fontMetrics.top, // 注意 top 是负数
            textPaint
        )

        // 右下
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            text,
            width.toFloat(),
            height.toFloat() - textPaint.fontMetrics.bottom,
            textPaint
        )
    }
}

class AlignEdgeTextViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AlignEdgeTextView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
