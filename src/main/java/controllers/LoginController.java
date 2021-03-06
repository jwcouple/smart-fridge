package controllers;

import project.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends mysqlConnector implements Initializable {

    @FXML private ImageView background;
    @FXML private ImageView icon;
    @FXML private Button buttLogin;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUsername;
    @FXML private Label lblWrongLogin;
    @FXML private Button goRegister;

    private String userDBserver = "readUser";
    private String userkey = "username";
    private String passwordvalue = "password";
    private boolean loginSucceed = false;
    private boolean userExist = false;
    private boolean pwdCorrect = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){}

    @FXML
    void userLogin(ActionEvent event) throws IOException, InterruptedException {
        checkLogin();
    }
    @FXML
    void goRegister(ActionEvent e) throws Exception{
        Main m = new Main();
        m.changeScene("RegisterScene.fxml");
    }

    /* data base connection and comparison */
    public boolean compareUserDB (String username, String password, String serverName) throws IOException {
        JSONArray dbJSON = parseIntoJSONarray(makeGETRequest(serverName));
        //iterate and compare to JSONarray
        try {
            for (int i = 0; i < dbJSON.length(); i++) {
                JSONObject curObject = dbJSON.getJSONObject(i);
                if (username.equals(curObject.getString(userkey))) {
                    userExist = true; //userName incorrect, userExist remains false
                    if (password.equals(curObject.getString(passwordvalue))) {
                        pwdCorrect = true; //password incorrect, pwdCorrect remains false
                        loginSucceed = true;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginSucceed;
    }

    private void checkLogin() throws IOException {
        Main m = new Main();
        compareUserDB(txtUsername.getText(),txtPassword.getText(),userDBserver); //user exist, pwd correct
        if(loginSucceed){
            lblWrongLogin.setText("Login successful");
            m.changeScene("FridgeScene.fxml");
        }
        else if(txtUsername.getText().isEmpty() && txtPassword.getText().isEmpty()) { //no input
            lblWrongLogin.setText("Please enter your data.");
        }
        else if(userExist && !pwdCorrect){ //user exist, password incorrect
            lblWrongLogin.setText("Incorrect password");
        }
        else{
            lblWrongLogin.setText("No user found");//no user data in database
        }
    }
}
