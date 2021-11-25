package org.javawebstack.todoexample.request;

import lombok.Getter;
import org.javawebstack.validator.Rule;

@Getter
public class TaskCreateRequest {

    @Rule("required")
    String text;

}
