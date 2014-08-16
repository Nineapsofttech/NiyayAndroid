package com.niyay.reader;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by APPLE on 2/3/14.
 */
public class ChapterListItem implements Item {

    private JSONObject chapter;

    public ChapterListItem(JSONObject data){
        chapter = data;
    }

    @Override
    public int getViewType() {
        return MenuAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.chapter_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        TextView txtNo = (TextView) view.findViewById(R.id.lblChapNo);
        TextView txtTitle = (TextView) view.findViewById(R.id.lblChapTitle);
        TextView txtUpdate = (TextView) view.findViewById(R.id.lblChapUpdate);
        TextView txtView = (TextView) view.findViewById(R.id.lblChapView);
        try {
            txtNo.setText(chapter.getString("no"));
            txtTitle.setText(chapter.getString("chapter_name"));
            txtUpdate.setText(chapter.getString("last_update"));
            txtView.setText("อ่าน "+chapter.getString("view"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public JSONObject getContent() {
        return chapter;
    }
}
