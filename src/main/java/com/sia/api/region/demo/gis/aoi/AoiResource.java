package com.sia.api.region.demo.gis.aoi;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import java.util.Arrays;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 관심지역관련 self relation 링크 정보 전달
 */
public class AoiResource extends EntityModel<Object> {
    public AoiResource(AoiWrapper aoiWrapper, AoiResponseDto aoiDto, Link... links) {
        super(aoiWrapper, Arrays.asList(links));
        add(linkTo(AoiController.class).slash(aoiDto.getId()).withSelfRel());
    }

    public AoiResource(Aoi aoi, Link... links) {
        super(aoi, Arrays.asList(links));
        add(linkTo(AoiController.class).slash(aoi.getId()).withSelfRel());
    }
}
