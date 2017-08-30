package com.example.krb.myawsapplication.Supporting;

/**
 * Created by krb on 23/08/2017.
 */

/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 *
 * For functions checkMD5 + calculateMD5
 */

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    private static final String AndroidKeyStore = "AndroidKeyStore";
    //    private static final String AES_MODE = "AES/ECB/PKCS7Padding";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private KeyStore keyStore;
    private SecretKey secretKey;
    private byte[] salt;

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void generateKey(String KEY_ALIAS) {
        try {
            keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
                keyGenerator.init(new KeyGenParameterSpec
                        .Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(false)
                        .build());
                keyGenerator.generateKey();
            }
        } catch (Exception e) {
            Log.e(e.getClass().toString(), e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String encryptData(String input) {
        if (secretKey == null) {
            secretKey = new SecretKeySpec("dewmiu32j432idnj".getBytes(), "AES");
//            generateKey(AES_MODE);
        }

        if (salt == null) {
            salt = "23fewfewifm3oi4j35".getBytes();
//            salt = c.getIV();
        }

        try {
            Cipher c = Cipher.getInstance(AES_MODE);
            c.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, salt));
            byte[] encodedBytes = c.doFinal(input.getBytes());
            String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
            return encryptedBase64Encoded;
        } catch (Exception e) {
            Log.e(e.getClass().toString(), e.getMessage());
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String decryptData(String encrypted) {
        if (salt == null) {
            return null;
        }

        try {
            Cipher c = Cipher.getInstance(AES_MODE);
            c.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, salt));
            byte[] decodedBytes = c.doFinal(encrypted.getBytes());
            salt = null;
            return decodedBytes.toString();
        } catch (Exception e) {
            Log.e(e.getClass().toString(), e.getMessage());
        }
        return null;
    }

    public static String md5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean checkMD5(String md5, File updateFile) {
        return Crypto.checkMD5(md5, updateFile, "MD5");
    }

    public static boolean checkMD5(String md5, File updateFile, String TAG) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            Log.e(TAG, "MD5 string empty or updateFile null");
            return false;
        }

        String calculatedDigest = calculateMD5(updateFile);
        if (calculatedDigest == null) {
            Log.e(TAG, "calculatedDigest null");
            return false;
        }

        Log.v(TAG, "Calculated digest: " + calculatedDigest);
        Log.v(TAG, "Provided digest: " + md5);

        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String calculateMD5(File updateFile) {
        return calculateMD5(updateFile, "MD5");
    }

    public static String calculateMD5(File updateFile, String TAG) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(TAG);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception while getting FileInputStream", e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }
}
