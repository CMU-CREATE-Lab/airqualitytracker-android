package org.cmucreatelab.tasota.airprototype.helpers;

// AN IMPORTANT NOTE ABOUT CALCULATING DISTANCES ON EARTH
//
// No method is perfect. However, Haversine's Formula is
// better for smaller distances and is therefore the best
// option for computing shorter distances on the Great
// Circle, which is what we are doing in this application
// (as long as your bounding box for points doesn't span
// across half of the Great Circle, AKA Earth). From
// Wikipedia:
//
//     "Although this formula is accurate for most
//     distances on a sphere, it too suffers from
//     rounding errors for the special (and somewhat
//     unusual) case of antipodal points (on opposite
//     ends of the sphere)."
//
// About the Earth's radius constant:
//
//     "the 'Earth radius' R varies from 6356.752 km
//     at the poles to 6378.137 km at the equator.
//     More importantly, the radius of curvature of
//     a north-south line on the earth's surface is
//     1% greater at the poles (≈6399.594 km) than
//     at the equator (≈6335.439 km)— so the haversine
//     formula and law of cosines can't be guaranteed
//     correct to better than 0.5%. More accurate
//     methods that consider the Earth's ellipticity
//     are given by Vincenty's formulae and the other
//     formulas in the geographical distance article."

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import java.util.ArrayList;

/**
 * Created by mike on 6/1/15.
 */
public class MapGeometry {


    // implementation of the haversine function using sine
    private static double haversine(double theta) {
        double temp = Math.sin(theta / 2.0);
        return temp * temp;
    }


    // the inverse of the haversine function
    // ASSERT: value is in range [0,1]
    private static double archaversine(double value) {
        return 2 * Math.asin( Math.sqrt(value) );
    }


    // calculates distance between two points on a Great Sphere
    public static double getDistance(double lat1, double long1, double lat2, double long2) {
        double result;
        double p1,p2,l1,l2;

        // convert units from degrees to radians
        p1 = lat1 * Math.PI / 180.0;
        p2 = lat2 * Math.PI / 180.0;
        l1 = long1 * Math.PI / 180.0;
        l2 = long2 * Math.PI / 180.0;
        // Taken from the haversine formula: hsin(d/r) = hsine(p2-p1) + cos(p1)*cos(p2)*hsin(l2-l1)
        // where hsin(t) = sin^2(t/2)
        result = haversine(p2-p1) + Math.cos(p2)*Math.cos(p1)*haversine(l2-l1);
        result = Constants.MapGeometry.RADIUS_EARTH * archaversine(result);
        return result;
    }


    public static double getDistanceFromFeedToAddress(SimpleAddress simpleAddress, Feed feed) {
        return getDistance(simpleAddress.getLatitude(),simpleAddress.getLongitude(),
                feed.getLatitude(),feed.getLongitude());
    }


    public static Feed getClosestFeedToAddress(SimpleAddress simpleAddress, ArrayList<Feed> feeds) {
        Feed closestFeed = null;
        double distance = 0.0;

        for (Feed feed : feeds) {
            if (closestFeed == null) {
                distance = getDistanceFromFeedToAddress(simpleAddress,feed);
                closestFeed = feed;
            } else {
                double temp = getDistanceFromFeedToAddress(simpleAddress,feed);
                if (temp < distance) {
                    distance = temp;
                    closestFeed = feed;
                }
                if (temp < 0) {
                    Log.w(Constants.LOG_TAG, "Distance from address=" + simpleAddress.get_id() +
                            " to feed=" + feed.getFeed_id() +
                            " has negative distance=" + temp);
                }
            }
        }
        if (closestFeed == null) {
            Log.w(Constants.LOG_TAG, "getClosestFeedToAddress returning null.");
        }
        else {
            Log.d(Constants.LOG_TAG, "FEED=" + closestFeed.getFeed_id() + " has closest distance=" + distance);
        }
        return closestFeed;
    }

}
