package com.bqliang.customview.anim.animation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bqliang.customview.R

class FrameAnimWithAnimDrawableFragment : Fragment() {

    private lateinit var imageView: AppCompatImageView
    private lateinit var rocketAnimation: AnimationDrawable

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

        imageView = AppCompatImageView(requireContext()).apply {
            setBackgroundResource(R.drawable.rocket_thrust)
            rocketAnimation = background as AnimationDrawable
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER
            }
        }.also { root.addView(it) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView.setOnClickListener {
            rocketAnimation.start()
        }
    }
}
