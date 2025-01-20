package com.company.PersonalBlog.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpApiResponse<T> {
    private int code;
    private String message;
    private T content;

    private List<ErrorDto> errorList;

}
