package com.bqliang.customview.touch.drag

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment

class OnDragListenerGirdLayout : ViewGroup {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    companion object {
        private const val ROWS = 3
        private const val COLUMNS = 2
    }

    private var childWidth = 0
    private var childHeight = 0
    private val dragListener: OnDragListener = MyDragListener()
    private var draggedView: View? = null
    private var orderedChildren: MutableList<View> = ArrayList()

    init {
        isChildrenDrawingOrderEnabled = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 忽略 MeasureSpec.UNSPECIFIED 的情况
        val selfMeasureWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val selfMeasureHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        childWidth = selfMeasureWidthSize / COLUMNS
        childHeight = selfMeasureHeightSize / ROWS
        // 测量所有子 View
        measureChildren(
            /* widthMeasureSpec = */ MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
            /* heightMeasureSpec = */ MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        )
        // 设置自身尺寸
        setMeasuredDimension(
            /* measuredWidth = */ selfMeasureWidthSize,
            /* measuredHeight = */ selfMeasureHeightSize
        )
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        orderedChildren.add(child)
        child.setOnLongClickListener { view ->
            draggedView = view
            // [ViewCompat.startDragAndDrop] 方法
            view.startDragAndDrop(
                /* data = */ null, // data 只有在 onDrop() 时才能获取到: event.clipData
                /* shadowBuilder = */ DragShadowBuilder(view), // shadowBuilder 用于配置拖拽时 view 的样式
                /* myLocalState = */ view, // localState 随时可以通过 event.localState 获取
                /* flags = */ 0
            )
            /**
             * data: [ClipData] VS myLocalState: [Any]
             *
             * data 可以在 [DragEvent.ACTION_DROP] 时获取；而 myLocalState 可以在任何时候获取。
             * 在实用性上，二者的区别在于 data 可以跨进程传递
             */
            false
        }
        child.setOnDragListener(dragListener)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 统一放置在左上角
        children.forEachIndexed { index, child ->
            child.layout(
                /* l = */ 0,
                /* t = */ 0,
                /* r = */ childWidth,
                /* b = */ childHeight
            )

            // 使用 translationX 和 translationY 对子 View 进行偏移
            val row = index / COLUMNS
            val column = index % COLUMNS
            child.translationX = column * childWidth.toFloat() // child left
            child.translationY = row * childHeight.toFloat() // child top
        }
    }

    private inner class MyDragListener : OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                // 拖拽开始，也就是调用 startDrag() 时会被触发
                DragEvent.ACTION_DRAG_STARTED ->
                    if (v === event.localState) { // 如果当前 View 是被拖拽的 View，那么将其设置为不可见
                        v.visibility = View.INVISIBLE
                    }

                DragEvent.ACTION_DRAG_ENTERED ->
                    if (v !== event.localState) { // 拖拽手指进入 View 的区域
                        sort(v)
                    }

                DragEvent.ACTION_DRAG_LOCATION -> { // 拖拽手指在 View 的区域内移动

                }

                DragEvent.ACTION_DRAG_EXITED -> { // 拖拽手指离开 View 的区域

                }

                // 拖拽结束
                DragEvent.ACTION_DRAG_ENDED ->
                    if (v === event.localState) { // 如果当前 View 是被拖拽的 View，那么将其设置为可见
                        v.visibility = View.VISIBLE
                    }
            }
            return true
        }

        private fun sort(targetView: View) {
            var draggedIndex = -1
            var targetIndex = -1

            orderedChildren.forEachIndexed { index, child ->
                when {
                    targetView === child -> targetIndex = index
                    draggedView === child -> draggedIndex = index
                }
            }

            orderedChildren.removeAt(draggedIndex)
            orderedChildren.add(targetIndex, draggedView!!)

            orderedChildren.forEachIndexed { index, child ->
                val row = index / COLUMNS
                val column = index % COLUMNS
                val childLeft = column * childWidth
                val childTop = row * childHeight

                child.animate()
                    .setDuration(150)
                    .translationX(childLeft.toFloat())
                    .translationY(childTop.toFloat())
            }
        }
    }
}

class DragListenerFragment : Fragment() {

    companion object {
        private val COLORS = listOf(
            "#61ad51",
            "#633fb6",
            "#f4991b",
            "#da2e65",
            "#4497f1",
            "#4353b4",
        ).map { Color.parseColor(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = OnDragListenerGirdLayout(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        COLORS.forEach { color ->
            View(requireContext()).apply {
                setBackgroundColor(color)
            }.also { view -> addView(view) }
        }
    }
}
