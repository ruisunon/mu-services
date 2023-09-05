package com.rean.dto.request;


import lombok.Data;

@Data
public class PaginationRequest {

    private Integer page;
    private Integer size;

    public void setPaginationRequest(Integer page, Integer size){
        if(page == null || page <= 0) {
            page = 1;
        }
        if(size == null || size <= 0) {
            size = 10;
        }
        this.page = page;
        this.size = size;
    }
}
