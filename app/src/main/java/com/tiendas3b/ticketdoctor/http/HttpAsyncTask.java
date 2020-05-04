package com.tiendas3b.ticketdoctor.http;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by dfa on 23/02/2016.
 */
public class HttpAsyncTask extends AsyncTask<Void, Void, Void> {

    protected static final int OK = 1;
    protected static final int ERROR = -1;
    protected int status = 0;

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    public OutputStream createOutputStream(final HttpsURLConnection conn) throws IOException {

        OutputStream outputStream = null;

        //2SSL beg
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        conn.setHostnameVerifier(hostnameVerifier);
        //2SSL end

//                //1SSL beg
//                try {
//                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
//
//                    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
//                    InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
//                    Certificate ca;
//                    try {
//                        ca = cf.generateCertificate(caInput);
//                        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//                    } finally {
//                        caInput.close();
//                    }
//
//                    // Create a KeyStore containing our trusted CAs
//                    String keyStoreType = KeyStore.getDefaultType();
//                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//                    keyStore.load(null, null);
//                    keyStore.setCertificateEntry("ca", ca);
//
//                    // Create a TrustManager that trusts the CAs in our KeyStore
//                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//                    tmf.init(keyStore);
//
//                    // Create an SSLContext that uses our TrustManager
//                    SSLContext context = SSLContext.getInstance("TLS");
//                    context.init(null, tmf.getTrustManagers(), null);
//                    conn.setSSLSocketFactory(context.getSocketFactory());
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (KeyStoreException e) {
//                    e.printStackTrace();
//                } catch (KeyManagementException e) {
//                    e.printStackTrace();
//                }
//                //1SSL end

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // Send message content.
        outputStream = conn.getOutputStream();
        return outputStream;
    }
}
