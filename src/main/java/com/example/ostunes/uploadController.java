package com.example.ostunes;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class uploadController implements Initializable {
    @FXML
    private ImageView logout_btn, explore, library;
    @FXML
    private Button chooseSong, choosePic, uploadSong;
    @FXML
    private Label songLabel, picLabel;
    @FXML
    private TextField songName, artistName;
    private File selectedFileSong;
    private File selectedFilePic;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logout_btn.setOnMouseClicked((EventHandler<Event>) event -> {
            DBUtils.changeScene(event, "Login.fxml", "Login", 800, 500, -1);

        });

        explore.setOnMouseClicked((EventHandler<Event>) event -> {
            DBUtils.changeScene(event, "LoggedIn.fxml", "OSTunes", 1260, 720, 1);
        });
        library.setOnMouseClicked(((EventHandler<Event>) event -> {
            DBUtils.changeScene(event, "Library.fxml", "OSTunes", 1260, 720, 1);
        }));

        chooseSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose MP3 File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
                );
                 selectedFileSong = fileChooser.showOpenDialog(null);
                if (selectedFileSong != null) {
                    songLabel.setText(selectedFileSong.getName());
                }
            }
        });

        choosePic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Picture");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg")
                );
                selectedFilePic = fileChooser.showOpenDialog(null);
                if (selectedFilePic != null) {
                    picLabel.setText(selectedFilePic.getName());
                }
            }
        });

        uploadSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!songName.getText().trim().isEmpty() &&
                        !artistName.getText().trim().isEmpty() &&
                        selectedFileSong != null && selectedFilePic != null)
                {
                    DBUtils.uploadSong(event, songName.getText(), artistName.getText(), selectedFilePic, selectedFileSong);
                } else{
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all fields to upload song!");
                    alert.show();
                }
            }
        });


    }
}
