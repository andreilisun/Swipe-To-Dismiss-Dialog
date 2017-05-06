package com.github.andreilisun.swipetodismissdialog;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

public class Overlay extends FrameLayout {

    private GestureDetector gestureDetector;
    private Params params;

    public Overlay(@NonNull Context context, Params params) {
        super(context);
        this.params = params;
        this.gestureDetector = new GestureDetector(context, new SwipeGestureListener());
        init();
    }

    // TODO: 06.05.17 If no focus, focus this view
    private void init() {
        setBackgroundColor(Color.parseColor("#80444444"));
        params.view.setOnTouchListener(touchListener);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        addView(params.view, layoutParams);
    }

    public void dismiss(SwipeDismissDirection direction) {
        // TODO: 06.05.17 callback
        if (params.swipeDismissListener != null) {
            params.swipeDismissListener.onSwipeDismiss(this, direction);
        }
        dismiss();
    }

    public void dismiss() {
        WindowManager windowManager = (WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeViewImmediate(this);
    }

    private OnTouchListener touchListener = new OnTouchListener() {

        public float initDy;
        public float initDx;
        public float initY;
        public float initX;

        public boolean onTouch(View view, MotionEvent motionEvent) {
            /*Fling detected*/
            if (gestureDetector.onTouchEvent(motionEvent)) {
                return true;
            }


            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    initX = view.getX();
                    initY = view.getY();
                    initDx = initX - motionEvent.getRawX();
                    initDy = initY - motionEvent.getRawY();
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    view.setX(motionEvent.getRawX() + initDx);
                    view.setY(motionEvent.getRawY() + initDy);
                    view.invalidate();
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    float deltaX = initX - view.getLeft();
                    float deltaY = initY - view.getTop();
                    PropertyValuesHolder horizontalAnimation =
                            PropertyValuesHolder.ofFloat("translationX", deltaX);
                    PropertyValuesHolder verticalAnimation =
                            PropertyValuesHolder.ofFloat("translationY", deltaY);
                    PropertyValuesHolder rotateAnimation =
                            PropertyValuesHolder.ofFloat("rotation", 0f);
                    ObjectAnimator originBackAnimation =
                            ObjectAnimator.ofPropertyValuesHolder(view, horizontalAnimation,
                                    verticalAnimation, rotateAnimation);
                    originBackAnimation.setInterpolator(
                            new AccelerateDecelerateInterpolator());
                    originBackAnimation.setDuration(300);
                    originBackAnimation.start();
                    break;
                }
            }

            return true;
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        /*Log.d(Overlay.class.getName(), "dispatchKeyEvent: " + (((ViewGroup)getFocusedChild()).getFocusedChild().getId() == R.id.edit_text));*/
        Log.d(Overlay.class.getName(), "dispatchKeyEvent: ");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(Overlay.class.getName(), "onKeyDown: ");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(Overlay.class.getName(), "onKeyUp: ");
        return super.onKeyUp(keyCode, event);
    }

    class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int maxVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
            float normalizedVelocityX = Math.abs(velocityX) / maxVelocity;
            float normalizedVelocityY = Math.abs(velocityY) / maxVelocity;
            if (normalizedVelocityX > params.flingVelocity) {
                SwipeDismissDirection direction = (e2.getRawX() > e1.getRawX())
                        ? SwipeDismissDirection.RIGHT
                        : SwipeDismissDirection.LEFT;
                dismiss(direction);
                return true;
            } else if (normalizedVelocityY > params.flingVelocity) {
                SwipeDismissDirection direction = (e2.getRawY() > e1.getRawY())
                        ? SwipeDismissDirection.BOTTOM
                        : SwipeDismissDirection.TOP;
                dismiss(direction);
                return true;
            } else {
                return false;
            }
        }
    }
}
