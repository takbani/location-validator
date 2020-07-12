package com.location.validator.service;

import com.location.validator.model.Coordinates;
import com.location.validator.model.User;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.location.validator.util.CommonConstants.LONDON_CITY;
import static com.location.validator.util.CommonConstants.cityNameToCityCoordinatesMap;
import static java.lang.Double.valueOf;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class LocationValidatorService {

    @Autowired
    ApiConnectorService apiConnectorService;

    @Value("${all.users.url}")
    String userUrl;

    @Value("${london.users.url}")
    String londonUserUrl;

   //merges users that are listed as living in a city with the users that are based within 50 miles of the city
    public List<User> getListedUsersAndUsersBasedAroundACity(String city) {
        return concat(getUsersListedAsLivingInACity(city).stream(), getUsersBasedInAndAroundACity(city).stream()).distinct().collect(Collectors.toList());
    }

    public List<User> getUsersListedAsLivingInACity(String city) {
        if (isNotBlank(city)) {
            if (LONDON_CITY.equalsIgnoreCase(city)) {
                List users = apiConnectorService.getResponseFromApiForGetRequest(londonUserUrl, List.class);
                return users;
            }
        }
        return new ArrayList<>();
    }


    public List<User> getUsersBasedInAndAroundACity(String city) {
        if (isNotBlank(city)) {
            Coordinates coordinates = cityNameToCityCoordinatesMap.get(city);
            if (Objects.nonNull(coordinates)) {
                List users = getApiConnectorService().getResponseFromApiForGetRequest(userUrl, List.class);
                List<User> usersBasedAroundLondon = (List<User>) users.stream().filter(LinkedHashMap.class::isInstance)
                        .filter(user -> {
                            double latitude = valueOf(((LinkedHashMap) user).get("latitude").toString());
                            double longitude = valueOf(((LinkedHashMap) user).get("longitude").toString());
                            double distanceInMiles = calculateDistanceBetweenCoordinates(coordinates.getLatitude(), coordinates.getLongitude(), latitude, longitude);
                            return distanceInMiles <= 50;
                        }).collect(Collectors.toCollection(ArrayList::new));
                return usersBasedAroundLondon;
            }
        }
        return new ArrayList<>();
    }

    public double calculateDistanceBetweenCoordinates(double sourceLatitude, double sourceLongitude, double destLatitude, double destLongitude) {
        GeodesicData result = Geodesic.WGS84.Inverse(sourceLatitude, sourceLongitude, destLatitude, destLongitude);
        return result.s12 / 1609.34;
    }

    public ApiConnectorService getApiConnectorService() {
        return apiConnectorService;
    }

}
