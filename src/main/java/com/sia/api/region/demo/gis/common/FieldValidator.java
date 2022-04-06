package com.sia.api.region.demo.gis.common;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import java.util.List;


/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 요청 값에 대한 필드 조건 유효성 검사
 */
@Component
public class FieldValidator {

    public void validate(RequestDto requestDto, Errors errors) {

        List<Coordinatation> coords = requestDto.getArea();

        if (coords.size() < 5) {
            errors.reject("wrongCoordsCount", "Wrong polygon numbers");
        }

        for (int i =0; i<coords.size(); i++) {
            if (coords.get(i).getX() == null || coords.get(i).getY() == null) {
                errors.rejectValue("area", "hasNullCoordsValue", "coords has null value");
            }
            if (requestDto.getName().isBlank() || requestDto.getName() == null) {
                errors.rejectValue("name", "blankNameValue", "name is blank");
            }
            // Todo add other fields
        }
    }
}
