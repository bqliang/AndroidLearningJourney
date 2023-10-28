package com.bqliang.customview.touch.drag

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import kotlin.math.abs

class DragUpDownLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val dropCallback: ViewDragHelper.Callback = DropCallback()
    private val dragHelper = ViewDragHelper.create(this, dropCallback)
    private val viewConfiguration = ViewConfiguration.get(context)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
        dragHelper.shouldInterceptTouchEvent(ev)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private inner class DropCallback : ViewDragHelper.Callback() {

        private var capturedTop = 0

        override fun tryCaptureView(child: View, pointerId: Int): Boolean =
            child.id == DragUpDownFragment.dragUpDownViewId

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            capturedTop = capturedChild.top
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = top

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val finalTop = if (abs(yvel) < viewConfiguration.scaledMinimumFlingVelocity) { // 达到快滑阈值
                if (yvel < 0) 0 else (height - releasedChild.height)
            } else { // 没有达到快滑阈值，根据滑动距离进行吸附
                val centerYReleasedChild = releasedChild.top + releasedChild.height / 2
                if (centerYReleasedChild < height / 2) 0 else (height - releasedChild.height)
            }
            dragHelper.settleCapturedViewAt(releasedChild.left, finalTop)
            invalidate()
        }
    }
}

class DragUpDownFragment : Fragment() {

    companion object {
        var dragUpDownViewId = -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DragUpDownLayout(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply {
            View(requireContext()).apply {
                id = View.generateViewId().also { dragUpDownViewId = it }
                setBackgroundColor(Color.parseColor("#3ca752"))
                layoutParams = FrameLayout.LayoutParams(
                    /* width = */ ViewGroup.LayoutParams.MATCH_PARENT,
                    /* height = */ 200.dp.toInt()
                )
            }.also { addView(it) }
        }
    }
}