/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentssysteminjpa;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class SecondQuestionController implements Initializable {

    List<Student> studentList;

    @FXML
    private TextArea textArea;
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
    private Button exquteBtn;

    private EntityManagerFactory emf;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.emf = Persistence.createEntityManagerFactory("StudentsSystemINJPAPU");
        
        id.setCellValueFactory(new PropertyValueFactory("id"));
        name.setCellValueFactory(new PropertyValueFactory("name"));
        grade.setCellValueFactory(new PropertyValueFactory("grade"));
        major.setCellValueFactory(new PropertyValueFactory("major"));
    }

    @FXML
    private void exqute(ActionEvent event) {
        EntityManager em = this.emf.createEntityManager();
        if (!textArea.getText().isEmpty()) {
            String stat = textArea.getText();
            if (stat.toLowerCase().contains("update") || stat.toLowerCase().contains("insert")
                    || stat.toLowerCase().contains("delete")) {
                em.createQuery(stat).executeUpdate();
                em.getTransaction().commit(); // to make the changes in the database
                JOptionPane.showMessageDialog(null, "Success Query");
            } else if (stat.toLowerCase().contains("select")) {
                studentList = em.createQuery(stat).getResultList();
                StudentTable.getItems().setAll(studentList);
                JOptionPane.showMessageDialog(null, "Success Query and Show The result in Table View");
            } else {
                JOptionPane.showMessageDialog(null, "You Should Enter a Correct Query", "Erro Messge", JOptionPane.ERROR_MESSAGE);
            }
            if (StudentTable.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No Result");
            }
        } else {
            JOptionPane.showMessageDialog(null, "You Should Enter a Query", "Error Messge", JOptionPane.ERROR_MESSAGE);

        }
    }


}
