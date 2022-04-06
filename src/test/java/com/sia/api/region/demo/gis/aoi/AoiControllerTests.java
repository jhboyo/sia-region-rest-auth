package com.sia.api.region.demo.gis.aoi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.api.region.demo.account.LoginDto;
import com.sia.api.region.demo.account.TokenDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



public class AoiControllerTests extends BaseTest {

    @Autowired
    AoiRepository aoiRepository;


    @Test
    @DisplayName("행정지역에 지리적으로 포함되는 관심지역을 조회")
    public void getAoi() throws Exception {
        //Given
        int regionId = 1;

        // When & Then
      this.mockMvc.perform(get("/sia/regions/{id}/aois/intersects", regionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("aois").exists())
                .andExpect(jsonPath("aois").isArray())
                .andExpect(jsonPath("aois").isNotEmpty())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-aoi"))
        ;

    }

    @Test
    @DisplayName("행정지역에 지리적으로 포함되지 않는 관심지역을 조회")
    public void getAoi_notfound() throws Exception {
        //Given
        int regionId = 1212120012;

        // When & Then
        this.mockMvc.perform(get("/sia/regions/{id}/aois/intersects", regionId))
                .andExpect(status().isNotFound())
        ;
    }



    @Test
    @DisplayName("token을 발급받고 권한에 따라 aoi를 정상적으로 생성하는 테스트")
    public void createAoi() throws Exception {

        //USER 권한으로 Token 요청 - 컨트롤러에서 접근 권한을 ADMIN 으로 제한하면 Access Denied 에러가 발생해야 한다!
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("joonho3");
        loginDto.setPassword("joonho3");

        ResultActions resultActionsForToken = mockMvc.perform(post("/api/authenticate")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .accept(MediaTypes.HAL_JSON)
                                                        .content(objectMapper.writeValueAsString(loginDto)))
                                                .andDo(print())
                                                .andExpect(status().isOk())
                                                ;

        MvcResult result = resultActionsForToken.andReturn();

        TokenDto tokenDto = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TokenDto.class);

        List<Coordinatation> coords = getCoordinatations();

        ResponseDto aoiDto = ResponseDto.builder()
                            .name("수원시4")
                            .area(coords)
                            .build();

        ResultActions resultActions = mockMvc.perform(post("/sia/aois")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(aoiDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                //.andExpect(jsonPath("name").exists())
                //.andExpect(jsonPath("area").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))

        ;
    }

    @Test
    @DisplayName("aoi 입력값이 비어 있는 경우에 에러가 발생하는 테스트")
    public void createAoi_BadRequest_Empty_Input() throws Exception {

        //USER 권한으로 Token 요청 - 컨트롤러에서 접근 권한을 ADMIN 으로 제한하면 Access Denied 에러가 발생해야 한다!
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("joonho3");
        loginDto.setPassword("joonho3");

        ResultActions resultActionsForToken = mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk())
                ;

        MvcResult result = resultActionsForToken.andReturn();

        TokenDto tokenDto = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TokenDto.class);

        // set empty value
        List<Coordinatation> coords = new ArrayList<>();

        ResponseDto aoiDto = ResponseDto.builder()
                                            .name("수원시3")
                                            .area(coords)
                                            .build();

        this.mockMvc.perform(post("/sia/aois")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getToken())
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
