package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerMenu {

    @FXML
    private AnchorPane content_area;

    @FXML
    private TableView<Ingredient> tableView;

    @FXML
    private TableColumn<Ingredient, Integer> colId;

    @FXML
    private TableColumn<Ingredient, String> colName;

    @FXML
    private TableColumn<Ingredient, Integer> colAmount;

    @FXML
    private TableColumn<Ingredient, Integer> colCost;

    public static class Ingredient {
        private int id;
        private String name;
        private int amount;
        private int cost;

        public Ingredient(int id, String name, int amount, int cost) {
            this.id = id;
            this.name = name;
            this.amount = amount;
            this.cost = cost;
        }

        public Ingredient() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }
    }

    private ObservableList<Ingredient> myData = FXCollections.observableArrayList();

    // инициализируем форму данными
    @FXML
    private void initialize() throws SQLException {
        myData.clear();
        // устанавливаем тип и значение которое должно хранится в колонке
        colId.setCellValueFactory(new PropertyValueFactory<Ingredient, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));
        colAmount.setCellValueFactory(new PropertyValueFactory<Ingredient, Integer>("amount"));
        colCost.setCellValueFactory(new PropertyValueFactory<Ingredient, Integer>("cost"));

        // заполняем таблицу данными
        tableView.setItems(sqlCommands.lookinT(myData));
    }

    @FXML
    private Button createTButton;

    @FXML
    private Button DropTButton;

    @FXML
    private Button SearchButton;

    @FXML
    private Button ClearButton;

    @FXML
    private Button InsertButton;

    @FXML
    private Button UpdateButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private TextField idfield;

    @FXML
    private TextField namefield;

    @FXML
    private TextField amounfield;

    @FXML
    private TextField costfield;

    @FXML
    private Label errorLabel;
    @FXML
    private Button LookButton;


    @FXML
    private Button LogOutB;

    @FXML
    void look(ActionEvent event) throws SQLException {
        initialize();
    }

    @FXML
    void ClearFromT(ActionEvent event) {
        try {
            sqlCommands.clearT();
            initialize();
        } catch (SQLException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    @FXML
    void DeleteFromT(ActionEvent event) {
        try {
            sqlCommands.deleteT(namefield.getText());
            initialize();
        } catch (SQLException e) {
            errorLabel.setText(e.getMessage());
        }


    }


    @FXML
    void InsertInT(ActionEvent event) {
        try {
            sqlCommands.ins(Integer.parseInt(idfield.getText()), namefield.getText(), Integer.parseInt(amounfield.getText()),
                    Integer.parseInt(costfield.getText()));
            initialize();
        } catch (SQLException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    @FXML
    void SearchInT(ActionEvent event) throws SQLException {
        myData.clear();
        tableView.setItems(sqlCommands.searchinT(namefield.getText(), myData));
    }

    @FXML
    void UpdateT(ActionEvent event) {
        try {
            sqlCommands.updateT(Integer.parseInt(idfield.getText()), namefield.getText(), Integer.parseInt(amounfield.getText()),
                    Integer.parseInt(costfield.getText()));
            initialize();
        } catch (SQLException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    @FXML
    void LogOut(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Authorization");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
