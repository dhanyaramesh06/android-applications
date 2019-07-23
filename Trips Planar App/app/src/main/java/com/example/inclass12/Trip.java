package com.example.inclass12;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {
    String trip_name,destination_city, date, key;
    ArrayList<Place> selectedPlaces = new ArrayList<>();

    public Trip() {
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public void setDestination_city(String destination_city) {
        this.destination_city = destination_city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Place> getSelectedPlaces() {
        return selectedPlaces;
    }

    public void setSelectedPlaces(ArrayList<Place> selectedPlaces) {
        this.selectedPlaces = selectedPlaces;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "trip_name='" + trip_name + '\'' +
                ", destination_city='" + destination_city + '\'' +
                ", date='" + date + '\'' +
                ", key='" + key + '\'' +
                ", selectedPlaces=" + selectedPlaces +
                '}';
    }
}
