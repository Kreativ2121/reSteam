package com.resteam;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class SelectedGame {
    private String desc_snippet;
    private String download;
    private String game_description;
    private String game_details;
    private String game_name;
    private String minimum_requirements;
    private String original_price;
    private String discount_price;
    private String developer;
    private String recommended_requirements;
    private String release_date;

    public String getDesc_snippet() {
        return desc_snippet;
    }

    public void setDesc_snippet(String desc_snippet) {
        this.desc_snippet = desc_snippet;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getGame_description() {
        return game_description;
    }

    public void setGame_description(String game_description) {
        this.game_description = game_description;
    }

    public String getGame_details() {
        return game_details;
    }

    public void setGame_details(String game_details) {
        this.game_details = game_details;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getMinimum_requirements() {
        return minimum_requirements;
    }

    public void setMinimum_requirements(String minimum_requirements) {
        this.minimum_requirements = minimum_requirements;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getRecommended_requirements() {
        return recommended_requirements;
    }

    public void setRecommended_requirements(String recommended_requirements) {
        this.recommended_requirements = recommended_requirements;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public SelectedGame() {}
}
