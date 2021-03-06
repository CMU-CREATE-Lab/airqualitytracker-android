package org.cmucreatelab.tasota.airprototype.helpers.static_classes;

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
import org.cmucreatelab.tasota.airprototype.classes.readables.AirQualityFeed;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
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
    public static double getDistance(Location from, Location to) {
        double result;
        double p1,p2,l1,l2;

        // convert units from degrees to radians
        p1 = from.latitude * Math.PI / 180.0;
        p2 = to.latitude * Math.PI / 180.0;
        l1 = from.longitude * Math.PI / 180.0;
        l2 = to.longitude * Math.PI / 180.0;
        // Taken from the haversine formula: hsin(d/r) = hsine(p2-p1) + cos(p1)*cos(p2)*hsin(l2-l1)
        // where hsin(t) = sin^2(t/2)
        result = haversine(p2-p1) + Math.cos(p2)*Math.cos(p1)*haversine(l2-l1);
        result = Constants.MapGeometry.RADIUS_EARTH * archaversine(result);
        return result;
    }


    public static double getDistanceFromFeedToAddress(SimpleAddress simpleAddress, Feed feed) {
        return getDistance(simpleAddress.getLocation(),
                feed.getLocation());
    }


    public static AirQualityFeed getClosestFeedWithPm25ToAddress(SimpleAddress simpleAddress, ArrayList<AirQualityFeed> feeds) {
        AirQualityFeed closestFeed = null;
        double distance = 0.0;

        for (AirQualityFeed feed : feeds) {
            if (feed.getPm25Channels().size() == 0) {
                continue;
            }
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
            Log.w(Constants.LOG_TAG, "getClosestFeedWithPm25ToAddress returning null.");
        }
        else {
            Log.v(Constants.LOG_TAG, "FEED=" + closestFeed.getFeed_id() + " has closest distance=" + distance);
        }
        return closestFeed;
    }


    public static AirQualityFeed getClosestFeedWithOzoneToAddress(SimpleAddress simpleAddress, ArrayList<AirQualityFeed> feeds) {
        AirQualityFeed closestFeed = null;
        double distance = 0.0;

        for (AirQualityFeed feed : feeds) {
            if (feed.getOzoneChannels().size() == 0) {
                continue;
            }
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
            Log.w(Constants.LOG_TAG, "getClosestFeedWithPm25ToAddress returning null.");
        }
        else {
            Log.v(Constants.LOG_TAG, "FEED=" + closestFeed.getFeed_id() + " has closest distance=" + distance);
        }
        return closestFeed;
    }

}
