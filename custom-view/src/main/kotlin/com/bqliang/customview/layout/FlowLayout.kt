package com.bqliang.customview.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import kotlin.random.Random

class FlowLayout : ViewGroup {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val childrenBounds = mutableListOf<Rect>()

    // 非滑动控件，重写返回 false
    override fun shouldDelayChildPressedState(): Boolean = false

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val layoutWidthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val layoutWidthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)

        var lineWidthUsed = 0  // 当前行已用的宽度
        var lineHeightUsed = 0  // 当前行已用的高度
        var widthUsed = 0  // 已用的宽度
        var heightUsedWithoutCurrentLine = 0  // 已用的高度，不包括当前行

        children.forEachIndexed { index, child ->
            val layoutParams = child.layoutParams


            /*
            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)
            val heightSize = MeasureSpec.getSize(heightMeasureSpec)

            var childWidthMeasureMode = 0
            var childWidthMeasureSize = 0
            var childHeightMeasureMode = 0
            var childHeightMeasureSize = 0

            when (widthMode) {
                MeasureSpec.EXACTLY ->
                    when (layoutParams.width) {
                        LayoutParams.MATCH_PARENT -> {
                            childWidthMeasureMode = MeasureSpec.EXACTLY
                            childWidthMeasureSize = widthSize - widthUsed
                        }

                        LayoutParams.WRAP_CONTENT -> {
                            childWidthMeasureMode = MeasureSpec.AT_MOST
                            childWidthMeasureSize = widthSize - widthUsed
                        }

                        else -> {
                            childWidthMeasureMode = MeasureSpec.EXACTLY
                            childWidthMeasureSize = layoutParams.width
                        }
                    }

                MeasureSpec.AT_MOST ->
                    when (layoutParams.width) {
                        LayoutParams.MATCH_PARENT -> {
                            childWidthMeasureMode = MeasureSpec.AT_MOST
                            childWidthMeasureSize = widthSize - widthUsed
                        }

                        LayoutParams.WRAP_CONTENT -> {
                            childWidthMeasureMode = MeasureSpec.AT_MOST
                            childWidthMeasureSize = widthSize - widthUsed
                        }

                        else -> {
                            childWidthMeasureMode = MeasureSpec.EXACTLY
                            childWidthMeasureSize = layoutParams.width
                        }
                    }

                MeasureSpec.UNSPECIFIED ->
                    when (layoutParams.width) {
                        LayoutParams.MATCH_PARENT -> {
                            childWidthMeasureMode = MeasureSpec.UNSPECIFIED
                            childWidthMeasureSize = 0
                        }

                        LayoutParams.WRAP_CONTENT -> {
                            childWidthMeasureMode = MeasureSpec.UNSPECIFIED
                            childWidthMeasureSize = 0
                        }

                        else -> {
                            childWidthMeasureMode = MeasureSpec.EXACTLY
                            childWidthMeasureSize = layoutParams.width
                        }
                    }
            }
            ...
            child.measure(
                MeasureSpec.makeMeasureSpec(childWidthMeasureSize, childWidthMeasureMode),
                MeasureSpec.makeMeasureSpec(childHeightMeasureSize, childHeightMeasureMode)
            )
            */

            /**
             * 上面的代码可以简化为 [ViewGroup.measureChild] / [ViewGroup.measureChildWithMargins]
             */

            measureChildWithMargins(
                /* child = */ child,
                /* parentWidthMeasureSpec = */ widthMeasureSpec,
                /* widthUsed = */ 0,
                /* parentHeightMeasureSpec = */ heightMeasureSpec,
                /* heightUsed = */ heightUsedWithoutCurrentLine
            )

            // 换行
            if (layoutWidthMeasureMode != MeasureSpec.UNSPECIFIED
                && child.measuredWidth + lineWidthUsed > layoutWidthMeasureSize - paddingLeft - paddingRight
            ) {
                // 换行后，重置已用高度，以及当前行已用的宽度和高度
                heightUsedWithoutCurrentLine += lineHeightUsed
                lineWidthUsed = 0
                lineHeightUsed = 0

                // 换行再次测量
                measureChildWithMargins(
                    /* child = */ child,
                    /* parentWidthMeasureSpec = */ widthMeasureSpec,
                    /* widthUsed = */ 0,
                    /* parentHeightMeasureSpec = */ heightMeasureSpec,
                    /* heightUsed = */ heightUsedWithoutCurrentLine
                )
            }

            // 计算当前行已用的宽度和高度
            lineWidthUsed += child.measuredWidth
            lineHeightUsed = lineHeightUsed.coerceAtLeast(child.measuredHeight)
            // 更新已用宽度
            widthUsed = widthUsed.coerceAtLeast(lineWidthUsed)

            // 记录子 view 的位置
            if (childrenBounds.size <= index) {
                childrenBounds.add(Rect())
            }
            with(childrenBounds[index]) {
                left = lineWidthUsed - child.measuredWidth
                top = heightUsedWithoutCurrentLine
                right = lineWidthUsed
                bottom = top + child.measuredHeight
            }
        }

        // 测量完子 view 后，根据所有子 view 的尺寸计算自己的尺寸
        setMeasuredDimension(
            /* measuredWidth = */ widthUsed + paddingLeft + paddingRight,
            /* measuredHeight = */
            heightUsedWithoutCurrentLine + lineHeightUsed + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
            with(childrenBounds[index]) {
                child.layout(left, top, right, bottom)
            }
        }
    }

    /**
     * 重写 [ViewGroup.generateLayoutParams] 方法，返回 [MarginLayoutParams] 对象
     * 以支持 [android:layout_margin] 属性
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)
}


class TagView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    companion object {
        private val random = Random.Default
        private val COLORS by lazy {
            listOf(
                "#dee5cd",
                "#fadbd2",
                "#dbe3f8",
                "#e6e3d1",
            ).map { Color.parseColor(it) }
        }
    }

    private var padding_x_extra = 0

    private val tagPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = COLORS.random(random)
    }

    init {
        textSize = (8 + random.nextInt(13)).dp
        padding_x_extra = textSize.toInt() / 2
        setPadding(
            /* left = */ paddingLeft + padding_x_extra,
            /* top = */ paddingTop,
            /* right = */ paddingRight + padding_x_extra,
            /* bottom = */ paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            /* left = */ (paddingLeft - padding_x_extra).toFloat(),
            /* top = */ paddingTop.toFloat(),
            /* right = */ (width - (paddingRight - padding_x_extra)).toFloat(),
            /* bottom = */ (height - paddingBottom).toFloat(),
            /* rx = */ padding_x_extra.toFloat(),
            /* ry = */ padding_x_extra.toFloat(),
            /* paint = */ tagPaint
        )
        super.onDraw(canvas)
    }
}


class FlowLayoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = ScrollView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }

        val flowLayout = FlowLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }.also { root.addView(it) }

        LANGUAGES.forEach { language ->
            TagView(requireContext()).apply {
                text = language
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
                )
            }.also { flowLayout.addView(it) }
        }

        return root
    }

    private val LANGUAGES = listOf(
        "Android",
        "Kotlin",
        "Java",
        "Python",
        "C",
        "C++",
        "a very long long long long long long long long text.",
        "C#",
        "JavaScript",
        "Go",
        "Swift",
        "PHP",
        "Ruby",
        "Rust",
        "Dart",
        "TypeScript",
        "HTML",
        "CSS",
        "SQL",
        "Objective-C",
        "Perl",
        "Scala",
        "Groovy",
        "Lua",
        "R",
        "Matlab",
        "Visual Basic",
        "Assembly",
        "Erlang",
        "Haskell",
        "Kotlin",
        "Lisp",
        "Pascal",
        "PowerShell",
        "R",
        "Scratch",
        "Scheme",
        "Shell",
        "VBScript",
        "VHDL",
        "XML"
    )
}
