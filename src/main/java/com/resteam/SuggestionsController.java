package com.resteam;

import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient;
import software.amazon.awssdk.services.personalizeruntime.model.GetRecommendationsRequest;
import software.amazon.awssdk.services.personalizeruntime.model.GetRecommendationsResponse;
import software.amazon.awssdk.services.personalizeruntime.model.PredictedItem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class SuggestionsController {
    @FXML
    private Label customer_name;
    @FXML
    private Label desc_snippet;
    @FXML
    private Label desc_snippet_tr;
    @FXML
    private Button download;
    @FXML
    private Label game_description;
    @FXML
    private Label game_description_tr;
    @FXML
    private Label game_details;
    @FXML
    private Label game_name;
    @FXML
    public ListView<String> games_list;
    @FXML
    private Label minimum_requirements;
    @FXML
    private Label original_price;
    @FXML
    private Label discount_price;
    @FXML
    private Label developer;
    @FXML
    private Label recommended_requirements;
    @FXML
    private Label release_date;


    @FXML
    void onGameListClicked(MouseEvent event) {
        //System.out.println("Clicked on: " + games_list.getSelectionModel().getSelectedItem());

        try (PreparedStatement statement = Main.connection.prepareStatement("""
            SELECT `desc_snippet`,`url`,`game_description`,`game_details`,`name`, `minimum_requirements`,\s
            `original_price`, `discount_price`, `publisher`, `recommended_requirements`, `release_date`\s
            FROM resteam.steam_games WHERE `name` = ?;""")) {
            statement.setString(1, games_list.getSelectionModel().getSelectedItem());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                //Download data about games
                Main.selection.setDesc_snippet(resultSet.getString(1));
                Main.selection.setDownload(resultSet.getString(2));
                Main.selection.setGame_description(resultSet.getString(3));
                Main.selection.setGame_details(resultSet.getString(4));
                Main.selection.setGame_name(resultSet.getString(5));
                Main.selection.setMinimum_requirements(resultSet.getString(6));
                Main.selection.setOriginal_price(resultSet.getString(7));
                Main.selection.setDiscount_price(resultSet.getString(8));
                Main.selection.setDeveloper(resultSet.getString(9));
                Main.selection.setRecommended_requirements(resultSet.getString(10));
                Main.selection.setRelease_date(resultSet.getString(11));

                //Translate game description snippet
                TranslateTextRequest requestSnippetTranslation = new TranslateTextRequest()
                        .withText(Main.selection.getDesc_snippet())
                        .withSourceLanguageCode("en")
                        .withTargetLanguageCode("pl");

                TranslateTextResult resultSnippetTranslation = Main.translateClient.translateText(requestSnippetTranslation);

                //Translate game description
                TranslateTextRequest requestDescriptionTranslation = new TranslateTextRequest()
                        .withText(Main.selection.getGame_description())
                        .withSourceLanguageCode("en")
                        .withTargetLanguageCode("pl");

                TranslateTextResult resultDescriptionTranslation = Main.translateClient.translateText(requestDescriptionTranslation);
                //DEBUG
                //System.out.println(result.getTranslatedText());
                //-----Comment above lines to disable translate

                //Set data about games in UI
                desc_snippet.setText(Main.selection.getDesc_snippet());
                desc_snippet_tr.setText(resultSnippetTranslation.getTranslatedText());
                //download.setText(Main.selection.());
                game_description.setText(Main.selection.getGame_description());
                game_description_tr.setText(resultDescriptionTranslation.getTranslatedText());
                game_details.setText(Main.selection.getGame_details());
                game_name.setText(Main.selection.getGame_name());
                minimum_requirements.setText(Main.selection.getMinimum_requirements());
                if(!Objects.equals(Main.selection.getOriginal_price(), "")){
                    original_price.setText(Main.selection.getOriginal_price());
                } else {
                    original_price.setText("No data.");
                }
                if(!Objects.equals(Main.selection.getDiscount_price(), "")){
                    discount_price.setText(Main.selection.getDiscount_price());
                }else{
                    discount_price.setText("No data.");
                }
                developer.setText(Main.selection.getDeveloper());
                recommended_requirements.setText(Main.selection.getRecommended_requirements());
                release_date.setText(Main.selection.getRelease_date());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void getRecsIntoList(PersonalizeRuntimeClient personalizeRuntimeClient, String campaignArn, String userId){
        try {
            GetRecommendationsRequest recommendationsRequest = GetRecommendationsRequest.builder()
                    .campaignArn(campaignArn)
                    .numResults(30)
                    .userId(userId)
                    .itemId("ITEM_ID")
                    .build();

            GetRecommendationsResponse recommendationsResponse = personalizeRuntimeClient.getRecommendations(recommendationsRequest);
            List<PredictedItem> items = recommendationsResponse.itemList();
            for (PredictedItem item: items) {
                games_list.getItems().add(item.itemId());
            }
        } catch (AwsServiceException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    @FXML
    void initialize(){
        //Setting initial data
        customer_name.setText(Main.login + "!");

        desc_snippet.setText("");
        game_description.setText("");
        game_details.setText("");
        game_name.setText("");
        minimum_requirements.setText("");
        original_price.setText("");
        discount_price.setText("");
        developer.setText("");
        recommended_requirements.setText("");
        release_date.setText("");

        //Connecting recommendations to the user - mock version
        if(Main.login.equals("Kreativ")){
            Main.steamID="309404240";
        } else if(Main.login.equals("Alicja")){
            Main.steamID="309188905";
        } else {
            Main.steamID="309052991";
        }

        //Get recommendations
        getRecsIntoList(Main.personalizeRuntimeClient,"arn:aws:personalize:eu-west-1:151479359615:campaign/Resteam", "309188905");

        //Mock data. To be replaced by AWS Personalize Recommendations.
//        games_list.getItems().add("DOOM");
//        games_list.getItems().add("PLAYERUNKNOWN'S BATTLEGROUNDS");
//        games_list.getItems().add("BATTLETECH");
//        games_list.getItems().add("Golf It!");
//        games_list.getItems().add("Cities: Skylines Collection");
//        games_list.getItems().add("UNO");
//        games_list.getItems().add("NieR:Automata");
//        games_list.getItems().add("BeamNG.drive");
//        games_list.getItems().add("Pavlov VR");
//        games_list.getItems().add("Wreckfest");
//        games_list.getItems().add("Garry's Mod");
//        games_list.getItems().add("Overcooked! 2");
//        games_list.getItems().add("Black Desert Online");
//        games_list.getItems().add("Dota 2");
//        games_list.getItems().add("Abandon Ship");
//        games_list.getItems().add("rFactor 2");
//        games_list.getItems().add("Rocksmith");
//        games_list.getItems().add("FINAL FANTASY XIII");
//        games_list.getItems().add("911 Operator");
//        games_list.getItems().add("Mass Effect 2 Digital Deluxe Edition");
//        games_list.getItems().add("GRID 2");
//        games_list.getItems().add("Deus Ex: Mankind Divided");
//        games_list.getItems().add("Sword Art Online: Fatal Bullet");
//        games_list.getItems().add("Left 4 Dead");
//        games_list.getItems().add("Dishonored - Definitive Edition");
//        games_list.getItems().add("Grand Theft Auto: San Andreas");
//        games_list.getItems().add("Metro Redux Bundle");
//        games_list.getItems().add("Killing Floor");
//        games_list.getItems().add("Need For Speed: Hot Pursuit");
//        games_list.getItems().add("Serious Sam VR: The Last Hope");
    }

}
