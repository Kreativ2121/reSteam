package com.resteam;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class MenuEmployeeController {

    @FXML
    private Label NameLabel;

    public void EditCV() {
        try {
            Main.root = FXMLLoader.load(getClass().getResource("../../../resources/edit_cv.fxml"));

            Main.stage.close();
            Main.stage.setTitle("Emplomatic");
            Main.stage.setScene(new Scene(Main.root, 1280, 800));
            Main.stage.show();
        } catch (Exception e) {
            System.err.println("Cannot open CV edit panel.");
        }
    }

    public void Logout() {
        try {
            Main.root = FXMLLoader.load(getClass().getResource("../../../resources/loginpage.fxml"));

            Main.stage.close();
            Main.stage.setTitle("Emplomatic");
            Main.stage.setScene(new Scene(Main.root, 1280, 800));
            Main.stage.show();

            System.out.println("Logged out successfully.");
        } catch (Exception e) {
            System.err.println("Cannot logout.");
        }
    }

    public MenuEmployeeController(){}

    @FXML
    void initialize(){
        //System.out.println("Name: ");
        NameLabel.setText(Main.loggedUser.getName() + "!");
    }

/*    button.setOnAction(new EventHandler() {
        @Override
        public void handle(ActionEvent actionEvent) {
            //... do something in here.
        }
    });*/
}
