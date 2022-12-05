package com.iitrab.hrtool.project.api;

import com.iitrab.hrtool.client.api.Client;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @JoinColumn(name = "client_id")
    @ManyToOne(cascade = CascadeType.DETACH)
    @Nullable
    private Client client;

    public Project(String name, Client client) {
        this.name = name;
        this.client = client;
    }

}
