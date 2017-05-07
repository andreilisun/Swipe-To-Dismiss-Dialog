package com.github.andreilisun.swipetodismissdialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.github.andreilisun.swipedismissdialog.OnCancelListener;
import com.github.andreilisun.swipedismissdialog.OnSwipeDismissListener;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDirection;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeDismissDialog.Builder dialogBuilder = new SwipeDismissDialog.Builder(this);

        View v = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);

        final SwipeDismissDialog swipeDismissDialog = dialogBuilder
                .setView(v)
                .setOnSwipeDismissListener(new OnSwipeDismissListener() {
                    @Override
                    public void onSwipeDismiss(View view, SwipeDismissDirection direction) {
                        Toast.makeText(MainActivity.this, "Dismissed direction: " + direction,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setOverlayColor(Color.parseColor("#66ea1717"))
                .setFlingVelocity(.2f)
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(View v) {
                        Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setHorizontalOscillation(35f)
                .build()
                .show();
    }
}
