package com.location.validator.util;

import com.location.validator.model.Coordinates;

import java.util.HashMap;
import java.util.Map;

public class CommonConstants {

    public static final Map<String, Coordinates> cityNameToCityCoordinatesMap = new HashMap<String, Coordinates>() {
        {
            put("london", new Coordinates(51.507222222222225, -0.1275));
        }

    };


}



