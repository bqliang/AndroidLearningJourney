package com.bqliang.customview.layout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bqliang.customview.R
import com.bqliang.customview.utils.dp
import kotlin.math.min

class AlwaysSquareImageView(context: Context) : AppCompatImageView(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec) // 先让父类测量
        val minSize = min(measuredWidth, measuredHeight) // 然后取最小值
        setMeasuredDimension(minSize, minSize) // 保证宽高一致

        /**
         * [View.getMeasuredWidth] VS [View.getWidth]
         * [View.getMeasuredWidth] 是测量阶段自己期望的大小，
         * 而 [View.getWidth] 是布局阶段实际的大小
         */
    }
}

class AlwaysSquareImageViewFragment : Fragment() {
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

        AlwaysSquareImageView(requireContext()).apply {
            setImageResource(R.drawable.cat)
            scaleType = ImageView.ScaleType.CENTER_CROP
            layoutParams = ViewGroup.LayoutParams(200.dp.toInt(), 200.dp.toInt())
        }.also { root.addView(it) }

        return  root
    }
}
