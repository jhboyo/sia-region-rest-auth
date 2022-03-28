package com.sia.api.region.demo.gis.aoi;

import com.sia.api.region.demo.gis.common.Coordinatation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author joonhokim
 * @date 2022/03/28
 * @description aoi request dto 객체
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AoiRequestDto {


    @NotNull @NotEmpty
    private String name;

    @NotNull @NotEmpty
    private List<Coordinatation> area;

}
