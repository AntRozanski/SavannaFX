package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import model.EditorModel;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			SplitPane root = new SplitPane();

			root = (SplitPane) FXMLLoader.load(Main.class.getResource("/view/MainPane.fxml"));

			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Savanna Simulator v1.1");
			primaryStage.show();
			EditorModel.getInstance().sayHello();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
