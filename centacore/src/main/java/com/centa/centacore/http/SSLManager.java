package com.centa.centacore.http;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * https支持
 * 提供OKhttp 所需 SSLSocketFactory 和 X509TrustManager
 * SSLSocketFactory 需要keyManager 的支持 KeyManager 主要用于 支持私有证书文件。
 */
public class SSLManager {

    private File mBksFile;
    private String mPass;
    private File[] mCerFiles;
    private FileInputStream mBKSStream;
    private ArrayList<InputStream> mCerStreams;
    private X509TrustManager x509;


    public SSLManager(File... cerFiles) {
        this(null, null, cerFiles);
    }

    public SSLManager(File bksFile, String pass, File... cerFiles) {

        mBksFile = bksFile;
        mPass = pass;
        mCerFiles = cerFiles;

        //传入文件转化成IO流文件
        mCerStreams = new ArrayList<>();
        try {
            mBKSStream = new FileInputStream(mBksFile);
            for (File cerFile : cerFiles) {
                mCerStreams.add(new FileInputStream(cerFile));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取SSL
     * @return
     */
    public SSLSocketFactory getSSLSocketFactory () {
        try {
            SSLContext tls = SSLContext.getInstance("TLS");
            tls.init(getKayManagers(), getTrustManager(), new SecureRandom());
            return tls.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cer 证书相关管理器
     * @return
     */
    private TrustManager[] getTrustManager() {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null);
            //根据证书数量添加 Certificate
            for (int i = 0; i < mCerStreams.size(); i++) {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                ks.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(mCerStreams.get(i)));
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            //TODO 此处遍历出所有的 x509 证书，一边用于获取出来，供外界调用调用取使用。
            poolX509(tmf);
            return tmf.getTrustManagers();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void poolX509(TrustManagerFactory tmf) {
        for (TrustManager trustManager : tmf.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                x509 = (X509TrustManager) trustManager;
                break;
            }
        }
    }

    /**
     * BKS相关
     * @return
     */
    private KeyManager[] getKayManagers () {
        if (mBksFile == null || TextUtils.isEmpty(mPass)) {//如果 BKS 相关文件不存在，则返回null，表示不需要KeyManager
            return null;
        }
        try {
            KeyStore bks = KeyStore.getInstance("BKS");
            bks.load(mBKSStream, mPass.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(bks, mPass.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public X509TrustManager getX509(){
        return x509;
    }

    public static HostnameVerifier hostNameVerifier(){
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
}
