package com.bqliang.customview.xfermode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class CircleAvatarViewWithXfermode : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val avatarWidth = 250.dp
    private val bounds = RectF()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds.set(
            /* left = */ w / 2 - avatarWidth / 2,
            /* top = */ h / 2 - avatarWidth / 2,
            /* right = */ w / 2 + avatarWidth / 2,
            /* bottom = */ h / 2 + avatarWidth / 2
        )
    }

    override fun onDraw(canvas: Canvas) {
        // 开启离屏缓冲
        val count = canvas.saveLayer(bounds, null)
        canvas.drawOval(bounds, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(
            /* bitmap = */ resources.loadCatBitmap(avatarWidth.toInt()),
            /* src = */ null,
            /* dst = */ bounds,
            /* paint = */ paint
        )
        paint.xfermode = null
        // 关闭离屏缓冲
        canvas.restoreToCount(count)
    }
}

class CircleAvatarViewWithXfermodeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = CircleAvatarViewWithXfermode(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
