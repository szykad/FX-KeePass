package FX;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;


public class AddController implements Initializable {



    @FXML TextField passwordTextfield;
    @FXML TextField sourceTextfield;
    @FXML TextField noteTextfield;
    @FXML TextField loginTextfield;
    @FXML TextField URLTextfield;
    @FXML Button addButton;
    @FXML Button cancelButton;
    @FXML CheckBox generateCheckbox;

    private MainController mainController;


    public static String generatePassword()
    {
        final int lenght = 10;
        final String chars = "!@#$$%^&*(*)_+abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPERSTUWXYZ";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lenght; i++) {

            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelButton.setOnAction(e -> getStage().close());
        addButton.setOnAction(e -> onAddClicked());
        generateCheckbox.selectedProperty().addListener((a, b, isChecked) -> {
            if(isChecked) {
                passwordTextfield.setText(generatePassword());
            }
        });
    }

    public void onAddClicked(){

        if(sourceTextfield.getText().trim().length() == 0 || passwordTextfield.getText().trim().length() == 0 ||  noteTextfield.getText().trim().length() ==0 ||  loginTextfield.getText().trim().length() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Proszę wypełnić wszystkie pola");
            alert.show();
        }
        else
        {
            mainController.getTableList().add(new ObjectPassword(passwordTextfield.getText(), sourceTextfield.getText(), noteTextfield.getText(), loginTextfield.getText(), URLTextfield.getText()));
            getStage().close();
        }

    }

    public Stage getStage(){
        return (Stage)passwordTextfield.getScene().getWindow();
    }


    public void setMainController(MainController controller) {
        this.mainController = controller;
    }
}
