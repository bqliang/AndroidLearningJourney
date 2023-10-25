package com.bqliang.customview.simpledraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathMeasure
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import kotlin.math.cos
import kotlin.math.sin

class DashBoardView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    companion object {
        private const val OPEN_ANGLE = 120f
        private const val MARK = 20
        private val radius = 100.dp
        private val dashWidth = 2.dp
        private val dashHeight = 10.dp
        private val needleLength = 80.dp
    }

    private var currentMark = 17


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val dashPath = Path()
    private lateinit var pathEffect: PathDashPathEffect

    init {
        paint.apply {
            color = Color.parseColor("#3b5b10")
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        dashPath.addRect(0f, 0f, dashWidth, dashHeight, Path.Direction.CCW)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        path.reset()
        path.addArc(
            /* left = */ w / 2f - radius,
            /* top = */ h / 2f - radius,
            /* right = */ w / 2f + radius,
            /* bottom = */ h / 2f + radius,
            /* startAngle = */ 90 + OPEN_ANGLE / 2f,
            /* sweepAngle = */ 360 - OPEN_ANGLE
        )

        val pathMeasure = PathMeasure(path, /* forceClosed = */ false).length

        // 注意这里 Google 的文档把 phase 和 advance 搞反了
        pathEffect = PathDashPathEffect(
            /* shape = */ dashPath,
            /* phase = */ (pathMeasure - dashWidth) / MARK,
            /* advance = */0f,
            /* style = */ PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas) {
        // 弧线
        canvas.drawPath(path, paint)

        // 刻度
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        // 指针
        canvas.drawLine(
            /* startX = */ width / 2f,
            /* startY = */
            height / 2f,
            /* stopX = */
            width / 2f + needleLength * cos(Math.toRadians(getAngleFromMark(currentMark).toDouble())).toFloat(),
            /* stopY = */
            height / 2f + needleLength * sin(Math.toRadians(getAngleFromMark(currentMark).toDouble())).toFloat(),
            paint
        )
    }

    private fun getAngleFromMark(mark: Int) =
        /* 表盘起始角度 */ (90 + OPEN_ANGLE / 2f) +
            /* 当前刻度扫过的角度 */ (360 - OPEN_ANGLE) / MARK * mark
}

class DashBoardViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DashBoardView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
