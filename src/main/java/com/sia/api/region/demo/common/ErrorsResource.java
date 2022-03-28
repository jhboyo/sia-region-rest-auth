package com.sia.api.region.demo.common;

import com.sia.api.region.demo.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * @author joonhokim
 * @date 2022/03/28
 * @description badRequest등이 발생 시 홀출 되는 객체로서 index 정보를 준다.
 */
public class ErrorsResource extends EntityModel<Errors> {

    public static EntityModel<Errors> modelOf(Errors errors) {
        EntityModel<Errors> errorsModel = EntityModel.of(errors);
        errorsModel.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));

        return errorsModel;
    }

}
