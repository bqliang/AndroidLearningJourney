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
 * 多点触控 (接力型)
 */
class MultiTouchView1 : View {
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
    private var activePointerId = 0

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
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                activePointerId = event.getPointerId(0)
                downX = event.x
                downY = event.y
                downOffsetX = offsetX
                downOffsetY = offsetY
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                activePointerId = event.getPointerId(event.actionIndex)
                downX = event.getX(event.actionIndex)
                downY = event.getY(event.actionIndex)
                downOffsetX = offsetX
                downOffsetY = offsetY
            }

            MotionEvent.ACTION_MOVE -> {
                val index = event.findPointerIndex(activePointerId)
                offsetX = event.getX(index) - downX + downOffsetX
                offsetY = event.getY(index) - downY + downOffsetY
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                // No need to reset activePointerId, because it will be reset in ACTION_POINTER_UP
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val upPointerId = event.getPointerId(event.actionIndex)
                if (upPointerId == activePointerId) {
                    val newIndex = if (event.actionIndex == event.pointerCount - 1) {
                        event.pointerCount - 2
                    } else {
                        event.pointerCount - 1
                    }
                    activePointerId = event.getPointerId(newIndex)
                    downX = event.getX(newIndex)
                    downY = event.getY(newIndex)
                    downOffsetX = offsetX
                    downOffsetY = offsetY
                }
            }
        }
        return true
    }
}

class MultiTouchFragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = MultiTouchView1(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
