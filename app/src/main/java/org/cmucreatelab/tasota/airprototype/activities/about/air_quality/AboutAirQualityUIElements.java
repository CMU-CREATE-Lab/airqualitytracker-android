package org.cmucreatelab.tasota.airprototype.activities.about.air_quality;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;

/**
 * Created by mike on 3/22/16.
 */
public class AboutAirQualityUIElements {

    private AboutAirQualityActivity activity;
    private TextView textAboutAirqualityLink1;
    private TextView textAboutAirqualityLink2;
    private String
            text1="Help keep our air pollution laws strong by joining the @SpeckSensor conversation on Facebook and Twitter! You can also purchase your own indoor air quality monitor at <a href='http://www.specksensor.com'>http://www.specksensor.com</a>.",
            text2="Each category corresponds to a different level of health concern. The six levels of health concern and what they mean are taken from AirNow's website: <a href='http://airnow.gov/index.cfm?action=aqibasics.aqi'>http://airnow.gov</a>";


    public AboutAirQualityUIElements(AboutAirQualityActivity activity) {
        this.activity = activity;
    }


    public void populate() {
        // provide HTML-like links
        textAboutAirqualityLink1 =(TextView)activity.findViewById(R.id.textAboutAirqualityLink1);
        textAboutAirqualityLink1.setClickable(true);
        textAboutAirqualityLink1.setMovementMethod(LinkMovementMethod.getInstance());
        textAboutAirqualityLink1.setText(Html.fromHtml(text1));

        textAboutAirqualityLink2 =(TextView)activity.findViewById(R.id.textAboutAirqualityLink2);
        textAboutAirqualityLink2.setClickable(true);
        textAboutAirqualityLink2.setMovementMethod(LinkMovementMethod.getInstance());
        textAboutAirqualityLink2.setText(Html.fromHtml(text2));
    }

}
