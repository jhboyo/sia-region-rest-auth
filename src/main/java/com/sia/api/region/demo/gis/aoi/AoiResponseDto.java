package com.sia.api.region.demo.gis.aoi;

import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @description aoi response dto 객체
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AoiResponseDto {

    private Integer id;

    private String name;

    private List<Coordinatation> area;

}
