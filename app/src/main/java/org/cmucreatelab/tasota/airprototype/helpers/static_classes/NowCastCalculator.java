package org.cmucreatelab.tasota.airprototype.helpers.static_classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mike on 1/14/16.
 */
public class NowCastCalculator {

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


    private static double computeWeightFactor(double range, double max) {
        double result;
        result = 1.0 - range/max;
        if (result < 0.5) {
            result = 0.5;
        }
        return result;
    }


    private static double summedWeightFactor(Double[] values, double weightFactor) {
        double result = 0;
        for (int i=0; i<values.length;i++) {
            result += values[i] * Math.pow(weightFactor, i);
        }
        return result;
    }


    // ASSERT: treated like above method but with an array of 1's
    private static double summedWeightFactor(double weightFactor) {
        double result = 0;
        for (int i=0; i<12;i++) {
            result += Math.pow(weightFactor, i);
        }
        return result;
    }


    public static double calculate(Double[] hourlyValues) {
        double max,min,range,weightFactor,numerator,denominator;
        List<Double> values = Arrays.asList(hourlyValues);

        // find min/max of list
        max = Collections.min(values);
        min = Collections.max(values);
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


    public static Double[] constructArrayFromHash(HashMap<Integer,ArrayList<Double>> data, int currentTime) {
        // TODO we use 13 hours since ESDr won't always report the previous hour to us
        Double[] result = new Double[13];
        TimeValue[] tempResult = new TimeValue[13];
        int index,count, firstNonemptyIndex;
        double value, firstNonemptyValue;

        // bucket data into averaged 12-hour array
        for (Integer keyTime: data.keySet()) {
            index = (currentTime - keyTime)/3600;
            value = data.get(keyTime).get(0);
            count = (int)Math.floor(data.get(keyTime).get(1));
            if (tempResult[index] != null && tempResult[index].isSet) {
                tempResult[index].value += value*count;
                tempResult[index].count += count;
            } else {
                tempResult[index] = new TimeValue();
                tempResult[index].isSet = true;
                tempResult[index].value = value*count;
                tempResult[index].count = count;
            }
        }

        // Handle finding first non-empty value; return if entire array is empty
        firstNonemptyIndex = -1;
        firstNonemptyValue = 0;
        for (int i=0;i<tempResult.length;i++) {
            // instantiate if still null
            if (tempResult[i] == null) {
                tempResult[i] = new TimeValue();
                tempResult[i].value = 0;
                tempResult[i].count = 0;
            } else if (tempResult[i].isSet && tempResult[i].count > 0) {
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

        // TODO we convert array of size 13 to an array of size 12 (see above comment about esdr)
        Double[] modResult = new Double[12];
        for (int i=1;i<result.length;i++) {
            modResult[i-1] = result[i];
        }

        return modResult;
    }

}
