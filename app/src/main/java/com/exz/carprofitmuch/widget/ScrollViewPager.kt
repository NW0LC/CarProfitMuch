package com.exz.carprofitmuch.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by 史忠文
 * on 2017/11/13.
 */
class ScrollViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setScanScroll(canScroll: Boolean) {
        isCanScroll = canScroll
    }

    override fun performClick(): Boolean {
        return if (!isCanScroll)
            false
        else
            super.performClick()
    }
    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        /* return false;//super.onTouchEvent(arg0); */
        performClick()
        return if (!isCanScroll)
            false
        else
            super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (!isCanScroll)
            false
        else
            super.onInterceptTouchEvent(arg0)
    }

    companion object {
        private var isCanScroll = true
    }
}
