package com.example.krb.myawsapplication.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.regions.Regions;
import com.example.krb.myawsapplication.AsyncTasks.ReadTableFromDBTask;
import com.example.krb.myawsapplication.AsyncTasks.WriteRowToDBTask;
import com.example.krb.myawsapplication.R;
import com.example.krb.myawsapplication.Supporting.AWSManager;
import com.example.krb.myawsapplication.Supporting.Crypto;
import com.example.krb.myawsapplication.Supporting.CryptoManager;
import com.example.krb.myawsapplication.Supporting.DeveloperAuthenticationProvider;
import com.example.krb.myawsapplication.Supporting.MyOnClickListener;
import com.example.krb.myawsapplication.mapperClass.MessageClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class CloudActivity extends AppCompatActivity {

    protected CognitoSyncManager syncClient;
    protected AWSManager AWSManager;

    protected Dataset dataset;
    protected String datasetName = "";
    protected CognitoCachingCredentialsProvider credentialsProvider;
    protected String awsAccountId = "";
    protected String awsIdentityPoolId = "";
    protected Regions awsRegions = ;

    protected Button buttonConnect;
    protected Button buttonSynchronize;
    protected Button buttonAdd;
    protected Button buttonRemove;

    protected TextView textViewDataset;
    protected String textViewDatasetText;
    protected EditText inputKey;
    protected EditText inputRemoveKey;
    protected EditText inputValue;

    protected String KEY_ALIAS = "MyAWSApplication_AES";

    public CryptoManager cryptoManager;

    public CloudActivity that;
    protected boolean connected = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);

        that = this;

        initSetup();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void initSetup() {
        setupTextViews();
//        setupAWS();
        setupCrypto();
//        setupDDB();
//        setupInputs();
        setupButtons();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setupCrypto() {
//        Crypto crypto = new Crypto();
//        createToast(crypto.getAndroidVersion());
        CryptoManager cryptoManager = new CryptoManager();
        cryptoManager.init(KEY_ALIAS);
        cryptoManager.createNewKeys(that);

//        Map<String, String> keys = cryptoManager.getKeyPair();

        String initialText = "Welcome!";

        String encrypted = cryptoManager.encryptString(this, initialText);
        String decrypted = cryptoManager.decryptString(that, encrypted);
//        String md5 = cryptoManager.md5(initialText);
//        String sha1 = cryptoManager.SHA1(initialText);

        String textViewText = "" +
//                "Public Key: " + keys.get("public key") + "\n" +
//                "Private Key: " + keys.get("private key") + "\n" +
                "Initial: " + initialText + "\n" +
                "Encrypted: " + encrypted + "\n" +
                "Decrypted: " + decrypted + "\n" +
//                "MD5 Hash: " + md5 + "\n" +
//                "SHA-1 Hash: " + sha1 + "\n" +
                "";
        textViewDataset.setText(textViewText);
    }

    protected void setupTextViews() {
        textViewDataset = (TextView) findViewById(R.id.textview_dataset);
    }

    public void setTextViewText(String text) {
        textViewDatasetText = text;
    }

    protected void setupInputs() {
        inputKey = (EditText) findViewById(R.id.input_enter_key);
        inputValue = (EditText) findViewById(R.id.input_enter_value);
        inputRemoveKey = (EditText) findViewById(R.id.input_enter_remove_key);
    }

    protected void setupButtons() {
        setupButtonConnect();
//        setupButtonSynchronize();
//        setupButtonAdd();
//        setupButtonRemove();
    }

//    protected void setupButtonSynchronize() {
//        buttonSynchronize = (Button) findViewById(R.id.button_synchronize);
//        buttonSynchronize.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                textViewDataset.setText("Synchronise button is pressed");
//                try {
//                    dataset.synchronize(new DefaultSyncCallback() {
//                        @Override
//                        public void onSuccess(Dataset dataset, List newRecords) {
////                            Toast.makeText(getApplicationContext(), "Dataset is Synchronised with the old data", Toast.LENGTH_SHORT).show();
////                            that.dataset = dataset;
////                            that.textViewDataset.setText(dataset.getAllRecords().toString());
//                        }
//                    });
//                } catch (Exception e) {
//                    textViewDataset.setText(e.getMessage());
//                }
//
//                String text = "";
//                for (Record record : dataset.getAllRecords()) {
//                    text += record.getKey();
//                    text += " : ";
//                    text += record.getValue();
//                    text += "\n";
//                }
//                textViewDataset.setText(text);
//            }
//        });
//
//    }

    protected void setupButtonSynchronize() {
        buttonSynchronize = (Button) findViewById(R.id.button_synchronize);
        buttonSynchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReadTableFromDBTask(getApplicationContext(), that).execute();
            }

        });

    }

    public CryptoManager getCryptoManager() {
        return this.cryptoManager;
    }

    protected void setupButtonConnect() {
        buttonConnect = (Button) findViewById(R.id.button_connect);
//        buttonConnect.setOnClickListener(new View.OnClickListener() {
        buttonConnect.setOnClickListener(new MyOnClickListener(this) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
//                CloudActivity cloudActivity = (CloudActivity) that;
//                CloudActivity cloudActivity = (CloudActivity) activity;
//                String encrypted = cloudActivity.getCryptoManager().encryptString(cloudActivity, "Welcome!");
//                String decrypted = cryptoManager.decryptString(that, encrypted);
//                textViewDataset.setText("Encrypted: " + encrypted + "\nDecrypted: " + decrypted);
//                textViewDataset.setText(Html.fromHtml(textViewDatasetText));


//                buttonConnect.setText(getResources().getString(R.string.button_disconnect));
//                textViewDataset.setText("Connecting...");

//                dataset.put("myNewTestKey", "myNewTestValue");
//                dataset.put("myKey", "myValue");

//                try {
//                    dataset.synchronize(new DefaultSyncCallback() {
//                        @Override
//                        public void onSuccess(Dataset dataset, List newRecords) {
////                            that.createToast("Dataset is Synchronised with the new data");
//                        }
//
//                        @Override
//                        public void onFailure(DataStorageException dse) {
//                        }
//                    });
//                } catch (Exception e) {
//                    textViewDataset.setText(e.getMessage());
//                }
//                String identityId = credentialsProvider.getIdentityId();
//                textViewDataset.setText(identityId);
//
            }
        });
    }

    protected void setupAWS() {
// Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                awsIdentityPoolId,
                awsRegions
        );

        DeveloperAuthenticationProvider developerProvider = new DeveloperAuthenticationProvider(awsAccountId, awsIdentityPoolId, awsRegions);
//        DeveloperAuthenticationProvider developerProvider = new DeveloperAuthenticationProvider(null, awsIdentityPoolId, getApplicationContext(), awsRegions);
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), developerProvider, awsRegions);

//        HashMap<String, String> loginsMap = new HashMap<String, String>();
//        loginsMap.put(developerProvider.getProviderName(), developerUserIdentifier);
//
//        credentialsProvider.setLogins(loginsMap);
//        credentialsProvider.refresh();

//        String identityId = credentialsProvider.getIdentityId();
//
//        credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                awsAccountId,
//                awsIdentityPoolId,
//                "arn:aws:iam::458341318104:user/newtestuser",
//                "arn:aws:iam::458341318104:role/Cognito_MyAWSApplicationUnauth_Role",
//                awsRegions
//        );
//
//        String newIdentityId = credentialsProvider.getIdentityId();
//
//        createToast(identityId==newIdentityId ? "Identity has NOT changed" : "Identity HAS changed");

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                getApplicationContext(),
                awsRegions,
                credentialsProvider);

        dataset = syncClient.openOrCreateDataset(datasetName);
        dataset.put("myNewTestKey", "myNewTestValue");
        try {
            dataset.synchronize(new DefaultSyncCallback() {
                @Override
                public void onSuccess(Dataset dataset, List newRecords) {
                    that.createToast("Synchronization Succeeded");
                }

                @Override
                public void onFailure(DataStorageException dse) {
//                    createToast(dse.getMessage());
                }
            });
        } catch (Exception e) {
            textViewDataset.setText(e.getMessage());
        }


    }

    protected void setupDDB() {
        AWSManager = new AWSManager();
        AWSManager.getCredentialProvider(getApplicationContext());
//        AWSManager.cacheData(getApplicationContext(),"myFirstTest","IsWorking");
        AWSManager.getDDBClient();

    }

//    protected void setupButtonAdd() {
//        buttonAdd = (Button) findViewById(R.id.button_add);
//
//        buttonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dataset.put(inputKey.getText().toString(), inputValue.getText().toString());
//
//                try {
//                    dataset.synchronize(new DefaultSyncCallback() {
//                        @Override
//                        public void onSuccess(Dataset dataset, List newRecords) {
//                        }
//
//                    });
//                } catch (Exception e) {
//                    textViewDataset.setText(e.getMessage());
//                }
//                inputKey.setText(getResources().getString(R.string.input_enter_key));
//                inputValue.setText(getResources().getString(R.string.input_enter_value));
//
//            }
//        });
//
//    }

    protected void setupButtonAdd() {
        buttonAdd = (Button) findViewById(R.id.button_add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy H:mm:ss");
                String dateTimeString = format.format(calendar.getTime()).toString();
                String identityId = AWSManager.getIdentityId();
                String[] parts = identityId.split("-");
                String identifier = parts[parts.length - 1];
                String text = inputValue.getText().toString();
//                createToast(Html.fromHtml("<b>"+dateTimeString + " " + identifier + ": </b><br>" + text));
//                textViewDataset.setText(Html.fromHtml("<b>"+dateTimeString + " " + identifier + ": </b><br>" + text));
                MessageClass messageClass = new MessageClass();
                messageClass.setDatetime(dateTimeString);
                messageClass.setIdentityId(identifier);
                messageClass.setText(text);
//                MessageClass.setLastname(inputValue.getText().toString());

                new WriteRowToDBTask(getApplicationContext(), messageClass).execute();
//                new WriteRowToDBTask(getApplicationContext(), shudlockyoutube).execute();

                inputKey.setText(getResources().getString(R.string.input_enter_key));
                inputValue.setText(getResources().getString(R.string.input_enter_value));

            }
        });

    }

    protected void setupButtonRemove() {
        buttonRemove = (Button) findViewById(R.id.button_remove);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataset.remove(inputRemoveKey.getText().toString());

                try {
                    dataset.synchronize(new DefaultSyncCallback() {
                        @Override
                        public void onSuccess(Dataset dataset, List newRecords) {
                        }

                    });
                } catch (Exception e) {
                    textViewDataset.setText(e.getMessage());
                }
                inputRemoveKey.setText(getResources().getString(R.string.input_enter_remove_key));
            }
        });

    }

    protected void createToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

//    protected void authenticateUser() {
//        var authenticationData = {
//                Username :'username',
//                Password :'password',
//    };
//        var authenticationDetails = new AWSCognito.CognitoIdentityServiceProvider.AuthenticationDetails(authenticationData);
//        var poolData = {UserPoolId :'us-east-1_TcoKGbf7n',
//                ClientId :'4pe2usejqcdmhi0a25jp4b5sh3'
//    };
//        var userPool = new AWSCognito.CognitoIdentityServiceProvider.CognitoUserPool(poolData);
//        var userData = {
//                Username :'username',
//                Pool :userPool
//    };
//        var cognitoUser = new AWSCognito.CognitoIdentityServiceProvider.CognitoUser(userData);
//        cognitoUser.authenticateUser(authenticationDetails, {
//                onSuccess:function(result) {
//            console.log('access token + ' + result.getAccessToken().getJwtToken());
//            /*Use the idToken for Logins Map when Federating User Pools with Cognito Identity or when passing through an Authorization Header to an API Gateway Authorizer*/
//            console.log('idToken + ' + result.idToken.jwtToken);
//        },
//
//        onFailure:
//        function(err) {
//            alert(err);
//        },
//
//    });
//    }

}
