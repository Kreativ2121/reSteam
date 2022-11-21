package com.resteam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;

public class Main extends Application {
    public static Parent root;
    public static Stage stage;
    public static Controller.User loggedUser;
    public static Controller.Employer loggedEmployer;
    public static Controller.UserType userType = new Controller.UserType();
    public static String login;
    public static String password;
    public static final String REST = "http://emplomaticrest-env.eba-52wvzx9s.us-east-1.elasticbeanstalk.com/";
    //private static final String IMAGE_URL = "https://www.zut.edu.pl/fileadmin/pliki/2015/wynikiszachy/5.jpg";
    //private Image loadImage;
    public static String token;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Loading the login stage
        stage = primaryStage;
        //root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("D:\\Repozytorium\\Frontend Java Project\\src\\main\\resources\\loginpage.fxml")));
        root = FXMLLoader.load(getClass().getClassLoader().getResource("loginpage.fxml"));
        stage.setTitle("Emplomatic");

        //Setting icon
        //Image icon = new Image(getClass().getResourceAsStream("../resources/logo_small.png"));
        stage.getIcons().add(new Image("logo_small_2.png"));

        //Launching login stage
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
