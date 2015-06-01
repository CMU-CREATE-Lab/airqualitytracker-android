package org.cmucreatelab.tasota.airprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mike on 5/29/15.
 */
public class TemporaryFeedPopulator {

    public static void addFeedsToDatabase(JSONObject json, Context ctx) {
        try {
            JSONArray feeds = json.getJSONObject("data").getJSONArray("rows");
            int size = feeds.length();
            for (int i=0;i<size;i++) {
                JSONObject feed = (JSONObject)feeds.get(i);
                Log.i("addFeedsToDatabase", "FOUND JSON OBJECT id="+feed.get("id").toString() + ", name=" + feed.get("name").toString());

                FeedDbHelper mDbHelper = new FeedDbHelper(ctx);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put( FeedContract.COLUMN_FEED_ID, feed.get("id").toString() );
                values.put( FeedContract.COLUMN_NAME, feed.get("name").toString() );
                long newId;
                newId = db.insert(FeedContract.TABLE_NAME, "null", values);
                Log.i("addFeedsToDatabase", "INSERTED RECORD INTO DATABASE id=" + newId);
            }
        } catch (Exception e) {
            // TODO catch exception "failed to find JSON attr"
            e.printStackTrace();
        }
    }

    static final String[] PROJECTION = new String[]{"_id",FeedContract.COLUMN_NAME,FeedContract.COLUMN_FEED_ID};
    public static void addFeedsToList(Context ctx, SimpleCursorAdapter cursorAdapter) {
        FeedDbHelper mDbHelper = new FeedDbHelper(ctx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.query(FeedContract.TABLE_NAME, PROJECTION, null, null, null, null, null);
        cursorAdapter.changeCursor(c);
        cursorAdapter.notifyDataSetChanged();
    }


    public static void sendFeedsRequest(final Context ctx, final SimpleCursorAdapter cursorAdapter) {

        int requestMethod = Request.Method.GET;
        String requestUrl = "https://esdr.cmucreatelab.org/api/v1/feeds";
        JSONObject requestParams = null;
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO handle results
                addFeedsToDatabase(response, ctx);
                addFeedsToList(ctx, cursorAdapter);
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle errors
            }
        };

        HttpRequestHandler curl = HttpRequestHandler.getInstance(ctx);
        curl.sendJsonRequest(requestMethod,requestUrl,requestParams,response,error);
    }
}
