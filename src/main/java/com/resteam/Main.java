package com.resteam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClientBuilder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.util.Objects;

import static com.resteam.CognitoLogon.*;
import static software.amazon.awssdk.services.cognitoidentityprovider.model.UserStatusType.FORCE_CHANGE_PASSWORD;

public class Main extends Application {
    public static Parent root;
    public static Stage stage;
    public static Controller.User loggedUser;
    public static Controller.UserType userType = new Controller.UserType();
    public static String login;
    public static String password;
    public static final String REST = "http://emplomaticrest-env.eba-52wvzx9s.us-east-1.elasticbeanstalk.com/";
    public static String token;

    public CognitoIdentityClient cognitoClient;
    public static CognitoIdentityProviderClient cognitoProviderClient;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Loading the login stage
        stage = primaryStage;
        //root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("D:\\Repozytorium\\Frontend Java Project\\src\\main\\resources\\loginpage.fxml")));
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("loginpage.fxml")));
        stage.setTitle("reSteam");

        //Setting icon
        //Image icon = new Image(getClass().getResourceAsStream("../resources/logo_small.png"));
        stage.getIcons().add(new Image("reSteam_icon.png"));

        //Launching login stage
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();

        //Launching Cognito Identity Client
         cognitoClient = CognitoIdentityClient.builder()
                .region(Region.EU_WEST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        cognitoProviderClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_WEST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();


        //Testy Cognito
//        listAllUserPools(cognitoProviderClient);
//
//        listAllUsers(cognitoProviderClient,"eu-west-1_gAc2ZYDy2");
//
//        getAdminUser(cognitoProviderClient,"kowalski","eu-west-1_gAc2ZYDy2");
//
//        //initiateAuth(cognitoProviderClient,"719ljiqmgmrna7aoldjnuqo71v","kowalski","Nowehaslo123@");
//        initiateAuth(cognitoProviderClient,"719ljiqmgmrna7aoldjnuqo71v","kowalski","errorhaselko");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
