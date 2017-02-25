package com.example.asus.qqcehua;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by ASUS on 2017/2/21.
 */

public class DragLayout extends FrameLayout {
    private View mredview;
    private View buleview;
    private ViewDragHelper viewDragHelper;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);

    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mredview = getChildAt(0);
        buleview = getChildAt(1);
    }


    //protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    //  int mesurespace = MeasureSpec.makeMeasureSpec(mredview.getLayoutParams().width,MeasureSpec.EXACTLY);
    //  mredview.measure(mesurespace,mesurespace);
    //  buleview.measure(mesurespace,mesurespace);*/
    // 没有特殊的测量需求
    // measureChild(mredview,widthMeasureSpec,heightMeasureSpec);
//   }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        mredview.layout(left, top, left + mredview.getMeasuredWidth(), top + mredview.getMeasuredHeight());
        buleview.layout(left, mredview.getBottom(), left + mredview.getMeasuredWidth(), mredview.getBottom() + buleview.getMeasuredHeight());
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        //是否捕获
        public boolean tryCaptureView(View child, int pointerId) {
            return child == buleview || child == mredview;
        }

        @Override
        //水平移动
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                left = 0;
            } else if (left > getMeasuredWidth() - buleview.getMeasuredWidth()) {
                left = getMeasuredWidth() - child.getMeasuredWidth();
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top < 0) {
                top = 0;
            } else if (top > getMeasuredHeight() - buleview.getMeasuredHeight()) {
                top = getMeasuredHeight() - buleview.getMeasuredHeight();
            }
            return top;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == buleview) {
                mredview.layout(mredview.getLeft() + dx, mredview.getTop() + dy, mredview.getRight() + dx, mredview.getBottom() + dy);
            } else if (changedView == mredview) {
                buleview.layout(buleview.getLeft() + dx, buleview.getTop() + dy, buleview.getRight() + dx, buleview.getBottom() + dy);
            }
            float fraction = changedView.getLeft()*1f / (getMeasuredWidth() - changedView.getMeasuredWidth());
            Log.e("TAG", "fraction :"+fraction );
            executeAnmi(fraction);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
           int center  = getMeasuredWidth()/2-releasedChild.getMeasuredWidth()/2;
            //在左边的时候，让它缓慢向左边移动
            if(releasedChild.getLeft()<center){
                viewDragHelper.smoothSlideViewTo(releasedChild,0,releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }else  if(releasedChild.getLeft()>center){
                //在右边的时候向右边缓慢移动
                viewDragHelper.smoothSlideViewTo(releasedChild,getMeasuredWidth()-releasedChild.getMeasuredWidth(),releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }
        }
    };

    //执行伴随动画
        private void executeAnmi(float fraction) {
          //mredview.setScaleX(1+0.5f*fraction);
          //mredview.setScaleY(1+0.5f*fraction);
            //mredview.setScaleX(1+0.5f*fraction);
            //mredview.setRotation(360*fraction);
           // mredview.setRotationX(360*fraction);
            //mredview.setRotationY(360*fraction);
           // mredview.setTranslationX(90*fraction);
            mredview.setAlpha(1-fraction);
            //设置颜色过度渐变

        }

         public void computeScroll() {
            if (viewDragHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }
        }
    }
