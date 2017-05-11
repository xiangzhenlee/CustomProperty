package com.yushan.customproperty;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class HomeActivity extends Activity {

    private CustomToggleButton toggle_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle_button = (CustomToggleButton) findViewById(R.id.toggle_button);

        toggle_button.setState(true);
        toggle_button.setSlideBackground(R.drawable.btn_bg, R.drawable.btn_icon);

        toggle_button.setOnStateChangedListener(new CustomToggleButton.OnStateChangedListener() {

            @Override
            public void onStateChanged(boolean state) {
                Toast.makeText(getApplicationContext(), state ? "打开" : "关闭", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
