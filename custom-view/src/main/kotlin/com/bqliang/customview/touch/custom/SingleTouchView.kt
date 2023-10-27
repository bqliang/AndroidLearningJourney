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

class SingleTouchView : View {
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
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                downOffsetX = offsetX
                downOffsetY = offsetY
            }

            MotionEvent.ACTION_MOVE -> {
                offsetX = event.x - downX + downOffsetX
                offsetY = event.y - downY + downOffsetY
                invalidate()
            }
        }
        return true
    }
}

class SingleTouchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SingleTouchView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
