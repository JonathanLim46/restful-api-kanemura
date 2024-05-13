package com.pentahelix.kanemuraproject.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

    private T data;
    private String errors;
    private PagingResponse paging;

//    @Setter
//    private int code;
//
//    @Setter
//    private String message;
}
