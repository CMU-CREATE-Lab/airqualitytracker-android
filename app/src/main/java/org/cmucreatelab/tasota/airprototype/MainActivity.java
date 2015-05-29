package org.cmucreatelab.tasota.airprototype;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<String> dataset;
    SimpleCursorAdapter cursorAdapter;
    Cursor cursor;

    // rows to receive (Cursor must include column _id or SimpleCursorAdapter it will not work)
    static final String[] PROJECTION = new String[]{"_id",FeedContract.COLUMN_NAME,FeedContract.COLUMN_FEED_ID};
    // selection criteria (empty for now)
    static final String SELECTION = "(())";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // simple cursor adapter handles layout stuff for us, instead of writing our own
        String[] from = {FeedContract.COLUMN_NAME,FeedContract.COLUMN_FEED_ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        cursor = getFeeds();
        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item, cursor,
                from, to);

        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(cursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Cursor getFeeds() {
        FeedDbHelper mDbHelper = new FeedDbHelper(this.getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.query(FeedContract.TABLE_NAME, PROJECTION, null, null, null, null, null);
        return c;
    }
    public void clickAddToDatabase(View view) {
        // add random feed to the DB
        FeedDbHelper mDbHelper = new FeedDbHelper(this.getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedContract.COLUMN_FEED_ID, "1");
        values.put(FeedContract.COLUMN_NAME, String.valueOf(Math.random()));
        long newId;
        newId = db.insert(FeedContract.TABLE_NAME, "null", values);
        Log.i("clickAddToDatabase", "INSERTED RECORD INTO DATABASE id=" + newId);
    }
    public void clickRefreshList(View view) {
        cursor = getFeeds();
        cursorAdapter.changeCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
    }
    public void clickHttpRequest(View view) {
        TemporaryFeedPopulator.sendFeedsRequest(this.getApplicationContext(),cursor,cursorAdapter);
    }
}
