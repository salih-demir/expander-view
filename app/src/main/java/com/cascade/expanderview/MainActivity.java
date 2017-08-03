package com.cascade.expanderview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeActivity();
    }

    private void initializeActivity() {
        ExpanderView expanderViewSuperScript = (ExpanderView) findViewById(R.id.ev_super_script);
        expanderViewSuperScript.setTitle("Expander View With", "Super Script");

        final ExpanderView expanderViewDynamicMargin = (ExpanderView) findViewById(R.id.ev_dynamic_margin);
        final ExpanderView expanderViewDynamicTitle = (ExpanderView) findViewById(R.id.ev_dynamic_title);
        final ExpanderView expanderViewDynamicVisibility = (ExpanderView) findViewById(R.id.ev_dynamic_visibility);

        Button buttonChangeMargin = (Button) findViewById(R.id.b_change_margin);
        buttonChangeMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expanderViewDynamicMargin.setContentMarginEnabled(true);
            }
        });

        Button buttonChangeTitle = (Button) findViewById(R.id.b_change_title);
        buttonChangeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expanderViewDynamicTitle.setTitle("Title Changed");
            }
        });

        Button buttonHideMe = (Button) findViewById(R.id.b_hide_me);
        buttonHideMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expanderViewDynamicVisibility.hide();
            }
        });
    }
}