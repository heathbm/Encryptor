package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			 Stage stage; 
		     Parent root;
		     root = FXMLLoader.load(getClass().getResource("FXML_File.fxml"));
		     Scene scene = new Scene(root, 1000, 1000);
		     primaryStage.setScene(scene);
			 primaryStage.setResizable(false);
		     primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
