package com.example.asus.qqcehua;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.transition.Slide;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.IllegalFormatException;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.TRANSPARENT;

/**
 * Created by ASUS on 2017/2/22.
 */

public class SlideMenu extends FrameLayout {
    private View mainView;
    private View menuView;
    private ViewDragHelper viewDragHelper;
    private  int width;
    private float dragRange;
    private FloatEvaluator floatEvaluator;
    private IntEvaluator intEvaluator;
    private ArgbEvaluator argbEvaluator;

    public SlideMenu(Context context) {
        super(context);
        init();
    }
    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()!=2){
            throw new IllegalArgumentException("SildeManu only two childcount");
        }
        menuView = getChildAt(0);
        mainView = getChildAt(1);

    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callbcak);
    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        dragRange = width*0.6f;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public ViewDragHelper.Callback callbcak = new ViewDragHelper.Callback() {
        @Override
        //申明
        public boolean tryCaptureView(View child, int pointerId) {
            return child==mainView || child==menuView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return (int) dragRange;
        }

        @Override
        //修正
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(child == mainView){
                //限制左边
                if(left<0){
                    left = 0;
                }else if(left>dragRange){
                    //限制右边
                    left  = (int) dragRange;
                }
            }
            return left;
        }

        @Override
        //事发
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int center = (int) (dragRange/2);
            if(releasedChild.getLeft()<center){
                //左半边
                viewDragHelper.smoothSlideViewTo(mainView,0,0);
                ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
            }else{
                //右半边
                viewDragHelper.smoothSlideViewTo(mainView, (int) dragRange,0);
                //刷新
                ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
            }

        }

        @Override
        //伴随
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float fraction = mainView.getLeft()/dragRange;
            exectuAmi(fraction);


            if (changedView == menuView){
                //固定menuView
                menuView.layout(0,0,menuView.getMeasuredWidth(),menuView.getMeasuredHeight());
                int newLeft = mainView.getLeft()+dx;
                //限制左边界
                if(newLeft<0){
                    newLeft = 0;
                }else if(newLeft>dragRange){
                    //限制右边
                    newLeft  = (int) dragRange;
                }
                mainView.layout(newLeft,mainView.getTop()+dy,mainView.getRight()+dx,mainView.getBottom()+dy);
            }
        }
    };
    private void exectuAmi(float fraction){
             floatEvaluator = new FloatEvaluator();
             intEvaluator = new IntEvaluator();
             argbEvaluator  =new ArgbEvaluator();
             mainView.setScaleX(floatEvaluator.evaluate(fraction,1f,0.8f));
             mainView.setScaleY(floatEvaluator.evaluate(fraction,1f,0.8f));
             menuView.setTranslationX(intEvaluator.evaluate(fraction,-menuView.getMeasuredWidth()/2,0));
             menuView.setScaleX(floatEvaluator.evaluate(fraction,0.5f,1f));
             menuView.setScaleY(floatEvaluator.evaluate(fraction,0.5f,1f));
             menuView.setAlpha(floatEvaluator.evaluate(fraction,0.2f,1f));
            getBackground().setColorFilter((Integer) argbEvaluator.evaluate(fraction,BLACK,TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }
    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
        super.computeScroll();
    }
}
