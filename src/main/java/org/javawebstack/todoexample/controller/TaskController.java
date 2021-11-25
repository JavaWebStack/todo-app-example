package org.javawebstack.todoexample.controller;

import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.With;
import org.javawebstack.httpserver.router.annotation.params.Path;
import org.javawebstack.httpserver.router.annotation.verbs.Delete;
import org.javawebstack.httpserver.router.annotation.verbs.Get;
import org.javawebstack.httpserver.router.annotation.verbs.Post;
import org.javawebstack.httpserver.router.annotation.verbs.Put;
import org.javawebstack.orm.Repo;
import org.javawebstack.todoexample.model.Task;
import org.javawebstack.todoexample.model.User;
import org.javawebstack.todoexample.request.TaskCreateRequest;
import org.javawebstack.todoexample.request.TaskUpdateRequest;
import org.javawebstack.todoexample.resource.TaskResource;
import org.javawebstack.webutils.Resource;

import java.util.List;

@PathPrefix("api/tasks")
@With("auth")
public class TaskController extends Controller {

    @Get
    public List<TaskResource> list(Exchange exchange) {
        return Resource.make(TaskResource.class, Repo.get(Task.class).accessible(exchange).all());
    }

    @Post
    public Object create(Exchange exchange, User user, TaskCreateRequest request) {
        Task task = new Task()
                .setUserId(user.getId())
                .setText(request.getText())
                .setDone(false);
        task.save(); // Create the task
        return Resource.make(TaskResource.class, task);
    }

    @Put("{task:task}")
    public Object update(Exchange exchange, @Path("task") Task task, TaskUpdateRequest request) {
        task
                .setDone(request.getDone()) // Change the done state
                .save(); // Save the task
        return Resource.make(TaskResource.class, task);
    }

    @Delete("{task:task}")
    public AbstractObject delete(@Path("task") Task task) {
        task.delete();
        return new AbstractObject().set("success", true);
    }

}
