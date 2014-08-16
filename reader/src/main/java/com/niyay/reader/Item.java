package com.niyay.reader;

import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;

/**
 * Created by APPLE on 1/10/14.
 */
public interface Item {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
    public JSONObject getContent();
}
