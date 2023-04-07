package com.example.ostunes;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private ImageView logout_btn;
    @FXML
    private ImageView library;
    @FXML
    private ImageView trackLogo;
    @FXML
    private ImageView prev;
    @FXML
    private ImageView playpause;
    @FXML
    private ImageView next;
    @FXML
    private ImageView like;
    @FXML
    private ImageView upload;
    @FXML
    private Slider time;
    @FXML
    private Label song_name, artist_name, play_time;
    @FXML
    private Pane bgPaneUpload;

    @FXML
    private ScrollPane sp;
    private MediaPlayer mediaPlayer;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private Media media;
    private static int songNumber;
    private Song beingPlayed;
    private ArrayList<Song> songs;
    private static int isPremium = -1;

    private  static int songID;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logout_btn.setOnMouseClicked((EventHandler<Event>) event -> {
            DBUtils.changeScene(event, "Login.fxml", "Login", 800, 500, -1);
            mediaPlayer.stop();
        });

       upload.setOnMouseClicked((EventHandler<Event>) event -> {
            DBUtils.changeScene(event, "upload.fxml", "OSTunes", 1260, 720, -1);
            mediaPlayer.stop();
        });

        library.setOnMouseClicked((EventHandler<Event>) event -> {
            DBUtils.changeScene(event, "Library.fxml", "OSTunes", 1260, 720, isPremium);
            mediaPlayer.stop();
        });

        like.setOnMouseClicked((EventHandler<Event>) event -> {

            if ( DBUtils.getLikes(songID)){
                like.setImage(new Image("C:\\Users\\96655\\IdeaProjects\\OSTunes\\src\\main\\resources\\com\\example\\ostunes\\images\\notLiked.png"));
            }
            else {
                like.setImage(new Image("C:\\Users\\96655\\IdeaProjects\\OSTunes\\src\\main\\resources\\com\\example\\ostunes\\images\\Liked.png"));
            }

        });


       songs = DBUtils.getSongInfo();

        beingPlayed = songs.get(songNumber);
        songID = beingPlayed.getSongID();
        ArrayList<Song> likes = DBUtils.viewLikes();


        try{
            File source = new File(beingPlayed.getUrl());
            media = new Media(source.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            time.setValue(1);
            song_name.setText(beingPlayed.getSongName());
            File src = new File(beingPlayed.getSongImage());
            trackLogo.setImage(new Image(src.toURL().toString()));
            artist_name.setText((beingPlayed.getArtistName()));
        } catch (Exception e){
            e.printStackTrace();
        }

        Pane songPane = new Pane();
        Label ex = new Label("Explore");
        ex.setTextFill(Color.WHITE);
        ex.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        ex.setLayoutX(20);
        ex.setLayoutY(20);
        songPane.getChildren().add(ex);

        int row = 0;
        int column = 0;
       // ArrayList<Song> thirdList= new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);

//            for (int j = 0; j < likes.size(); j++) {
//                if (likes.get(j).getSongName().equals(songs.get(i).getSongName())) {
//                    System.out.println("true");
//                    like.setImage(new Image("C:\\Users\\96655\\IdeaProjects\\OSTunes\\src\\main\\resources\\com\\example\\ostunes\\images\\Liked.png"));
//                }
//            }
            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10, 5, 0, 0));
            ImageView images = new ImageView(new Image(new File(song.getSongImage()).toURI().toString()));
            images.setFitWidth(200);
            images.setFitHeight(200);
            Label l = new Label(song.getSongName());
            l.setTextFill(Color.WHITE);
            l.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            Label s = new Label(song.getArtistName());
            s.setTextFill(Color.LIGHTGRAY);
            s.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            vbox.getChildren().addAll(images, l, s);
            vbox.setAlignment(Pos.TOP_LEFT);
            vbox.setSpacing(20); // add spacing between each VBox
            vbox.setStyle("-fx-margin-bottom: 30px;");
            vbox.setMaxWidth(210); // fit 5 VBoxes in a row
            vbox.setStyle("-fx-border-width: 1;");
            vbox.setOnMousePressed(e -> vbox.setStyle("-fx-border-color: white; -fx-border-width: 2;"));

            vbox.setOnMouseClicked(e -> {

                beingPlayed = song;
                songID = beingPlayed.getSongID();
                try {
                    File source = new File(beingPlayed.getUrl());
                    media = new Media(source.toURI().toString());
                    mediaPlayer.stop();
                    mediaPlayer = new MediaPlayer(media);
                    time.setValue(1);
                    mediaPlayer.play();
                    updateValues();
                    song_name.setText(beingPlayed.getSongName());
                    File src = new File(beingPlayed.getSongImage());
                    trackLogo.setImage(new Image(src.toURI().toString()));
                    artist_name.setText(beingPlayed.getArtistName());

                    mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                        public void invalidated(Observable ov) {
                            updateValues();
                        }
                    });
                    playpause.setImage(new Image(getClass().getResourceAsStream("images/pause.png")));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            });
            vbox.setOnMouseReleased(e -> vbox.setStyle("-fx-border-width: 1;"));
            songPane.getChildren().add(vbox);
            vbox.setLayoutX(column * 220 + 20);
            vbox.setLayoutY(row * 220 + 100 + (row * 60));

            column++;
            if (column == 5) { // fit 5 VBoxes in a row
                row++;
                column = 0;
            }

        }
        songPane.setPrefSize(1050, (row + 1) * 220 + ex.getHeight() + 60); // fit 5 VBoxes in a row

        Pane background = new Pane();
        background.setPrefSize(1114, 689);
        background.setStyle("-fx-background-color:  #1e1e1e;");
        background.getChildren().add(songPane);
        sp.setContent(background);



        playpause.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
               Status status = mediaPlayer.getStatus();

                if(status == Status.UNKNOWN || status == Status.HALTED){
                    // do nothing
                    return;
                }

                if(status == Status.PAUSED || status == Status.READY || status == Status.STOPPED){
                    if(atEndOfMedia) {
                        if (songNumber < songs.size() - 1) {
                            songNumber++;
                        } else {
                            songNumber = 0;
                        }
                        beingPlayed = songs.get(songNumber);
                        songID = beingPlayed.getSongID();
                        try {
                            File source = new File(beingPlayed.getUrl());
                            media = new Media(source.toURI().toString());
                            mediaPlayer.stop();
                            mediaPlayer = new MediaPlayer(media);
                            time.setValue(1);
                            mediaPlayer.play();
                            updateValues();
                            song_name.setText(beingPlayed.getSongName());
                            File src = new File(beingPlayed.getSongImage());
                            trackLogo.setImage(new Image(src.toURI().toString()));
                            artist_name.setText(beingPlayed.getArtistName());
                            mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                                public void invalidated(Observable ov) {
                                    updateValues();
                                }
                            });
                            playpause.setImage(new Image(getClass().getResourceAsStream("images/pause.png")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    mediaPlayer.play();
                    System.out.println("working");
                    playpause.setImage(new Image(getClass().getResourceAsStream("images/pause.png")));
                } else {
                    mediaPlayer.pause();
                    playpause.setImage(new Image(getClass().getResourceAsStream("images/play.png")));
                }
            }
        });
        next.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (songNumber < songs.size() - 1) {
                    songNumber++;
                } else {
                    songNumber = 0;
                }
                beingPlayed = songs.get(songNumber);
                songID = beingPlayed.getSongID();
                try {
                    File source = new File(beingPlayed.getUrl());
                    media = new Media(source.toURI().toString());
                    mediaPlayer.stop();
                    mediaPlayer = new MediaPlayer(media);
                    time.setValue(1);
                    mediaPlayer.play();
                    updateValues();
                    song_name.setText(beingPlayed.getSongName());
                    File src = new File(beingPlayed.getSongImage());
                    trackLogo.setImage(new Image(src.toURI().toString()));
                    artist_name.setText(beingPlayed.getArtistName());
                    mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                        public void invalidated(Observable ov) {
                            updateValues();
                        }
                    });
                    playpause.setImage(new Image(getClass().getResourceAsStream("images/pause.png")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        prev.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (songNumber > 0) {
                    songNumber--;
                } else {
                    songNumber = songs.size()-1;
                }
                beingPlayed = songs.get(songNumber);
                songID = beingPlayed.getSongID();
                try {
                    File source = new File(beingPlayed.getUrl());
                    media = new Media(source.toURI().toString());
                    mediaPlayer.stop();
                    mediaPlayer = new MediaPlayer(media);
                    time.setValue(1);
                    mediaPlayer.play();
                    updateValues();
                    song_name.setText(beingPlayed.getSongName());
                    File src = new File(beingPlayed.getSongImage());
                    trackLogo.setImage(new Image(src.toURI().toString()));
                    artist_name.setText(beingPlayed.getArtistName());
                    mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                        public void invalidated(Observable ov) {
                            updateValues();
                        }
                    });
                    playpause.setImage(new Image(getClass().getResourceAsStream("images/pause.png")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                updateValues();
            }
         });


        mediaPlayer.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                if(stopRequested){
                    mediaPlayer.pause();
                    stopRequested = false;
                } else {
                    File src = new File("C:\\Users\\96655\\IdeaProjects\\OSTunes\\src\\main\\resources\\com\\example\\ostunes\\images\\pause.png");
                    try {
                        playpause.setImage(new Image(src.toURL().toString()));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        mediaPlayer.setOnPaused(new Runnable() {
            @Override
            public void run() {
                System.out.println("onPaused");
                // here too
                File src = new File("C://Users//96655//IdeaProjects//OSTunes//src//main//resources//com//example//ostunes//images//play.png");
                try {
                    playpause.setImage(new Image(src.toURL().toString()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                duration = mediaPlayer.getMedia().getDuration();
                updateValues();
            }
        });
        mediaPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {

                    if (songNumber < songs.size() - 1) {
                        songNumber++;
                    } else {
                        songNumber = 0;
                    }
                    beingPlayed = songs.get(songNumber);
                    songID = beingPlayed.getSongID();
                    try {
                        File source = new File(beingPlayed.getUrl());
                        media = new Media(source.toURI().toString());
                        mediaPlayer.stop();
                        mediaPlayer = new MediaPlayer(media);
                        time.setValue(1);
                        mediaPlayer.play();
                        updateValues();
                        song_name.setText(beingPlayed.getSongName());
                        File src = new File(beingPlayed.getSongImage());
                        trackLogo.setImage(new Image(src.toURI().toString()));
                        artist_name.setText(beingPlayed.getArtistName());
                        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                            public void invalidated(Observable ov) {
                                updateValues();
                            }
                        });
                        playpause.setImage(new Image(getClass().getResourceAsStream("images/pause.png")));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    mediaPlayer.play();
                }


        });



        time.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(time.isValueChanging()){
                    // multiply duration by time calculated by slider position
                    mediaPlayer.seek(duration.multiply(time.getValue() / 100.0));
                    time.setValue(time.getValue());
                }
            }
        });
    }

    protected void updateValues() {
        if (play_time != null && time != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    duration = mediaPlayer.getMedia().getDuration();
                    String currentTimeString = formatTime(currentTime, duration);
                    play_time.setText(currentTimeString);
                    time.setDisable(duration.isUnknown());
                    if (!time.isDisabled() && duration.greaterThan(Duration.ZERO) && !time.isValueChanging()) {
                        time.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration){
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60*60);
        if(elapsedHours > 0){
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        if(duration.greaterThan(Duration.ZERO)){
                int intDuration = (int) Math.floor(duration.toSeconds());
                int durationHours = intDuration / (60 * 60);
                if(durationHours > 0){
                    intDuration -= durationHours * 60 * 60;
                }

                int durationMinutes = intDuration / 60;
                int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
                if(durationHours > 0){
                    return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds, durationHours, durationMinutes, durationSeconds);
                } else{
                    return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
                }
        } else {
            if(elapsedHours > 0){
                return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",  elapsedMinutes, elapsedSeconds);
            }
        }
    }


    public void setUserInfo(int isPremium){
        System.out.println(isPremium);
        LoggedInController.isPremium = isPremium;
        System.out.println(LoggedInController.isPremium);
    }

}


