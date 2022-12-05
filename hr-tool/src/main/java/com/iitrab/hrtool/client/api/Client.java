package com.iitrab.hrtool.client.api;

import com.iitrab.hrtool.employee.api.Employee;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "clients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "contact_number")
    @Nullable
    private String contactNumber;
    @Column(name = "contact_email")
    @Nullable
    private String contactEmail;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "manager_id")
    @Nullable
    private Employee manager;

    public Client(String name, @Nullable String contactNumber, @Nullable String contactEmail, Employee manager) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
        this.manager = manager;
    }

}
