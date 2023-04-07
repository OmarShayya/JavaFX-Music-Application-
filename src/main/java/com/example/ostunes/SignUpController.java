package com.example.ostunes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_email;
    @FXML
    private PasswordField pf_password;
    @FXML
    private PasswordField pf_confirm_password;
    @FXML
    private Button sign_up_btn;
    @FXML
    private Button login_btn;
    @FXML
    private CheckBox cb_premium;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign_up_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tf_username.getText().trim().isEmpty() &&
                   !tf_email.getText().trim().isEmpty() &&
                   !pf_password.getText().trim().isEmpty() &&
                   !pf_confirm_password.getText().isEmpty())
                {
                    DBUtils.signUP(event, tf_username.getText(), tf_email.getText(), pf_password.getText(), pf_confirm_password.getText(), cb_premium.isSelected());
                } else{
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "Login.fxml", "Login", 800, 500, -1);
            }
        });
    }
}
