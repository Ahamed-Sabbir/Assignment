package com.sabbir.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "authority_table")
public class Authority {
    @Id
    @GeneratedValue
    @Column(name = "authority_id")
    private UUID id;

    @Column(name = "authority", unique = true)
    private String authority;
}
