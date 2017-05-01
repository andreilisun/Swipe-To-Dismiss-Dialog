package com.github.andreilisun.swipetodismissdialog;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

public class Overlay extends FrameLayout {

    private GestureDetector gestureDetector;
    private View view;

    public Overlay(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Overlay(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Overlay(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setView(@NonNull View v) {
        view = v;
        view.setOnTouchListener(touchListener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(400, 400);
        params.gravity = Gravity.CENTER;
        addView(view, params);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new SwipeGestureListener());
    }

    private OnTouchListener touchListener = new OnTouchListener() {

        /*public int parentWidth;*/
        public float initDy;
        public float initDx;
        public float initCenterY;
        public float initCenterX;
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
                    initCenterX = initX + view.getWidth() / 2;
                    initCenterY = initY + view.getHeight() / 2;
                    initDx = initX - motionEvent.getRawX();
                    initDy = initY - motionEvent.getRawY();
                    /*ViewParent parent = view.getParent();
                    if (parent instanceof Overlay) {
                        Overlay overlay = (Overlay) parent;
                        parentWidth = overlay.getWidth();
                    } else {
                        throw new IllegalStateException("Overlay should be parent of a view");
                    }*/
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    view.setX(motionEvent.getRawX() + initDx);
                    view.setY(motionEvent.getRawY() + initDy);
                    /*float verticalGap = (view.getX() - initX);
                    Log.d("MOVE", "onTouch. Vertiacal gap: " + verticalGap);
                    view.setRotation(*//*verticalGap / 10f*//*90);*/
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

    class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(Overlay.class.getName(), "onFling X: " + velocityX);
            Log.d(Overlay.class.getName(), "onFling Y: " + velocityY);
            return false;
        }
    }
}
