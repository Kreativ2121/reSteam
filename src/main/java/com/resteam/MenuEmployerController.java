package com.resteam;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class MenuEmployerController {

    @FXML
    private Label NameLabel;

    public void PotentialEmployees() {
        try {
            Main.root = FXMLLoader.load(getClass().getResource("../../../resources/potential_employees.fxml"));

            Main.stage.close();
            Main.stage.setTitle("Emplomatic");
            Main.stage.setScene(new Scene(Main.root, 1280, 800));
            Main.stage.show();
        } catch (Exception e) {
            System.err.println("Cannot logout.");
        }
    }

    public void Logout() {
        try {
            Main.root = FXMLLoader.load(getClass().getResource("../../../resources/loginpage.fxml"));

            Main.stage.close();
            Main.stage.setTitle("Emplomatic");
            Main.stage.setScene(new Scene(Main.root, 1280, 800));
            Main.stage.show();
        } catch (Exception e) {
            System.err.println("Cannot logout.");
        }
    }

    @FXML
    void initialize(){
        //System.out.println("Name: ");
        NameLabel.setText(Main.loggedEmployer.getCompanyName() + "!");
    }
}
