package ru.demo.sessia4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Students extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("student-view.fxml"));
        Scene scene = new Scene(loader.load(), 750, 374);
        stage.setTitle("Студенты");
        stage.setScene(scene);
        stage.show();
    }
}
