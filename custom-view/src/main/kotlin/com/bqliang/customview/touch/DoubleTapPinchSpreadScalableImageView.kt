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
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bqliang.customview.utils.dp
import com.bqliang.customview.utils.loadCatBitmap

class DoubleTapPinchSpreadScalableImageView : View {
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
    private var offsetX = 0f
    private var offsetY = 0f
    private var smallScale = 0f
    private var bigScale = 0f
    private var isBig = false

    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val scaleAnimator =
        ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)

    private val myGestureListener = MyGestureListener()
    private val myScaleGestureListener = MyScaleGestureListener()
    private val flingRunner = FlingRunner()
    private val gestureDetector = GestureDetectorCompat(context, myGestureListener).apply {
        setIsLongpressEnabled(false) // disable long press
    }
    private val scaleGestureDetector = ScaleGestureDetector(context, myScaleGestureListener)

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

        currentScale = smallScale
        scaleAnimator.setFloatValues(smallScale, bigScale)
    }

    override fun onDraw(canvas: Canvas) {
        canvasMatrix.apply {
            reset()
            postScale(currentScale, currentScale, width / 2f, height / 2f)
            val scaleFraction = (currentScale - smallScale) / (bigScale - smallScale)
            postTranslate(offsetX * scaleFraction, offsetY * scaleFraction)
        }.also { canvas.setMatrix(it) }
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean = true

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
                ViewCompat.postOnAnimation(this@DoubleTapPinchSpreadScalableImageView, flingRunner)
            }
            return false
        }

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
    }

    inner class MyScaleGestureListener: ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            // 与计算双击后的偏移量类似
            offsetX = (detector.focusX - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (detector.focusY - height / 2f) * (1 - bigScale / smallScale)
            return super.onScaleBegin(detector)
        }

        /**
         * @return 是否消费事件，如果不消费，那么 scaleFactor 是相对于缩放起点而言，也就是会累积；
         * 如果消费，那么 scaleFactor 是相对于上一次缩放而言，也就是不会累积
         */
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // currentScale: [0, +∞)
            // detector.scaleFactor: [0, +∞)

            currentScale = (currentScale * detector.scaleFactor).coerceIn(smallScale, bigScale)
            // 这里要返回 true 消费事件，因为我们计算 currentScale 时是以自身(currentScale)为基准的
            // 那么 scaleFactor 就不能累积
            return true
        }
    }

    inner class FlingRunner: Runnable {
        // 计算惯性滑动的偏移量
        override fun run() {
            if (overScroller.computeScrollOffset()) {
                offsetX = overScroller.currX.toFloat()
                offsetY = overScroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@DoubleTapPinchSpreadScalableImageView, this)
            }
        }
    }
}


class DoubleTapPinchSpreadScalableImageViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DoubleTapPinchSpreadScalableImageView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
