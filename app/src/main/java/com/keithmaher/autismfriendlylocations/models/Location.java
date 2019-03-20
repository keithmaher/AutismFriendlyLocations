package com.keithmaher.autismfriendlylocations.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Location {

    public String locationId;
    public String locationName;
    public double locationLong;
    public double locationLat;
    public String locationAddress;
    public String locationIcon;
    public List<Comment> locationComments;


    public Location() {
    }

    public Location(String locationId, String locationName, double locationLong, double locationLat, String locationAddress, String locationIcon,  List<Comment> locationComments) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.locationAddress = locationAddress;
        this.locationIcon = locationIcon;
        this.locationComments = locationComments;
    }

    public Location(String locationId, String locationName, double locationLong, double locationLat, String locationAddress, String locationIcon) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.locationAddress = locationAddress;
        this.locationIcon = locationIcon;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public List<Comment> getLocationComments() {
        return locationComments;
    }

    public void setLocationComments(List<Comment> locationComments) {
        this.locationComments = locationComments;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId='" + locationId + '\'' +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
