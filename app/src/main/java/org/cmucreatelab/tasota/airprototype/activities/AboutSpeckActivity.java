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

public class AboutSpeckActivity extends ActionBarActivity {

    private TextView textAboutSpeckLink1;
    private TextView textAboutSpeckLink2;
    private String
            text1="The Speck Sensor (<a href='http://www.specksensor.com'>www.specksensor.com</a>) is an air quality monitor that detects fine particulate matter in your indoor environment and informs you about trends and changes in particle concentration. At its core, Speck is about empowerment, enabling you to make informed decisions about how to improve your air quality. Data collected with Speck belongs to you, its user, and you have the power to decide how to apply that information and take action to breathe easier!",
            text2="Coming soon! Login to your <a href='http://www.specksensor.com'>www.specksensor.com</a> account.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_speck);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("About SpeckSensor");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // provide HTML-like links
        textAboutSpeckLink1 =(TextView)findViewById(R.id.textAboutSpeckLink1);
        textAboutSpeckLink1.setClickable(true);
        textAboutSpeckLink1.setMovementMethod(LinkMovementMethod.getInstance());
        textAboutSpeckLink1.setText(Html.fromHtml(text1));

        textAboutSpeckLink2 =(TextView)findViewById(R.id.textAboutSpeckLink2);
        textAboutSpeckLink2.setClickable(true);
        textAboutSpeckLink2.setMovementMethod(LinkMovementMethod.getInstance());
        textAboutSpeckLink2.setText(Html.fromHtml(text2));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_speck, menu);
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
