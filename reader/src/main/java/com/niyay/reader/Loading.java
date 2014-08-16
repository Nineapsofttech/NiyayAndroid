package com.niyay.reader;

import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;

/**
 * Created by APPLE on 2/2/14.
 */
public class Loading implements Item {

    @Override
    public int getViewType() {
        return MenuAdapter.RowType.LOADING.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.indicator, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        //ImageView indicator = (ImageView) view.findViewById(R.id.imgLoading);
        return view;
    }

    @Override
    public JSONObject getContent() {
        return null;
    }
}
