package com.location.validator.service;

import com.location.validator.exception.DataNotFoundException;
import com.location.validator.model.City;
import com.location.validator.model.Coordinates;
import com.location.validator.util.CityFactory;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.Double.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class LocationValidatorService {

    @Autowired
    ApiConnectorService apiConnectorService;

    @Value("${all.users.url}")
    String userUrl;

    @Autowired
    CityFactory cityFactory;


    //merges users that are listed as living in a city with the users that are based within 50 miles of the city
    public List getListedUsersAndUsersBasedAroundACity(String city) {
        return singletonList(concat(getUsersListedAsLivingInACity(city).stream(), getUsersBasedInAndAroundACity(city).stream()).distinct().collect(toList()));
    }

    public List getUsersListedAsLivingInACity(String cityName) {
        if (isNotBlank(cityName)) {
            City city = cityFactory.getCity(cityName);
            if (nonNull(city)) {
                List users = apiConnectorService.getResponseFromApiForGetRequest(city.getListedUsersUrl(), List.class);
                return users;
            } else {
                throw new DataNotFoundException(cityName.toUpperCase() + " is not configured to return results");
            }
        }
        return emptyList();
    }


    public List getUsersBasedInAndAroundACity(String cityName) {
        if (isNotBlank(cityName)) {
            City city = cityFactory.getCity(cityName);
            if (nonNull(city)) {
                Coordinates coordinates = city.getCoordinates();
                List users = getApiConnectorService().getResponseFromApiForGetRequest(userUrl, List.class);
                List usersBasedAroundCity = (List) users.parallelStream().filter(LinkedHashMap.class::isInstance)
                        .filter(user -> {
                            double latitude = valueOf(((LinkedHashMap) user).get("latitude").toString());
                            double longitude = valueOf(((LinkedHashMap) user).get("longitude").toString());
                            double distanceInMiles = calculateDistanceBetweenCoordinates(coordinates.getLatitude(), coordinates.getLongitude(), latitude, longitude);
                            return distanceInMiles <= 50;
                        }).collect(toList());
                return usersBasedAroundCity;
            } else {
                throw new DataNotFoundException(cityName.toUpperCase() + " is not configured to return results");
            }
        }
        return emptyList();
    }

    public double calculateDistanceBetweenCoordinates(double sourceLatitude, double sourceLongitude, double destLatitude, double destLongitude) {
        GeodesicData result = Geodesic.WGS84.Inverse(sourceLatitude, sourceLongitude, destLatitude, destLongitude);
        return result.s12 / 1609.34;
    }

    public ApiConnectorService getApiConnectorService() {
        return apiConnectorService;
    }

}
