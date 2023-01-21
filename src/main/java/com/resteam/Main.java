package com.resteam;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClientBuilder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import static com.resteam.CognitoLogon.*;
import static com.resteam.PersonalizeRecommendations.getRecs;
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

    public static AmazonTranslate translateClient;

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

        //Setting up game's data for selection on suggestions screen
        selection = new SelectedGame();

        //Launching Cognito Identity Client
         cognitoClient = CognitoIdentityClient.builder()
                .region(Region.EU_WEST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        //Launching Cognito Provider Client
        cognitoProviderClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_WEST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        //Launching Amazon Translate Client
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAXLBUHGXL654LLVPN", "3P+xPGAeoK9ndjRRFl75XLbGIooxAHyyqZP8AJvT");
        translateClient = AmazonTranslateClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.EU_WEST_1)
                .build();

        //Launching Amazon Personalize Client
        AwsBasicCredentials awsCredsPersonalize = AwsBasicCredentials.create("AKIAXLBUHGXL654LLVPN","3P+xPGAeoK9ndjRRFl75XLbGIooxAHyyqZP8AJvT");
        PersonalizeRuntimeClient personalizeRuntimeClient = PersonalizeRuntimeClient.builder()
                .region(Region.EU_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredsPersonalize))
                .build();

        //TODO TESTY PERSONALIZE
        //getRecs(personalizeRuntimeClient,"arn:aws:personalize:eu-central-1:504768181719:campaign/movieLensCampaign", "125");

        //Połączenie z DB
        connection = DriverManager.getConnection(
                "jdbc:mariadb://resteam-db2.cysgqma8u6h9.eu-central-1.rds.amazonaws.com:3306/resteam",
                "resteam", "H54z1$9uP$H"
        );

        System.out.println("Finished startup...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
