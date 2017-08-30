package com.example.krb.myawsapplication.Supporting;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.security.auth.x500.X500Principal;

/**
 * Created by krb on 27/08/2017.
 */

public class CryptoManager {

    static final String TAG = "SimpleKeystoreApp";
    static final String CIPHER_TYPE = "AES/CBC/PKCS7Padding";
    static final Boolean SYMMETRIC_KEY = true;
    //    static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";
//    static final Boolean SYMMETRIC_KEY = false;
    static final String CIPHER_PROVIDER = "AndroidOpenSSL";
    static final String SALT = "okedis23os32si1u";

    String KEY_ALIAS;

    List<String> keyAliases;

    KeyPair keyPair;

    KeyStore keyStore;

    public void init(String alias) {
        KEY_ALIAS = alias;

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
        }
        refreshKeys();
    }

    private void refreshKeys() {
        keyAliases = new ArrayList<>();
        try {
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement());
            }
        } catch (Exception e) {
        }

//        for(String key:keyAliases) {
//            if (key==KEY_ALIAS){
//                this.
//            }
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createNewKeys(Activity activity) {
        try {
            if (keyStore.containsAlias(KEY_ALIAS)) {
                this.deleteKey(activity);
            }

            // Create new key if needed
            if (keyPair == null) {
                if (SYMMETRIC_KEY) {
                    KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
                    KeyGenParameterSpec keySpec = builder
                            .setKeySize(256)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .setRandomizedEncryptionRequired(false)
//                        .setRandomizedEncryptionRequired(true)
//                        .setUserAuthenticationRequired(true)
//                        .setUserAuthenticationValidityDurationSeconds(5 * 60)
                            .build();
                    KeyGenerator kg = KeyGenerator.getInstance("AES", "AndroidKeyStore");
                    kg.init(keySpec);
                    SecretKey key = kg.generateKey();
                } else {
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    end.add(Calendar.YEAR, 1);
                    KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(activity)
                            .setAlias(KEY_ALIAS)
                            .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(start.getTime())
                            .setEndDate(end.getTime())
                            .build();
                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                    generator.initialize(spec);

                    keyPair = generator.generateKeyPair();
                }
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
        refreshKeys();
    }

    public void deleteKey(final Activity activity) {
//        AlertDialog alertDialog = new AlertDialog.Builder(activity)
//                .setTitle("Delete Key")
//                .setMessage("Do you want to delete the key \"" + KEY_ALIAS + "\" from the keystore?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
//                            keyStore.deleteEntry(KEY_ALIAS);
//                            refreshKeys();
//                        } catch (KeyStoreException e) {
//                            Toast.makeText(activity,
//                                    "Exception " + e.getMessage() + " occured",
//                                    Toast.LENGTH_LONG).show();
//                            Log.e(TAG, Log.getStackTraceString(e));
//                        }
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .create();
//        alertDialog.show();

        try {
            keyStore.deleteEntry(KEY_ALIAS);
            refreshKeys();
        } catch (KeyStoreException e) {
            Toast.makeText(activity,
                    "Exception " + e.getMessage() + " occured",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }

    public Map<String, String> getKeyPair() {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            byte[] publicKeyEncoded = publicKey.getEncoded();
            byte[] publicKeyBytes = Base64.encode(publicKeyEncoded, 0);
            String pubKey = new String(publicKeyBytes);

            Key privateKey = privateKeyEntry.getPrivateKey();
            byte[] privateKeyEncoded = privateKey.getEncoded();
//            byte[] privateKeyBytes = Base64.encode(privateKeyEncoded,0);
//            String privKey = new String(privateKeyBytes);

            Map<String, String> keyPair = new HashMap<String, String>();
            keyPair.put("public key", pubKey);
//            keyPair.put("private key", privKey);

            return keyPair;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String encryptString(Activity activity, String initialText) {
        try {

            Cipher inCipher;
            // Symmetric key, like AES, is handled different from asymmetric key, RSA
            if (SYMMETRIC_KEY) {
                KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

                if (initialText.isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(), "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                    return null;
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Secret Key: " + secretKeyEntry.getSecretKey().toString(), Toast.LENGTH_LONG);
                }

                inCipher = Cipher.getInstance(CIPHER_TYPE);
                inCipher.init(Cipher.ENCRYPT_MODE, secretKeyEntry.getSecretKey(), new IvParameterSpec(SALT.getBytes()));
//                inCipher.init(Cipher.ENCRYPT_MODE, secretKeyEntry.getSecretKey(), new GCMParameterSpec(128, SALT.getBytes()));

            } else {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
                RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

                if (initialText.isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(), "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                    return null;
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Public Key: " + publicKey.toString(), Toast.LENGTH_LONG);
                }

                inCipher = Cipher.getInstance(CIPHER_TYPE);
                inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inCipher);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
            String encryptedText = Base64.encodeToString(vals, Base64.DEFAULT);
            return encryptedText;
        } catch (Exception e) {
            Toast.makeText(activity, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String decryptString(Activity activity, String cipherText) {
        try {
            Cipher output;
            if (SYMMETRIC_KEY) {
                KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

                output = Cipher.getInstance(CIPHER_TYPE);
                output.init(Cipher.DECRYPT_MODE, secretKeyEntry.getSecretKey(), new IvParameterSpec(SALT.getBytes()));
//                output.init(Cipher.DECRYPT_MODE, secretKeyEntry.getSecretKey(), new GCMParameterSpec(128, SALT.getBytes()));

            } else {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

                output = Cipher.getInstance(CIPHER_TYPE);
                output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            }
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");

            return finalText;

        } catch (Exception e) {
            Toast.makeText(activity, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
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

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] textBytes = text.getBytes("iso-8859-1");
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
