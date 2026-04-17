module ru.demo.sessia4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.desktop;
    requires java.naming;


    opens ru.demo.sessia4 to javafx.fxml;
    exports ru.demo.sessia4;
    opens ru.demo.sessia4.model to org.hibernate.orm.core, javafx.base;
}