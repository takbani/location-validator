package com.location.validator.model;


public class City {

    String name;

    Coordinates coordinates;

    String listedUsersUrl;

    public City(String name, Coordinates coordinates,String listedUsersUrl) {
        this.name = name;
        this.coordinates = coordinates;
        this.listedUsersUrl=listedUsersUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getListedUsersUrl() {
        return listedUsersUrl;
    }

    public void setListedUsersUrl(String listedUsersUrl) {
        this.listedUsersUrl = listedUsersUrl;
    }
}
