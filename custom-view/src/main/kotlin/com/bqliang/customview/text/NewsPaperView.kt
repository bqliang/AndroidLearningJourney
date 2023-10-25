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
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class NewsPaperView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val text = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque congue augue sed tellus feugiat, a cursus ligula volutpat. Vivamus dictum quis tortor sit amet feugiat. Fusce at ullamcorper ligula, tristique hendrerit turpis. Aliquam nisl elit, ullamcorper vel porta mollis, scelerisque ac risus. Etiam tempor diam in lacus consectetur fermentum. Quisque commodo sapien auctor est luctus consequat. Quisque finibus a nisi id suscipit.
    """.trimIndent()

    private val bitmapSize = 150.dp
    private val bitmapMarginTop = 50.dp
    private val fontMetrics = Paint.FontMetrics()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 18.dp
    }


    override fun onDraw(canvas: Canvas) {
        // 靠右
        canvas.drawBitmap(
            /* bitmap = */ resources.loadCatBitmap(bitmapSize.toInt()),
            /* left = */ width - bitmapSize,
            /* top = */ bitmapMarginTop,
            /* paint = */ paint
        )

        var start = 0
        var count: Int
        var verticalOffset = -paint.fontMetrics.top

        paint.getFontMetrics(fontMetrics)
        while (start < text.length) {
            val maxWidth = if (
                verticalOffset + fontMetrics.bottom < bitmapMarginTop
                || verticalOffset + fontMetrics.top > bitmapSize + bitmapMarginTop
            ) {
                width.toFloat()
            } else {
                width - bitmapSize
            }

            val measuredWidth = floatArrayOf()

            count = paint.breakText(
                /* text = */ text,
                /* start = */ start,
                /* end = */ text.length,
                /* measureForwards = */ true,
                /* maxWidth = */ maxWidth,
                /* measuredWidth = */ measuredWidth
            )

            canvas.drawText(
                /* text = */ text,
                /* start = */ start,
                /* end = */ start + count,
                /* x = */ 0f,
                /* y = */ verticalOffset,
                /* paint = */ paint
            )

            start += count
            verticalOffset += paint.fontSpacing
        }
    }
}

class NewsPaperViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = NewsPaperView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
