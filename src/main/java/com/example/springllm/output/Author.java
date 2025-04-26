package com.example.springllm.output;

import java.util.List;

public record Author(
    String author,
    List<String> books
) {

}
