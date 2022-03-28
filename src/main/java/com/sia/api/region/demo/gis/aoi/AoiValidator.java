package com.sia.api.region.demo.gis.aoi;

import com.sia.api.region.demo.gis.common.Coordinatation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AoiValidator {

    public void validate(AoiRequestDto aoiRequestDto, Errors errors) {

        List<Coordinatation> coords = aoiRequestDto.getArea();

        if (coords.size() < 5) {
            errors.reject("wrongCoordsCount", "Wrong polygon numbers");
        }

        for (int i =0; i<coords.size(); i++) {
            if (coords.get(i).getX() == null || coords.get(i).getY() == null) {
                errors.rejectValue("area", "hasNullCoordsValue", "coords has null value");

            }
        }


        // Todo add other fields


    }
}
