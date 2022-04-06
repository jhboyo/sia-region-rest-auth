package com.sia.api.region.demo.gis.region;

import com.sia.api.region.demo.common.BaseTest;
import com.sia.api.region.demo.gis.common.Coordinatation;
import com.sia.api.region.demo.gis.common.ResponseDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegionControllerTests extends BaseTest {


    @Autowired
    RegionRepository regionRepository;


    @Test
    @DisplayName("region을 정상적으로 생성하는 테스트")
    public void createRegion() throws Exception {

        List<Coordinatation> coords = getCoordinatations();

        ResponseDto aoiDto = ResponseDto.builder()
                .name("과천시3")
                .area(coords)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/sia/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(aoiDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))

                ;
    }

    @Test
    @DisplayName("region 입력값이 비어 있는 경우에 에러가 발생하는 테스트")
    public void createAoi_BadRequest_Empty_Input() throws Exception {

        // set empty value
        List<Coordinatation> coords = new ArrayList<>();

        ResponseDto aoiDto = ResponseDto.builder()
                .name("과천시")
                .area(coords)
                .build();

        this.mockMvc.perform(post("/sia/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(aoiDto))
                )
                .andExpect(status().isBadRequest())
        ;
    }



    @NotNull
    private List<Coordinatation> getCoordinatations() {
        List<Coordinatation> coords = new ArrayList<>();
        coords.add(new Coordinatation(127.02, 37.742));
        coords.add(new Coordinatation(127.023, 37.664));
        coords.add(new Coordinatation(126.945, 37.605));
        coords.add(new Coordinatation(126.962, 37.692));
        coords.add(new Coordinatation(127.02, 37.742));
        return coords;
    }


}
