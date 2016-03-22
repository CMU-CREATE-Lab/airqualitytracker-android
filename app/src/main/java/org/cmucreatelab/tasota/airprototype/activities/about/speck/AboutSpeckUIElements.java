package org.cmucreatelab.tasota.airprototype.activities.about.speck;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;

/**
 * Created by mike on 3/22/16.
 */
public class AboutSpeckUIElements {

    private AboutSpeckActivity activity;
    private TextView textAboutSpeckLink1;
    private String
            text1="The Speck Sensor (<a href='http://www.specksensor.com'>www.specksensor.com</a>) is an air quality monitor that detects fine particulate matter in your indoor environment and informs you about trends and changes in particle concentration. At its core, Speck is about empowerment, enabling you to make informed decisions about how to improve your air quality. Data collected with Speck belongs to you, its user, and you have the power to decide how to apply that information and take action to breathe easier!";


    public AboutSpeckUIElements(AboutSpeckActivity activity) {
        this.activity = activity;
    }


    public void populate() {
        // provide HTML-like links
        textAboutSpeckLink1 =(TextView)activity.findViewById(R.id.textAboutSpeckLink1);
        textAboutSpeckLink1.setClickable(true);
        textAboutSpeckLink1.setMovementMethod(LinkMovementMethod.getInstance());
        textAboutSpeckLink1.setText(Html.fromHtml(text1));
    }

}
