package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RSAKeys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RSAKeys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer keyId;

    @ManyToOne
    @JoinColumn(name = "DegreeID")
    private Degree degree;

    @Column(nullable = false)
    private String rsaPublicKey;

    @Column(nullable = false)
    private String rsaSignature;
}
