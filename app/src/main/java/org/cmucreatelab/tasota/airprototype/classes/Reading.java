package org.cmucreatelab.tasota.airprototype.classes;

/**
 * Created by mike on 8/14/15.
 */
public class Reading {

    public enum Type {
        SPECK, ADDRESS
    }
    private Device device;
    private SimpleAddress simpleAddress;
    private Type readingType;


    public Reading(Device device) {
        this.device = device;
        this.readingType = Type.SPECK;
    }


    public Reading(SimpleAddress simpleAddress) {
        this.simpleAddress = simpleAddress;
        this.readingType = Type.ADDRESS;
    }

}
