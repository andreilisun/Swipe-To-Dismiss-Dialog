package com.github.andreilisun.swipetodismissdialog.swipedismissdialog;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

@SuppressLint("ViewConstructor")
public class Overlay extends FrameLayout {

    private final GestureDetector gestureDetector;
    private final Params params;
    private View dialog;

    public Overlay(@NonNull Context context, Params params) {
        super(context);
        this.params = params;
        this.gestureDetector = new GestureDetector(context, new SwipeGestureListener());
        init();
    }

    private void init() {
        setOnClickListener(overlayClickListener);
        setBackgroundColor(params.overlayColor);
        dialog = params.view;
        if (dialog == null) {
            // TODO: 06.05.17  
            dialog = LayoutInflater.from(getContext()).inflate(params.layoutRes, this, false);
        }
        dialog.setOnTouchListener(touchListener);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        addView(dialog, layoutParams);
    }

    private void dismiss(SwipeDismissDirection direction) {
        if (params.swipeDismissListener != null) {
            params.swipeDismissListener.onSwipeDismiss(this, direction);
        }
        dismiss();
    }

    public void cancel() {
        if (params.cancelListener != null) {
            params.cancelListener.onCancel(dialog);
        }
        dismiss();
    }

    private void dismiss() {
        WindowManager windowManager = (WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeViewImmediate(this);
    }

    private final OnTouchListener touchListener = new OnTouchListener() {

        private float initCenterX;
        private float lastEventY;
        private float lastEventX;
        private float initY;
        private float initX;

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
                    lastEventX = motionEvent.getRawX();
                    lastEventY = motionEvent.getRawY();
                    initCenterX = initX + view.getWidth() / 2;
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    float eventX = motionEvent.getRawX();
                    float eventY = motionEvent.getRawY();
                    float eventDx = eventX - lastEventX;
                    float eventDy = eventY - lastEventY;
                    float centerX = view.getX() + eventDx + view.getWidth() / 2;
                    float centerDx = centerX - initCenterX;
                    view.setX(view.getX() + eventDx);
                    view.setY(view.getY() + eventDy);
                    float rotationAngle = centerDx * params.horizontalOscillation / initCenterX;
                    view.setRotation(rotationAngle);
                    view.invalidate();
                    lastEventX = eventX;
                    lastEventY = eventY;
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    PropertyValuesHolder horizontalAnimation =
                            PropertyValuesHolder.ofFloat("x", initX);
                    PropertyValuesHolder verticalAnimation =
                            PropertyValuesHolder.ofFloat("y", initY);
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

    private final OnClickListener overlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            cancel();
            return true;
        }
        return false;
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
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
