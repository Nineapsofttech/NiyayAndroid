package com.niyay.reader;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by APPLE on 2/2/14.
 */
public class LoadItemAsync extends AsyncTask<MenuAdapter, Void, JSONArray> {

    MenuAdapter adapter = null;

    @Override
    protected JSONArray doInBackground(MenuAdapter... params) {
        adapter = params[0];

        JSONArray json = null;
        JSONParser jParser = new JSONParser();
        try {
            json = jParser.getJSONFromUrl(adapter.url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONArray result)
    {
        if (result != null){
            adapter.remove(adapter.getItem(adapter.getCount()-1));
            for (int i=0; i<result.length(); i++){
                try {
                    JSONObject c = result.getJSONObject(i);
                    adapter.add(new TextListItem(c));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.setNotifyOnChange(true);
            adapter.UpdateTaskFinished();
        }
    }

}
