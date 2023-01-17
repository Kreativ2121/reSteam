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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public static String token;

    //Database variables
    public static Connection connection;
    static SelectedGame selection;

    //Boolean for login error message
    public static boolean wasLoginFailed = false;

    //AWS Cognito variables
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

        selection = new SelectedGame();

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

        //Połączenie z DB
        connection = DriverManager.getConnection(
                "jdbc:mariadb://resteam-db2.cysgqma8u6h9.eu-central-1.rds.amazonaws.com:3306/resteam",
                "resteam", "H54z1$9uP$H"
        );

//        try (PreparedStatement statement = connection.prepareStatement("""
//            SELECT `desc_snippet`,`url`,`game_description`,`game_details`,`name`, `minimum_requirements`,\s
//            `original_price`, `discount_price`, `publisher`, `recommended_requirements`, `release_date`\s
//            FROM resteam.steam_games WHERE `name` = "DOOM";
//        """)) {
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                selection.setDesc_snippet(resultSet.getString(1));
//                selection.setDownload(resultSet.getString(2));
//                selection.setGame_description(resultSet.getString(3));
//                selection.setGame_details(resultSet.getString(4));
//                selection.setGame_name(resultSet.getString(5));
//                selection.setMinimum_requirements(resultSet.getString(6));
//                selection.setOriginal_price(resultSet.getString(7));
//                selection.setDiscount_price(resultSet.getString(8));
//                selection.setDeveloper(resultSet.getString(9));
//                selection.setRecommended_requirements(resultSet.getString(10));
//                selection.setRelease_date(resultSet.getString(11));
//            }
//        }

        System.out.println("Finished startup...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
