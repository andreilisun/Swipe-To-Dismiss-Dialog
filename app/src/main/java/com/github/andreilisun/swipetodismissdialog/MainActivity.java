package com.github.andreilisun.swipetodismissdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View dialog = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);
        Overlay swipeController = (Overlay) findViewById(R.id.swipeController);
        swipeController.setView(dialog);
    }
}
