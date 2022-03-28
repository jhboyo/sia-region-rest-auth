package com.sia.api.region.demo.index;

import com.sia.api.region.demo.gis.aoi.AoiController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {


    /**
     * @author joonhokim
     * @date 2022/03/28
     * @description 잘못 된 url이 요청 될 경우 index로 이동
     */
    @GetMapping("/api")
    public RepresentationModel index() {
        var index = new RepresentationModel<>();
        index.add(linkTo(AoiController.class).withRel("aois"));

        return index;
    }

}
