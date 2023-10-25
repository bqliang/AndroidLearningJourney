package com.bqliang.customview.anim.animator

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp

class ViewPropertyAnimatorFragment : Fragment() {

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
            setBackgroundColor(Color.parseColor("#3dda86"))
            layoutParams = FrameLayout.LayoutParams(100.dp.toInt(), 100.dp.toInt())
        }.also { v ->
            root.addView(v)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView.setOnClickListener { v ->
            v.animate()
                .translationX(100.dp)
                .translationY(100.dp)
                .alpha(0.5f)
                .setDuration(1000)
                .start()
        }
    }
}