package com.bqliang.customview.clip

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.withSave
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class CircleAvatarViewWithClip: View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapWidth = 200.dp
    private val clipPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clipPath.addOval(
            /* left = */ width / 2 - bitmapWidth / 2,
            /* top = */ height / 2 - bitmapWidth / 2,
            /* right = */ width / 2 + bitmapWidth / 2,
            /* bottom = */ height / 2 + bitmapWidth / 2,
            /* dir = */ Path.Direction.CCW
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            canvas.clipPath(clipPath)
            canvas.drawBitmap(
                /* bitmap = */ resources.loadCatBitmap(bitmapWidth.toInt()),
                /* left = */ width / 2 - bitmapWidth / 2,
                /* top = */ height / 2 - bitmapWidth / 2,
                /* paint = */ paint
            )
        }
    }
}

class CircleAvatarViewWithClipFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = CircleAvatarViewWithClip(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
