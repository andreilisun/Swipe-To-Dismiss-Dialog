package com.github.andreilisun.testapplication;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;

public class MainActivity extends AppCompatActivity {

    private SwipeDismissDialog swipeDismissDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btn_show_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        swipeDismissDialog = new SwipeDismissDialog.Builder(this)
                .setLayoutResId(R.layout.dialog)
                .build()
                .show();
    }

    @VisibleForTesting
    public void dismissDialog() {
        if (swipeDismissDialog != null) {
            swipeDismissDialog.dismiss();
        }
    }
}
