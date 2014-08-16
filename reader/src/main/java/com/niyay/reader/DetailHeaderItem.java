package com.niyay.reader;

import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by APPLE on 2/3/14.
 */
public class DetailHeaderItem implements Item{

    public WebView viewSynopsis;

    @Override
    public int getViewType() {
        return MenuAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.detail, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        viewSynopsis = (WebView) view.findViewById(R.id.viewSynopsis);
        viewSynopsis.getSettings().setJavaScriptEnabled(true);
        viewSynopsis.getSettings().setLoadWithOverviewMode(true);
        viewSynopsis.getSettings().setUseWideViewPort(true);
        viewSynopsis.getSettings().setBuiltInZoomControls(true);
        viewSynopsis.setBackgroundColor(0x00000000);
        viewSynopsis.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        viewSynopsis.setWebChromeClient(new WebChromeClient());
        return view;
    }

    @Override
    public JSONObject getContent() {
        return null;
    }
}
