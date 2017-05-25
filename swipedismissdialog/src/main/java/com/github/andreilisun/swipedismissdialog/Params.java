package com.github.andreilisun.swipedismissdialog;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

public class Params {
    public View view = null;
    @RequiresApi(19)
    public boolean translucentStatus;
    @RequiresApi(19)
    public boolean translucentNavigation;
    @LayoutRes
    public int layoutRes = 0;
    @FloatRange(from = 0, to = 1.0)
    public float flingVelocity = 0.1f;
    @ColorInt
    public int overlayColor = Color.parseColor("#80444444");
    @Nullable
    public OnSwipeDismissListener swipeDismissListener;
    @Nullable
    public OnCancelListener cancelListener;
    @FloatRange(from = 0.0, to = 35.0)
    public float horizontalOscillation = 35.0f;
}
