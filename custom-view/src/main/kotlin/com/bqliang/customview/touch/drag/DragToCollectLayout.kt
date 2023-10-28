package com.bqliang.customview.touch.drag

import android.content.ClipData
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bqliang.customview.R
import com.bqliang.customview.utils.dp

class DragToCollectLayout : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val dragListener: OnDragListener = MyDragListener()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20.dp
        textAlign = Paint.Align.CENTER
    }
    private val hintText = "Drag to collect"
    private val dragCollectArea = RectF()
    private val dragCollectAreaPadding = 10.dp
    private val dashPathEffect = DashPathEffect(floatArrayOf(4.dp, 4.dp), 0f)
    private val textBounds = Rect()

    init {
        setWillNotDraw(false)
        setOnDragListener(dragListener) // TODO 特别注意：这里的拖拽监听器是设置给 DragToCollectLayout 的
    }

    override fun onViewAdded(view: View) {
        super.onViewAdded(view)
        view.setOnLongClickListener { v ->
            ViewCompat.startDragAndDrop(
                /* v = */ v,
                /* data = */ ClipData.newPlainText("desc", v.contentDescription),
                /* shadowBuilder = */ DragShadowBuilder(v),
                /* myLocalState = */ null,
                /* flags = */ 0
            )
            false
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        dragCollectArea.set(
            /* left = */ 0 + dragCollectAreaPadding,
            /* top = */ h - h / 4 + dragCollectAreaPadding,
            /* right = */ w - dragCollectAreaPadding,
            /* bottom = */ h - dragCollectAreaPadding
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.dp
        paint.pathEffect = dashPathEffect
        canvas.drawRoundRect(
            /* rect = */ dragCollectArea,
            /* rx = */ 10.dp,
            /* ry = */ 10.dp,
            /* paint = */ paint
        )
        paint.pathEffect = null


        // 绘制文字
        paint.style = Paint.Style.FILL
        paint.getTextBounds(
            /* text = */ hintText,
            /* start = */ 0,
            /* end = */ "Drag to collect".length,
            /* bounds = */ textBounds
        )

        canvas.drawText(
            /* text = */ hintText,
            /* x = */
            dragCollectArea.centerX(),
            /* y = */
            dragCollectArea.centerY() - textBounds.bottom + (textBounds.bottom - textBounds.top) / 2f,
            /* paint = */
            paint
        )
    }

    private inner class MyDragListener : OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // 判断是否在收集区域内
                    if (dragCollectArea.contains(event.x, event.y)) {
                        val contentDescription = event.clipData.getItemAt(0).text
                        Toast.makeText(context, contentDescription, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            return true
        }
    }
}

class DragToCollectFragment : Fragment() {

    private var catImageViewId = -1
    private var androidImageViewId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DragToCollectLayout(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply {
            AppCompatImageView(requireContext()).apply {
                id = View.generateViewId().also { catImageViewId = it }
                contentDescription = "🐈"
                setImageResource(R.drawable.cat)
            }.also { addView(it) }

            AppCompatImageView(requireContext()).apply {
                id = View.generateViewId().also { androidImageViewId = it }
                contentDescription = "Android"
                setBackgroundColor(Color.parseColor("#3c3c3c"))
                setImageResource(R.drawable.android_robot)
            }.also { addView(it) }

            ConstraintSet().apply {
                // set ConstraintSet for catImageView
                connect(
                    /* startID = */ catImageViewId,
                    /* startSide = */ ConstraintSet.LEFT,
                    /* endID = */ ConstraintSet.PARENT_ID,
                    /* endSide = */ ConstraintSet.LEFT,
                    /* margin = */ 0
                )
                setDimensionRatio(catImageViewId, "1:1")
                constrainPercentWidth(catImageViewId, 0.5f)

                // set ConstraintSet for androidImageView
                connect(
                    /* startID = */ androidImageViewId,
                    /* startSide = */ ConstraintSet.RIGHT,
                    /* endID = */ ConstraintSet.PARENT_ID,
                    /* endSide = */ ConstraintSet.RIGHT,
                    /* margin = */ 0
                )
                setDimensionRatio(androidImageViewId, "1:1")
                constrainPercentWidth(androidImageViewId, 0.5f)
            }.also { setConstraintSet(it) }
        }
    }
}
