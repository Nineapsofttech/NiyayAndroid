package com.niyay.reader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by APPLE on 2/3/14.
 */
public class List_fragment extends Fragment implements MenuAdapter.AdapterDelegate {
    private ArrayList<Item> listItems;
    static Integer limit = 10;
    protected MenuAdapter adapter = null;
    private Loading indicator = null;
    String headerTitle = "นิยาย update";
    String mKeySearch = "";
    boolean mSearch = false;
    private JSONArray mPreload = null;
    private ListView listView = null;
    private View curView = null;
    NiyayType curtype = NiyayType.UPDATE;
    //private AdView mAdView;
    //private AdView mAdView;

    private LoadMoreScrollListener scrollListener = new LoadMoreScrollListener(){

        @Override
        public void onLoadMore(Integer page) {
            feed(page);
        }
    };

    public List_fragment(Activity activity, JSONArray preload){
        listItems = new ArrayList<Item>();
        indicator = new Loading();
        mPreload = preload;
        adapter = new MenuAdapter(activity, android.R.layout.simple_list_item_1, listItems);
        adapter.delegate = (MenuAdapter.AdapterDelegate) this;
    }

    public void load(NiyayType type, String title, boolean search){
        listView.clearChoices();
        clearSelectView();
        curtype = type;
        headerTitle = title;
        mSearch = search;
        adapter.clear();
        adapter.add(new ListHeader(getActivity(), headerTitle, search));
        scrollListener.reset();
        if (curtype != NiyayType.FAVORITE)
            adapter.postValue = null;
        if (search == false)
            feed(0);
        else
            adapter.setNotifyOnChange(true);
    }

    public void search(String keyword){
        adapter.clear();
        adapter.add(new ListHeader(getActivity(), headerTitle, true));
        scrollListener.reset();
        mKeySearch = keyword;
        feed(0);
    }

    public void load(){
        load(curtype, headerTitle, mSearch);
    }

    private void feed(Integer page){
        adapter.add(indicator);
        adapter.setNotifyOnChange(true);
        if (curtype == NiyayType.FAVORITE){
            adapter.postValue = DataBaseSQLite.getInstance().getFavorite(page, limit);
            adapter.url = "http://www.niyay.com/mobile_app/favorite.php?limit=" + adapter.postValue.size();
        }
        else{
            adapter.url = "http://www.niyay.com/mobile_app/feed.php?page="+page.toString()+"&limit="+limit.toString()+"&cat="+Config.getInstance().category.toString()+"&show="+curtype.toString()+"&search="+mKeySearch;
        }
        new LoadListAsync().execute(adapter);
    }

    private void getFavorite(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FeedDelegate delegate = (FeedDelegate) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.listItem);
        if (mPreload != null){
            adapter.add(new ListHeader(getActivity(), headerTitle, mSearch));
            for (int i=0; i<mPreload.length(); i++){
                try {
                    JSONObject c = mPreload.getJSONObject(i);
                    Boolean distinct = true;
                    for (int j=1; j<adapter.getCount(); j++){
                        TextListItem inst = (TextListItem) adapter.getItem(j);
                        if (inst.getContent().getString("story_id").equals(c.getString("story_id"))){
                            distinct = false;
                            break;
                        }
                    }
                    if (distinct)
                        adapter.add(new TextListItem(c));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (mPreload.length() >= limit)
                scrollListener.ready();
            mPreload = null;
            //load(headerTitle, mSearch);
        }
        //listView.setSelector(R.drawable.bg_key);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //view.setSelected(true);
                //JSONObject item = listItems.get(i).getContent();
                //delegate.onFeedSelectItem(item.getString("story_id"), item.getString("story_name"), item.getString("yehyeh_user_id"));
                delegate.onFeedSelectItem(listItems.get(i).getContent());
                TextView title = (TextView) view.findViewById(R.id.lblNiyayTitle);
                TextView txtCount = (TextView) view.findViewById(R.id.lblViewCount);
                TextView txtUpdate = (TextView) view.findViewById(R.id.lblUpdate);
                title.setTextColor(Color.WHITE);
                txtUpdate.setTextColor(Color.WHITE);
                txtCount.setTextColor(Color.RED);
                clearSelectView();
                curView = view;

            }
        });

        listView.setOnScrollListener(scrollListener);
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
            TextView prevTitle = (TextView) curView.findViewById(R.id.lblNiyayTitle);
            TextView prevCount = (TextView) curView.findViewById(R.id.lblViewCount);
            TextView prevUpdate = (TextView) curView.findViewById(R.id.lblUpdate);
            prevCount.setTextColor(Color.parseColor("#168cce"));
            prevTitle.setTextColor(Color.BLACK);
            prevUpdate.setTextColor(Color.DKGRAY);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onTaskFinished() {
        scrollListener.ready();
    }

    public interface FeedDelegate{
        public void onFeedSelectItem(JSONObject selItem);
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
    private class LoadListAsync extends AsyncTask<MenuAdapter, Void, JSONArray> {

        MenuAdapter adapter = null;

        @Override
        protected JSONArray doInBackground(MenuAdapter... params) {
            adapter = params[0];

            JSONArray json = null;
            JSONParser jParser = new JSONParser();
            try {
                if (adapter.postValue != null)
                    json = jParser.getJSONFromUrl(adapter.url, adapter.postValue);
                else
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
                        Boolean distinct = true;
                        for (int j=1; j<adapter.getCount(); j++){
                            TextListItem inst = (TextListItem) adapter.getItem(j);
                            if (inst.getContent().getString("story_id").equals(c.getString("story_id"))){
                                distinct = false;
                                break;
                            }
                        }
                        if (distinct)
                            adapter.add(new TextListItem(c));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (result.length() >= limit)
                    adapter.UpdateTaskFinished();
            }
            adapter.setNotifyOnChange(true);
        }
    }
}
