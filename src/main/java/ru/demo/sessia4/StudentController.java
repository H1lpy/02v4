package ru.demo.sessia4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import ru.demo.sessia4.model.Student;
import ru.demo.sessia4.repository.StudentDao;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StudentController implements Initializable {

    @FXML public TableColumn<Student, String> numberpp;
    @FXML public TableColumn<Student, String> specialtyCode;
    @FXML public TableColumn<Student, String> lastName;
    @FXML public TableColumn<Student, String> firstName;
    @FXML public TableColumn<Student, String> middleName;
    @FXML public TableColumn<Student, Integer> birthYear;
    @FXML public TableView<Student> tableView;
    @FXML private TextField searchSpecialtyField;
    @FXML private TextField yearFromField;
    @FXML private TextField yearToField;
    @FXML private ComboBox<String> sortComboBox;

    private StudentDao studentDao = new StudentDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellTable();
        initSortComboBox();
        setupListeners();
        filterData();
    }

    private void setCellTable() {
        numberpp.setCellValueFactory(new PropertyValueFactory<>("id"));
        specialtyCode.setCellValueFactory(new PropertyValueFactory<>("specialtyCode"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        birthYear.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
    }

    private void initSortComboBox() {
        sortComboBox.getItems().addAll("Без сортировки", "А -> Я", "Я -> А");
        sortComboBox.setValue("Без сортировки");
    }

    private void setupListeners() {
        searchSpecialtyField.textProperty().addListener((obs, old, val) -> filterData());
        yearFromField.textProperty().addListener((obs, old, val) -> filterData());
        yearToField.textProperty().addListener((obs, old, val) -> filterData());
        sortComboBox.valueProperty().addListener((obs, old, val) -> filterData());
    }

    private void filterData() {
        List<Student> students = studentDao.findAll();

        // Поиск по шифру специальности
        String searchText = searchSpecialtyField.getText();
        if (searchText != null && !searchText.isEmpty()) {
            students = students.stream()
                    .filter(s -> s.getSpecialtyCode() != null && s.getSpecialtyCode().contains(searchText))
                    .collect(Collectors.toList());
        }

        // Фильтрация по году рождения (от)
        String fromText = yearFromField.getText();
        if (fromText != null && !fromText.isEmpty()) {
            try {
                int from = Integer.parseInt(fromText);
                students = students.stream()
                        .filter(s -> s.getBirthYear() >= from)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: год должен быть числом");
            }
        }

        // Фильтрация по году рождения (до)
        String toText = yearToField.getText();
        if (toText != null && !toText.isEmpty()) {
            try {
                int to = Integer.parseInt(toText);
                students = students.stream()
                        .filter(s -> s.getBirthYear() <= to)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: год должен быть числом");
            }
        }

        // Сортировка по ФИО
        String sortValue = sortComboBox.getValue();
        if (sortValue != null) {
            if (sortValue.equals("А -> Я")) {
                students.sort((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()));
            } else if (sortValue.equals("Я -> А")) {
                students.sort((s1, s2) -> s2.getFullName().compareToIgnoreCase(s1.getFullName()));
            }
        }

        tableView.getItems().clear();
        tableView.getItems().addAll(students);
    }

    @FXML
    public void onAddClick(ActionEvent event) {
        showAddDialog();
        filterData();
    }

    @FXML
    public void onRemoveClick(ActionEvent event) {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите запись для удаления", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удаление записи");
        alert.setContentText("Вы действительно хотите удалить студента \"" + selected.getFullName() + "\"?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentDao.delete(selected);
            filterData();
            showAlert("Успех", "Запись успешно удалена", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void onUpdateClick(ActionEvent event) {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите запись для изменения", Alert.AlertType.ERROR);
            return;
        }
        showEditDialog(selected);
        filterData();
    }

    private void showAddDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Добавление студента");
        dialog.setHeaderText("Введите данные студента");

        TextField specialtyField = new TextField();
        specialtyField.setPromptText("Шифр специальности");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");
        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Отчество");
        TextField birthYearField = new TextField();
        birthYearField.setPromptText("Год рождения");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        grid.add(new Label("Шифр специальности:"), 0, 0);
        grid.add(specialtyField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Имя:"), 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(new Label("Отчество:"), 0, 3);
        grid.add(middleNameField, 1, 3);
        grid.add(new Label("Год рождения:"), 0, 4);
        grid.add(birthYearField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveBtn = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                try {
                    int birthYear = Integer.parseInt(birthYearField.getText());
                    if (birthYear < 1990 || birthYear > 2040) {
                        showAlert("Ошибка", "Год рождения должен быть в диапазоне 1990-2040", Alert.AlertType.ERROR);
                        return null;
                    }
                    return new Student(specialtyField.getText(), lastNameField.getText(),
                            firstNameField.getText(), middleNameField.getText(), birthYear);
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Год рождения должен быть числом", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> {
            studentDao.save(student);
            showAlert("Успех", "Студент успешно добавлен", Alert.AlertType.INFORMATION);
        });
    }

    private void showEditDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Редактирование студента");
        dialog.setHeaderText("Измените данные студента");

        TextField specialtyField = new TextField(student.getSpecialtyCode());
        TextField lastNameField = new TextField(student.getLastName());
        TextField firstNameField = new TextField(student.getFirstName());
        TextField middleNameField = new TextField(student.getMiddleName());
        TextField birthYearField = new TextField(String.valueOf(student.getBirthYear()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        grid.add(new Label("Шифр специальности:"), 0, 0);
        grid.add(specialtyField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Имя:"), 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(new Label("Отчество:"), 0, 3);
        grid.add(middleNameField, 1, 3);
        grid.add(new Label("Год рождения:"), 0, 4);
        grid.add(birthYearField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveBtn = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                try {
                    int birthYear = Integer.parseInt(birthYearField.getText());
                    if (birthYear < 1990 || birthYear > 2040) {
                        showAlert("Ошибка", "Год рождения должен быть в диапазоне 1990-2040", Alert.AlertType.ERROR);
                        return null;
                    }
                    student.setSpecialtyCode(specialtyField.getText());
                    student.setLastName(lastNameField.getText());
                    student.setFirstName(firstNameField.getText());
                    student.setMiddleName(middleNameField.getText());
                    student.setBirthYear(birthYear);
                    return student;
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Год рождения должен быть числом", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            studentDao.update(updated);
            showAlert("Успех", "Студент успешно обновлен", Alert.AlertType.INFORMATION);
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}