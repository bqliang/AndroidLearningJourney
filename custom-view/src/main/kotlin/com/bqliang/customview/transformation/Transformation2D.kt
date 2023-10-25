package com.bqliang.customview.transformation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class Transformation2D: View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    private val bitmapSize = 150.dp
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#f6b5b6")
    }

    override fun onDraw(canvas: Canvas) {
        // draw1(canvas)
        // draw2(canvas)
        draw3(canvas)
    }

    // 正着思考，变换都应用于 canvas 坐标系
    private fun draw1(canvas: Canvas) {
        canvas.translate((width - bitmapSize) / 2f, (height - bitmapSize) / 2f) // 1.将 canvas 坐标系向右下角移动
        canvas.rotate(45f, bitmapSize / 2f, bitmapSize / 2f) // 2.旋转坐标系，注意旋转的中心点是 (bitmapSize / 2)，因为上一步已经移动了坐标系
        canvas.drawBitmap(resources.loadCatBitmap(bitmapSize.toInt()), 0f, 0f, paint) // 3.画出图形

        // 用这种方式，每次 canvas 坐标系发生变换后，需要在心里想象出新的坐标系，然后再画图形
    }

    // 倒着思考，视为"变换都应用于 View/图形，坐标系不发生变化"
    private fun draw2(canvas: Canvas) {
        canvas.rotate(45f, width / 2f, height / 2f) // 3.旋转（视为坐标系没有变化）
        canvas.translate((width - bitmapSize) / 2f, (height - bitmapSize) / 2f) // 2.将图形移动到中心位置
        canvas.drawBitmap(resources.loadCatBitmap(bitmapSize.toInt()), 0f, 0f, paint) // 1.先在左上角画出图形
        // 从这里往上看 ↑
    }

    // 倒着思考但正着写代码
    private fun draw3(canvas: Canvas) {
        val matrix = Matrix()
        matrix.apply {
            reset()
            preTranslate((width - bitmapSize) / 2f, (height - bitmapSize) / 2f)
            preRotate(45f, bitmapSize / 2f, bitmapSize / 2f)
        }
        canvas.setMatrix(matrix)
        canvas.drawBitmap(resources.loadCatBitmap(bitmapSize.toInt()), 0f, 0f, paint)
    }
}

class Transformation2DFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = Transformation2D(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
