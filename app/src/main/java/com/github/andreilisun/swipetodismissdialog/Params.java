package com.github.andreilisun.swipetodismissdialog;

import android.support.annotation.FloatRange;
import android.view.View;

public class Params {
    public View view = null;
    @FloatRange(from = 0, to = 1.0)
    public float flingVelocity = 0; // TODO: 06.05.17 set default velocity
    // TODO: 02.05.17 dragRotationAngle, cancelable, overlay background
}
