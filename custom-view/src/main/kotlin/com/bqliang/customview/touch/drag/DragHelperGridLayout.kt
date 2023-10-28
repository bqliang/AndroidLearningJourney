package com.bqliang.customview.touch.drag

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import androidx.fragment.app.Fragment

class DragHelperGridLayout : ViewGroup {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    companion object {
        private const val ROWS = 3
        private const val COLUMNS = 2
    }

    private val viewDragHelper = ViewDragHelper.create(this, 1f, MyDragHelperCallback())
    private var childWidth = 0
    private var childHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 忽略 MeasureSpec.UNSPECIFIED 的情况
        val selfMeasureWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val selfMeasureHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        childWidth = selfMeasureWidthSize / COLUMNS
        childHeight = selfMeasureHeightSize / ROWS
        // 测量所有子 View
        measureChildren(
            /* widthMeasureSpec = */ MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
            /* heightMeasureSpec = */ MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        )
        // 设置自身尺寸
        setMeasuredDimension(
            /* measuredWidth = */ selfMeasureWidthSize,
            /* measuredHeight = */ selfMeasureHeightSize
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
            val row = index / COLUMNS
            val column = index % COLUMNS
            val childLeft = column * childWidth
            val childTop = row * childHeight
            val childRight = childLeft + childWidth
            val childBottom = childTop + childHeight
            child.layout(childLeft, childTop, childRight, childBottom)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
        viewDragHelper.shouldInterceptTouchEvent(ev)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private inner class MyDragHelperCallback : ViewDragHelper.Callback() {
        private var capturedLeft = 0
        private var capturedTop = 0

        // tryCaptureView 返回 true 表示允许拖动
        override fun tryCaptureView(child: View, pointerId: Int): Boolean = true

        // 当 View 被捕获（即 tryCaptureView 返回 true）时调用
        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            // 使其不会被遮挡
            capturedChild.elevation = elevation + 1
            capturedLeft = capturedChild.left
            capturedTop = capturedChild.top
        }

        // clampViewPositionHorizontal 和 clampViewPositionVertical 用于限制 View 的移动范围
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int = left
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = top

        // 松手时调用
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            releasedChild.elevation = elevation - 1
            viewDragHelper.settleCapturedViewAt(capturedLeft, capturedTop)
            invalidate()
        }

        override fun onViewDragStateChanged(state: Int) {
            when(state) {
                ViewDragHelper.STATE_IDLE -> {
                    // 非拖拽或者动画结束
                }
                ViewDragHelper.STATE_DRAGGING -> {
                    // 正在拖拽
                }
                ViewDragHelper.STATE_SETTLING -> {
                    // 拖拽结束后自动移动到指定位置
                }
            }
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {

        }
    }
}

class DragHelperFragment : Fragment() {

    companion object {
        private val COLORS = listOf(
            "#61ad51",
            "#633fb6",
            "#f4991b",
            "#da2e65",
            "#4497f1",
            "#4353b4",
        ).map { Color.parseColor(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DragHelperGridLayout(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        COLORS.forEach {
            View(requireContext()).apply {
                setBackgroundColor(it)
            }.also { view -> addView(view) }
        }
    }
}