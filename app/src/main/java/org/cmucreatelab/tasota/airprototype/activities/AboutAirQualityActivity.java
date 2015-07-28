package org.cmucreatelab.tasota.airprototype.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;

public class AboutAirQualityActivity extends ActionBarActivity {

    private TextView textAboutAirqualityLink1;
    private TextView textAboutAirqualityLink2;
    private String
            text1="Help keep our air pollution laws strong by joining the @SpeckSensor conversation on Facebook and Twitter! You can also purchase your own indoor air quality monitor at <a href='http://www.specksensor.com'>http://www.specksensor.com</a>.",
            text2="Each category corresponds to a different level of health concern. The six levels of health concern and what they mean are taken from AirNow's website: <a href='http://airnow.gov/index.cfm?action=aqibasics.aqi'>http://airnow.gov</a>";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_airquality);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("About the Data");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // provide HTML-like links
        textAboutAirqualityLink1 =(TextView)findViewById(R.id.textAboutAirqualityLink1);
        textAboutAirqualityLink1.setClickable(true);
        textAboutAirqualityLink1.setMovementMethod(LinkMovementMethod.getInstance());
        textAboutAirqualityLink1.setText(Html.fromHtml(text1));

        textAboutAirqualityLink2 =(TextView)findViewById(R.id.textAboutAirqualityLink2);
        textAboutAirqualityLink2.setClickable(true);
        textAboutAirqualityLink2.setMovementMethod(LinkMovementMethod.getInstance());
        textAboutAirqualityLink2.setText(Html.fromHtml(text2));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_airquality, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
