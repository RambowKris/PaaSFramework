package com.example.krb.myawsapplication.AsyncTasks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.krb.myawsapplication.Supporting.AWSManager;

/**
 * Created by krb on 18/08/2017.
 */

public class ReadTableFromDBTask extends AsyncTask<Void, Integer, Integer> {

    //        Dialog dialog;
    protected Activity activity;
    protected Context context;
    protected Dialog dialog;

    public ReadTableFromDBTask(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        AWSManager AWSManager = new AWSManager();
        CognitoCachingCredentialsProvider credentialsProvider = AWSManager.getCredentialProvider(context);

        if (credentialsProvider != null) {
            DynamoDBMapper dynamoDBMapper = AWSManager.getDDBClient();

            String text = AWSManager.dbReadTable();
//            activity.setTextViewText(text);
//            final String text = AWSManager.dbReadTable();
//            Thread t = new Thread() {
//                public void run() {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            activity.setTextView(text);
//                        }
//                    });
//                }
//            };
        } else {
            return 1;
        }

//
//            dialog = ProgressDialog.show(context, "Loaded", name);
//            Toast.makeText(context, "Async Task is running ...", Toast.LENGTH_SHORT).show();

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (integer == 0) {
            Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}

