package com.example.odoo.imgload2;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

class CustomDialogClass extends AppCompatDialog implements
        android.view.View.OnClickListener {

    public MainActivity c;
    public AppCompatDialog d;
    public Button yes;

    public CustomDialogClass(MainActivity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_pop_up);
//        yes = (Button) findViewById(R.id.btn_ok);
        yes.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        c.finish();
    }
}