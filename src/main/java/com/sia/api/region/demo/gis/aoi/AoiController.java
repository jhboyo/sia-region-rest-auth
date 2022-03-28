package com.sia.api.region.demo.gis.aoi;

import com.sia.api.region.demo.common.ErrorsResource;
import com.sia.api.region.demo.gis.common.Coordinatation;
import org.locationtech.jts.geom.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 관심지역 컨트롤러로 Http Method 요청에 대한 RESTFul API
 */
@Controller
@RequestMapping(value = "/", produces = MediaTypes.HAL_JSON_VALUE)
public class AoiController {

    private AoiRepository aoiRepository;

    private AoiValidator aoiValidator;

    private ModelMapper modelMapper;

    /**
     * 생성자를 통한 의존성 주입
     * */
    public AoiController(AoiValidator aoiValidator, AoiRepository aoiRepository, ModelMapper modelMapper) {
        this.aoiValidator = aoiValidator;
        this.aoiRepository = aoiRepository;
        this.modelMapper = modelMapper;
    }


    @PostMapping("/aois")
    public ResponseEntity saveAoi(@RequestBody @Valid AoiRequestDto aoiRequestDto, Errors errors) {

        // validation
        aoiValidator.validate(aoiRequestDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Aoi aoi = modelMapper.map(aoiRequestDto, Aoi.class);

        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[] {
                new Coordinate(aoiRequestDto.getArea().get(0).getX(), aoiRequestDto.getArea().get(0).getY()),
                new Coordinate(aoiRequestDto.getArea().get(1).getX(), aoiRequestDto.getArea().get(1).getY()),
                new Coordinate(aoiRequestDto.getArea().get(2).getX(), aoiRequestDto.getArea().get(2).getY()),
                new Coordinate(aoiRequestDto.getArea().get(3).getX(), aoiRequestDto.getArea().get(3).getY()),
                new Coordinate(aoiRequestDto.getArea().get(4).getX(), aoiRequestDto.getArea().get(4).getY()),
        }), null);


        aoi.setArea(polygon);
        Aoi newAoi = this.aoiRepository.save(aoi);

        Integer newAoiId = newAoi.getId();

        AoiResponseDto aoiResponseDto = new AoiResponseDto();
        aoiResponseDto.setId(newAoiId);

        return ResponseEntity.ok(aoiResponseDto);
    }


    @GetMapping("/regions/{id}/aois/intersects")
    public ResponseEntity getAoi(@PathVariable Integer id) throws Exception {

        Optional<Aoi> optionalAoi = this.aoiRepository.findById(id);

        if (optionalAoi.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        /**
         * Geometry Polygon 타입의 Point 데이터를 Deserialize 하는 library method 를 찾지 못하여 아래와 같이 구현
         */
        Aoi aoi = optionalAoi.get();
        List list = Arrays.stream(aoi.getArea().getCoordinates()).collect(Collectors.toList());

        // polygon 좌표를 리스트에 따로 담음
        Coordinatation coordinatation = new Coordinatation();
        List<Coordinatation> coords = getCoordinatations(list, coordinatation);

        // modelMapper를 사용해서 데이터 매핑
        AoiResponseDto aoiDto = new AoiResponseDto();
        modelMapper.map(aoi, aoiDto);
        aoiDto.setArea(coords);

        // 리스트에 dto 데이터를 담은 후
        List<AoiResponseDto> aoiDtoList = new ArrayList<>();
        aoiDtoList.add(aoiDto);

        // 최종 wrapper 객체에 리스트 데이터를 담음
        AoiWrapper aoiWrapper = new AoiWrapper();
        aoiWrapper.setAois(aoiDtoList);

        // rest api -  링크 정보를 담아서 함께 리턴
        AoiResource aoiResource = new AoiResource(aoiWrapper, aoiDto);
        aoiResource.add(Link.of("/docs/index.html#resources-aois-get").withRel("profile"));

        return ResponseEntity.ok(aoiResource);
    }


    /**
     * @param list geometry polygon 좌표 리스트
     * @param coordinatation dto
     * @return polygon 좌표를 dto 리스트에 담아서 리턴
     */
    private List<Coordinatation> getCoordinatations(List list, Coordinatation coordinatation) {
        List<Coordinatation> coords = new ArrayList<>();

        IntStream.range(0, list.size()).mapToObj(i -> (Coordinate) list.get(i)).forEach(coordinate -> {
            coordinatation.setX(coordinate.x);
            coordinatation.setY(coordinate.y);
            coords.add(coordinatation);
        });

        return coords;
    }


    private ResponseEntity<EntityModel<Errors>> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource().modelOf(errors));
    }
}
