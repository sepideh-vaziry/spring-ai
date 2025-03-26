package com.example.aitutorial;

import java.util.List;

public record Author(
    String author,
    List<String> books
) {

}
