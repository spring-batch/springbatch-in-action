package com.example.mail.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

}
