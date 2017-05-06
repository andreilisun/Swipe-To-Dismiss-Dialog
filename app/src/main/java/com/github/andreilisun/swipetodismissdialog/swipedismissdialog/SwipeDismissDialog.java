package com.github.andreilisun.swipetodismissdialog.swipedismissdialog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

public class SwipeDismissDialog {

    private final Context context;
    private Overlay overlay;
    private final Params params;

    protected SwipeDismissDialog(Context context, Params params) {
        this.context = context;
        this.params = params;
    }

    public SwipeDismissDialog show() {
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        overlay = new Overlay(context, params);
        windowManager.addView(overlay, layoutParams);
        return this;
    }

    public void hide() {
        overlay.cancel();
    }

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
