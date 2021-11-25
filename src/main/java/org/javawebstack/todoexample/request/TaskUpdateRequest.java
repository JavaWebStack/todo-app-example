package org.javawebstack.todoexample.request;

import lombok.Getter;
import org.javawebstack.validator.Rule;

@Getter
public class TaskUpdateRequest {

    @Rule("required")
    Boolean done;

}
