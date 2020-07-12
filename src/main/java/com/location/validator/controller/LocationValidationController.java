package com.location.validator.controller;

import com.location.validator.model.User;
import com.location.validator.service.LocationValidatorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LocationValidationController {

    @Autowired
    LocationValidatorService locationValidatorService;

    @GetMapping(value = "/city/{city}/users",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Find users that are either listed as living in a particular city or are based within 50 miles from it",notes="Provide a city to look up the users.")
    public List<User> getUsersListedAsLivingInACityOrWithinFiftyMilesFromIt(@ApiParam(value=" Only London city is configured to return results") @PathVariable String city) {
            return locationValidatorService.getListedUsersAndUsersBasedAroundACity(city.toLowerCase());
        }


    @GetMapping(value = "/listed/{city}/users",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Find users that are listed as living in a particular city",notes="Provide a city to look up the users.")
    public List<User> getUsersListedAsLivingInACity(@ApiParam(value=" Only London city is configured to return results") @PathVariable String city) {
            return locationValidatorService.getUsersListedAsLivingInACity(city.toLowerCase());
    }

    @GetMapping(value = "/around/{city}/users",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Find users that are living within fifty miles from a particular city",notes="Provide a city to look up the users.")
    public List<User> getUsersLessThanFiftyMilesFromACity(@ApiParam(value=" Only London city is configured to return results") @PathVariable String city) {
            return locationValidatorService.getUsersBasedInAndAroundACity(city.toLowerCase());
        }
}
