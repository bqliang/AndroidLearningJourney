package com.bqliang.customview.transformation

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class FlipView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val bitmapSize = 150.dp
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera().apply {
        rotateX(60f)
        setLocation(0f, 0f, -6f * resources.displayMetrics.density)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.translate(width / 2f, height / 2f) // 4.把猫移动回屏幕中心
        camera.applyToCanvas(canvas) // 3.旋转 X 轴
        canvas.translate(-width / 2f, -height / 2f) // 2.把猫移动到轴心
        canvas.drawBitmap(
            resources.loadCatBitmap(bitmapSize.toInt()),
            width / 2 - bitmapSize / 2,
            height / 2 - bitmapSize / 2,
            paint
        ) // 1.先在中间画一只猫
        // 倒着写
    }
}

class FlipViewFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FlipView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
