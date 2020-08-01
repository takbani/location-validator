package com.location.validator.util;

import com.location.validator.model.City;
import com.location.validator.model.Coordinates;

public class CityBuilder {


    String name;

    Coordinates coordinates;

    String listedUsersUrl;

    public CityBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CityBuilder setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public CityBuilder setCityListedUserUrl(String listedUsersUrl) {
        this.listedUsersUrl = listedUsersUrl;
        return this;
    }

    public City getCity(){
        return new City(this.name,this.coordinates,this.listedUsersUrl);
    }

}
