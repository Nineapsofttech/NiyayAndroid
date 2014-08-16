package com.niyay.reader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import com.google.android.gms.ads.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, List_fragment.FeedDelegate,
Chapter_fragment.ChapterDelegate, NiyayPager.NavigationDelegate, ListHeader.HeaderDelegate{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    NiyayPager mViewPager;
    List_fragment viewList;
    Chapter_fragment viewChapter;
    Synopsis_fragment viewSynopsis;
    Detail_fragment viewDetail;
    MenuItem selMenu;
    int selMenuBackground;
    String storyUser;
    //String selStory = null;
    //String selStoryName = null;
    String selChapter = null;
    JSONObject selStory = null;
    private AdView mAdView;

    android.support.v7.app.ActionBar.Tab tabSynopsis;
    android.support.v7.app.ActionBar.Tab tabChapter;
    android.support.v7.app.ActionBar.Tab tabDetail;

    ImageButton curMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout mainLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(mainLayout);

        Intent i = getIntent();
        String preload = i.getStringExtra("pre_load");

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        if (savedInstanceState == null){
            View.OnClickListener menuListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = (String)view.getTag();
                    curMenu.setImageResource(selMenuBackground);
                    curMenu = (ImageButton) view;
                    if (tag.equals("search")){
                        selMenuBackground = R.drawable.ic_search;
                        curMenu.setImageResource(R.drawable.ic_search_sel);
                    }
                }
            };
            //main_menu = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.menu_bar, null);
           // ImageButton btnSearch = (ImageButton) mainLayout.findViewById(R.id.search);
            //ImageButton btnCategory = (ImageButton) mainLayout.findViewById(R.id.search);
            //ImageButton btnRecommend = (ImageButton) mainLayout.findViewById(R.id.search);
            //ImageButton btnOther = (ImageButton) mainLayout.findViewById(R.id.search);
            //btnHome.setOnClickListener(menuListener);
            //btnSearch.setOnClickListener(menuListener);
            //btnCategory.setOnClickListener(menuListener);
            //btnRecommend.setOnClickListener(menuListener);
            //btnOther.setOnClickListener(menuListener);

            //curMenu = btnSearch;
            //selMenuBackground = R.drawable.ic_home;
            //curMenu.setImageResource(R.drawable.ic_hot_sel);
        }



        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (NiyayPager) findViewById(R.id.pager);
        mViewPager.delegate = this;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        try {
            viewList = new List_fragment(this, new JSONArray(preload));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewChapter = new Chapter_fragment(this);
        viewSynopsis = new Synopsis_fragment(this);
        viewDetail = new Detail_fragment();
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        tabSynopsis = actionBar.newTab().setText("คำโปรย").setTabListener(this);
        tabChapter = actionBar.newTab().setText("ตอน").setTabListener(this);
        tabDetail = actionBar.newTab().setText("เนื้อหา").setTabListener(this);

        actionBar.addTab(actionBar.newTab().setText("รายการ").setTabListener(this));

        ImageLoader.getInstance(this);
        DataBaseSQLite.getInstance(this);

        mAdView = new AdView(this);
        mAdView.setAdUnitId(getResources().getString(R.string.ad_unit_id));
        mAdView.setAdSize(AdSize.SMART_BANNER);
        RelativeLayout layout = (RelativeLayout) mainLayout.findViewById(R.id.layAds);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mAdView, params);
        mAdView.loadAd(new AdRequest.Builder().build());

    }












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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        selMenu = menu.getItem(1);
        selMenuBackground = R.drawable.ic_update;
        selMenu.setIcon(R.drawable.ic_update_sel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.home){
            mViewPager.setCurrentPageOnSaved();
            return true;
        }
        else{
            ActionBar actionBar = getSupportActionBar();
            while (actionBar.getTabCount() > 1){
                getSupportFragmentManager().popBackStack();
                actionBar.removeTab(actionBar.getTabAt(actionBar.getTabCount()-1));
            }
            selMenu.setIcon(selMenuBackground);
            selMenu = item;
            mViewPager.setCurrentItem(0, true);
            if (id == R.id.menu_update){
                selMenuBackground = R.drawable.ic_update;
                item.setIcon(R.drawable.ic_update_sel);
                viewList.load(NiyayType.UPDATE, "นิยาย Update", false);
            }
            else if (id == R.id.menu_hot){
                selMenuBackground = R.drawable.ic_hot;
                item.setIcon(R.drawable.ic_hot_sel);
                viewList.load(NiyayType.HOT, "นิยายติดอันดับs", false);
            }
            else if (id == R.id.menu_new){
                selMenuBackground = R.drawable.ic_new;
                item.setIcon(R.drawable.ic_new_sel);
                viewList.load(NiyayType.NEW, "นิยายมาใหมs่", false);
            }
            else if (id == R.id.menu_finished){
                selMenuBackground = R.drawable.ic_finished;
                item.setIcon(R.drawable.ic_finished_sel);
                viewList.load(NiyayType.FINISHED, "นิยายจบแล้ว", false);
            }
            else if (id == R.id.menu_favorite){
                selMenuBackground = R.drawable.ic_favorite;
                item.setIcon(R.drawable.ic_favorite_sel);
                viewList.load(NiyayType.FAVORITE, "นิยายชื่นชอบ", false);
            }
            else if (id == R.id.menu_search){
                selMenuBackground = R.drawable.ic_search;
                item.setIcon(R.drawable.ic_search_sel);
                viewList.load(NiyayType.SEARCH, "ค้นหานิยาย", true);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        int position = tab.getPosition();
        mViewPager.setCurrentItem(position, true);
        if (position == 2)
            viewChapter.load(selStory);
        else if (position == 3)
            viewDetail.load(selChapter);
        mViewPager.clearBackStack();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onFeedSelectItem(JSONObject selItem) {
        selStory = selItem;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar.getTabCount() == 1){
            actionBar.addTab(tabSynopsis);
            actionBar.addTab(tabChapter);
        }
        else if (actionBar.getTabCount() == 4){
            getSupportFragmentManager().popBackStack();
            actionBar.removeTab(tabDetail);
        }
        viewSynopsis.clearContent();
        mViewPager.setCurrentItem(1, true, true);
        try {
            viewSynopsis.load(selStory.getString("story_id"), selStory.getString("yehyeh_user_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectChapter(String ChapterId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar.getTabCount() == 3)
            actionBar.addTab(tabDetail);
        selChapter = ChapterId;
        viewDetail.clearContent();
        mViewPager.setCurrentItem(3);
    }

    @Override
    public void setBackNavitationShow(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }

    @Override
    public void onSearch(String keyword) {
        viewList.search(keyword);
    }

    @Override
    public void onSelectCategory() {
        ActionBar actionBar = getSupportActionBar();
        while (actionBar.getTabCount() > 1){
            getSupportFragmentManager().popBackStack();
            actionBar.removeTab(actionBar.getTabAt(actionBar.getTabCount()-1));
        }

        viewList.load();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0)
                fragment = viewList;
            else if (position == 1)
                fragment = viewSynopsis;
            else if (position == 2)
                fragment = viewChapter;
            else if (position == 3)
                fragment = viewDetail;
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

}
