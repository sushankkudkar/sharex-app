package com.sharex.app.infrastructure.persistence.read;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_read_view")
@Data
public class UserReadEntity {

    @Id
    private String id;
    private String name;
    private String email;
}
