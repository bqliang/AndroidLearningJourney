package com.bqliang.customview.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.R
import com.bqliang.customview.utils.dp

class SportView: View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    private val radius = 135.dp
    private val strokeWidth = 20.dp
    private val circleColor = 0xff90a4ae.toInt()
    private val progressColor = 0xff2196f3.toInt()
    private val textSize = 60.dp
    private val fontMetrics = Paint.FontMetrics()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = this@SportView.textSize
        textAlign = Paint.Align.CENTER
        typeface = resources.getFont(R.font.wixmade_for_display_variable_font_wght)
    }

    private var progress = 0.35f
        set(value) {
            field = value
            invalidate()
        }
    override fun onDraw(canvas: Canvas) {
        // 绘制圆环
        paint.color = circleColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

        // 绘制进度条
        paint.color = progressColor
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            width / 2f - radius,
            height / 2f - radius,
            width / 2f + radius,
            height / 2f + radius,
            -90f,
            progress * 360,
            false,
            paint
        )

        // 绘制文字
        paint.style = Paint.Style.FILL
        canvas.drawText(
            "%02d%%".format((progress * 100).toInt()),
            width / 2f,
            /* 由 height / 2f - descent + (descent - ascent) / 2f 化简得到 */
            height / 2f - (paint.ascent() + paint.descent()) / 2f,
            paint
        )

        /**
         * 除了直接使用 [Paint.ascent] 和 [Paint.descent] 之外，还可以使用 [Paint.getFontMetrics] 方法
         */
        paint.getFontMetrics(fontMetrics)
        val offset = (fontMetrics.ascent + fontMetrics.descent) / 2f

        // 也可以使用简便写法
        paint.fontMetrics.run {
            val offset = (ascent + descent) / 2f
        }
    }
}

class SportViewFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SportView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
