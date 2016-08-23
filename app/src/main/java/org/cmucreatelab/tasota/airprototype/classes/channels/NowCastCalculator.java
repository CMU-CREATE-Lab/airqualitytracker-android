package org.cmucreatelab.tasota.airprototype.classes.channels;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mike on 1/14/16.
 */
public class NowCastCalculator {

    private int hours;
    private WeightType weightType;

    enum WeightType {
        RATIO, PIECEWISE
    }


    public NowCastCalculator(int hours, WeightType weightType) {
        this.hours = hours;
        this.weightType = weightType;
    }


    private static class TimeValue {
        boolean isSet = false;
        int count;
        double value;

        public void set(int count, double value) {
            this.count = count;
            this.value = value;
            isSet = true;
        }
    }


    private double computeWeightFactor(double range, double max) {
        double result;
        result = 1.0 - range/max;
        if (this.weightType == WeightType.PIECEWISE && result < 0.5) {
            result = 0.5;
        }
        return result;
    }


    private double summedWeightFactor(Double[] values, double weightFactor) {
        double result = 0;
        for (int i=0; i<values.length;i++) {
            result += values[i] * Math.pow(weightFactor, i);
        }
        return result;
    }


    // ASSERT: treated like above method but with an array of 1's
    private double summedWeightFactor(double weightFactor) {
        double result = 0;
        for (int i=0; i<12;i++) {
            result += Math.pow(weightFactor, i);
        }
        return result;
    }


    private Double[] constructArrayFromHash(HashMap<Integer,ArrayList<Double>> data, int currentTime) {
        // TODO we use N+1 hours since ESDr won't always report the previous hour to us
        Double[] result = new Double[this.hours+1];
        TimeValue[] tempResult = new TimeValue[this.hours+1];
        for (int i=0;i<tempResult.length;i++) {
            tempResult[i] = new TimeValue();
        }
        int index,count, firstNonemptyIndex;
        double value, firstNonemptyValue;

        // bucket data into averaged hourly array
        for (Integer keyTime: data.keySet()) {
            index = (currentTime - keyTime)/3600;
            value = data.get(keyTime).get(0);
            count = (int)Math.floor(data.get(keyTime).get(1));
            if (tempResult[index].isSet) {
                tempResult[index].value += value*count;
                tempResult[index].count += count;
            } else {
                tempResult[index].isSet = true;
                tempResult[index].value = value*count;
                tempResult[index].count = count;
            }
        }

        // Handle finding first non-empty value; return if entire array is empty
        firstNonemptyIndex = -1;
        firstNonemptyValue = 0;
        for (int i=0;i<tempResult.length;i++) {
            if (tempResult[i].isSet && tempResult[i].count > 0) {
                firstNonemptyIndex = i;
                firstNonemptyValue = tempResult[i].value / (double)(tempResult[i].count);
                break;
            }
        }
        if (firstNonemptyIndex < 0) {
            return new Double[0];
        }

//        // The last value we saw in the array we are constructing
//        double lastValue;
//        // Initially, we want this to be the first non-null value in the array
//        lastValue = firstNonemptyValue;

        // Now, construct our final resulting array (from buckets)
        for (int i=0;i<tempResult.length;i++) {
            if (i <= firstNonemptyIndex) {
                // set all values to be the same as the first nonempty value
                result[i] = firstNonemptyValue;
            } else if (tempResult[i].isSet && tempResult[i].count > 0) {
                // next, populate each hour with values from the data
                double currentValue = tempResult[i].value/(double)(tempResult[i].count);
                result[i] = currentValue;
//                lastValue = currentValue;
            } else {
                // when data missing, assume 0
                // TODO if either the first two hours are missing then NowCast should not be reported
                result[i] = 0.0;
            }
        }

        // TODO we convert array of size N+1 to an array of size N (see above comment about esdr)
        Double[] modResult = new Double[this.hours];
        for (int i=1;i<result.length;i++) {
            modResult[i-1] = result[i];
        }

        return modResult;
    }


    public double calculate(HashMap<Integer,ArrayList<Double>> data, int currentTime) {
        Double[] hourlyValues = constructArrayFromHash(data,currentTime);
        double max,min,range,weightFactor,numerator,denominator;
        List<Double> values = Arrays.asList(hourlyValues);
        if (values.size() == 0) {
            Log.w(Constants.LOG_TAG, "tried NowCastCalculator.calculate with an empty array; returning 0");
            return 0;
        }

        // find min/max of list
        max = Collections.max(values);
        min = Collections.min(values);
        range = max - min;
        // compute the weight factor
        weightFactor = computeWeightFactor(range, max);
        // sum the products of concentrations and weight factors
        numerator = summedWeightFactor(hourlyValues, weightFactor);
        // sum of weight factors raised to the power
        denominator = summedWeightFactor(weightFactor);
        // resulting value for NowCast
        return numerator/denominator;
    }

}
