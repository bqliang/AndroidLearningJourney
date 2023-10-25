package com.bqliang.customview.simpledraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import kotlin.math.cos
import kotlin.math.sin

class PieView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val angles = listOf(60f, 100f, 150f, 50f)
    private val colors = listOf(
        "#fe878d",
        "#faddaf",
        "#c1f198",
        "#b2d9f7"
    ).map { Color.parseColor(it) }
    private val protrudeIndex = 1

    private val radius = 120.dp
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val offset = 20.dp

    override fun onDraw(canvas: Canvas) {
        var startAngle = 0f
        for ((index, angle) in angles.withIndex()) {
            paint.color = colors[index]
            if (index == protrudeIndex) {
                canvas.save()
                canvas.translate(
                    /* dx = */ offset * cos(Math.toRadians((startAngle + angle / 2f).toDouble())).toFloat(),
                    /* dy = */
                    offset * sin(Math.toRadians((startAngle + angle / 2f).toDouble())).toFloat()
                )
            }

            canvas.drawArc(
                /* left = */ width / 2f - radius,
                /* top = */ height / 2f - radius,
                /* right = */ width / 2f + radius,
                /* bottom = */ height / 2f + radius,
                /* startAngle = */ startAngle,
                /* sweepAngle = */ angle,
                /* useCenter = */ true,
                /* paint = */ paint
            )

            if (index == protrudeIndex) {
                canvas.restore()
            }
            startAngle += angle
        }
    }
}

class PieViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = PieView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}