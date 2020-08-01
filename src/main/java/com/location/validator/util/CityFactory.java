package com.location.validator.util;

import com.location.validator.model.City;
import com.location.validator.model.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import static com.location.validator.util.CommonConstants.*;
import static java.lang.Double.valueOf;
import static java.util.Locale.getDefault;
import static org.apache.commons.lang3.text.WordUtils.capitalize;

@Component
public class CityFactory {

    @Autowired
    MessageSource messageSource;

    public City getCity(String cityName) {
        if (LONDON_CITY.equalsIgnoreCase(cityName)) {
            Coordinates coordinates = new Coordinates(valueOf(messageSource.getMessage(LONDON_CITY_LATITUDE_MSG_KEY, null, getDefault())), valueOf(messageSource.getMessage(LONDON_CITY_LONGITUDE_MSG_KEY, null, getDefault())));
            String londonUserUrl = messageSource.getMessage(CITY_LISTED_USERS_URL_MSG_KEY, new Object[]{capitalize(cityName)}, getDefault());
            return new CityBuilder().setName(LONDON_CITY).setCoordinates(coordinates).setCityListedUserUrl(londonUserUrl).getCity();
        }
        return null;
    }
}
