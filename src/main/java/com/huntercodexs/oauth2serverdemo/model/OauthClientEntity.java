package com.huntercodexs.oauth2serverdemo.model;

import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OauthClientEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column
    private String client;

    @Column
    private String password;

    @Column
    private int accessTokenValiditySeconds;

    @Column
    private int refreshTokenValiditySeconds;
    
}
