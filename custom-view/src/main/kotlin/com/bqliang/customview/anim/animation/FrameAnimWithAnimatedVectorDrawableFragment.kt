package com.bqliang.customview.anim.animation

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bqliang.customview.R

class FrameAnimWithAnimatedVectorDrawableFragment : Fragment() {

    private lateinit var imageView: AppCompatImageView
    private var playing = false

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
            setImageResource(R.drawable.pause_to_play_anim)
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
            if (playing) {
                imageView.setImageResource(R.drawable.pause_to_play_anim)
            } else {
                imageView.setImageResource(R.drawable.play_to_pause_anim)
            }
            playing = !playing

            imageView.drawable?.let {
                when (it) {
                    is AnimatedVectorDrawable -> it.start()
                    is AnimatedVectorDrawableCompat -> it.start()
                }
            }
        }
    }
}
