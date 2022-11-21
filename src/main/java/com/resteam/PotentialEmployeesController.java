package com.resteam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TablePosition;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import java.io.IOException;
import java.util.List;

public class PotentialEmployeesController {

    @FXML
    javafx.scene.control.TableView<OfferTable> offerTable;

    @FXML
    javafx.scene.control.TableView<SuggestedEmployeeTable> suggestedEmployeesTable;

    @FXML
    javafx.scene.control.TableColumn<OfferTable, String> offerColumn;

    @FXML
    javafx.scene.control.TableColumn<SuggestedEmployeeTable, String> nameColumn;

    @FXML
    javafx.scene.control.TableColumn<SuggestedEmployeeTable, String> photoColumn;

    @FXML
    javafx.scene.control.TableColumn<SuggestedEmployeeTable, String> degreeColumn;

    @FXML
    javafx.scene.control.TableColumn<SuggestedEmployeeTable, String> ageColumn;

    @FXML
    javafx.scene.control.TableColumn<SuggestedEmployeeTable, String> workExperienceColumn;

    @FXML
    javafx.scene.control.TableColumn<SuggestedEmployeeTable, String> cityColumn;

    //TODO Make this class run in parallel!
    public void initialize(){
        List<Controller.Offer> offers = null;
        List<Controller.User> users = null;
        try{
            Gson gson = new Gson();
            offers = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/jobOffers"), new TypeToken<List<Controller.Offer>>(){}.getType());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        offerColumn.setCellValueFactory(new PropertyValueFactory<>("offerRecord"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("png"));
        degreeColumn.setCellValueFactory(new PropertyValueFactory<>("degree"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        workExperienceColumn.setCellValueFactory(new PropertyValueFactory<>("workExperience"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        final ObservableList<OfferTable> dataOffer = FXCollections.observableArrayList();

        assert offers != null;
        for (Controller.Offer offer : offers) {
            dataOffer.add(new OfferTable(offer.getPosition() + " " + offer.getJobReq()));
        }

        offerTable.getItems().setAll(dataOffer);
        //System.out.println(offerTable.getSelectionModel().getSelectedIndex());

        offerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ObservableList<TablePosition> selected = offerTable.getSelectionModel().getSelectedCells();
                OfferTable wybrany = offerTable.getSelectionModel().getSelectedItem();
                //System.out.println(wybrany.getOfferRecord());
                int row = selected.stream().findFirst().get().getRow();
                //System.out.println(row);
                try {
                    Gson gson = new Gson();
                    String url = ("http://emplomaticrest-env.eba-52wvzx9s.us-east-1.elasticbeanstalk.com/users/Search=education:"+wybrany.getOfferRecord()).replace(" ", "%20");
                    String usersIDs = Controller.readUrl(url);
                    usersIDs =usersIDs.replace(" ", "").replace("[","").replace("]","").replace("\n","");
                    String [] usersID = usersIDs.split(",");
                    //System.out.println(usersID[0]);

                    Controller.User [] user = new Controller.User[usersID.length];
                    Controller.CVExperience [] experience = new Controller.CVExperience[usersID.length];
                    Controller.CVInfo[] cvs = new Controller.CVInfo[usersID.length];
                    String[] exp = new String[usersID.length];
                    int [] ages = new int[usersID.length];
                    for(int i = 0; i < usersID.length; i++) {
                        user[i] = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + usersID[i] + "/general"), Controller.User.class);
                        experience[i] = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + usersID[i] + "/experience"), Controller.CVExperience.class);
                        List<Controller.CVInfo> cv = gson.fromJson(Controller.readUrl(Main.REST + Main.token + "/users/" + usersID[i] + "/cv"), new TypeToken<List<Controller.CVInfo>>(){}.getType());
                        DateFormat dateFormat = new SimpleDateFormat("yyyy");
                        cvs[i] = cv.get(0);
                        String date = dateFormat.format(new Date());
                        ages[i] = Integer.parseInt(date) - Integer.parseInt(cvs[i].getBirthDate().substring(12,16));
                        //System.out.println("Wiek: "+ages[i]);
                        if(experience[i] != null) {
                            exp[i] = (experience[i].getCompany() + " " + experience[i].getPosition());
                        }
                        else{
                            exp[i] = "None";
                        }
                    }

                    final ObservableList<SuggestedEmployeeTable> dataSuggestedEmployee = FXCollections.observableArrayList();

                    for(int i = 0; i<usersID.length; i++){
                        dataSuggestedEmployee.add(new SuggestedEmployeeTable(null, user[i].getName(),"None", Integer.toString(ages[i]), exp[i], "None"));
                    }
                    suggestedEmployeesTable.getItems().setAll(dataSuggestedEmployee);
                }
                catch (Exception e){
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    System.out.println("Message: "+e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    public void exit(ActionEvent actionEvent) throws IOException {
        System.out.println(offerTable.getSelectionModel().getSelectedIndex());
        Main.root = FXMLLoader.load(getClass().getResource("../../../resources/menu_employer.fxml"));
        Main.stage.close();
        Main.stage.setTitle("Emplomatic");
        Main.stage.setScene(new Scene(Main.root, 1280, 800));
        Main.stage.show();
    }

    /*Class containing offers connected to a company*/
    public static class OfferTable {
        private String offerRecord;

        public OfferTable(){
        }

        public OfferTable(String offerRecord){
            setOfferRecord(offerRecord);
        }

        public String getOfferRecord(){
            return this.offerRecord;
        }

        public void setOfferRecord(String offerRecord){
            this.offerRecord = offerRecord;
        }
    }
}
