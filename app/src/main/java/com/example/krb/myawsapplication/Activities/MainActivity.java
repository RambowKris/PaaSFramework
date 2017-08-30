package com.example.krb.myawsapplication.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.krb.myawsapplication.R;
import com.example.krb.myawsapplication.Supporting.DeCryptor;
import com.example.krb.myawsapplication.Supporting.EnCryptor;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
   _____                       _        _    _                      
  / ____|                     | |      | |  | |                     
 | (___   __ _ _ __ ___  _ __ | | ___  | |  | |___  __ _  __ _  ___ 
  \___ \ / _` | '_ ` _ \| '_ \| |/ _ \ | |  | / __|/ _` |/ _` |/ _ \
  ____) | (_| | | | | | | |_) | |  __/ | |__| \__ \ (_| | (_| |  __/
 |_____/ \__,_|_| |_| |_| .__/|_|\___|  \____/|___/\__,_|\__, |\___|
                        | |                               __/ |     
                        |_|                              |___/      
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAMPLE_ALIAS = "MYALIAS";

    Toolbar toolbar;

    EditText edTextToEncrypt;

    TextView tvEncryptedText;
    TextView tvDecryptedText;

    Button buttonEncrypt;
    Button buttonDecrypt;

    private EnCryptor encryptor;
    private DeCryptor decryptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setSupportActionBar(toolbar);

        tvEncryptedText = (TextView) findViewById(R.id.textview_encrypt);
        tvDecryptedText = (TextView) findViewById(R.id.textview_decrypt);

        edTextToEncrypt = (EditText) findViewById(R.id.edittext_enter_text);

        encryptor = new EnCryptor();

        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }

        buttonEncrypt = (Button) findViewById(R.id.button_encrypt);
        buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                encryptText();
            }

        });

        buttonDecrypt = (Button) findViewById(R.id.button_decrypt);
        buttonDecrypt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                decryptText();
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void decryptText() {
        try {
            tvDecryptedText.setText(decryptor
                    .decryptData(SAMPLE_ALIAS, encryptor.getEncryption(), encryptor.getIv()));
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void encryptText() {

        try {
            final byte[] encryptedText = encryptor
                    .encryptText(SAMPLE_ALIAS, edTextToEncrypt.getText().toString());
            tvEncryptedText.setText(Base64.encodeToString(encryptedText, Base64.DEFAULT));
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "onClick() called with: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}