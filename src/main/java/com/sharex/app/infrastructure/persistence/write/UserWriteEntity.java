package com.sharex.app.infrastructure.persistence.write;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(name = "uk_user_email", columnNames = "email")}
)
@Data
public class UserWriteEntity {

    @Id
    private String id;

    private String name;

    private String email;
}
