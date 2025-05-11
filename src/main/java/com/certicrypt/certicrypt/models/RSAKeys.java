package com.certicrypt.certicrypt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rsakeys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RSAKeys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="keyid")
    private Integer keyId;

    @ManyToOne
    @JoinColumn(name = "degreeid")
    private Degree degree;

    @Column(nullable = false, name="rsa_publickey")
    private String rsaPublicKey;

    @Column(nullable = false, name="rsa_signature")
    private String rsaSignature;
}
