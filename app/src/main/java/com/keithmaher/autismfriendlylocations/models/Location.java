package com.keithmaher.autismfriendlylocations.models;

import java.util.ArrayList;
import java.util.UUID;

public class Location {

    public String locationId;
    public String locationName;
    public double locationLong;
    public double locationLat;
    public String locationAddress;
    public int locationLikes;
    public ArrayList<Comment> locationComments;


    public Location() {
    }

    public Location(String locationId, String locationName, double locationLong, double locationLat, String locationAddress, int locationLikes) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.locationAddress = locationAddress;
        this.locationLikes = locationLikes;
    }

    public Location(String locationId, String locationName, double locationLong, double locationLat, String locationAddress, int locationLikes, ArrayList<Comment> locationComments) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.locationAddress = locationAddress;
        this.locationLikes = locationLikes;
        this.locationComments = locationComments;
    }

    public Location(String locationName, double locationLong, double locationLat, String locationAddress, int locationLikes, ArrayList<Comment> locationComments) {
        this.locationId = UUID.randomUUID().toString();
        this.locationName = locationName;
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.locationAddress = locationAddress;
        this.locationLikes = locationLikes;
        this.locationComments = locationComments;
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

    public int getLocationLikes() {
        return locationLikes;
    }

    public void setLocationLikes(int locationLikes) {
        this.locationLikes = locationLikes;
    }

    public ArrayList<Comment> getLocationComments() {
        return locationComments;
    }

    public void setLocationComments(ArrayList<Comment> locationComments) {
        this.locationComments = locationComments;
    }
}
