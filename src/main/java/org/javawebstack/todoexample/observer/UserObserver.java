package org.javawebstack.todoexample.observer;

import org.javawebstack.orm.Observer;
import org.javawebstack.todoexample.model.Task;
import org.javawebstack.todoexample.model.User;

public class UserObserver implements Observer<User> {

    public void created(User user) {
        System.out.println("Registered new user " + user.getName() + " (#" + user.getId() + ")");
    }

    public void deleting(User user) {
        user.hasMany(Task.class).delete(); // Delete all tasks of a user when the user gets deleted
    }

}
