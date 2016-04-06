package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import model.EditorModel;
import model.Size;
import model.Terrain;

public class EditorPaneController implements Initializable
{

	@FXML
	private ChoiceBox<String> terrainBox;

	@FXML
	private ChoiceBox<String> sizeBox;

	@FXML
	private ColorPicker colorBox;

	@FXML
	private Button readyButton;
	@FXML
	private Button clearButton;
	@FXML
	private CheckBox animationCheckBox;

	public CheckBox getAnimationCheckBox()
	{
		return animationCheckBox;
	}

	public void setAnimationCheckBox(CheckBox animationCheckBox)
	{
		this.animationCheckBox = animationCheckBox;
	}

	public ChoiceBox<String> getTerrainBox()
	{
		return terrainBox;
	}

	public void setTerrainBox(ChoiceBox<String> terrainBox)
	{
		this.terrainBox = terrainBox;
	}

	public ChoiceBox<String> getSizeBox()
	{
		return sizeBox;
	}

	public void setSizeBox(ChoiceBox<String> sizeBox)
	{
		this.sizeBox = sizeBox;
	}

	public ColorPicker getColorBox()
	{
		return colorBox;
	}

	public void setColorBox(ColorPicker colorBox)
	{
		this.colorBox = colorBox;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		EditorModel em = EditorModel.getInstance();
		em.terrainProperty().bind(getTerrainBox().getSelectionModel().selectedItemProperty());
		em.sizeProperty().bind(getSizeBox().getSelectionModel().selectedItemProperty());
		em.animationProperty().bind(getAnimationCheckBox().selectedProperty());

	}

	@FXML
	private void clearMap()
	{
		EditorModel.getInstance().clearMap();
	}

	@FXML
	private void readyToGo()
	{
		EditorModel.getInstance().goToNextWindow();
	}

}
