package com.customphotoview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TrickyViewPager extends ViewPager {
    private boolean isLocked;

    public TrickyViewPager(Context context) {
        super(context);
        this.isLocked = false;
    }

    public TrickyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isLocked = false;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean z = false;
        if (!this.isLocked) {
            try {
                z = super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.isLocked) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    public void toggleLock() {
        this.isLocked = !this.isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return this.isLocked;
    }
}
