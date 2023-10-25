package com.bqliang.customview.anim.animator

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class ObjAnimatorFragment : Fragment() {

    class CircleView(context: Context) : View(context) {

        private val color = Color.parseColor("#e73f32")
        private var radius = 50.dp
            set(value) {
                field = value
                invalidate()
            }
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = this@CircleView.color
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawCircle(width / 2f, height / 2f, radius, paint)
        }
    }

    private lateinit var circleView: CircleView

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

        circleView = CircleView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
        }.also {
            root.addView(it)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        circleView.setOnClickListener {
            /**
             * ofXXX 可以填入多个值，当填入多个值时，第一个值被当作起点，
             * 但如果只填一个值，那么这个值会被当作终点，会尝试通过反射调用 getXXX 来获取起点
             * 如果 getXXX 是 private，获取起点失败，那么会以默认值 0 为起点
             */

            // 这里因为 CircleView 的 getRadius 是 private 的，所以会以默认值 0 为起点
            ObjectAnimator
                .ofFloat(circleView, "radius", 150f.dp)
                .start()
        }
    }
}
