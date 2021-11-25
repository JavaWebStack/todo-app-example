package org.javawebstack.todoexample.model;

import lombok.Getter;
import lombok.Setter;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.webutils.crypt.Crypt;

import java.sql.Timestamp;

@Dates
@Getter
@Setter
public class User extends Model {

    @Column
    int id;
    @Column
    String name;
    @Column
    String password;
    @Column
    Timestamp createdAt;
    @Column
    Timestamp updatedAt;

    public User setPassword(String password) {
        this.password = Crypt.hash(password);
        return this;
    }

    public boolean checkPassword(String password) {
        return Crypt.check(this.password, password);
    }

}
