package sample;

        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.PasswordField;
        import javafx.scene.control.TextField;
        import javafx.scene.text.Text;
        import javafx.stage.Stage;
        import javafx.stage.WindowEvent;

        import java.sql.Connection;
        import java.sql.SQLException;

public class ControllerAutorization {


    @FXML
    private TextField LoginField;


    @FXML
    private Button enterButton;

    @FXML
    private Text AuthText;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField DBName;

    @FXML
    private Button createBD;


    @FXML
    void createDB(ActionEvent event) throws Exception {
        Connection conn1 = sqlCommands.getConnected(LoginField.getText(), PasswordField.getText(), "postgres");
        try {
            sqlCommands.createDB(DBName.getText(), LoginField.getText(), PasswordField.getText());
        } catch (Exception e) {
            AuthText.setText("У вас нет прав на создание базы данных");
        }
        conn1.close();
        Connection conn = sqlCommands.getConnected(LoginField.getText(), PasswordField.getText(), DBName.getText());
        sqlCommands.createTb(conn);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("Menu");
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();

            stage.setOnCloseRequest( closeEvent ->
            {
                try {
                    sqlCommands.conn1.close();
                    System.out.println("Соединение прекращено");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void dropDB(ActionEvent event) throws Exception {
        Connection conn1 = sqlCommands.getConnected(LoginField.getText(), PasswordField.getText(), "postgres");
        try {
            sqlCommands.dropDB(DBName.getText(), LoginField.getText(), PasswordField.getText());
        } catch (SQLException e) {
            AuthText.setText("У вас нет прав на удаление базы данных");
        }
    }


    @FXML
    void authorize(ActionEvent event) throws Exception {
        sqlCommands.getConnected(LoginField.getText(), PasswordField.getText(), DBName.getText());
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.setTitle("Menu");
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();

                stage.setOnCloseRequest( closeEvent ->
                {
                    try {
                        sqlCommands.conn1.close();
                        System.out.println("Соединение прекращено");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } catch(Exception e) {
                e.printStackTrace();
            }

    }
}
