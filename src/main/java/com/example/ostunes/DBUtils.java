package com.example.ostunes;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class DBUtils {

    static final String DB_URL = "jdbc:mysql://localhost:3306/ostunes";
    static final String USER = "root";
    static final String PASS = "root";

    static int ID;

    public static void changeScene(Event event, String fmxlFile, String title, int width, int height, int isPremium) {
        Parent root = null;
        if(isPremium != -1){
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fmxlFile));
                root = loader.load();

                System.out.println(loader.getController().getClass().toString());
                if (loader.getController().getClass().toString().equals("class com.example.ostunes.LoggedInController")) {
                    LoggedInController loggedInController = loader.getController();
                    loggedInController.setUserInfo(isPremium);

                }
                else if (loader.getController().getClass().toString().equals("class com.example.ostunes.LibraryController")) {
                    LibraryController libraryController = loader.getController();
                    libraryController.setUserInfo(isPremium);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fmxlFile));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Image icon = new Image(DBUtils.class.getResourceAsStream("images/OSTunes.png"));
        stage.getIcons().add(icon);
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        if(isPremium == 0) {
            System.out.println("here");
            Pane pane = (Pane) stage.getScene().lookup("#uploadPane");
            pane.setVisible(false);
            pane.setDisable(true);
        }
        stage.setX((Screen.getPrimary().getVisualBounds().getWidth() - stage.getWidth()) / 2);
        stage.setY((Screen.getPrimary().getVisualBounds().getHeight() - stage.getHeight()) / 2);
        stage.setResizable(false);
        stage.show();
    }




    public static void signUP(ActionEvent event, String username, String email, String pass, String confPass, boolean isPremium){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        PreparedStatement getUserId = null;
        ResultSet resultSet = null;
        ResultSet userID = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            psCheckUserExists.setString(1, email);
            resultSet = psCheckUserExists.executeQuery();

            // check if resultSet is empty
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Email is already registered");
                alert.show();
            } else {
                if (pass.equals(confPass)) {
                    String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());
                    psInsert = connection.prepareStatement("INSERT INTO user (user_name, email, password, is_premium) VALUES (?, ?, ?, ?)");
                    psInsert.setString(1, username);
                    psInsert.setString(2, email);
                    psInsert.setString(3, hashedPassword);
                    psInsert.setBoolean(4, isPremium); // use setBoolean method to set the value
                    psInsert.executeUpdate();
                    if(isPremium)
                        changeScene(event, "LoggedIn.fxml", "OSTunes", 1260, 720, 1);
                    else
                        changeScene(event, "LoggedIn.fxml", "OSTunes", 1260, 720, 0);

                    getUserId = connection.prepareStatement("SELECT ID FROM user WHERE email = ?");
                    getUserId.setString(1, email);
                    userID = getUserId.executeQuery();

                    while (userID.next()){
                        ID =userID.getInt("ID");
                    }
                }
                else {
                    System.out.println("make sure you entered the password correctly in both fields");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Make sure you entered the password correctly in both fields.\"");
                    alert.show();
                }

            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(userID != null){
                try {
                    userID.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(psCheckUserExists != null){
                try{
                    psCheckUserExists.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(psInsert != null){
                try {
                    psInsert.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void login(ActionEvent event, String email, String pass){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("SELECT ID, password, is_premium FROM user WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("User not Found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User not Found");
                alert.show();
            } else {
                while (resultSet.next()){
                    ID = resultSet.getInt("ID");
                   int isPremium = resultSet.getInt("is_premium");
                    String retrievedPass = resultSet.getString("password");
                    if (BCrypt.checkpw(pass, retrievedPass)) {
                        // Passwords match
                        changeScene(event, "LoggedIn.fxml", "OSTunes", 1260, 720, isPremium);
                    } else {
                        // Passwords do not match
                        System.out.println("Wrong Password");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void uploadSong(ActionEvent event, String songName, String artistName, File picPath, File songPath){
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            preparedStatement = connection.prepareStatement("INSERT INTO songs (song_name, artist_name, song_image, url) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, songName);
            preparedStatement.setString(2, artistName);
            preparedStatement.setString(3, picPath.toString());
            preparedStatement.setString(4, songPath.toString());
            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Action Successful");
            alert.setContentText("Your song was uploaded successfully!");
            alert.showAndWait();

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {

            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean getLikes(int songID){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psDelete = null;
        PreparedStatement psCheckifLiked = null;
        ResultSet resultSet = null;
        boolean deleted = false;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            psCheckifLiked = connection.prepareStatement("select * FROM likes WHERE ID = ? AND song_id = ?" );
            psCheckifLiked.setInt(1, ID);
            psCheckifLiked.setInt(2, songID);
            resultSet = psCheckifLiked.executeQuery();

            if(!resultSet.isBeforeFirst()){
                psInsert = connection.prepareStatement("INSERT INTO likes (ID, song_id) VALUES (?, ?)");
                psInsert.setInt(1, ID);
                psInsert.setInt(2, songID);
                psInsert.executeUpdate();
                deleted = false;
            } else {
                while (resultSet.next()) {
                    psDelete = connection.prepareStatement("DELETE FROM likes WHERE ID = ? AND song_id = ?");
                    psDelete.setInt(1, ID);
                    psDelete.setInt(2, songID);
                    psDelete.executeUpdate();
                    deleted = true;
                }

            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(psCheckifLiked != null){
                try{
                    psCheckifLiked.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(psDelete != null){
                try {
                    psDelete.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(psInsert != null){
                try {
                    psInsert.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }
        return deleted;


    }

    public static ArrayList<Song> getSongInfo(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // DoublyCircularLinkedList<Song> DCList = new <Song>DoublyCircularLinkedList();
        ArrayList<Song> songs = new ArrayList<Song>();

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("SELECT song_name, artist_name, url, song_image, song_id  FROM songs");
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("No songs found.");

            } else {

                while (resultSet.next()){
                    String songName = resultSet.getString("song_name");
                    String artistName = resultSet.getString("artist_name");
                    String url = resultSet.getString("url");
                    String songImage = resultSet.getString("song_image");
                    int songID = resultSet.getInt("song_id");
                    Song song = new Song(songName, artistName, url, songImage, songID);
                        songs.add(song);

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return songs;
    }

    public static ArrayList<Song> viewLikes(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // DoublyCircularLinkedList<Song> DCList = new <Song>DoublyCircularLinkedList();
        ArrayList<Song> songs = new ArrayList<Song>();

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = connection.prepareStatement("SELECT song_name, artist_name, url, song_image, song_id " +
                    "FROM songs WHERE song_id in (SELECT song_id from likes where ID = ?)");
            preparedStatement.setInt(1,ID);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("No songs found.");

            } else {

                while (resultSet.next()){
                    String songName = resultSet.getString("song_name");
                    String artistName = resultSet.getString("artist_name");
                    String url = resultSet.getString("url");
                    String songImage = resultSet.getString("song_image");
                    int songID = resultSet.getInt("song_id");
                    Song song = new Song(songName, artistName, url, songImage, songID);
                    songs.add(song);

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return songs;
        }

}
