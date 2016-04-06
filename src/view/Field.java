package view;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import model.EditorModel;
import model.Terrain;

public class Field extends Region
{

	private final ObjectProperty<Terrain> terrainProperty =
			new SimpleObjectProperty<>(this, "terrain", Terrain.NONE);
	private final EditorModel model = EditorModel.getInstance();
	private final Region highlight;

	public final FadeTransition highlightTransition;// TODO: create setters!
	public final FadeTransition exitTransition;// TODO: create setters!

	public ObjectProperty<Terrain> terrainProperty()
	{
		return terrainProperty;
	}

	public Terrain getTerrain()
	{
		return terrainProperty.get();
	}

	public void setTerrain(Terrain terrain)
	{
		terrainProperty.set(terrain);
	}

	public Field(final int x, final int y)
	{
		highlight = new Region();
		highlight.setOpacity(0);
		highlight.setStyle(" -fx-border-width: 3; -fx-border-color: red");

		highlightTransition = new FadeTransition(Duration.millis(10), highlight);
		highlightTransition.setFromValue(0);
		highlightTransition.setToValue(1);

		exitTransition = new FadeTransition(Duration.millis(10), highlight);
		exitTransition.setFromValue(1);
		exitTransition.setToValue(0);

		styleProperty().bind(
				Bindings.when(terrainProperty().isEqualTo(Terrain.FOREST)).then("-fx-background-color: green")
						.otherwise(Bindings.when(terrainProperty().isEqualTo(Terrain.RIVER))
								.then("-fx-background-color: dodgerblue")
								.otherwise("-fx-background-color: burlywood")));
		/*
		 * terrainProperty().addListener((ov, oldVal, newVal) -> { try { if
		 * (newVal.equals(Terrain.FOREST)) { //exitTransition.play(); } } catch
		 * (Exception e) { System.out.println("lol"); e.printStackTrace(); } });
		 */

		Light.Distant light = new Light.Distant();
		light.setAzimuth(135);
		light.setElevation(38);
		setEffect(new Lighting(light));

		setPrefSize(400, 400);
		getChildren().add(highlight);

		/*
		 * addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, t -> { // if
		 * (model.legalMove(x, y).get()) { highlightTransition.setRate(5);
		 * highlightTransition.play(); // } });
		 * addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, t -> {
		 * highlightTransition.setRate(-2); highlightTransition.play(); });
		 */
		setOnMouseClicked(me ->
		{
			if (me.getButton() == MouseButton.PRIMARY)
				model.createTerrain(x, y);
			else if (me.getButton() == MouseButton.SECONDARY)
				model.deleteTerrain(x, y);// highlightTransition.setRate(-1);
											// highlightTransition.play();

		});
		setOnMouseEntered(e -> highlightTransition.play());
		setOnMouseExited(e -> exitTransition.play());
	}

	@Override
	protected void layoutChildren()
	{
		layoutInArea(highlight, 0, 0, getWidth(), getHeight(), getBaselineOffset(), HPos.CENTER, VPos.CENTER);
	}

}
