package com.github.andreilisun.swipetodismissdialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.andreilisun.swipetodismissdialog.swipedismissdialog.OnCancelListener;
import com.github.andreilisun.swipetodismissdialog.swipedismissdialog.OnSwipeDismissListener;
import com.github.andreilisun.swipetodismissdialog.swipedismissdialog.SwipeDismissDialog;
import com.github.andreilisun.swipetodismissdialog.swipedismissdialog.SwipeDismissDirection;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeDismissDialog.Builder dialogBuilder = new SwipeDismissDialog.Builder(this);

        /*View v = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);
        final EditText inputView = (EditText) v.findViewById(R.id.edit_text);
        Button submitButton = (Button) v.findViewById(R.id.button);*/

        final SwipeDismissDialog swipeDismissDialog = dialogBuilder
                .setLayoutResId(R.layout.layout_dialog)
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

        /*submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, inputView.getText(), Toast.LENGTH_SHORT).show();
                swipeDismissDialog.hide();
            }
        });*/
    }
}
