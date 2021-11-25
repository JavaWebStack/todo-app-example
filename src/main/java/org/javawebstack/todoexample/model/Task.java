package org.javawebstack.todoexample.model;

import lombok.Getter;
import lombok.Setter;
import org.javawebstack.orm.annotation.Column;
import org.javawebstack.orm.annotation.Dates;
import org.javawebstack.orm.annotation.SoftDelete;
import org.javawebstack.webutils.modelbind.ModelBind;

import java.sql.Timestamp;
import java.util.UUID;

@Dates // Automatically sets and updates createdAt and updatedAt timestamps
@SoftDelete // Enables soft-deletes, calling delete will only set deletedAt and queries are filtered if withDeleted() isn't called
@Getter
@Setter
@ModelBind("task") // This enabled model-binding in routes and sets the type name
public class Task extends Model {

    @Column
    UUID id;
    @Column
    Integer userId;
    @Column
    String text;
    @Column
    Boolean done;
    @Column
    Timestamp createdAt;
    @Column
    Timestamp updatedAt;
    @Column
    Timestamp deletedAt;

}
