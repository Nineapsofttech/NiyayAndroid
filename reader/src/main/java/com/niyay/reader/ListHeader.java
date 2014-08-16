package com.niyay.reader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by APPLE on 2/7/14.
 */
public class ListHeader implements Item {

    String mType = null;
    ArrayAdapter<CharSequence> adapter;
    //ArrayAdapter<BasicNameValuePair> adapter;
    //ArrayList<BasicNameValuePair> categories;
    HeaderDelegate delegate;
    boolean mSearch;
    private Context mContext = null;

    public ListHeader(Context context, String type, boolean search){
        mContext = context;
        mType = type;
        mSearch = search;
        delegate = (HeaderDelegate)mContext;
        adapter = ArrayAdapter.createFromResource(mContext, R.array.planets_array, android.R.layout.simple_spinner_item);
        //adapter = new ArrayAdapter<BasicNameValuePair>(mContext, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public int getViewType() {
        return MenuAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.list_header, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        TextView txtHeader = (TextView) view.findViewById(R.id.lblCategory);
        txtHeader.setText(mType);
        final SearchView txtSearch = (SearchView) view.findViewById(R.id.txtSearch);
        txtSearch.setQueryHint("ค้นหานิยาย");
        int searchPlateId = txtSearch.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = txtSearch.findViewById(searchPlateId);
        if (searchPlate!=null) {
            searchPlate.setBackgroundColor(Color.LTGRAY);
            searchPlate.setBackgroundResource(R.drawable.frame01);
            //searchPlate.setMinimumHeight(72);
            //searchPlate.setBackground(R.drawable.frame01);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText!=null) {
                searchText.setTextColor(Color.DKGRAY);
                //searchText.setTextSize(40);
                searchText.setHintTextColor(Color.LTGRAY);
            }
        }
        /*
        TextView txtSearch = (TextView) view.findViewById(R.id.txtSearch2);
        */
        Category spinner = (Category) view.findViewById(R.id.spinType);
        //spinner.setSelection(Config.getInstance().category);
        String cat = adapter.getItem(Config.getInstance().category).toString();
        if (mSearch){
            spinner.setVisibility(View.GONE);
            txtSearch.setVisibility(View.VISIBLE);
            txtSearch.requestFocus();
            txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
                    if (TextUtils.isEmpty(s) == false){
                        delegate.onSearch(s);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
            /*
            */
        }
        else{
            spinner.setVisibility(View.VISIBLE);
            spinner.setAdapter(adapter);
            spinner.setSelection(Config.getInstance().category);
            spinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Config config = Config.getInstance();
                    config.category = i;
                    DataBaseSQLite.getInstance().save_config(config);
                    delegate.onSelectCategory();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spinner.setPrompt(cat);
            txtSearch.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public JSONObject getContent() {
        return null;
    }

    public interface HeaderDelegate{
        public void onSearch(String keyword);
        public void onSelectCategory();
    }
}
