package com.example.androidcodes.fillcolorinimages.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.androidcodes.fillcolorinimages.R;

public class MainActivity extends AppCompatActivity {

    /* This Project is Implemented from link : https://github.com/byronsanchez/coloring-book-android

    also look at this link : https://github.com/shahan312/princess-coloring-book-hd-android/tree/master/src/com/gpasoftware/princess
    *
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, FillColorsInImagesActivity.class));

            }
        });
    }
}
