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

import static com.location.validator.util.CommonConstants.cityNameToCityCoordinatesMap;
import static java.lang.Double.valueOf;
import static java.util.stream.Stream.concat;

@Service
public class LocationValidatorService {

    @Autowired
    ApiConnectorService apiConnectorService;

    @Value("${all.users.url}")
    String userUrl;

    @Value("${london.users.url}")
    String londonUserUrl;


    public List<User> getEligibleUsers(String city){
     return concat( getLondonListedUsers(city).stream(),getUsersBasedInAndAroundLondon(city).stream()).distinct().collect(Collectors.toList());

    }

    public List<User> getLondonListedUsers(String city) {
        if("london".equalsIgnoreCase(city)) {
            List users = apiConnectorService.getResponseFromApiForGetRequest(londonUserUrl, List.class);
            return users;
        }
        else{
            return new ArrayList<>();
        }

    }


    public List<User> getUsersBasedInAndAroundLondon(String city) {
        Coordinates coordinates = cityNameToCityCoordinatesMap.get(city);
        if(Objects.nonNull(coordinates)) {
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
        return new ArrayList<>();
    }

    public double calculateDistanceBetweenCoordinates(double sourceLatitude,double sourceLongitude,double destLatitude, double destLongitude) {
        GeodesicData result = Geodesic.WGS84.Inverse(sourceLatitude, sourceLongitude, destLatitude, destLongitude);
        return result.s12 / 1609.34;
    }

    public ApiConnectorService getApiConnectorService() {
        return apiConnectorService;
    }

}
