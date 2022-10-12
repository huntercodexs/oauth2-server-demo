package com.huntercodexs.oauth2serverdemo.config.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "operator_oauth2_server")
public class OperatorEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String role;

    @Column
    public String email;

    @Column
    public int deleted;

    @Column
    public int status;

}
