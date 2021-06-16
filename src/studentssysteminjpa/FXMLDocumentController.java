/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentssysteminjpa;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class FXMLDocumentController implements Initializable {

    int index = -1;
    List<Course> coursesList;
    List<Student> studentList;

    @FXML
    private TableView<Student> StudentTable;
    @FXML
    private TableColumn<Student, Integer> id;
    @FXML
    private TableColumn<Student, String> name;
    @FXML
    private TableColumn<Student, String> major;
    @FXML
    private TableColumn<Student, Double> grade;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private Slider gradeSlider;
    @FXML
    private Label gradeLabel;
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private TextField majorTextField;
    @FXML
    private Button registerBtn;
    @FXML
    private ChoiceBox<String> courseChoiceBox;
    @FXML
    private TextField semesterTextField;
    @FXML
    private Button secondPageBtn;

    private EntityManagerFactory emf;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.emf = Persistence.createEntityManagerFactory("StudentsSystemINJPAPU");
        id.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        major.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));
        grade.setCellValueFactory(new PropertyValueFactory<Student, Double>("grade"));

        EntityManager em = emf.createEntityManager();
        studentList = em.createNamedQuery("Student.findAll").getResultList();
        StudentTable.getItems().setAll(studentList);

        coursesList = em.createNamedQuery("Course.findAll").getResultList();
        for (Course course : coursesList) {
            courseChoiceBox.getItems().add(course.getId());
        }

        gradeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gradeLabel.setText(gradeSlider.getValue() + "");
            }
        });

    }

    @FXML
    private void load(MouseEvent event) {
        index = StudentTable.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Student s = studentList.get(index);
            idTextField.setText("" + s.getId());
            nameTextField.setText("" + s.getName());
            gradeSlider.setValue(s.getGrade());
            majorTextField.setText(s.getMajor());
        } else {
            JOptionPane.showMessageDialog(null, "You Should Select A Row");
        }
    }

    @FXML
    private void add(ActionEvent event) {
        if (virify()) {
            boolean idFound = false;
            EntityManager em = this.emf.createEntityManager();
            List<Student> tempStudentList = em.createNamedQuery("Student.findAll").getResultList();
            for (Student student : tempStudentList) { // check if the entered id is available or not
                if (student.getId() == Integer.parseInt(idTextField.getText())) {
                    idFound = true;
                    break;
                }
            }
            if (idFound == false) { // don't add unless the student's id is not duplicated
                Student addedStudent = new Student(Integer.parseInt(idTextField.getText()), nameTextField.getText(), majorTextField.getText(), (double) Math.round(gradeSlider.getValue() * 100) / 100);
                em.getTransaction().begin();
                em.persist(addedStudent);
                em.getTransaction().commit(); // to make the changes in the database
                em.close();
                StudentTable.getItems().add(addedStudent); // add addedStudent obejct also to StudentsTable ^_^
                JOptionPane.showMessageDialog(new JFrame(), "Added Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "it's kind of logic that there are should not be two students holds the same id ^_^", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All Fields are Required!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void delete(ActionEvent event) {
        if (index != -1) {
            Student tempStudent = StudentTable.getSelectionModel().getSelectedItem();
            EntityManager em = this.emf.createEntityManager();
            em.getTransaction().begin();
            em.createNamedQuery("Student.deleteById")
                    .setParameter("id", tempStudent.getId())
                    .executeUpdate();
            em.getTransaction().commit(); //changes in the database
            em.close();
            StudentTable.getItems().remove(tempStudent);
            JOptionPane.showMessageDialog(new JFrame(), "Deleted Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "You Should Select a Student to Delete!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void update(ActionEvent event) {
        if (virify()) {
            if (index != -1) {
                Integer requiredId = StudentTable.getSelectionModel().getSelectedItem().getId();
                // New Values :
                Integer id = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText();
                String major = majorTextField.getText();
                double grade = (double) Math.round(gradeSlider.getValue() * 100) / 100;
                Student tempStudent = new Student(id, name, major, grade);

                EntityManager em = this.emf.createEntityManager();
                em.getTransaction().begin();
                em.createNamedQuery("Student.updateById")
                        .setParameter("id", id)
                        .setParameter("name", name)
                        .setParameter("major", major)
                        .setParameter("grade", grade)
                        .setParameter("oldID", requiredId)
                        .executeUpdate();
                em.getTransaction().commit(); // changes in the database
                em.close();
                StudentTable.getItems().set(index, tempStudent);
                JOptionPane.showMessageDialog(new JFrame(), "Updated Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "You Should Select a Student to Update!", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All Fields are Required!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void register(ActionEvent event) {
        if (courseChoiceBox.getValue() != null && semesterTextField.getText() != null) {
            if (index != -1) { 
                boolean studentCourseFound = false;
                Student registerStudent = StudentTable.getSelectionModel().getSelectedItem();
                EntityManager em = this.emf.createEntityManager();
                List<Course> studentCourses = em.createNamedQuery("Student.findStudentCourses")
                        .setParameter("id", registerStudent.getId()) 
                        .getResultList();
                for (Course studentCourse : studentCourses) {
                    if (courseChoiceBox.getValue().equalsIgnoreCase(studentCourse.getId())) {
                        studentCourseFound = true;
                        break;
                    }
                }
                if (studentCourseFound == false) {
                    em.getTransaction().begin();
                    Registration registration = new Registration(registerStudent.getId(), courseChoiceBox.getValue());
                    registration.setSemester(Integer.parseInt(semesterTextField.getText()));
                    em.persist(registration);
                    em.getTransaction().commit(); // to make the changes in the database
                    em.close();
                    JOptionPane.showMessageDialog(new JFrame(), "Registered Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "You Can't Register This Course Twice!", "Error Message", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You Should Select a Student to Register!", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You Should Select a Course and The Semester to Register!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void secondPageBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SecondQuestion.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Second Question");
        stage.show();
    }

    public boolean virify() {
        if ((!idTextField.getText().isEmpty()) && (!nameTextField.getText().isEmpty())
                && !majorTextField.getText().isEmpty()) {
            return true;
        }
        return false;
    }

}
