package com.niyay.reader;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by APPLE on 2/6/14.
 */
public class ProfileHeader implements Item {

    JSONObject profile;
    Boolean favorite;

    public ProfileHeader(JSONObject data){
        profile = data;
    }

    @Override
    public int getViewType() {
        return MenuAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.profile_user, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        ImageView imgUser = (ImageView) view.findViewById(R.id.imgDetailUser);
        TextView txtAuthor = (TextView) view.findViewById(R.id.lblAuthor);
        TextView txtEmail = (TextView) view.findViewById(R.id.lblEmail);
        TextView txtStory = (TextView) view.findViewById(R.id.lblTitle);
        TextView txtLevel = (TextView) view.findViewById(R.id.lblLevel);
        TextView txtPoint = (TextView) view.findViewById(R.id.lblPoint);
        TextView txtView = (TextView) view.findViewById(R.id.lblView);
        final Button btnFavorite = (Button) view.findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String story_id = null;
                try {
                    story_id = profile.getString("story");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (favorite){
                    favorite = false;
                    DataBaseSQLite.getInstance().remove_favorite(story_id);
                    btnFavorite.setText("เก็บไว้อ่าน");
                }
                else{
                    favorite = true;
                    DataBaseSQLite.getInstance().add_favorite(story_id);
                    btnFavorite.setText("เลิกเก็บไว้อ่าน");
                }
            }
        });
        txtEmail.setTextColor(Color.LTGRAY);

        try {
            txtAuthor.setText(profile.getString("name"));
            txtEmail.setText(profile.getString("email"));
            txtLevel.setText(profile.getString("level"));
            txtPoint.setText(profile.getString("point"));
            txtStory.setText(profile.getString("story_name"));
            txtView.setText("อ่าน "+profile.getString("view"));
            String url = "http://www.niyay.com/member/logo/"+profile.getString("logo");
            if (DataBaseSQLite.getInstance().isFavorite(profile.getString("story"))){
                favorite = true;
                btnFavorite.setText("เลิกเก็บไว้อ่าน");
            }
            else{
                favorite = false;
                btnFavorite.setText("เก็บไว้อ่าน");
            }
            //ImageLoader loader = new ImageLoader();
            //loader.bind(imgUser, url, null);
            ImageLoader imageLoader = ImageLoader.getInstance(null);
            imageLoader.DisplayImage(url, imgUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public JSONObject getContent() {
        return null;
    }
}
