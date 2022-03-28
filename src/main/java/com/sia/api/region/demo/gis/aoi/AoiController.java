package com.sia.api.region.demo.gis.aoi;

import com.sia.api.region.demo.gis.common.Coordinatation;
import org.locationtech.jts.geom.Coordinate;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 관심지역 컨트롤러로 Http Method 요청에 대한 RESTFul API
 */
@Controller
@RequestMapping(value = "/", produces = MediaTypes.HAL_JSON_VALUE)
public class AoiController {

    private AoiRepository aoiRepository;

    private ModelMapper modelMapper;

    /**
     * 생성자를 통한 의존성 주입
     * */
    public AoiController(AoiRepository aoiRepository, ModelMapper modelMapper) {
        this.aoiRepository = aoiRepository;
        this.modelMapper = modelMapper;
    }


    @PostMapping("regions")
    public ResponseEntity savePositions(@RequestBody @Valid AoiDto aoiDto, Errors errors) {

        System.out.println(aoiDto.getName());

        return ResponseEntity.ok(null);
    }


    @GetMapping("regions/{id}/aois/intersects")
    public ResponseEntity getAoi(@PathVariable Integer id) throws Exception {

        Optional<Aoi> optionalAoi = this.aoiRepository.findById(id);

        if (optionalAoi.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        /**
         * Geometry Polygon 타입의 Point 데이터를 Deserialize 하는 메서드를 찾지 못하여 아래와 같이 구현
         */
        Aoi aoi = optionalAoi.get();
        List list = Arrays.stream(aoi.getArea().getCoordinates()).collect(Collectors.toList());

        // polygon 좌표를 리스트에 따로 담음
        Coordinatation coordinatation = new Coordinatation();
        List<Coordinatation> coords = getCoordinatations(list, coordinatation);

        // modelMapper를 사용해서 데이터 매핑
        AoiDto aoiDto = new AoiDto();
        modelMapper.map(aoi, aoiDto);
        aoiDto.setArea(coords);

        // 리스트에 dto 데이터를 담은 후
        List<AoiDto> aoiDtoList = new ArrayList<>();
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
     * @param Geometry polygon 좌표 리스트
     * @param coordinatation dto
     * @return polygon 좌표를 dto 리스트에 담아서 리턴
     */
    private List<Coordinatation> getCoordinatations(List list, Coordinatation coordinatation) {
        List<Coordinatation> coords = new ArrayList<>();

        IntStream.range(0, list.size()).mapToObj(i -> (Coordinate) list.get(i)).forEach(coordinate -> {
            coordinatation.setXCoord(coordinate.x);
            coordinatation.setYCoord(coordinate.y);
            coords.add(coordinatation);
        });

        return coords;
    }
}
