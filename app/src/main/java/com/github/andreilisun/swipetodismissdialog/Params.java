package com.github.andreilisun.swipetodismissdialog;

import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

public class Params {
    public View view = null;
    @FloatRange(from = 0, to = 1.0)
    public float flingVelocity = 0.15f;// TODO: 06.05.17 set default velocity
    @Nullable
    public OnSwipeDismissListener swipeDismissListener;
    // TODO: 02.05.17 dragRotationAngle, cancelable, overlay background
}
