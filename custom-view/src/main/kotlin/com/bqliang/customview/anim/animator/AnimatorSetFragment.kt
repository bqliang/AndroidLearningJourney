package com.bqliang.customview.anim.animator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.graphics.withSave
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class AnimatorSetFragment : Fragment() {

    class FlipView(context: Context) : View(context) {

        private val bitmapSize = 150.dp
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val camera = Camera().apply {
            setLocation(0f, 0f, -6f * resources.displayMetrics.density)
        }

        private var topFlip = 0f
            set(value) {
                field = value
                invalidate()
            }

        private var bottomFlip = 0f
            set(value) {
                field = value
                invalidate()
            }

        override fun onDraw(canvas: Canvas) {
            // 上半部分
            canvas.withSave {
                canvas.translate(width / 2f, height / 2f) // 5.把猫移动回屏幕中心
                camera.save()
                camera.rotateX(topFlip)
                camera.applyToCanvas(canvas) // 4.旋转 X 轴
                camera.restore()
                canvas.translate(-width / 2f, -height / 2f) // 3.把猫移动到轴心
                canvas.clipRect(
                    width / 2 - bitmapSize / 2,
                    height / 2f - bitmapSize / 2,
                    width / 2 + bitmapSize / 2,
                    height / 2f
                ) // 2.裁剪出上半部分
                canvas.drawBitmap(
                    resources.loadCatBitmap(bitmapSize.toInt()),
                    width / 2 - bitmapSize / 2,
                    height / 2 - bitmapSize / 2,
                    paint
                ) // 1.先在中间画一只猫
                // 倒着写 ↑
            }

            // 下半部分
            canvas.withSave {
                canvas.translate(width / 2f, height / 2f) // 5.把猫移动回屏幕中心
                camera.save()
                camera.rotateX(bottomFlip)
                camera.applyToCanvas(canvas) // 4.旋转 X 轴
                camera.restore()
                canvas.translate(-width / 2f, -height / 2f) // 3.把猫移动到轴心
                canvas.clipRect(
                    width / 2 - bitmapSize / 2,
                    height / 2f,
                    width / 2 + bitmapSize / 2,
                    height / 2 + bitmapSize / 2
                ) // 2.裁剪出下半部分
                canvas.drawBitmap(
                    resources.loadCatBitmap(bitmapSize.toInt()),
                    width / 2 - bitmapSize / 2,
                    height / 2 - bitmapSize / 2,
                    paint
                ) // 1.先在中间画一只猫
                // 倒着写 ↑
            }
        }
    }

    private lateinit var flipView: FlipView
    private lateinit var myView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        flipView = FlipView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }.also { root.addView(it) }

        myView = View(requireContext()).apply {
            setBackgroundColor(Color.parseColor("#0e9ed5"))
            layoutParams = FrameLayout.LayoutParams(80.dp.toInt(), 80.dp.toInt()).apply {
                gravity = Gravity.TOP or Gravity.LEFT
            }
        }.also { root.addView(it) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flipView.setOnClickListener {
            val topFlipAnimator = ObjectAnimator
                .ofFloat(flipView, "topFlip", 0f, -45f).apply {
                    duration = 1000
                }
            val bottomFlipAnimator = ObjectAnimator
                .ofFloat(flipView, "bottomFlip", 0f, 45f).apply {
                    duration = 1000
                }

            AnimatorSet().apply {
                // playTogether() // 同时播放
                playSequentially(topFlipAnimator, bottomFlipAnimator) // 顺序播放
                start()
            }

            // 虽然这里只是对同一个 view 的多个属性进行动画，
            // 如果是对一个或多个的 view 的一个或多个属性进行动画，也可以使用 AnimatorSet
        }


        myView.setOnClickListener {
            val colorObjAnimator = ObjectAnimator.ofArgb(
                myView,
                "backgroundColor",
                Color.parseColor("#0e9ed5"),
                Color.parseColor("#ffb900")
            )
            val translationXObjAnimator = ObjectAnimator.ofFloat(myView, "translationX", 0f, 200.dp)
            val rotationXObjAnimator = ObjectAnimator.ofFloat(myView, "rotationX", 0f, 45f)

            /**
             * play(anim): Builder 对象播放 anim 动画，
             * with(anim): anim 与 Builder 对象中的动画一起播放，
             * before(anim): Builder 对象中的动画在 anim 之前播放，
             * after(anim): Builder 对象中的动画在 anim 之后播放。
             */

            val animatorSet = AnimatorSet()
            animatorSet
                .play(colorObjAnimator)
                .before(translationXObjAnimator)
                .with(rotationXObjAnimator)
            // rotationXObjAnimator 和 colorObjAnimator 一起播放，而且是在 translationXObjAnimator 之前

            animatorSet.duration = 3000
            animatorSet.start()
        }
    }
}
