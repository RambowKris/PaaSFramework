package com.example.krb.myawsapplication.Supporting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by krb on 18/08/2017.
 */

public class GenerateKeys {
    protected Context context;

    protected ArrayList<HashMap<String, Object>> keyHolder = new ArrayList<>();
    protected List<S3ObjectSummary> s3ObjectSummaries;

    public GenerateKeys(Context context) {
        this.context = context;

    }

    private class downloadKeys extends AsyncTask<Void, Void, Void> {

        protected Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Loading...", "downloading keys");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AWSManager AWSManager = new AWSManager();
            AWSManager.getCredentialProvider(context);
            AmazonS3Client s3Client = AWSManager.initS3Client(context);
            s3ObjectSummaries = s3Client.listObjects(Utils.myBucket).getObjectSummaries();

            for (S3ObjectSummary summary : s3ObjectSummaries) {
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("key", summary.getKey());
                keyHolder.add(maps);
            }

            return null;
        }
    }
}
