package com.location.validator;

import com.location.validator.exception.DataNotFoundException;
import com.location.validator.model.City;
import com.location.validator.model.Coordinates;
import com.location.validator.service.ApiConnectorService;
import com.location.validator.service.LocationValidatorService;
import com.location.validator.util.CityFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTests {

    @Spy
    @InjectMocks
    LocationValidatorService testObj;

    @Mock
    ApiConnectorService apiConnectorServiceMock;

    double londonLatitude;

    double londonLongitude;

    @Spy
    CityFactory cityFactory;

    City londonCity;

    @Before
    public void init() {
        londonCity= new City("london",new Coordinates(51.50722,-0.1275),"");
        londonLatitude = londonCity.getCoordinates().getLatitude();
        londonLongitude = londonCity.getCoordinates().getLongitude();
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = DataNotFoundException.class)
    public void givenACityWhoseCoordinatesAreNotConfiguredThenThrowDataNotFoundException() {
        testObj.getUsersBasedInAndAroundACity("paris");
    }

    @Test
    public void givenNoCityProvidedThenReturnEmptyList(){
        List usersListedAsLivingInACity = testObj.getUsersListedAsLivingInACity("");
        Assert.assertTrue(Collections.emptyList().equals(usersListedAsLivingInACity));
    }

    @Test
    public void givenAUserWhoseAddressIsNotWithinFiftyMilesOfLondonThenReturnUserListWithSizeZero() {

        Map userMap = new LinkedHashMap<>();
        userMap.put("id", 135);
        userMap.put("firstName", "Mechelle");
        userMap.put("lastName", "Boam");
        userMap.put("email", "mboam3q@thetimes.co.uk");
        userMap.put("ipAddress", "113.71.242.187");
        userMap.put("latitude", -6.5115909);
        userMap.put("longitude", 105.652983);
        List users = new ArrayList(asList(userMap));
        doReturn(users).when(apiConnectorServiceMock).getResponseFromApiForGetRequest(any(), any());
        doReturn(londonCity).when(cityFactory).getCity("london");
        List usersFound = testObj.getUsersBasedInAndAroundACity("london");
        assertTrue(usersFound.size() == 0);
    }

    @Test
    public void givenAUserWhoseAddressIsInLondonThenReturnUserListWithSizeOne() {

        Map userMap = new LinkedHashMap<>();
        userMap.put("id", 135);
        userMap.put("firstName", "Mechelle");
        userMap.put("lastName", "Boam");
        userMap.put("email", "mboam3q@thetimes.co.uk");
        userMap.put("ipAddress", "113.71.242.187");
        userMap.put("latitude", 51.6710832);
        userMap.put("longitude", 0.8078532);
        List users = new ArrayList(asList(userMap));
        doReturn(users).when(apiConnectorServiceMock).getResponseFromApiForGetRequest(any(), any());
        doReturn(londonCity).when(cityFactory).getCity("london");
        List usersFound = testObj.getUsersBasedInAndAroundACity("london");
        assertTrue(usersFound.size() == 1);
    }

}
