package com.niyay.reader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by APPLE on 2/3/14.
 */
public class Detail_fragment extends Fragment {

    private WebView viewDetail = null;
    private String mChapter = null;
    private ImageView loadding;
    //private AdView mAdView;

    public void load(String chapter){
        mChapter = chapter;
        viewDetail.loadUrl("http://www.niyay.com/mobile_app/story.php?chapter=" + chapter);
    }

    public void clearContent(){
        mChapter = null;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail, container, false);
        loadding = (ImageView) rootView.findViewById(R.id.imageLoading1);
        viewDetail = (WebView) rootView.findViewById(R.id.viewSynopsis);
        viewDetail.getSettings().setJavaScriptEnabled(true);
        viewDetail.getSettings().setLoadWithOverviewMode(true);
        viewDetail.getSettings().setUseWideViewPort(true);
        viewDetail.getSettings().setBuiltInZoomControls(true);
        viewDetail.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                viewDetail.setVisibility(View.GONE);
                loadding.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //loadding.setVisibility(View.GONE);
                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnable, 1000);//Message will be delivered in 1 second.
                viewDetail.setVisibility(View.VISIBLE);
            }
        });
        viewDetail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        viewDetail.setLongClickable(false);
        if (mChapter != null)
            load(mChapter);
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
}
