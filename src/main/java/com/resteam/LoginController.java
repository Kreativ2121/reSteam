package com.resteam;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

import java.util.Objects;

import static com.resteam.CognitoLogon.getCredsForIdentity;
import static com.resteam.CognitoLogon.initiateAuth;

public class LoginController {

    @FXML
    private Label errorField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /**
     * Task to fetch employee details from JSONURL.
     */
    public Task<Controller.User> fetchUser = new Task() {

        @Override
        protected Controller.User call() throws Exception {
            Controller.User user = new Controller.User();

            //TODO FETCH AND SHOW USER DATA

            return user;
        }
    };

    /**
     * Task to authenticate user in database.
     * @param <V>
     */
    public Task<String> authenticateUser = new Task() {

        @Override
        protected String call() throws Exception {
            try {
                Main.login=usernameField.getText();
                Main.password=passwordField.getText();

                System.out.println("Login: " + Main.login);
                System.out.println("Password: " + Main.password);

                //Authentication
                InitiateAuthResponse response = initiateAuth(Main.cognitoProviderClient,"719ljiqmgmrna7aoldjnuqo71v",Main.login,Main.password);

                if(response == null){
                    //TODO Ustawić wartość labela na ekranie na "Wrong login credentials."
                    return null;
                }

                if(response.challengeNameAsString() == null)
                {
                    System.out.println("User authenticated successfully.");
                } else {
                    System.out.println("User needs further authentication!");
                    //TODO Further authentication
                }

                String tempToken = "TempNull";

                return tempToken;
            }
            catch (Exception e) {
                System.err.println("Could not authenticate user!");
                e.printStackTrace();
                return null;
            }
        }
    };

    /**
     * Action for clicking "submit" button: authentication and parsing data from REST.
     */
    @FXML
    public void submit(ActionEvent actionEvent) {
        try {
            Thread au = new Thread(authenticateUser);
            au.setDaemon(true);
            au.start();

            /** authenticateUser task functions **/
            authenticateUser.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    try {
                        Main.token=authenticateUser.getValue();

                        //Handling proper login credentials
                        if(Main.token!=null && !(Main.token.isEmpty())) {
                            //TODO Przerzucić to do thread!
                            Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("suggestions.fxml")));
                            Main.wasLoginFailed = false;
//                            Thread th;
//                            if (Main.userType.getType().equals("Employer")) {
//                                th = new Thread(fetchEmployer);
//                            }
//                            else {
//                                th = new Thread(fetchUser);
//                            }
//                            th.setDaemon(true);
//                            th.start();
                        }
                        else
                        {
                            //Handling wrong password.
                            //System.err.println("Wrong login credentials. Please try again.");
                            Main.wasLoginFailed = true;
                            Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("loginpage.fxml")));
                            //Platform.runLater(() -> errorField.setText("Wrong login credentials."));
                        }
                        Main.stage.close();
                        Main.stage.setTitle("reSteam");
                        Main.stage.getIcons().add(new Image("reSteam_icon.png"));
                        Main.stage.setScene(new Scene(Main.root, 1280, 720));
                        Main.stage.show();

                    } catch (Exception e) {
                        System.err.println("Access denied.");
                        e.printStackTrace();
                    }
                }
            });

            authenticateUser.setOnRunning(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.out.println("Trying to authenticate user...");
                }
            });

            authenticateUser.setOnFailed(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.err.println("Cannot authenticate user!");
                    //errorField.setText("Wrong login credentials.");
                }
            });

            /** fetchUser task functions **/
            fetchUser.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    //TODO LEGACY CODE FOR WHEN TWO TYPES OF USERS EXIST -> DELETE IF NOT NEEDED
                    //Fetching user data from REST after successful authentication
                    System.out.println("Finished fetching user data from REST server.");
                    try {
                        Main.loggedUser = fetchUser.getValue();

                        //DEBUG UserType
                        System.out.println("User-ID: " + Main.userType.getId());
                        System.out.println("UserType: " + Main.userType.getType());
                        //DEBUG User
                        System.out.println("ID: " + Main.loggedUser.getId());
                        System.out.println("Name: " + Main.loggedUser.getName());
                        System.out.println("Phone: " + Main.loggedUser.getContactNumber());
                        System.out.println("E-mail: " + Main.loggedUser.getEmail());


                        Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../../resources/suggestions.fxml")));
                        Main.stage.close();
                        Main.stage.setTitle("reSteam");
                        Main.stage.setScene(new Scene(Main.root, 1280, 800));
                        Main.stage.show();

                    } catch (Exception e) {
                        System.err.println("There was a problem with parsing data from REST server.");
                        e.printStackTrace();
                    }
                }
            });

            fetchUser.setOnRunning(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.out.println("Fetching user data from REST server.");
                }
            });
            fetchUser.setOnFailed(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.err.println("Unable to connect to reSteam database!");
                }
            });
        }
        catch (Exception e){
            System.err.println("Could not send data to authentication server!");
        }
    }

    @FXML
    void initialize(){
        if(Main.wasLoginFailed){
            errorField.setText("Wrong login credentials.");
        } else {
            errorField.setText("");
        }
    }

}

