package com.dev.jvh.mytravellocations;

/**
 * Created by JochemVanHespen on 10/8/2017.
 */

public class TravelLocation {
    private String title;
    private long latitude;
    private long longitude;

    public TravelLocation(String title, long latitude, long longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
