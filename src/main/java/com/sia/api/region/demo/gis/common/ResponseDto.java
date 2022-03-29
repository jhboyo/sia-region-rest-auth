package com.sia.api.region.demo.gis.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author joonhokim
 * @date 2022/03/28
 * @description aoi/region domain response dto 객체
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto {

    private Integer id;

    private String name;

    private List<Coordinatation> area;

}
