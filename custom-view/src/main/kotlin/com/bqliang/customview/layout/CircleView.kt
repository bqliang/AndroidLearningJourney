package com.bqliang.customview.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class CircleView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val RADIUS = 100.dp
    private val PADDING = 20.dp


    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(RADIUS + PADDING, RADIUS + PADDING, RADIUS, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = ((RADIUS + PADDING) * 2).toInt()

        /*
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measuredWidth = when(MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> specWidth
            MeasureSpec.AT_MOST -> if (size > specWidth) specWidth else size
            MeasureSpec.UNSPECIFIED -> size
            else -> throw IllegalArgumentException("Unknown measure mode")
        }

        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val measuredHeight = when(MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> specHeight
            MeasureSpec.AT_MOST -> if (size > specHeight) specHeight else size
            MeasureSpec.UNSPECIFIED -> size
            else -> throw IllegalArgumentException("Unknown measure mode")
        }
         */

        val measuredWidth = resolveSize(size, widthMeasureSpec)
        val measuredHeight = resolveSize(size, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }
}

class CircleViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        CircleView(requireContext()).apply {
            setBackgroundColor(Color.parseColor("#3bc3ff"))
            layoutParams = FrameLayout.LayoutParams(
                /* width = */ 200.dp.toInt(),
                /* height = */ FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }.also { root.addView(it) }

        return root
    }
}