package com.resteam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EditCVController {
    @FXML
    private TextField university;

    @FXML
    private TextField fieldOfStudy;

    @FXML
    private TextField dateOfBeginning;

    @FXML
    private TextField dateOfGraduation;

    @FXML
    private TextField additions;

    @FXML
    private TextField language;

    @FXML
    private TextField languageLevel;

    @FXML
    private TextField interest1;

    @FXML
    private TextField interest2;

    @FXML
    private TextField companyName;

    @FXML
    private TextField position;

    @FXML
    private TextField startDate;

    @FXML
    private TextField endDate;

    public void initialize(){
        System.out.println("Initializing CV edit.");
        try {
            Gson gson = new Gson();

            Controller.CVEdu cvedu = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/education"), Controller.CVEdu.class);
            Controller.CVExperience cvexp = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/experience"), Controller.CVExperience.class);
            List<Controller.CVLanguages> cvlang = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/language"), new TypeToken<List<Controller.CVLanguages>>(){}.getType());
            Controller.CVAchievement cvach = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/achievement"), Controller.CVAchievement.class);
            List<Controller.CVInterest> cvinterest = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/interest"), new TypeToken<List<Controller.CVInterest>>(){}.getType());

            university.setText(cvedu.getUniversity());
            fieldOfStudy.setText(cvedu.getFoS());
            dateOfBeginning.setText(cvedu.getDoB());
            dateOfGraduation.setText(cvedu.getDoG());
            additions.setText(cvach.getAchievementName());
            language.setText(cvlang.get(0).getLanguageName());
            languageLevel.setText(cvlang.get(0).getLanguageAdvancement());
            interest1.setText(cvinterest.get(0).getInterestName());
            interest2.setText(cvinterest.get(1).getInterestName());
            companyName.setText(cvexp.getCompany());
            position.setText(cvexp.getPosition());
            startDate.setText(cvexp.getStartDate());
            endDate.setText(cvexp.getEndDate());
            System.out.println("Finished initializing CV edit.");
        } catch (Exception e){
            System.err.println("Cannot fetch a CV from database.");
        }
    }

    /**
     * Task to send CV related data to database.
     */
    public Task<Boolean> sendCVData = new Task() {

        @Override
        protected Boolean call() throws Exception {
        Controller.CVAchievement cvach = new Controller.CVAchievement();
        Controller.CVExperience cvexp = new Controller.CVExperience();
        Controller.CVEdu cvedu = new Controller.CVEdu();

            try {
                Gson gson = new Gson();

                /*Controller.CVInterest cvint1 = new Controller.CVInterest();
                Controller.CVInterest cvint2 = new Controller.CVInterest();
                List<Controller.CVInterest> cvint = null;
                cvint1.setInterestName(interest1.getText());
                cvint2.setInterestName(interest2.getText());
                cvint.add(cvint1);
                cvint.add(cvint2);*/

                cvach.setAchievementName(additions.getText());
                cvexp.setCompany(companyName.getText());
                cvexp.setEndDate(endDate.getText());
                cvexp.setStartDate(startDate.getText());
                cvexp.setPosition(position.getText());
                cvedu.setDoB(dateOfBeginning.getText());
                cvedu.setDoG(dateOfGraduation.getText());
                cvedu.setFoS(fieldOfStudy.getText());
                cvedu.setUniversity(university.getText());

                /* Sending experience data back to server */
                URL url = new URL(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/experience");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("PUT");
                http.setDoOutput(true);

                String jsonInputString = gson.toJson(cvexp);

                //DEBUG
                //System.out.println(jsonInputString);

                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();

                try(OutputStream os = http.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    //Uncomment below line to show server response.
                    //System.out.println(response.toString());
                }


                /* Sending education data back to server */
                url = new URL(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/education");
                con = url.openConnection();
                http = (HttpURLConnection)con;
                http.setRequestMethod("PUT");
                http.setDoOutput(true);

                jsonInputString = gson.toJson(cvedu);

                //DEBUG
                //System.out.println(jsonInputString);

                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();

                try(OutputStream os = http.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    //Uncomment below line to show server response.
                    //System.out.println(response.toString());
                }


                /* Sending interests data back to server */
                /*url = new URL(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/interest");
                con = url.openConnection();
                http = (HttpURLConnection)con;
                http.setRequestMethod("PUT");
                http.setDoOutput(true);

                jsonInputString = gson.toJson(cvint);

                //DEBUG
                //System.out.println(jsonInputString);

                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();

                try(OutputStream os = http.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    //Uncomment below line to show server response.
                    //System.out.println(response.toString());
                }*/

                /* Sending additional data back to server */
                url = new URL(Main.REST + Main.token + "/users/" + Main.userType.getId() + "/achievement");
                con = url.openConnection();
                http = (HttpURLConnection)con;
                http.setRequestMethod("PUT");
                http.setDoOutput(true);

                jsonInputString = gson.toJson(cvach);

                //DEBUG
                //System.out.println(jsonInputString);

                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();

                try(OutputStream os = http.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    //Uncomment below line to show server response.
                    //System.out.println(response.toString());
                }

            }
            catch (Exception e) {
                System.err.println("Could not send data to database.");
                e.printStackTrace();
                return false;
            }
            return true;
        }
    };

    public void save(ActionEvent actionEvent) throws IOException {

        /* Running a thread to send data back to database in parallel. */
        Thread au = new Thread(sendCVData);
        au.setDaemon(true);
        au.start();

        /* sendCVData task functions */
        sendCVData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
          public void handle(WorkerStateEvent t) {
              System.out.println("Finished sending data back to database.");
              try {
                  exit();
              } catch (IOException e) {
                  System.err.println("Couldn't get back to main menu.");
                  e.printStackTrace();
              }
          }
        });

        sendCVData.setOnRunning(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent t) {
                System.out.println("Initializing data transfer to database.");
            }
        });

        sendCVData.setOnFailed(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent t) {
                System.err.println("Could not send data to database.");
            }
        });

    }

    public void exit() throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("../../../resources/menu_employee.fxml"));
        Main.stage.close();
        Main.stage.setTitle("Emplomatic");
        Main.stage.setScene(new Scene(Main.root, 1280, 800));
        Main.stage.show();
    }

}
