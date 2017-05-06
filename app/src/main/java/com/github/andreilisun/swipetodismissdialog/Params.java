package com.github.andreilisun.swipetodismissdialog;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.View;

public class Params {
    public View view = null;
    @FloatRange(from = 0, to = 1.0)
    public float flingVelocity = 0.15f;
    @ColorInt
    public int overlayColor = Color.parseColor("#80444444");
    @Nullable
    public OnSwipeDismissListener swipeDismissListener;
    @Nullable
    public OnCancelListener cancelListener;
    // TODO: 02.05.17 dragRotationAngle, cancelable
}
