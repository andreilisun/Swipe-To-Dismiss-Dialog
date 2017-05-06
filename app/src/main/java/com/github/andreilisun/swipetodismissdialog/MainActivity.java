package com.github.andreilisun.swipetodismissdialog;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

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
                .build()
                .show();
    }
}
