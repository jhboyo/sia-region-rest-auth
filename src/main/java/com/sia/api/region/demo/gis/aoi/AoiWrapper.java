package com.sia.api.region.demo.gis.aoi;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 리턴 할 Wrapper 객체
 */
@Getter
@Setter
public class AoiWrapper {

    private List<AoiResponseDto> aois;
}
