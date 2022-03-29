package com.sia.api.region.demo.gis.region;

import com.sia.api.region.demo.common.ErrorsResource;
import com.sia.api.region.demo.gis.common.FieldValidator;
import com.sia.api.region.demo.gis.common.ResponseDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 행정지역 컨트롤러로 Http Method 요청에 대한 RESTFul API
 */
@Controller
@RequestMapping(value = "/", produces = MediaTypes.HAL_JSON_VALUE)
public class RegionController {

    private RegionRepository regionRepository;

    private FieldValidator aoiValidator;

    private ModelMapper modelMapper;

    /**
     * 생성자를 통한 의존성 주입(DI)
     * */
    public RegionController(FieldValidator aoiValidator, RegionRepository regionRepository, ModelMapper modelMapper) {
        this.aoiValidator = aoiValidator;
        this.regionRepository = regionRepository;
        this.modelMapper = modelMapper;
    }


    /**
     * @param requestDto 요청 값 매핑
     * @param errors @Valid 에서 발생 한 값들 Errors객체에 매핑
     * @return DB에 저장 되고 자동 생성 된 serial id 값 리턴
     */
    @PostMapping("/regions")
    public ResponseEntity saveRegion(@RequestBody @Valid RegionRequestDto requestDto, Errors errors) {

        // validation
        aoiValidator.validate(requestDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Region region = modelMapper.map(requestDto, Region.class);

        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[] {
                new Coordinate(requestDto.getArea().get(0).getX(), requestDto.getArea().get(0).getY()),
                new Coordinate(requestDto.getArea().get(1).getX(), requestDto.getArea().get(1).getY()),
                new Coordinate(requestDto.getArea().get(2).getX(), requestDto.getArea().get(2).getY()),
                new Coordinate(requestDto.getArea().get(3).getX(), requestDto.getArea().get(3).getY()),
                new Coordinate(requestDto.getArea().get(4).getX(), requestDto.getArea().get(4).getY()),
        }), null);


        region.setArea(polygon);
        Region newRegion = this.regionRepository.save(region);

        Integer newRegionId = newRegion.getId();

        WebMvcLinkBuilder selfLinkBuilder = linkTo(RegionController.class).slash(newRegionId);
        URI createdUri = selfLinkBuilder.toUri();

        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(newRegionId);

        return ResponseEntity.created(createdUri).body(responseDto);

    }


    private ResponseEntity<EntityModel<Errors>> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource().modelOf(errors));
    }
}
