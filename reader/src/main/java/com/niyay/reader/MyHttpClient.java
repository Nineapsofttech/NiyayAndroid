package com.niyay.reader;

import android.content.Context;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.security.KeyStore;

/**
 * Created by APPLE on 1/30/14.
 */
public class MyHttpClient extends DefaultHttpClient {
    final Context context;

    public MyHttpClient(Context context) {
        this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
// Register for port 443 our SSLSocketFactory with our keystore
// to the ConnectionManager
        registry.register(new Scheme("https", newSslSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
// Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");
// Get the raw resource, which contains the keystore with
// your trusted certificates (root and any intermediate certs)
// Pass the keystore to the SSLSocketFactory. The factory is responsible
// for the verification of the server certificate.
            SSLSocketFactory sf = new SSLSocketFactory(trusted);
// Hostname verification from certificate
// http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            return sf;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
