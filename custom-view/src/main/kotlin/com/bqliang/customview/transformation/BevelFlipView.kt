package com.bqliang.customview.transformation

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.withSave
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class BevelFlipView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val bitmapSize = 150.dp
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera().apply {
        rotateX(70f)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            canvas.clipPath(
                Path().apply {
                    moveTo(width / 2f - bitmapSize / 2, height / 2f - bitmapSize / 2)
                    lineTo(width / 2f + bitmapSize / 2, height / 2f - bitmapSize / 2)
                    lineTo(width / 2f - bitmapSize / 2, height / 2f + bitmapSize / 2)
                    close()
                }
            ) // 2.裁剪出左上半部分
            canvas.drawBitmap(
                resources.loadCatBitmap(bitmapSize.toInt()),
                width / 2 - bitmapSize / 2,
                height / 2 - bitmapSize / 2,
                paint
            ) // 1.先在中间画一只猫
            // 倒着写 ↑
        }


        canvas.withSave {
            canvas.translate(width / 2f, height / 2f) // 7.把猫移动回屏幕中心
            canvas.rotate(-45f) // 6.将图片旋转回来
            camera.applyToCanvas(canvas) // 5.旋转 X 轴
            canvas.rotate(45f) // 4.旋转图片使得对角线和 X 轴平行
            canvas.clipPath(
                Path().apply {
                    moveTo(-bitmapSize / 2, +bitmapSize / 2)
                    lineTo(+bitmapSize / 2, +bitmapSize / 2)
                    lineTo(+bitmapSize / 2, -bitmapSize / 2)
                    close()
                }
            ) // 3.裁剪出右下半部分
            canvas.translate(-width / 2f, -height / 2f) // 2.把猫移动到轴心
            canvas.drawBitmap(
                resources.loadCatBitmap(bitmapSize.toInt()),
                width / 2 - bitmapSize / 2,
                height / 2 - bitmapSize / 2,
                paint
            ) // 1.先在中间画一只猫
            // 倒着写 ↑
        }


        paint.strokeWidth = 1.dp
        paint.color = Color.BLACK
        canvas.drawLine(
            width / 2f - bitmapSize / 2,
            height / 2f + bitmapSize / 2,
            width / 2f + bitmapSize / 2,
            height / 2f - bitmapSize / 2,
            paint
        )
    }
}

class BevelFlipViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = BevelFlipView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
