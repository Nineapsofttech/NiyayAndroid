package com.niyay.reader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by APPLE on 2/3/14.
 */
public class Synopsis_fragment extends Fragment {

    //private ArrayList<Item> listItems;
    //private DetailHeaderItem HeaderItem = null;
    //protected MenuAdapter adapter = null;
    private String mStory = null;
    private String mUser = null;
    private WebView webView = null;
    private ImageView loadding;

    //private AdView mAdView;

    public Synopsis_fragment(Activity activity){
        //listItems = new ArrayList<Item>();
        //HeaderItem = new DetailHeaderItem();
        //adapter = new MenuAdapter(activity, android.R.layout.simple_list_item_1, listItems);
    }

    public void load(String StoryId, String UserId){
        //adapter.urlheader = "http://www.niyay.com/mobile_app/story.php?id=" + StoryId;
        //new LoadSynopsisAsync().execute(adapter);
        mStory = StoryId;
        mUser = UserId;
        String html = "<html><head></head><body><div align=\"center\"><img src=\"ic_loading.png\" /></div></body></html>";
        //HeaderItem.viewSynopsis.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "UTF-8", null);
        //HeaderItem.viewSynopsis.loadUrl("http://www.niyay.com/mobile_app/synopsis.php?id=" + StoryId);
        webView.loadUrl("http://www.niyay.com/mobile_app/synopsis.php?id=" + StoryId);
        //adapter.urlheader = "http://www.niyay.com/mobile_app/profile.php?id=" + UserId;
        //new LoadProfileHeaderAsync().execute(adapter);
    }

    private Runnable mMyRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            //Change state here
            loadding.setVisibility(View.GONE);
        }
    };

    public void clearContent(){
        mStory = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.synopsis, container, false);
        loadding = (ImageView) rootView.findViewById(R.id.imageLoading1);
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient(){
            //ProgressDialog pd = null;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(View.GONE);
                loadding.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //loadding.setVisibility(View.GONE);
                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnable, 1000);//Message will be delivered in 1 second.
                webView.setVisibility(View.VISIBLE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);

        //ListView listView = (ListView) rootView.findViewById(R.id.listItem);
        //listView.setAdapter(adapter);
        //if (listItems.size() == 0){
        //    adapter.add(HeaderItem);
        //}
        if (mStory != null)
            load(mStory, mUser);
        /*
        mAdView = new AdView(getActivity());
        mAdView.setAdUnitId(getResources().getString(R.string.ad_unit_id));
        //mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        //mAdView.setAdListener(new ToastAdListener(getActivity()));
        RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.layAds);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(mAdView, params);
        mAdView.loadAd(new AdRequest.Builder().build());
        */
        return rootView;
    }
    /*
    @Override
    public void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    public void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
    */
    private class LoadSynopsisAsync extends AsyncTask<MenuAdapter, Void, String> {

        MenuAdapter adapter = null;

        @Override
        protected String doInBackground(MenuAdapter... params) {
            adapter = params[0];
            HttpGet post = null;
            try {
                post = new HttpGet(new URI(adapter.urlheader));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            HttpClient client = new DefaultHttpClient();
            client = this.sslClient(client);
            HttpResponse result = null;
            try {
                result = client.execute(post);
                return EntityUtils.toString(result.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result != null){
                DetailHeaderItem header = (DetailHeaderItem) adapter.getItem(0);
                header.viewSynopsis.loadUrl(result);
            }
        }

        private HttpClient sslClient(HttpClient client) {
            try {
                X509TrustManager tm = new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{tm}, null);
                SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
                ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                ClientConnectionManager ccm = client.getConnectionManager();
                SchemeRegistry sr = ccm.getSchemeRegistry();
                sr.register(new Scheme("https", ssf, 443));
                return new DefaultHttpClient(ccm, client.getParams());
            } catch (Exception ex) {
                return null;
            }
        }

    }
}
