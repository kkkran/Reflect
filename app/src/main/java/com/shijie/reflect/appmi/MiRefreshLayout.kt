package com.shijie.reflect.appmi

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.OverScroller
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.core.view.NestedScrollingParent
import androidx.core.view.ViewCompat

class MiRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), NestedScrollingParent {

    private lateinit var refreshView: View // 刷新视图
    private var scrollableView: View? = null // 可滑动的内容视图
    private var isRefreshing = false // 是否正在刷新
    private val refreshHeight = 200 // 刷新触发的高度
    private var totalDragDistance = 0 // 总下拉距离
    private var refreshListener: (() -> Unit)? = null

    init {
        // 添加默认刷新视图
        refreshView = View(context).apply {
            setBackgroundColor(Color.BLUE)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, refreshHeight)
        }
        addView(refreshView) // 默认将刷新视图添加到顶部
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        // 找到可滑动的子视图
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child !== refreshView) {
                scrollableView = child
                break
            }
        }
        requireNotNull(scrollableView) { "CustomRefreshLayout must contain a scrollable view!" }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(refreshView, widthMeasureSpec, MeasureSpec.makeMeasureSpec(refreshHeight, MeasureSpec.EXACTLY))
        scrollableView?.let { measureChild(it, widthMeasureSpec, heightMeasureSpec) }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        refreshView.layout(0, -refreshHeight, width, 0) // 默认隐藏刷新视图
        scrollableView?.layout(0, 0, width, bottom - top)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (dy > 0 && totalDragDistance > 0) { // 上滑时缩回刷新视图
            val delta = Math.min(totalDragDistance, dy)
            totalDragDistance -= delta
            scrollBy(0, delta)
            consumed[1] = delta
        }
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        if (dyUnconsumed < 0 && !canScrollUp(scrollableView)) { // 下拉时处理刷新视图
            totalDragDistance += -dyUnconsumed
            scrollBy(0, -dyUnconsumed)
            if (totalDragDistance > refreshHeight) {
                triggerRefresh()
            }
        }
    }

    override fun onStopNestedScroll(target: View) {
        if (totalDragDistance < refreshHeight) {
            reset() // 未达到刷新条件，重置状态
        }
    }

    private fun triggerRefresh() {
        isRefreshing = true
        smoothScrollTo(-refreshHeight) // 固定显示刷新视图
        refreshListener?.invoke() // 触发刷新回调
    }

    private fun reset() {
        isRefreshing = false
        totalDragDistance = 0
        smoothScrollTo(0) // 重置到初始状态
    }

    private fun smoothScrollTo(targetY: Int) {
        val scroller = OverScroller(context)
        scroller.startScroll(0, scrollY, 0, targetY - scrollY, 300)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun computeScroll() {
        if (scrollY != 0) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun setOnRefreshListener(listener: () -> Unit) {
        this.refreshListener = listener
    }

    fun stopRefresh() {
        reset()
    }

    private fun canScrollUp(view: View?): Boolean {
        return view?.canScrollVertically(-1) ?: false
    }
}