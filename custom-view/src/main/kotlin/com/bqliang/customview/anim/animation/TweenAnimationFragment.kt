package com.bqliang.customview.anim.animation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bqliang.customview.databinding.FragmentTweenAnimationBinding
import com.bqliang.customview.utils.dp

class TweenAnimationFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTweenAnimationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTweenAnimationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnScale.setOnClickListener(this)
        binding.btnAlpha.setOnClickListener(this)
        binding.btnRotate.setOnClickListener(this)
        binding.btnTranslate.setOnClickListener(this)
        binding.btnTranslateWithoutFillBefore.setOnClickListener(this)
        binding.btnSet.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.btnScale.id -> scale()
            binding.btnAlpha.id -> alpha()
            binding.btnRotate.id -> rotate()
            binding.btnTranslate.id -> translate()
            binding.btnTranslateWithoutFillBefore.id -> translate(fillBefore = false)
            binding.btnSet.id -> set()
        }
    }

    private fun scale() {
        ScaleAnimation(
            /* fromX = */ 0.5f,
            /* toX = */ 2f,
            /* fromY = */ 0.5f,
            /* toY = */ 2f,
            /* pivotXType = */ Animation.RELATIVE_TO_SELF,
            /* pivotXValue = */ 0.5f,
            /* pivotYType = */ Animation.RELATIVE_TO_SELF,
            /* pivotYValue = */ 0.5f
        ).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = Animation.RESTART
            fillAfter = true // 动画结束后保持最后的状态, 默认 false
        }.also { scaleAnimation ->
            binding.view.startAnimation(scaleAnimation)
        }
    }

    private fun alpha() {
        AlphaAnimation(
            /* fromAlpha = */ 1f,
            /* toAlpha = */ 0f
        ).apply {
            duration = 1000
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
        }.also { alphaAnimation ->
            binding.view.startAnimation(alphaAnimation)
        }
    }

    private fun rotate() {
        RotateAnimation(
            /* fromDegrees = */ 0f,
            /* toDegrees = */ 90f,
            /* pivotXType = */ Animation.RELATIVE_TO_SELF,
            /* pivotXValue = */ 0.5f,
            /* pivotYType = */ Animation.RELATIVE_TO_SELF,
            /* pivotYValue = */ 0.5f
        ).apply {
            duration = 1000
        }.also { rotateAnimation ->
            binding.view.startAnimation(rotateAnimation)
        }
    }

    private fun translate(fillBefore: Boolean = true) {
        // 初始位置：垂直方向 -> 原位置向上 1 个自己的高度； 水平方向 -> 原位置
        // 向下移动 3 个自己的高度，向右移动 100dp
        TranslateAnimation(
            /* fromXType = */ Animation.RELATIVE_TO_SELF,
            /* fromXValue = */ 0f,
            /* toXType = */ Animation.ABSOLUTE,
            /* toXValue = */ 100.dp,
            /* fromYType = */ Animation.RELATIVE_TO_SELF,
            /* fromYValue = */ -1f,
            /* toYType = */ Animation.RELATIVE_TO_SELF,
            /* toYValue = */ 2f
        ).apply {
            startOffset = 1500
            duration = 1000

            /**
             * fillBefore = false，那么在延迟这段时间里 view 会保持在原位置，会等到延迟结束后再从动画初始位置开始移动，动画是不包括延迟这段时间的
             * fillBefore = true (默认)，那么在一开始 view 就会移动到动画初始位置，等待延迟再开始移动，动画是包括延迟这段时间的
             */
            if (!fillBefore) {
                isFillEnabled =
                    true // 只有 isFillEnabled = true 时 fillBefore 才生效，否则 fillBefore 会被忽略直接当作是 true
                setFillBefore(false)
            }

            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    Toast.makeText(requireContext(), "start", Toast.LENGTH_SHORT).show()
                }

                override fun onAnimationEnd(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }.also { translateAnimation ->
            binding.view.startAnimation(translateAnimation)
        }
    }

    private fun set() {
        /**
         * AnimationSet 代表一组应一起播放的动画。每个单独动画的变换都会合并为一个变换。
         * 如果 AnimationSet 设置了其子动画也设置的任何属性（例如 duration 或 fillBefore），则 AnimationSet 的值会覆盖子动画的值。
         *
         * 一些应用于 AnimationSet 的 Animation 属性会影响 AnimationSet 本身，一些会向下推送到子代，还有一些会被忽略:
         * 1. duration, repeatMode, fillBefore, fillAfter: 这些属性在 AnimationSet 对象上设置后，将下推到所有子动画;
         * 2. repeatCount, fillEnabled: 对于 AnimationSet，这些属性将被忽略。
         * 3. startOffset, shareInterpolator: 这些属性适用于动画集本身。
         */

        val animationSet = AnimationSet(true)

        TranslateAnimation(
            /* fromXType = */ Animation.RELATIVE_TO_SELF,
            /* fromXValue = */ -1f,
            /* toXType = */ Animation.RELATIVE_TO_SELF,
            /* toXValue = */ 1f,
            /* fromYType = */ Animation.RELATIVE_TO_SELF,
            /* fromYValue = */ 0f,
            /* toYType = */ Animation.RELATIVE_TO_SELF,
            /* toYValue = */ 0f
        ).apply {

        }.also {
            animationSet.addAnimation(it)
        }

        TranslateAnimation(
            /* fromXType = */ Animation.RELATIVE_TO_SELF,
            /* fromXValue = */ 0f,
            /* toXType = */ Animation.RELATIVE_TO_SELF,
            /* toXValue = */ -1f,
            /* fromYType = */ Animation.RELATIVE_TO_SELF,
            /* fromYValue = */ 0f,
            /* toYType = */ Animation.RELATIVE_TO_SELF,
            /* toYValue = */ 0f
        ).apply {

        }.also {
            animationSet.addAnimation(it)
        }

        // 第一个动画向右移动两个自己的宽
        // 第二个动画向左移动一个自己的宽
        // 合成后，动画向右移动一个自己的宽

        animationSet.apply {
            duration = 1500
            startOffset = 1500
        }
        binding.view.startAnimation(animationSet)
    }
}
