package com.guisebastiao.springbootauth.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingDTO {
    private int totalItems;
    private int totalPages;
    private int currentPage;
    private int itemsPerPage;
}
