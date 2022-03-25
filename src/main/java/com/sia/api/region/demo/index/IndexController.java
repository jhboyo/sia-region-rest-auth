package com.sia.api.region.demo.index;

import com.sia.api.region.demo.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    //링크 정보만 리턴한다.

    /**
     * ResourceSupport is now RepresentationModel
     * Resource is now EntityModel
     * Resources is now CollectionModel
     * PagedResources is now PagedModel
     * @return
     */
    @GetMapping("/api")
    public RepresentationModel index() {
        var index = new RepresentationModel<>();
        index.add(linkTo(EventController.class).withRel("events"));

        return index;
    }

}
