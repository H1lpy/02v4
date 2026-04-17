package ru.demo.sessia4.repository;

import ru.demo.sessia4.model.Student;

public class StudentDao extends BaseDao<Student> {
    public StudentDao(){ super(Student.class);}
}
