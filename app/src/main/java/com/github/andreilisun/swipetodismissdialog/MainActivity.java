package com.github.andreilisun.swipetodismissdialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeDismissDialog.Builder dialogBuilder = new SwipeDismissDialog.Builder(this);
        dialogBuilder
                .setLayoutResId(R.layout.layout_dialog)
                .setOnSwipeDismissListener(new OnSwipeDismissListener() {
                    @Override
                    public void onSwipeDismiss(View view, SwipeDismissDirection direction) {
                        Toast.makeText(MainActivity.this, "Dismissed direction: " + direction,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setOverlayColor(Color.parseColor("#66ea1717"))
                .setFlingVelocity(0.2f)
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(View v) {
                        Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .show();
    }
}
