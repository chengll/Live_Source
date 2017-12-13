package com.test.withborderstextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TextViewBorder state1;
    private TextViewBorder state2;
    private TextViewBorder state3;
    private TextViewBorder state4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state1 = (TextViewBorder) findViewById(R.id.state1);
        state2 = (TextViewBorder) findViewById(R.id.state2);
        state3 = (TextViewBorder) findViewById(R.id.state3);
        state4 = (TextViewBorder) findViewById(R.id.state4);

        state2.setBorderColor(getResources().getColor(R.color.app_red_delete_color));
        state2.setTextColor(getResources().getColor(R.color.app_red_delete_color));

        state3.setBorderColor(getResources().getColor(R.color.app_blue_color));
        state3.setTextColor(getResources().getColor(R.color.progress_color));

        state4.setBorderColor(getResources().getColor(R.color.app_red_delete_color));
        state4.setTextColor(getResources().getColor(R.color.app_blue_color));
    }
}
