package com.blindkiosk.server.ApiResponse;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class ApiResponse<T> {
    @NonNull private T data; //RequiredArgsConstructor로 인해서 data변수에 대한 생성자가 만들어짐.
    private List<String> errors;

}
