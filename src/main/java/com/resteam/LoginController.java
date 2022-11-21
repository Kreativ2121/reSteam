package com.resteam;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    /**
     * Task to fetch employee details from JSONURL.
     */
    public Task<Controller.User> fetchUser = new Task() {

        @Override
        protected Controller.User call() throws Exception {
            Controller.User user = new Controller.User();

            try {
                Gson gson = new Gson();
                //Main.userType = gson.fromJson(Controller.readUrl(Main.REST + "/LoginInfo/" + Main.login), Controller.UserType.class);

                user = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/general"), Controller.User.class);
            }
            catch (Exception e) {
                System.err.println("Could not parse user data.");
                e.printStackTrace();
            }
            return user;
        }
    };

    /**
     * Task to fetch employer details from JSONURL.
     */
    public Task<Controller.Employer> fetchEmployer = new Task() {

        @Override
        protected Controller.Employer call() throws Exception {
            Controller.Employer employer = new Controller.Employer();

            try {
                Gson gson = new Gson();

                employer = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/employers/" + Main.userType.getId() + "/general"), Controller.Employer.class);
            }
            catch (Exception e) {
                System.err.println("Could not parse employer data.");
                e.printStackTrace();
            }
            return employer;
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
                //Parsing data from textFields
                Main.login=loginField.getText();
                Main.password=passwordField.getText();

                System.out.println("Login: " + Main.login);
                System.out.println("Password: " + Main.password);

                //Checking if a user is registered in a database
                Gson gson = new Gson();
                try {
                    Main.userType = gson.fromJson(Controller.readUrl(Main.REST + "/UserType/" + Main.login), Controller.UserType.class);
                }
                catch (JsonSyntaxException e)
                {
                    return null;
                }

                String tempToken;
                if (Main.userType.getType().equals("Employer")) {
                    tempToken = Controller.readUrlConnection("https://auth-server-emplomatic.herokuapp.com/Employer/?Login=" + Main.login + "&Password=" + Main.password);
                }
                else {
                    tempToken = Controller.readUrlConnection("https://auth-server-emplomatic.herokuapp.com/User/?Login=" + Main.login + "&Password=" + Main.password);
                }
                tempToken = StringUtils.remove(tempToken, '"');
                System.out.println("Token: " + tempToken);

                return tempToken;
            }
            catch (Exception e) {
                System.err.println("Could not authenticate user.");
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
                            System.out.println("User/Employer authenticated successfully.");

                            Thread th;
                            if (Main.userType.getType().equals("Employer")) {
                                th = new Thread(fetchEmployer);
                            }
                            else {
                                th = new Thread(fetchUser);
                            }
                            th.setDaemon(true);
                            th.start();
                        }
                        else
                        {
                            //Handling wrong password.
                            //TODO Make a proper reaction for wrong login credentials.
                            System.err.println("Wrong login credentials. Please try again.");
                            Main.root = FXMLLoader.load(getClass().getResource("../../../resources/loginpage.fxml"));
                            Main.stage.close();
                            Main.stage.setTitle("Emplomatic");
                            Main.stage.setScene(new Scene(Main.root, 1280, 800));
                            Main.stage.show();
                        }
                    } catch (Exception e) {
                        System.err.println("Database access denied.");
                        e.printStackTrace();
                    }
                }
            });

            authenticateUser.setOnRunning(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.out.println("Trying to authenticate user/employer.");
                }
            });

            authenticateUser.setOnFailed(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.err.println("Cannot authenticate user.");
                }
            });

            /** fetchUser task functions **/
            fetchUser.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
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


                        Main.root = FXMLLoader.load(getClass().getResource("../../../resources/menu_employee.fxml"));
                        Main.stage.close();
                        Main.stage.setTitle("Emplomatic");
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
                    System.err.println("Unable to connect to Emplomatic database.");
                }
            });

            /** fetchEmployer task functions **/
            fetchEmployer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    //Fetching user data from REST after successful authentication
                    System.out.println("Finished fetching employer data from REST server.");
                    try {
                        Main.loggedEmployer = fetchEmployer.getValue();

                        //DEBUG UserType
                        System.out.println("User-ID: " + Main.userType.getId());
                        System.out.println("UserType: " + Main.userType.getType());
                        //DEBUG Employer
                        System.out.println("ID: " + Main.loggedEmployer.getId());
                        System.out.println("Company Name: " + Main.loggedEmployer.getCompanyName());
                        System.out.println("Phone: " + Main.loggedEmployer.getCorrespondingNumber());
                        System.out.println("E-mail: " + Main.loggedEmployer.getEmail());


                        Main.root = FXMLLoader.load(getClass().getResource("../../../resources/menu_employer.fxml"));
                        Main.stage.close();
                        Main.stage.setTitle("Emplomatic");
                        Main.stage.setScene(new Scene(Main.root, 1280, 800));
                        Main.stage.show();

                    } catch (Exception e) {
                        System.err.println("There was a problem with parsing data from REST server.");
                        e.printStackTrace();
                    }
                }
            });

            fetchEmployer.setOnRunning(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.out.println("Fetching employer data from REST server.");
                }
            });
            fetchEmployer.setOnFailed(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent t) {
                    System.err.println("Unable to connect to Emplomatic database.");
                }
            });

        }
        catch (Exception e){
            System.err.println("Could not send data to authentication server.");
        }
    }
}

