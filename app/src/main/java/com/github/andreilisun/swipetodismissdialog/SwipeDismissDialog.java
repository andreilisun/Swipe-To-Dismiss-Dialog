package com.github.andreilisun.swipetodismissdialog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class SwipeDismissDialog {

    private final Context context;
    private WindowManager windowManager;
    private Overlay overlay;
    private Params params;

    protected SwipeDismissDialog(Context context, Params params) {
        this.context = context;
        this.params = params;
    }

    public SwipeDismissDialog show() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        // TODO: 02.05.17 Proper type
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        overlay = new Overlay(context, params);
        windowManager.addView(overlay, layoutParams);
        return this;
    }

    public void hide() {
        overlay.dismiss();
    }

    public static class Builder {

        private Params params;
        private Context context;

        public Builder(Context context) {
            this.context = context;
            this.params = new Params();
        }

        public Builder setView(@NonNull View view) {
            params.view = view;
            return this;
        }

        // TODO: 06.05.17 Add runtime check
        public Builder setLayoutResId(@LayoutRes int layoutResId) {
            params.view = LayoutInflater.from(context).inflate(layoutResId, null);
            return this;
        }

        public Builder setFlingVelocity(@FloatRange(from = 0, to = 1.0) float flingVelocity) {
            if (flingVelocity < 0 || flingVelocity > 1.0) {
                throw new IllegalArgumentException("flingVelocity should be in a range [0, 1.0]");
            }
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

        public SwipeDismissDialog build() {
            if (params.view == null) {
                throw new IllegalStateException("view should be set with setView(View view) method");
            }
            return new SwipeDismissDialog(context, params);
        }
    }
}
