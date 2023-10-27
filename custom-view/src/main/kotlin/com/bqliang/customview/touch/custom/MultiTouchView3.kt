package com.bqliang.customview.touch.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.fragment.app.Fragment

/**
 * 多点触控 (各自为战型)
 */
class MultiTouchView3 : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val paths = SparseArray<Path>()

    override fun onDraw(canvas: Canvas) {
        paths.forEach { _, path ->
            canvas.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val path = Path()
                path.moveTo(event.getX(event.actionIndex), event.getY(event.actionIndex))
                paths.put(event.getPointerId(event.actionIndex), path)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                paths.forEach { pointerId, path ->
                    val pointerIndex = event.findPointerIndex(pointerId)
                    path.lineTo(event.getX(pointerIndex), event.getY(pointerIndex))
                }
                invalidate()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                paths.remove(event.getPointerId(event.actionIndex))
                invalidate()
            }
        }
        return true
    }
}

class MultiTouchFragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = MultiTouchView3(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
