package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.EditorModel;
import view.Field;

public class MainPaneController implements Initializable
{
	private EditorModel editorModel;
	@FXML
	private EditorPaneController editorPaneController;


	@FXML
	private Label testLabel;

	@FXML
	private StackPane leftPane;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		setEditorModel(EditorModel.getInstance());
		// getEditorModel().init();
		leftPane.getChildren().add(createGridPane());
		/*
		 * System.out.println("ok"); testLabel.textProperty()
		 * .bind(editorPaneController.getTerrainBox().getSelectionModel().
		 * selectedItemProperty().asString());
		 * testLabel.textFillProperty().bind(editorPaneController.getColorBox().
		 * valueProperty()); testLabel.fontProperty() .bind(Bindings.when(
		 * editorPaneController.getSizeBox().getSelectionModel().
		 * selectedItemProperty().isEqualTo("Large")) .then(new
		 * Font(30)).otherwise(new Font(20)));
		 */

	}

	public Node createGridPane()
	{
		GridPane board = new GridPane();
		for (int i = 0; i < EditorModel.BOARD_X_SIZE; i++)
		{
			for (int j = 0; j < EditorModel.BOARD_Y_SIZE; j++)
			{
				Field field = new Field(i, j);
				field.terrainProperty().bind((getEditorModel().getMap())[i][j]);

				/*
				 * getEditorModel().getMap()[i][j].addListener((ov, oldVal,
				 * newVal) -> { if (newVal.equals(Terrain.FOREST)) {
				 * field.exitTransition.play(); } });
				 */
				Image im = new Image("file:pic1.png");
				ImageView iv = new ImageView(im);
				iv.setMouseTransparent(true);
				board.add(new StackPane(field), i, j);
			}
		}

		return board;
	}

	public EditorModel getEditorModel()
	{
		return editorModel;
	}

	public void setEditorModel(EditorModel editorModel)
	{
		this.editorModel = editorModel;
	}
}
