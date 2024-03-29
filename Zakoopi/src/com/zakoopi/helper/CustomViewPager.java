package com.zakoopi.helper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	private static boolean swipeable = true;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// Call this method in your motion events when you want to disable or enable
	// It should work as desired.
	public static void setSwipeable(boolean swipeable1) {
		swipeable = swipeable1;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return (this.swipeable) ? super.onInterceptTouchEvent(arg0) : false;
	}

}
