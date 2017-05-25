package com.github.andreilisun.swipedismissdialog;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
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
public class SwipeDismissDialog extends FrameLayout {

    private final GestureDetector gestureDetector;
    private final Params params;
    private View dialog;

    protected SwipeDismissDialog(@NonNull Context context, Params params) {
        super(context);
        this.params = params;
        this.gestureDetector = new GestureDetector(context, flingGestureListener);
        init();
    }

    private void init() {
        setOnClickListener(overlayClickListener);
        setBackgroundColor(params.overlayColor);
        dialog = params.view;
        if (dialog == null) {
            dialog = LayoutInflater.from(getContext()).inflate(params.layoutRes, this, false);
        }
        LayoutParams layoutParams = (LayoutParams) dialog.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        } else {
            layoutParams.gravity = Gravity.CENTER;
        }
        dialog.setOnTouchListener(touchListener);
        addView(dialog, layoutParams);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            cancel();
            return true;
        }
        return false;
    }

    public SwipeDismissDialog show() {
        WindowManager windowManager = (WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        windowManager.addView(this, layoutParams);
        return this;
    }

    public void cancel() {
        if (params.cancelListener != null) {
            params.cancelListener.onCancel(dialog);
        }
        if (params.dismissOnCancel) {
            dismiss();
        }
    }

    public void dismiss() {
        dialog.setOnTouchListener(null);
        removeView(dialog);
        WindowManager windowManager = (WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeViewImmediate(this);
    }

    private void dismiss(SwipeDismissDirection direction) {
        if (params.swipeDismissListener != null) {
            params.swipeDismissListener.onSwipeDismiss(this, direction);
        }
        dismiss();
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

    private final SimpleOnGestureListener flingGestureListener = new SimpleOnGestureListener() {
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
    };

    public static class Builder {

        private final Params params;
        private final Context context;

        public Builder(Context context) {
            this.context = context;
            this.params = new Params();
        }

        public Builder setView(@NonNull View view) {
            params.view = view;
            params.layoutRes = 0;
            return this;
        }

        public Builder setLayoutResId(@LayoutRes int layoutResId) {
            params.layoutRes = layoutResId;
            params.view = null;
            return this;
        }

        public Builder setDismissOnCancel(boolean enabled) {
            params.dismissOnCancel = enabled;
            return this;
        }

        public Builder setFlingVelocity(@FloatRange(from = 0, to = 1.0) float flingVelocity) {
            params.flingVelocity = flingVelocity;
            return this;
        }

        public Builder setOnSwipeDismissListener(@Nullable OnSwipeDismissListener swipeDismissListener) {
            params.swipeDismissListener = swipeDismissListener;
            return this;
        }

        public Builder setOverlayColor(@ColorInt int color) {
            params.overlayColor = color;
            return this;
        }

        public Builder setOnCancelListener(@Nullable OnCancelListener cancelListener) {
            params.cancelListener = cancelListener;
            return this;
        }

        public Builder setHorizontalOscillation(@FloatRange(from = 0.0, to = 35.0) float oscillation) {
            params.horizontalOscillation = oscillation;
            return this;
        }

        public SwipeDismissDialog build() {
            if (params.view == null && params.layoutRes == 0) {
                throw new IllegalStateException("view should be set with setView(View view) " +
                        "or with setLayoutResId(int layoutResId)");
            }
            return new SwipeDismissDialog(context, params);
        }
    }
}
