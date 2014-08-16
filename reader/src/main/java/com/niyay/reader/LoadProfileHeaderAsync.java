package com.niyay.reader;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by APPLE on 2/7/14.
 */
public class LoadProfileHeaderAsync extends AsyncTask<MenuAdapter, Void, JSONArray> {

    MenuAdapter adapter = null;

    @Override
    protected JSONArray doInBackground(MenuAdapter... params) {
        adapter = params[0];
        JSONArray json = null;
        JSONParser jParser = new JSONParser();
        try {
            json = jParser.getJSONFromUrl(adapter.urlheader);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONArray result)
    {
        if (result != null){
            try {
                JSONObject json = result.getJSONObject(0);
                json.put("story_name", adapter.parm.getString("story_name"));
                json.put("name", adapter.parm.getString("name"));
                json.put("view", adapter.parm.getString("view"));
                json.put("story", adapter.parm.getString("story_id"));
                adapter.insert(new ProfileHeader(json), 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.setNotifyOnChange(true);
        }
    }
}
