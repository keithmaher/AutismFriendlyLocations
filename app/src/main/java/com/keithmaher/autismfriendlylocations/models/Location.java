package com.keithmaher.autismfriendlylocations.models;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Location {

    public String locationId;
    public String locationName;
    public double locationLong;
    public double locationLat;
    public float locationRating;
    public String locationComments;
    public boolean locationFavourites;


    public Location() {
    }

    public Location(String locationId, String locationName, double locationLong, double locationLat, float locationRating, String locationComments, boolean locationFavourites) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.locationRating = locationRating;
        this.locationComments = locationComments;
        this.locationFavourites = locationFavourites;
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

    public float getLocationRating() {
        return locationRating;
    }

    public void setLocationRating(float locationRating) {
        this.locationRating = locationRating;
    }

    public String getLocationComments() {
        return locationComments;
    }

    public void setLocationComments(String locationComments) {
        this.locationComments = locationComments;
    }

    public boolean isLocationFavourites() {
        return locationFavourites;
    }

    public void setLocationFavourites(boolean locationFavourites) {
        this.locationFavourites = locationFavourites;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", locationName='" + locationName + '\'' +
                ", locationLong=" + locationLong +
                ", locationLat=" + locationLat +
                ", locationRating=" + locationRating +
                ", locationComments='" + locationComments + '\'' +
                ", locationFavourites=" + locationFavourites +
                '}';
    }
}
