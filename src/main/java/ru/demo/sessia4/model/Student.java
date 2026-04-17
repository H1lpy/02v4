package ru.demo.sessia4.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "students")
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int numberpp;

    @Column(name = "specialty_code", length = 6)
    private String specialtyCode;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "middle_name", length = 20)
    private String middleName;

    @Column(name = "birth_year")
    private int birthYear;

    public Student() {}

    public Student(String specialtyCode, String lastName, String firstName, String middleName, int birthYear) {
        this.specialtyCode = specialtyCode;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthYear = birthYear;
    }

    public String getId() { return String.valueOf(numberpp); }
    public void setId(int id) { this.numberpp = id; }

    public String getSpecialtyCode() { return specialtyCode; }
    public void setSpecialtyCode(String specialtyCode) { this.specialtyCode = specialtyCode; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public int getBirthYear() { return birthYear; }
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        fullName.append(lastName).append(" ").append(firstName);
        if (middleName != null && !middleName.trim().isEmpty()) {
            fullName.append(".").append(middleName);
        }
        return fullName.toString();
    }
}