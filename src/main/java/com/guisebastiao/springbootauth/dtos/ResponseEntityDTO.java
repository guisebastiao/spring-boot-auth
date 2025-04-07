package com.guisebastiao.springbootauth.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseEntityDTO {
    private int status;

    private String message;

    private Object data;

    private Boolean success;

    private PagingDTO paging;

    private List<FieldErrorDTO> fieldErrors;
}
