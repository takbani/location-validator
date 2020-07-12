package com.location.validator;

import com.location.validator.model.User;
import com.location.validator.service.ApiConnectorService;
import com.location.validator.service.LocationValidatorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.*;

import static com.location.validator.util.CommonConstants.cityNameToCityCoordinatesMap;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTests {

    @Spy
    LocationValidatorService testObj;

    double londonLatitude;

    double londonLongitude;

    @Mock
    ApiConnectorService apiConnectorServiceMock;

    @Before
    public void init() {
        testObj = new LocationValidatorService();
        londonLatitude = cityNameToCityCoordinatesMap.get("london").getLatitude();
        londonLongitude = cityNameToCityCoordinatesMap.get("london").getLongitude();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenAUserWhoseAddressIsNotWithinFiftyMilesOfLondonThenReturnUserListWithSizeZero() {

        Map userMap= new LinkedHashMap<>();
        userMap.put("id",135);
        userMap.put("firstName","Mechelle");
        userMap.put("lastName","Boam");
        userMap.put("email","mboam3q@thetimes.co.uk");
        userMap.put("ipAddress","113.71.242.187");
        userMap.put("latitude",-6.5115909);
        userMap.put("longitude",105.652983);
        List users = new ArrayList(asList(userMap));
        when(testObj.getApiConnectorService()).thenReturn(apiConnectorServiceMock);
        doReturn(users).when(apiConnectorServiceMock).getResponseFromApiForGetRequest(Mockito.any(), Mockito.any());
        List<User> usersFound = testObj.getUsersBasedInAndAroundACity("london");
        assertTrue(usersFound.size() == 0);
    }

    @Test
    public void givenAUserWhoseAddressIsInLondonThenReturnUserListWithSizeOne() {

        Map userMap= new LinkedHashMap<>();
        userMap.put("id",135);
        userMap.put("firstName","Mechelle");
        userMap.put("lastName","Boam");
        userMap.put("email","mboam3q@thetimes.co.uk");
        userMap.put("ipAddress","113.71.242.187");
        userMap.put("latitude",51.6710832);
        userMap.put("longitude",0.8078532);
        List users = new ArrayList(asList(userMap));
        when(testObj.getApiConnectorService()).thenReturn(apiConnectorServiceMock);
        doReturn(users).when(apiConnectorServiceMock).getResponseFromApiForGetRequest(Mockito.any(), Mockito.any());
        List<User> usersFound = testObj.getUsersBasedInAndAroundACity("london");
        assertTrue(usersFound.size() == 1);
    }

}
