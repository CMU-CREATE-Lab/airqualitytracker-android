package org.cmucreatelab.tasota.airprototype.helpers;

// Sources for conversions are taken from AirNow documents
// as well as references from Wikipedia:
//
// https://en.wikipedia.org/wiki/Air_quality_index#United_States

import android.util.Log;

/**
 * Created by mike on 6/23/15.
 */
public class Converter {


    private static double calculateLinearAqi(double ihi, double ilo, double chi, double clo, double micrograms) {
        return (ihi-ilo) / (chi-clo) * (micrograms-clo) + ilo;
    }

    private static double calculateLinearMicrograms(double ihi, double ilo, double chi, double clo, double aqi) {
        return (aqi-ilo) * (chi-clo) / (ihi-ilo) + clo;
    }


    public static double microgramsToAqi(double micrograms) {
        double aqi = 0.0;
        // round to tenths
        micrograms = ((int)(micrograms*10))/10.0;
        if (micrograms < 0) {
            Log.e(Constants.LOG_TAG, "tried to convert negative Micrograms.");
            aqi = 0.0;
        } else if (micrograms < 12.0) {
            aqi = calculateLinearAqi(50.0,0.0,12.0,0.0,micrograms);
        } else if (micrograms < 35.4) {
            aqi = calculateLinearAqi(100.0,50.0,35.4,12.1,micrograms);
        } else if (micrograms < 55.4) {
            aqi = calculateLinearAqi(150.0,101.0,55.4,35.5,micrograms);
        } else if (micrograms < 150.4) {
            aqi = calculateLinearAqi(200.0,151.0,150.4,55.5,micrograms);
        } else if (micrograms < 250.4) {
            aqi = calculateLinearAqi(300.0,201.0,250.4,150.5,micrograms);
        } else if (micrograms < 350.4) {
            aqi = calculateLinearAqi(400.0,301.0,350.4,250.5,micrograms);
        } else if (micrograms < 500.4) {
            aqi = calculateLinearAqi(500.0,401.0,500.4,350.5,micrograms);
        } else {
            Log.e(Constants.LOG_TAG, "Micrograms out of range.");
            aqi = 0.0;
        }
        return aqi;
    }


    public static double aqiToMicrograms(double aqi) {
        double micrograms = 0.0;
        // round to tenths
        aqi = ((int)(aqi*10))/10.0;
        if (aqi < 0) {
            Log.e(Constants.LOG_TAG, "tried to convert negative AQI.");
            micrograms = 0.0;
        } else if (aqi < 50.0) {
            micrograms = calculateLinearMicrograms(50.0,0.0,12.0,0.0,aqi);
        } else if (aqi < 100.0) {
            micrograms = calculateLinearMicrograms(100.0,50.0,35.4,12.1,aqi);
        } else if (aqi < 150.0) {
            micrograms = calculateLinearMicrograms(150.0,101.0,55.4,35.5,aqi);
        } else if (aqi < 200.0) {
            micrograms = calculateLinearMicrograms(200.0,151.0,150.4,55.5,aqi);
        } else if (aqi < 300.0) {
            micrograms = calculateLinearMicrograms(300.0,201.0,250.4,150.5,aqi);
        } else if (aqi < 400.0) {
            micrograms = calculateLinearMicrograms(400.0,301.0,350.4,250.5,aqi);
        } else if (aqi < 500.0) {
            micrograms = calculateLinearMicrograms(500.0,401.0,500.4,350.5,aqi);
        } else {
            Log.e(Constants.LOG_TAG, "AQI out of range.");
            micrograms = 0.0;
        }
        return micrograms;
    }

}
