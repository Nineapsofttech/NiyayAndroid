package com.niyay.reader;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * Created by APPLE on 2/6/14.
 */
public class Chapter_fragment extends Fragment implements MenuAdapter.AdapterDelegate {

    private ArrayList<Item> listItems;
    static Integer limit = 10;
    //private ProfileHeader HeaderItem = null;
    private Loading indicator = null;
    protected MenuAdapter adapter = null;
    private String storyId;
    private ListView listView;
    private View curView = null;
    private LoadMoreScrollListener scrollListener = new LoadMoreScrollListener(){

        @Override
        public void onLoadMore(Integer page) {
            feed(page);
        }
    };

    //private AdView mAdView;

    public Chapter_fragment(Activity activity){
        listItems = new ArrayList<Item>();
        indicator = new Loading();
        //HeaderItem = new ProfileHeader();
        adapter = new MenuAdapter(activity, android.R.layout.simple_list_item_1, listItems);
        adapter.delegate = this;
    }

    public void load(JSONObject parm){
        String story_id = null;
        String user_id = null;
        try {
            story_id = parm.getString("story_id");
            user_id = parm.getString("yehyeh_user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (story_id.equals(storyId)){
            adapter.setNotifyOnChange(true);
        }
        else{
            adapter.clear();
            listView.clearChoices();
            clearSelectView();
            scrollListener.reset();

            storyId = story_id;
            adapter.parm = parm;
            adapter.urlheader = "http://www.niyay.com/mobile_app/profile.php?id=" + user_id + "&story=" + story_id;
            new LoadProfileHeaderAsync().execute(adapter);
            feed(0);
        }
    }

    private void feed(Integer page){
        adapter.add(indicator);
        adapter.setNotifyOnChange(true);
        adapter.url = "http://www.niyay.com/mobile_app/chapter.php?id=" + storyId + "&page=" + page.toString() + "&limit=" + limit.toString();
        new LoadChapterAsync().execute(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ChapterDelegate delegate = (ChapterDelegate) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.listItem);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(scrollListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    JSONObject item = listItems.get(i).getContent();
                    try {
                        delegate.onSelectChapter(item.getString("chapter_id"));
                        TextView title = (TextView) view.findViewById(R.id.lblChapTitle);
                        TextView txtCount = (TextView) view.findViewById(R.id.lblChapView);
                        TextView txtUpdate = (TextView) view.findViewById(R.id.lblChapUpdate);
                        TextView txtNumber = (TextView) view.findViewById(R.id.lblChapNo);
                        title.setTextColor(Color.WHITE);
                        txtUpdate.setTextColor(Color.WHITE);
                        txtCount.setTextColor(Color.RED);
                        txtNumber.setTextColor(Color.WHITE);
                        clearSelectView();
                        curView = view;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

    public void clearSelectView(){
        if (curView != null){
            TextView prevTitle = (TextView) curView.findViewById(R.id.lblChapTitle);
            TextView prevCount = (TextView) curView.findViewById(R.id.lblChapView);
            TextView prevUpdate = (TextView) curView.findViewById(R.id.lblChapUpdate);
            TextView prevNo = (TextView) curView.findViewById(R.id.lblChapNo);
            prevCount.setTextColor(Color.parseColor("#ffffbb33"));
            prevTitle.setTextColor(Color.BLACK);
            prevNo.setTextColor(Color.DKGRAY);
            prevUpdate.setTextColor(Color.DKGRAY);
        }
    }

    @Override
    public void onTaskFinished() {
        scrollListener.ready();
    }

    public interface ChapterDelegate{
        public void onSelectChapter(String ChapterId);
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
    private class LoadChapterAsync extends AsyncTask<MenuAdapter, Void, JSONArray> {

        MenuAdapter adapter = null;

        @Override
        protected JSONArray doInBackground(MenuAdapter... params) {
            adapter = params[0];
            JSONArray json = null;
            JSONParser jParser = new JSONParser();
            try {
                json = jParser.getJSONFromUrl(adapter.url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray result)
        {
            adapter.remove(indicator);
            if (result != null){
                for (int i=0; i<result.length(); i++){
                    try {
                        JSONObject c = result.getJSONObject(i);
                        adapter.add(new ChapterListItem(result.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (result.length() >= 10)
                    adapter.UpdateTaskFinished();
            }
            adapter.setNotifyOnChange(true);
        }

    }

}
