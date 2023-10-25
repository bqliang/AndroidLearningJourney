package com.bqliang.customview.anim.animator

import android.animation.TimeInterpolator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class AdvancedValueAnimatorFragment : Fragment() {

    private lateinit var myView: View
    private lateinit var tv: AppCompatTextView

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

        myView = View(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(150.dp.toInt(), 150.dp.toInt())
            setBackgroundColor(Color.parseColor("#3ddc84"))
        }.also { root.addView(it) }

        tv = AppCompatTextView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = (Gravity.BOTTOM or Gravity.END)
                setMargins(16.dp.toInt())
            }
            text = "A"
            textSize = 20.dp
        }.also { root.addView(it) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView.setOnClickListener {
            ValueAnimator.ofFloat(
                0f,
                (myView.parent as FrameLayout).height - myView.height.toFloat()
            ).apply {
                duration = 2000
                interpolator = BounceInterpolator()
                // setEvaluator()
                addUpdateListener { valueAnimator ->
                    myView.layout(
                        myView.left,
                        (valueAnimator.animatedValue as Float).toInt(),
                        myView.right,
                        (valueAnimator.animatedValue as Float).toInt() + myView.height
                    )
                }
            }.start()
        }


        tv.setOnClickListener {
            ValueAnimator.ofObject(CharEvaluator(),'A', 'Z').apply {
                interpolator = AccelerateInterpolator()
                duration = 2000
                addUpdateListener { valueAnimator ->
                    tv.text = valueAnimator.animatedValue.toString()
                }
            }.start()
        }
    }

    class CustomInterpolator : TimeInterpolator {
        override fun getInterpolation(input: Float): Float {
            // TODO 根据时间进度，返回动作进度
            return 0f
        }
    }

    class CharEvaluator : TypeEvaluator<Char> {
        // TODO 根据动作进度，计算返回属性值
        override fun evaluate(fraction: Float, startValue: Char, endValue: Char): Char =
            // char to ascii
            Char(code = (startValue.code + fraction * (endValue.code - startValue.code)).toInt())
    }
}
