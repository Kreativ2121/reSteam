package com.resteam;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class SuggestionsController {

    @FXML
    private Label customer_name;

    @FXML
    private Label desc_snippet;

    @FXML
    private Button download;

    @FXML
    private Label game_description;

    @FXML
    private Label game_details;

    @FXML
    private Label game_name;

    @FXML
    private ListView<String> games_list;

    @FXML
    private Label minimum_requirements;

    @FXML
    private Label original_price;

    @FXML
    private Label publisher;

    @FXML
    private Label recommended_requirements;

    @FXML
    private Label release_date;

    @FXML
    void initialize(){
        customer_name.setText(Main.login + "!");

        //TODO Mock data. To be replaced by AWS Personalize Recommendations.
        games_list.getItems().add("DOOM");
        games_list.getItems().add("PLAYERUNKNOWN'S BATTLEGROUNDS");
        games_list.getItems().add("BATTLETECH");
        games_list.getItems().add("Golf It!");
        games_list.getItems().add("Cities: Skylines Collection");
        games_list.getItems().add("UNO");
        games_list.getItems().add("NieR:Automata™");
        games_list.getItems().add("BeamNG.drive");
        games_list.getItems().add("Pavlov VR");
        games_list.getItems().add("Wreckfest");
        games_list.getItems().add("Garry's Mod");
        games_list.getItems().add("Overcooked! 2");
        games_list.getItems().add("Black Desert Online");
        games_list.getItems().add("Dota 2");
        games_list.getItems().add("Abandon Ship");
        games_list.getItems().add("rFactor 2");
        games_list.getItems().add("Rocksmith™");
        games_list.getItems().add("FINAL FANTASY® XIII");
        games_list.getItems().add("911 Operator");
        games_list.getItems().add("Mass Effect 2 Digital Deluxe Edition");
        games_list.getItems().add("GRID 2");
        games_list.getItems().add("Deus Ex: Mankind Divided");
        games_list.getItems().add("Sword Art Online: Fatal Bullet");
        games_list.getItems().add("Left 4 Dead");
        games_list.getItems().add("Dishonored - Definitive Edition");
        games_list.getItems().add("Grand Theft Auto: San Andreas");
        games_list.getItems().add("Metro Redux Bundle");
        games_list.getItems().add("Killing Floor");
        games_list.getItems().add("Need For Speed: Hot Pursuit");
        games_list.getItems().add("Serious Sam VR: The Last Hope");
    }

}
