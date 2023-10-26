package com.bqliang.customview.touch

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class DoubleTapScalableImageView : View, GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener, Runnable {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    companion object {
        private val IMAGE_SIZE = 300.dp
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val canvasMatrix = Matrix()
    private val bitmap by lazy { resources.loadCatBitmap(IMAGE_SIZE.toInt()) }
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var offsetX = 0f // scroll offset
    private var offsetY = 0f // scroll offset
    private var smallScale = 0f
    private var bigScale = 0f
    private var isBig = false

    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val scaleAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f)
    }

    private val gestureDetector = GestureDetectorCompat(context, this).apply {
        setIsLongpressEnabled(false) // disable long press
        // 实际上这里可以不用设置，因为如果构造 GestureDetectorCompat 时传入的 OnGestureListener 对象也实现了 OnDoubleTapListener 接口，
        // 那么 GestureDetectorCompat 会自动调用 setOnDoubleTapListener 方法
        setOnDoubleTapListener(this@DoubleTapScalableImageView)
    }

    private val overScroller = OverScroller(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (width - bitmap.width) / 2f
        originalOffsetY = (height - bitmap.height) / 2f

        // calculate scale
        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat()
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvasMatrix.apply {
            reset()
            val scale = smallScale + (bigScale - smallScale) * scaleFraction
            postScale(scale, scale, width / 2f, height / 2f)
            postTranslate(offsetX * scaleFraction, offsetY * scaleFraction)
        }.also { canvas.setMatrix(it) }
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean = true

    override fun onShowPress(e: MotionEvent) {}

    // 单击事件，如果不支持双击，可以在这里处理单击事件
    override fun onSingleTapUp(e: MotionEvent): Boolean = false

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (isBig) {
            offsetX -= distanceX
            offsetY -= distanceY
            reviseOffset()
            invalidate()
        }
        return false
    }

    /**
     * 限制偏移量
     *
     */
    private fun reviseOffset() {
        val overX = (bitmap.width * bigScale - width) / 2f
        val overY = (bitmap.height * bigScale - height) / 2f
        offsetX = offsetX.coerceIn(-overX, overX)
        offsetY = offsetY.coerceIn(-overY, overY)
    }

    override fun onLongPress(e: MotionEvent) {}

    // 惯性滑动
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (isBig) {
            overScroller.fling(
                // 模型原点
                /* startX = */ offsetX.toInt(), /* startY = */ offsetY.toInt(),
                /* velocityX = */ velocityX.toInt(),
                /* velocityY = */ velocityY.toInt(),
                // 模型范围（相对于模型原点），用于限制滑动范围
                /* minX = */ (-bitmap.width * bigScale + width).toInt() / 2,
                /* maxX = */ (bitmap.width * bigScale - width).toInt() / 2,
                /* minY = */ (-bitmap.height * bigScale + height).toInt() / 2,
                /* maxY = */ (bitmap.height * bigScale - height).toInt() / 2,
                /* overX = */ 50.dp.toInt(),
                /* overY = */ 50.dp.toInt()
            )

            /**
             * [post] VS [postOnAnimation]
             * [post] 会将 [Runnable] 对象放在队列中等待执行
             * 而 [postOnAnimation] 会在下一帧立即执行 [Runnable] 对象
             */
            ViewCompat.postOnAnimation(this, this)
        }
        return false
    }

    // 计算惯性滑动的偏移量
    override fun run() {
        if (overScroller.computeScrollOffset()) {
            offsetX = overScroller.currX.toFloat()
            offsetY = overScroller.currY.toFloat()
            invalidate()
            ViewCompat.postOnAnimation(this, this)
        }
    }

    // 支持双击时，单击的判断会受到影响，因此需要在 onSingleTapConfirmed 中处理单击事件
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean = false

    override fun onDoubleTap(e: MotionEvent): Boolean {
        isBig = !isBig
        if (isBig) {
            // 计算双击后的偏移量
            // (e.x - width / 2f) 是放大前（small scale），双击点与缩放原点的横向距离
            // 这个横向偏移 * (bigScale / smallScale) 就是放大后（big scale），双击点与缩放原点的横向距离
            // 二者的差值就是 big scale 的初始 offsetX
            // 也就是 (e.x - width / 2f) * (bigScale / smallScale) - (e.x - width / 2f)
            // 化简就是 (e.x - width / 2f) * (bigScale / smallScale - 1)
            // 但是我们的目的是要消除这个偏移，所以要取反
            // 也就是 offsetX = - (e.x - width / 2f) * (bigScale / smallScale - 1)
            // 再化简就是 offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
            offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)

            // 限制偏移量
            reviseOffset()

            scaleAnimator.start()
        } else {
            scaleAnimator.reverse()
        }
        return false
    }

    // 用于接收双击事件的后续事件，如双击后的滑动
    override fun onDoubleTapEvent(e: MotionEvent): Boolean = false
}


class DoubleTapScalableImageViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DoubleTapScalableImageView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
