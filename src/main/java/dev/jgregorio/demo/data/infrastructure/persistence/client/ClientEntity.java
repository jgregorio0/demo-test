package dev.jgregorio.demo.data.infrastructure.persistence.client;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CLIENTS", schema = "TEST")
@Getter
@Setter
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTS_SEQ")
    @SequenceGenerator(name = "CLIENTS_SEQ", sequenceName = "TEST.CLIENTS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;
}
