package br.edu.uniara.lpi2.rest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String role;

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
