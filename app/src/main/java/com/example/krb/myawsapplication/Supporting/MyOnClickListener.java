package com.example.krb.myawsapplication.Supporting;

import android.app.Activity;
import android.view.View;

/**
 * Created by krb on 27/08/2017.
 */

public class MyOnClickListener implements View.OnClickListener {

    public Activity activity;

    public MyOnClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
    }

}
