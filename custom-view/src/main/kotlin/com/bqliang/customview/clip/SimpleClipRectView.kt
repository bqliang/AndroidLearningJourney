package com.bqliang.customview.clip

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.withSave
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class SimpleClipRectView: View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    private val bitmapSize = 200.dp
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            canvas.clipRect(0f, 0f, bitmapSize / 2, bitmapSize / 2)
            canvas.drawBitmap(resources.loadCatBitmap(bitmapSize.toInt()), 0f, 0f, paint)
        }
    }
}

class SimpleClipRectViewFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SimpleClipRectView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
