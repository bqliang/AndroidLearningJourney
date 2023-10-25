package com.bqliang.customview.anim.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.animation.addPauseListener
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class SimpleValueAnimatorFragment : Fragment(), Animator.AnimatorListener {

    private val TAG = SimpleValueAnimatorFragment::class.simpleName
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

        myView = View(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(200.dp.toInt(), 200.dp.toInt())
            setBackgroundColor(Color.parseColor("#eb7171"))
        }.also { root.addView(it) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView.setOnClickListener {
            ValueAnimator.ofArgb(
                Color.parseColor("#eb7171"),
                Color.parseColor("#57965c")
            ).apply {
                duration = 1000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                addUpdateListener { valueAnimator ->
                    val color = valueAnimator.animatedValue as Int
                    myView.setBackgroundColor(color)
                }
                addListener(this@SimpleValueAnimatorFragment)
                addPauseListener(
                    onPause = { },
                    onResume = { }
                )
            }.start()
        }
    }

    override fun onAnimationStart(animation: Animator) {
        Log.d(TAG, "onAnimationStart")
    }

    override fun onAnimationEnd(animation: Animator) {
        Log.d(TAG, "onAnimationEnd")
    }

    override fun onAnimationCancel(animation: Animator) {
        Log.d(TAG, "onAnimationCancel")
    }

    override fun onAnimationRepeat(animation: Animator) {
        Log.d(TAG, "onAnimationRepeat")
    }
}