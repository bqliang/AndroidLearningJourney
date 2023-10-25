package com.bqliang.customview.anim.animator

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class PropertyValueHolderFragment : Fragment() {

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
            setBackgroundColor(Color.parseColor("#2e7cee"))
            layoutParams = FrameLayout.LayoutParams(80.dp.toInt(), 80.dp.toInt())
        }.also { root.addView(it) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView.setOnClickListener {
            val moveLength = (myView.parent as FrameLayout).height - myView.height
            val keyFrames = arrayOf(
                Keyframe.ofFloat(0f, 0f * moveLength),
                Keyframe.ofFloat(0.25f, 0.4f * moveLength),
                Keyframe.ofFloat(0.75f, 0.6f * moveLength),
                Keyframe.ofFloat(1f, 1f * moveLength)
            )
            // 构通过 KeyFrame 来构建 PropertyValuesHolder
            val propertyValuesHolder = PropertyValuesHolder.ofKeyframe("translationY", *keyFrames)
            // 通过 PropertyValuesHolder 来构建 ObjectAnimator
            ObjectAnimator
                .ofPropertyValuesHolder(view, propertyValuesHolder)
                .setDuration(1500)
                .start()

            /**
             * 其实 [ValueAnimator.ofInt] 和 [ObjectAnimator.ofFloat]... 底层都是通过 [PropertyValuesHolder] 来实现的
             */
        }
    }
}
