package com.bqliang.customview.touch.custom

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

/**
 * 多点触控 (合作型)
 */
class MultiTouchView2 : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    companion object {
        private val BITMAP_SIZE = 250.dp
    }

    private val bitmap by lazy { resources.loadCatBitmap(BITMAP_SIZE.toInt()) }
    private var offsetX = 0f
    private var offsetY = 0f
    private var downX = 0f
    private var downY = 0f
    private var downOffsetX = 0f
    private var downOffsetY = 0f

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            /* measuredWidth = */ resolveSize(BITMAP_SIZE.toInt(), widthMeasureSpec),
            /* measuredHeight = */ resolveSize(BITMAP_SIZE.toInt(), heightMeasureSpec)
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var sumX = 0f
        var sumY = 0f
        var count = 0
        for (i in 0 until event.pointerCount) {
            if (event.actionMasked == MotionEvent.ACTION_POINTER_UP
                && i == event.actionIndex
            ) {
                continue
            }
            sumX += event.getX(i)
            sumY += event.getY(i)
            count++
        }

        val focusX = sumX / count
        val focusY = sumY / count

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                downX = focusX
                downY = focusY
                downOffsetX = offsetX
                downOffsetY = offsetY
            }

            MotionEvent.ACTION_MOVE -> {
                offsetX = focusX - downX + downOffsetX
                offsetY = focusY - downY + downOffsetY
                invalidate()
            }
        }
        return true
    }
}

class MultiTouchFragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = MultiTouchView2(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
