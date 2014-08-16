package com.niyay.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by APPLE on 1/20/14.
 */
public class TextListItem implements Item{

    private JSONObject item;
    //private Context mContext = null;

    public TextListItem(JSONObject json) {
        item = json;
    }

    @Override
    public int getViewType() {
        return MenuAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.niyay_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        TextView txtTitle = (TextView) view.findViewById(R.id.lblNiyayTitle);
        TextView txtViewCount = (TextView) view.findViewById(R.id.lblViewCount);
        TextView txtUser = (TextView) view.findViewById(R.id.lblUser);
        TextView txtUpdate = (TextView) view.findViewById(R.id.lblUpdate);
        ImageView imgUser = (ImageView) view.findViewById(R.id.imgUser);
        ImageView imgFavorite = (ImageView) view.findViewById(R.id.imgFavorite);
        try {
            txtTitle.setText(item.getString("story_name"));
            txtViewCount.setText("อ่าน "+item.getString("view"));
            txtUser.setText(item.getString("name"));
            txtUpdate.setText(item.getString("last_update"));
            String url = "http://www.niyay.com/member/logo/"+item.getString("logo");
            ImageLoader imageLoader = ImageLoader.getInstance(null);
            imageLoader.DisplayImage(url, imgUser);
            imgFavorite.setVisibility(DataBaseSQLite.getInstance().isFavorite(item.getString("story_id"))?View.VISIBLE:View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public JSONObject getContent() {
        return item;
    }

}
