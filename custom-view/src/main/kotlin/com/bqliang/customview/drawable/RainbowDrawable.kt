package com.bqliang.customview.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import kotlin.math.sin
import kotlin.math.tan

/**
 * Rainbow drawable
 *
 * Copy from ![Implementing Custom Drawables — part 1](https://blog.zen.ly/implementing-custom-drawables-part-1-5530a98cefc9)
 */
class RainbowDrawable : Drawable() {

    private val pink = Color.parseColor("#da2667")
    private val orange = Color.parseColor("#f2811a")
    private val yellow = Color.parseColor("#f9d420")
    private val green = Color.parseColor("#b6f72a")
    private val emeraldGreen = Color.parseColor("#6ee07a")
    private val azurBlue = Color.parseColor("#62eef0")
    private val blue = Color.parseColor("#51c7fe")
    private val ceruleanBlue = Color.parseColor("#81a4fe")
    private val purple = Color.parseColor("#9b78fc")

    @ColorRes
    private val rainbowColors = intArrayOf(
        pink,
        orange,
        yellow,
        green,
        emeraldGreen,
        azurBlue,
        blue,
        ceruleanBlue,
        purple
    )

    private val RAINBOW_ANGLE = Math.toRadians(30.0)

    private val colorCount = rainbowColors.size
    private val colors = IntArray(colorCount * 2)
    private val positions = FloatArray(colorCount * 2)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        rainbowColors.forEachIndexed { i, color ->
            colors[i * 2] = color
            colors[i * 2 + 1] = color
            positions[i * 2] = i / colorCount.toFloat()
            positions[i * 2 + 1] = (i + 1) / colorCount.toFloat()
        }

        val width = 360.dp // pay attention, not the every rainbow's width
        val y1 = (sin(2 * RAINBOW_ANGLE) / 2 * width).toFloat()
        val x1 = (width - tan(RAINBOW_ANGLE) * y1).toFloat()
        paint.shader = LinearGradient(
            /* x0 = */ 0f,
            /* y0 = */ 0f,
            /* x1 = */ x1,
            /* y1 = */ y1,
            /* colors = */ colors,
            /* positions = */ positions,
            /* tile = */ Shader.TileMode.REPEAT
        )
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(bounds, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getAlpha(): Int = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getColorFilter(): ColorFilter? = paint.colorFilter

    // Android 29 及之后，这个方法不再被调用
    override fun getOpacity(): Int = PixelFormat.OPAQUE
}


class CustomDrawableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AppCompatImageView(requireContext()).apply {
        background = RainbowDrawable()
        layoutParams = ViewGroup.LayoutParams(
            /* width = */ ViewGroup.LayoutParams.MATCH_PARENT,
            /* height = */ 250.dp.toInt()
        )
    }
}
