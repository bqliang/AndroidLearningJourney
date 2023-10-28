package com.bqliang.customview.touch.custom

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.core.view.children
import androidx.fragment.app.Fragment
import kotlin.math.abs

class TwoPager : ViewGroup {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val viewConfiguration = ViewConfiguration.get(context)
    private val velocityTracker = VelocityTracker.obtain()
    private val overScroller = OverScroller(context)
    private var downX = 0f
    private var downY = 0f
    private var downScrollX = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 1. 测量所有子 View
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        // 2. 测量自己
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
            child.layout(
                /* l = */ width * index,
                /* t = */ 0,
                /* r = */ width * (index + 1),
                /* b = */ height
            )
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                downScrollX = scrollX
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = downX - event.x
                if (abs(dx) > viewConfiguration.scaledPagingTouchSlop) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    return true
                }
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                downScrollX = scrollX
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = downX - event.x + downScrollX
                scrollX = dx.coerceIn(minimumValue = 0F, maximumValue = width.toFloat()).toInt()
            }

            MotionEvent.ACTION_UP -> {
                // need manual trigger compute current velocity
                velocityTracker.computeCurrentVelocity(
                    /* units = */ 1000,
                    /* maxVelocity = */ viewConfiguration.scaledMaximumFlingVelocity.toFloat()
                )
                val velocityX = velocityTracker.xVelocity
                val targetPager = if (velocityX < viewConfiguration.scaledMinimumFlingVelocity) {
                    // 速度小，则根据距离吸附
                    if (scrollX > width / 2) 1 else 0
                } else {
                    // 速度大，则根据滑动的方向吸附
                    if (velocityX < 0) 1 else 0
                }
                val scrollDistance = if (targetPager == 1) width - scrollX else -scrollX
                overScroller.startScroll(
                    /* startX = */ scrollX,
                    /* startY = */ 0,
                    /* dx = */ scrollDistance,
                    /* dy = */ 0
                )
                invalidate()
            }
        }

        return true
    }

    /**
     * [computeScroll] 会在 [draw] 方法中被调用
     *
     * 那么流程就是: [OverScroller.startScroll] -> [invalidate] -> [draw] -> [computeScroll] -> [postInvalidateOnAnimation]
     * -> 下一帧绘制之前 [invalidate] -> [draw] -> [computeScroll] -> [postInvalidateOnAnimation] -> ...
     */
    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            // postInvalidateOnAnimation 会在下一帧绘制之前将视图标记为无效, 相当于 postOnAnimation { invalidate() }
            postInvalidateOnAnimation()
        }
    }
}


class TwoPagerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = TwoPager(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            /* width = */ ViewGroup.LayoutParams.MATCH_PARENT,
            /* height = */ ViewGroup.LayoutParams.MATCH_PARENT
        )

        listOf(
            Color.parseColor("#65d282"),
            Color.parseColor("#2b73dd")
        ).forEach { color ->
            addView(
                View(requireContext()).apply {
                    setBackgroundColor(color)
                    layoutParams = ViewGroup.LayoutParams(
                        /* width = */ ViewGroup.LayoutParams.MATCH_PARENT,
                        /* height = */ ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            )
        }
    }
}
