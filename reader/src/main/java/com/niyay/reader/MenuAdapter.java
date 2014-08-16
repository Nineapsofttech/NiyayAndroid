package com.niyay.reader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by APPLE on 1/12/14.
 */
public class MenuAdapter extends ArrayAdapter<Item> {
    private LayoutInflater mInflater;
    public String urlheader;
    public String url;
    public JSONObject parm;
    public AdapterDelegate delegate = null;
    public List<NameValuePair> postValue = null;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM, LOADING
    }

    public MenuAdapter(Activity context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }

    public void UpdateTaskFinished(){
        if (delegate != null)
            delegate.onTaskFinished();
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }

    public interface AdapterDelegate{
        public void onTaskFinished();
    }
}
