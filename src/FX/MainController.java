package FX;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML TableView<ObjectPassword> passwordTable;
    @FXML TableColumn<ObjectPassword, String> passwordCol;
    @FXML TableColumn<ObjectPassword, String> sourceCol;
    @FXML TableColumn<ObjectPassword, String> noteCol;
    @FXML TableColumn<ObjectPassword, String> loginCol;
    @FXML TableColumn<ObjectPassword, String> URLCol;

    private ObservableList<ObjectPassword> tableList = FXCollections.observableList(new ArrayList<>());



    @FXML
    public void onLoadClicked(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(passwordTable.getScene().getWindow());
        if(selectedFile != null)
        {
            tableList.clear();

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Zakładanie hasła na plik");
            dialog.setHeaderText("Wprowadź hasło");
            dialog.setContentText("Wprowadź hasło do pliku:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                String password = result.get();
                Documents documents = new Documents(selectedFile);
                try {
                    boolean success = documents.decodeFile(password);
                    if(!success)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Niepoprawne hasło");
                        alert.show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Błąd odczytu pliku");
                    alert.show();
                }

                try {
                    List<String> decoded = documents.getDecodedFileContents();
                    for (String line : decoded) {
                        ObjectPassword item = new ObjectPassword(line);
                        tableList.add(item);
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Plik jest uszkodzony");
                    alert.show();
                }
            }
        }
    }




    @FXML
    public void onAddClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(AddController.class.getResource("addpass.fxml"));

        Parent root = loader.load();

        AddController controller = loader.getController();

        controller.setMainController(this);

        Stage stage = new Stage();

        stage.setTitle("Add Password");

        stage.setScene(new Scene(root));

        stage.initModality(Modality.WINDOW_MODAL);

        stage.initOwner(getStage());

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordTable.setPlaceholder(new Label("Wczytaj dane z menu Plik -> Wczytaj"));
        passwordTable.setItems(tableList);
        passwordCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPassword()));
        sourceCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSource()));
        noteCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getNote()));
        loginCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLogin()));
        URLCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getURL()));
    }

    @FXML
    public void onSaveClicked(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(passwordTable.getScene().getWindow());
        if(selectedFile != null)
        {
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Wprowadz hasło");
            dialog.setHeaderText("Nadaj hasło pliku");
            dialog.setContentText("Wprowadź hasło do odczytu:");

            Optional<String> result = dialog.showAndWait();



            if (result.isPresent()) {
                String password = result.get();

                List<String> content = new ArrayList<>();
                for (ObjectPassword objectPassword : getTableList()) {
                    content.add(objectPassword.getSaveFormat());
                }

                try {
                    Documents.saveAndEncode(selectedFile, content, password);
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Wystąpił problem z zapisem");
                    alert.show();
                }
            }
        }
    }

    public ObservableList<ObjectPassword> getTableList() {
        return tableList;
    }

    public Stage getStage(){
        return (Stage)passwordTable.getScene().getWindow();
    }
}
