package org.example.atm.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter

public abstract class AbstractPaginationResponse<T> {
    private List<T> content;
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;


    public AbstractPaginationResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNum = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }


}
