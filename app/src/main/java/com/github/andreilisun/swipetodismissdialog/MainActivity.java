package com.github.andreilisun.swipetodismissdialog;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);*/
        /*View dialog = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);
        View editText = dialog.findViewById(R.id.text_view);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Overlay swipeController = (Overlay) findViewById(R.id.swipeController);
        swipeController.setView(dialog);*/
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
                .build()
                .show();
    }
}
