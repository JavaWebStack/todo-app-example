package org.javawebstack.todoexample.util;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.orm.Accessible;
import org.javawebstack.orm.Model;
import org.javawebstack.orm.query.Query;
import org.javawebstack.todoexample.model.User;

public class TaskAccessible implements Accessible {

    public <T extends Model> Query<T> access(Query<T> query, Object accessor) {
        if(!(accessor instanceof User)) {
            if(accessor instanceof Exchange)
                return access(query, ((Exchange) accessor).attrib("user"));
            return query.where(1, "=", 2);
        }
        User user = (User) accessor;
        return query.where("userId", user.getId());
    }

}
