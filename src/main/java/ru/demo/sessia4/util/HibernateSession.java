package ru.demo.sessia4.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.demo.sessia4.model.Student;

public class HibernateSession {
    private static SessionFactory sessionFactory;
    private HibernateSession(){}

    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            try{
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                configuration.addAnnotatedClass(Student.class);
                sessionFactory = configuration.buildSessionFactory();
            }catch (Exception e){
                System.err.println("Session Factory: " + e.getMessage());
            }
        }
        return sessionFactory;
    }
}
