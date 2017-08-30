package com.example.krb.myawsapplication.Supporting;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.krb.myawsapplication.mapperClass.MessageClass;
import com.example.krb.myawsapplication.mapperClass.shudlockyoutube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by krb on 18/08/2017.
 */

public class AWSManager {
    protected CognitoCachingCredentialsProvider credentialsProvider;
    protected Regions region = ;
    protected String identityPoolId = "";
    protected String datasetName = "";

    protected String identityId;
    protected Context context;

    protected AmazonDynamoDBClient ddbClient;
    protected DynamoDBMapper ddbMapper;

    protected AmazonS3Client s3Client;
    protected TransferUtility transferUtility;

    public CognitoCachingCredentialsProvider getCredentialProvider(Context context) {
        if (this.context == null) {
            this.context = context;
        }

        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                this.context,
                identityPoolId,
                region
        );

        identityId = credentialsProvider.getIdentityId();

        return credentialsProvider;
    }

    public void setDatasetName(String name) {
        datasetName = name;
    }

    public void cacheData(Context context, String key, String value) {
        if (credentialsProvider == null) {
            getCredentialProvider(context);
        }

        // Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                context,
                region,
                credentialsProvider);

        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset(datasetName);
        dataset.put(key, value);
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
            }
        });
    }

    public void saveToDDB() {
        if (ddbClient == null) {
            getDDBClient();
        }

//            dbWriteRow();
//            String name = dbReadRow();
//        new WriteDBRowTask().execute();
    }

    public Context getContext() {
        return context;
    }

    public DynamoDBMapper getDDBClient() {
        if (credentialsProvider == null) {
            return null;
        }

        if (ddbClient == null) {
            ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            ddbClient.setRegion(Region.getRegion(region));
            ddbMapper = new DynamoDBMapper(ddbClient);
        }
        return ddbMapper;
    }

    public String dbReadTable() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        List<MessageClass> result = ddbMapper.scan(MessageClass.class, scanExpression);
//        List<shudlockyoutube> result = ddbMapper.scan(shudlockyoutube.class, scanExpression);
//        System.out.println(result.toString());
//        return result.toString();

        String outputResult = "";
//        Collections.sort(result, new Comparator<MessageClass>() {
//            @Override
//            public int compare(MessageClass mc1, MessageClass mc2) {
//                return mc1.getDatetime().compareTo(mc2.getDatetime());
//            }
//        });

        for (MessageClass row : result) {
            outputResult += row.toHtmlString();
//            System.out.println(row.toString());
        }

        return outputResult;
    }

    public String getIdentityId() {
        if (credentialsProvider == null) {
            getCredentialProvider(context);
        }

        return identityId;
    }

    public void dbWriteRow(MessageClass mapperClass) {

        if (credentialsProvider != null && ddbMapper != null) {
            ddbMapper.save(mapperClass);
        }
//        Book book = new Book();
//        book.setTitle("Great Expectations");
//        book.setAuthor("Charles Dickens");
//        book.setPrice(1299);
//        book.setIsbn("1234567890");
//        book.setHardCover(false);
    }

    public String dbReadRow() {
        shudlockyoutube selectedName = ddbMapper.load(shudlockyoutube.class, "test");
//        Book selectedBook = mapper.load(Book.class, "1234567890");
        return selectedName.getName();
    }

    public AmazonS3Client initS3Client(Context context) {
        if (credentialsProvider == null) {
            getCredentialProvider(context);
        }

        if (s3Client == null) {
            s3Client = new AmazonS3Client(credentialsProvider);
            s3Client.setRegion(Region.getRegion(region));
        }

        return s3Client;
    }

    public TransferUtility checkTransferUtility(AmazonS3Client s3Client, Context context) {
        if (transferUtility == null) {
            transferUtility = new TransferUtility(s3Client, context);
        }
        return transferUtility;

    }

//    private class WriteDBRowTask extends AsyncTask<Void, Integer, Integer> {
//
////        Dialog dialog;
//
//        @Override
//        protected Integer doInBackground(Void... params) {
//            if(android.os.Debug.isDebuggerConnected()){
//                android.os.Debug.waitForDebugger();
//            }
//
//            dbWriteRow();
////            String name = dbReadRow();
////
////            dialog = ProgressDialog.show(context, "Loaded", name);
////            Toast.makeText(context, "Async Task is running ...", Toast.LENGTH_SHORT).show();
//
//            return null;
//        }
//
//    }

}
