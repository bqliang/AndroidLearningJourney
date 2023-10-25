package com.bqliang.customview.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class MultiLineTextView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val text = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque congue augue sed tellus feugiat, a cursus ligula volutpat. Vivamus dictum quis tortor sit amet feugiat. Fusce at ullamcorper ligula, tristique hendrerit turpis. Aliquam nisl elit, ullamcorper vel porta mollis, scelerisque ac risus. Etiam tempor diam in lacus consectetur fermentum. Quisque commodo sapien auctor est luctus consequat. Quisque finibus a nisi id suscipit.
    """.trimIndent()

    private val textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG).apply {
        textSize = 22.dp
        color = Color.BLACK
    }

    private lateinit var staticLayout: StaticLayout

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        staticLayout = StaticLayout.Builder.obtain(
            /* source = */ text,
            /* start = */ 0,
            /* end = */ text.length,
            /* paint = */ textPaint,
            /* width = */ width
        )
            .setTextDirection(TextDirectionHeuristics.LTR) // 设置文字方向从左到右
            .setAlignment(Layout.Alignment.ALIGN_NORMAL) // 设置文字对齐方式为正常对齐
            .setLineSpacing(0f, 1.0f) // 设置行间距
            .setIncludePad(true) // 设置是否包含文字上下额外间距，默认为 true
            .build()
    }

    override fun onDraw(canvas: Canvas) {
        staticLayout.draw(canvas) // 绘制文字
    }
}

class MultiLineTextViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = MultiLineTextView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
