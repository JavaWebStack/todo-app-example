package org.javawebstack.todoexample.resource;

import org.javawebstack.todoexample.model.Task;
import org.javawebstack.webutils.Resource;

import java.util.UUID;

public class TaskResource implements Resource<Task> {

    UUID id;
    String text;
    Boolean done;

    public void map(Task task, Context context) {
        this.id = task.getId();
        this.text = task.getText();
        this.done = task.getDone();
    }

}
