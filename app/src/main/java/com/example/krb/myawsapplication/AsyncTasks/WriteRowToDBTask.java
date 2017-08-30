package com.example.krb.myawsapplication.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.krb.myawsapplication.Supporting.AWSManager;
import com.example.krb.myawsapplication.mapperClass.MessageClass;

/**
 * Created by krb on 18/08/2017.
 */

public class WriteRowToDBTask extends AsyncTask<Void, Integer, Integer> {

    //        Dialog dialog;
//    protected shudlockyoutube mapperClass;
    protected MessageClass mapperClass;
    protected Context context;

    public WriteRowToDBTask(Context context, MessageClass mapperClass) {
        this.context = context;
        this.mapperClass = mapperClass;
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
            AWSManager.dbWriteRow(mapperClass);
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

