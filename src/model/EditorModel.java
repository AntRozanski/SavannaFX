package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class EditorModel
{
	public static int BOARD_X_SIZE = 30;
	public static int BOARD_Y_SIZE = 22;

	private ObjectProperty<Terrain>[][] map;
	private ObjectProperty<Terrain>[][] tempMap;

	static EditorModel instance = null;
	private ObjectProperty<String> size = new SimpleObjectProperty<String>("Small");
	private ObjectProperty<String> terrain = new SimpleObjectProperty<String>("Forest");
	private SimpleIntegerProperty factor = new SimpleIntegerProperty();
	private SimpleIntegerProperty recCounter = new SimpleIntegerProperty(0);
	private boolean inputDisabled = false;
	private Point rp = new Point();// river building point
	private boolean onObstacle = false;
	private boolean isHorizontal;
	private SimpleBooleanProperty animation = new SimpleBooleanProperty(true);
	private SimpleIntegerProperty minimalSize = new SimpleIntegerProperty();
	private boolean creationOngoing;
	private ArrayList<TerrainArea> terrainsList = new ArrayList<TerrainArea>();
	private ArrayList<Point> tempList = new ArrayList<Point>();

	private Executor e = Executors.newCachedThreadPool();
	private Alert alert = new Alert(AlertType.INFORMATION);

	public SimpleBooleanProperty animationProperty()
	{
		return animation;
	}

	public void setAnimation(boolean animation)
	{
		this.animationProperty().set(animation);
	}

	public boolean getAnimation()
	{
		return animationProperty().get();
	}

	public ObjectProperty<String> terrainProperty()
	{
		return terrain;
	}

	public void setTerrainProperty(ObjectProperty<String> terrain)
	{
		this.terrain = terrain;
	}

	public String getTerrain()
	{
		return terrain.get();
	}

	public void setTerrain(String terr)
	{
		this.terrain.set(terr);
	}

	public String getSize()
	{
		return size.get();
	}

	public void setSize(String size)
	{
		this.size.set(size);
	}

	public ObjectProperty<String> sizeProperty()
	{
		return size;
	}

	public void setSizeProperty(ObjectProperty<String> size)
	{
		this.size = size;
	}

	public SimpleIntegerProperty minimalSizeProperty()
	{
		return minimalSize;
	}

	public void setMinimalSize(int minimalSize)
	{
		minimalSizeProperty().set(minimalSize);
	}

	public int getMinimalSize()
	{
		return minimalSizeProperty().get();
	}

	public SimpleIntegerProperty getRecCounterProperty()
	{
		return recCounter;
	}

	public void setRecCounter(int recC)
	{
		recCounter.set(recC);
	}

	public int getRecCounter()
	{
		return recCounter.get();
	}

	public SimpleIntegerProperty factorProperty()
	{
		return factor;
	}

	public synchronized int getFactor()
	{
		return this.factor.get();
	}

	public void setFactor(int factor)
	{
		this.factor.set(factor);
	}

	public boolean isHorizontal()
	{
		return isHorizontal;
	}

	public void setHorizontal(boolean isHorizontal)
	{
		this.isHorizontal = isHorizontal;
	}

	public boolean isInputDisabled()
	{
		return inputDisabled;
	}

	public void setInputDisabled(boolean inputDisabled)
	{
		this.inputDisabled = inputDisabled;
	}

	public synchronized ObjectProperty<Terrain>[][] getMap()
	{
		return map;
	}

	public void setMap(ObjectProperty<Terrain>[][] map)
	{
		this.map = map;
	}

	public ArrayList<TerrainArea> getTerrainsList()
	{
		return terrainsList;
	}

	public void setTerrainsList(ArrayList<TerrainArea> terrainsList)
	{
		this.terrainsList = terrainsList;
	}

	public ArrayList<Point> getTempList()
	{
		return tempList;
	}

	public void setTempList(ArrayList<Point> tempList)
	{
		this.tempList = tempList;
	}

	public boolean isCreationOngoing()
	{
		return creationOngoing;
	}

	public synchronized void setCreationOngoing(boolean creationOngoing)
	{
		this.creationOngoing = creationOngoing;
	}

	protected EditorModel()
	{
	}

	public static EditorModel getInstance()
	{
		if (instance == null)
		{
			instance = new EditorModel();
			instance.init();

		}
		return instance;
	}

	public void init()// TODO: fix getSize() to NOT be string
	{
		createMap();
		factorProperty().bind(new IntegerBinding()
		{
			{
				super.bind(sizeProperty(), recCounter);
			}

			@Override
			protected int computeValue()
			{

				if (sizeProperty().get().equals("Small"))
					return (60 + 3 * getRecCounter() * getRecCounter());
				else if (sizeProperty().get().equals("Medium"))
					return 25 + 7 * getRecCounter();
				else if (sizeProperty().get().equals("Large"))
					return 6 * getRecCounter();
				else
				{
					System.out.println("Error in factorProperty().bind()");
					return 0;
				}
			}
		});
		minimalSizeProperty().bind(new IntegerBinding()
		{

			{
				super.bind(sizeProperty(), terrainProperty());
			}

			@Override
			protected int computeValue()
			{
				if (terrainProperty().get().equals("Forest"))
				{
					if (sizeProperty().get().equals("Small"))
						return 8;
					else if (sizeProperty().get().equals("Medium"))
						return 20;
					else if (sizeProperty().get().equals("Large")) // (SIze.LARGE)
						return 35;
				}
				else if (terrainProperty().get().equals("River"))
					if (sizeProperty().get().equals("Small"))
					{
						return 25;
					}
					else if (sizeProperty().get().equals("Medium"))
					{
						return 40;
					} // 40;}
					else if (sizeProperty().get().equals("Large")) // (SIze.LARGE)
						return 60;
				return 0;
			}
		});
		setCreationOngoing(false);
	}

	public void sayHello()
	{
		alert.setTitle("Hello there!");
		alert.setHeaderText(null);
		alert.setContentText("Witaj w symulatorze sawanny!" + '\n'
				+ "W tej wersji alfa dostêpna jest jedynie kreacja mapy. Wybierz z menu po prawej stronie teren i jego wielkoœæ. Kliknij lewym klawiszem myszy na mapie, aby wygenerowaæ losowo teren. Kliknij na istniej¹cym terenie prawym klawiszem, aby go usun¹æ." + '\n'+"Have fun :)");
		alert.showAndWait();
	}

	private void createMap()
	{
		setMap(new ObjectProperty[BOARD_X_SIZE][BOARD_Y_SIZE]);
		for (int i = 0; i < BOARD_X_SIZE; i++)
		{
			for (int j = 0; j < BOARD_Y_SIZE; j++)
			{
				getMap()[i][j] = new SimpleObjectProperty<>(Terrain.NONE);
			}
		}

	}

	public void clearMap()
	{
		for (int i = 0; i < BOARD_X_SIZE; i++)
		{
			for (int j = 0; j < BOARD_Y_SIZE; j++)
			{
				getMap()[i][j].setValue(Terrain.NONE);
			}
		}
		getTerrainsList().clear();
	}

	public void createTerrain(int x, int y)
	{
		if (isInputDisabled() || creationOngoing)
			return;
		if (!isLegalField(x, y))
		{
			setInputDisabled(true);//czasami wykrywa podwojne klikniecia...
			alert.setTitle("Serio?");
			if (getTerrain().equals("Forest"))
				alert.setContentText(
						"Twój las nie mo¿e siê pojawiæ na zajêtym polu. Wybierz inne, wolne pole.");
			else if (getTerrain().equals("River"))
				alert.setContentText(
						"Twoja rzeka nie mo¿e zaczynaæ siê poœrodku mapy ani na zajêtym polu. Wybierz wolne pole na obrze¿ach mapy!");
			alert.showAndWait();
			while (alert.isShowing())
			{
			}
			setInputDisabled(false);
			return;
		}
		setCreationOngoing(true);

		((ExecutorService) e).execute(() ->
		{
			int attempts = 0;
			getTempList().clear();
			copyTabs();
			chooseAndBuildTerrain(x, y);
			while (tempList.size() < getMinimalSize())
			{
				getTempList().clear();
				copyTabs();
				chooseAndBuildTerrain(x, y);
				attempts++;
				if (attempts == 30)
				{
					Platform.runLater(() ->
					{
						setInputDisabled(true);
						alert.setTitle("OOps");
						alert.setContentText(
								"Wygl¹da na to, ¿e nie ma na mapie miejsca aby stworzyæ ten teren! :(");
						alert.showAndWait();
						while (alert.isShowing())
						{
						}
						setInputDisabled(false);
					});
					setCreationOngoing(false);

					return;
				}
			}

			getTerrainsList().add(new TerrainArea(tempList));
			terrainAnimation();

			setCreationOngoing(false);
			return;

		});
	}

	public void createForestSegment(int x, int y)
	{
		tempMap[x][y].setValue(Terrain.FOREST);
		getTempList().add(new Point(x, y));
		setRecCounter(getRecCounter() + 1);
		for (int i = x - 1; i < x + 2; i++)
		{
			if (i < 0 || i > BOARD_X_SIZE - 1)
				continue;

			for (int j = y - 1; j < y + 2; j++)
			{
				if (j < 0 || j > (BOARD_Y_SIZE - 1) || !(tempMap[i][j].get() == Terrain.NONE))
					continue;
				double r = Math.random() * 100;

				if (r > getFactor())
				{
					createForestSegment(i, j);
				}
			}
		}
		setRecCounter(getRecCounter() - 1);
	}

	public void copyTabs()
	{
		tempMap = new ObjectProperty[BOARD_X_SIZE][BOARD_Y_SIZE];
		for (int i = 0; i < BOARD_X_SIZE; i++)
			for (int j = 0; j < BOARD_Y_SIZE; j++)
				tempMap[i][j] = new SimpleObjectProperty<>(getMap()[i][j].get());
	}

	public void terrainAnimation()
	{

		for (Point p : getTempList())
		{
			if (getTerrain().equals("Forest"))
				getMap()[p.x][p.y].setValue(Terrain.FOREST);
			else
				getMap()[p.x][p.y].setValue(Terrain.RIVER);

			if (getAnimation())
			{
				try
				{
					Thread.sleep(6);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteTerrain(int x, int y)
	{
		if (isCreationOngoing() || getMap()[x][y].get() == Terrain.NONE)
			return;
		creationOngoing = true;
		e.execute(() ->
		{
			for (TerrainArea ta : getTerrainsList())
				if (ta.getListOfFields().contains(new Point(x, y)))
				{
					for (Point p : ta.getListOfFields())
					{
						getMap()[p.x][p.y].set(Terrain.NONE);
						if (getAnimation())
						{
							try
							{
								Thread.sleep(8);//czas animacji
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}
					getTerrainsList().remove(ta);
					setCreationOngoing(false);
					break;
				}
		});
	}

	public void chooseAndBuildTerrain(int x, int y)
	{
		if (terrainProperty().get().equals("Forest"))
			createForestSegment(x, y);
		else if (getTerrain().equals("River"))
			createRiver(x, y);
		else
			System.out.println("ChoiceBox selection not recognizable");
	}

	private boolean isLegalField(int x, int y)
	{
		if (getTerrain().equals("Forest"))
		{
			if (!(getMap()[x][y].get().equals(Terrain.NONE)))
				return false;
			return true;
		}

		else if (getTerrain().equals("River"))
		{
			if ((getMap()[x][y].get().equals(Terrain.NONE))
					&& (x == 0 || y == 0 || x == BOARD_X_SIZE - 1 || y == BOARD_Y_SIZE - 1))
				return true;
			return false;
		}
		else
		{
			System.out.println("error in isLegalField() : unrecognizable terrain");
			return false;
		}
	}

	public void createRiver(int x, int y)
	{
		onObstacle = false;
		rp = new Point(x, y);
		Directions dir = initRiver(x, y);
		updateHorizontal(dir);
		Directions[] nextDirs = new Directions[3];

		int width;
		switch (getSize())
		{
			case "Small":
				width = 1;
				break;
			case "Medium":
				width = 2;
				break;
			case "Large":
				width = 3;
				break;
			default:
				width = 1;

		}
		while (rp.x >= 0 && rp.x < BOARD_X_SIZE && rp.y >= 0 && rp.y < BOARD_Y_SIZE && !onObstacle)// BUDUJ
		{
			changeProbabilities(nextDirs, dir, width);
			createSegment(nextDirs, dir, width);
			dir = changeDir(nextDirs);
			updateHorizontal(dir);
			moveToNewSegment(dir);
		}

	}

	public void updateHorizontal(Directions dir)
	{
		if (dir.isMainDir())
			setHorizontal(dir.isHorizontal());

	};

	public void changeProbabilities(Directions[] nextDirs, Directions dir, int width)
	{
		int i;
		for (i = 0; i < Directions.values().length; i++)
		{
			if (Directions.values()[i] == dir)
				break;
		}
		nextDirs[0] = dir;
		if (i == 0)
			nextDirs[1] = Directions.values()[7];
		else
			nextDirs[1] = Directions.values()[i - 1];
		if (i == 7)
			nextDirs[2] = Directions.values()[0];
		else
			nextDirs[2] = Directions.values()[i + 1];
		if (!getSize().equals("Small"))
			for (int j = 1; j < 3; j++)
				if (nextDirs[j].isMainDir())
					if (nextDirs[j].isHorizontal() != isHorizontal())
					{
						nextDirs[j] = dir;
						break;
					}
	}

	public void moveToNewSegment(Directions dir)
	{
		Point unitVector = dir.getUnitVector();
		rp.setLocation(rp.x + unitVector.x, rp.y + unitVector.y);
	}

	public Directions changeDir(Directions[] nextDirs)
	{
		int r = (int) (Math.random() * 100);

		if (r >= 0 && r < 65)
		{
			return nextDirs[0];
		}
		else if (r >= 65 && r < 82)
		{
			return nextDirs[1];
		}
		else if (r >= 82 && r < 100)
		{
			return nextDirs[2];
		}
		System.out.println("Error in changeDir");
		return nextDirs[0];
	}

	public void createSegment(Directions[] nextDirs, Directions dir, int width)
	{
		int collisionCounter = 0;
		if (isHorizontal())
		{
			for (int i = rp.x - width; i <= rp.x + width; i++)
			{
				if (i < 0 || i > (BOARD_X_SIZE) - 1)
					continue;
				if ((tempMap[i][rp.y].get() != Terrain.NONE)) // river encounters forest - should be build from the beginning
				{
					collisionCounter++;

					if (collisionCounter == 2)
					{
						onObstacle = true;
						tempList.clear();
						break;
					}
				}
				else
				{
					tempMap[i][rp.y].setValue(Terrain.RIVER);
					tempList.add(new Point(i, rp.y));
				}
			}

		}
		else
		{
			for (int i = rp.y - width; i <= rp.y + width; i++)
			{
				if (i < 0 || i > (BOARD_Y_SIZE) - 1)
					continue;
				if ((tempMap[rp.x][i].get() != Terrain.NONE)) // river encounters forest - should be build from the beginning
				{
					collisionCounter++;

					if (collisionCounter == 2)
					{
						onObstacle = true;
						tempList.clear();
						break;
					}
				}
				else
				{
					tempMap[rp.x][i].setValue(Terrain.RIVER);
					tempList.add(new Point(rp.x, i));
				}
			}

		}

	}

	private Directions initRiver(int x, int y)
	{
		if (x == 0)
			return Directions.E;
		else if (x == BOARD_X_SIZE - 1)
			return Directions.W;
		else if (y == 0)
			return Directions.S;
		else if (y == BOARD_Y_SIZE - 1)
			return Directions.N;
		else
		{
			System.out.println(" initRiver: Coœ sie zepsulo");
			return null;
		}
	}

	public void goToNextWindow()
	{
		alert.setTitle("Soon (TM)");
		alert.setContentText("Przejœcie dalej wymaga wersji premium. Kup  ju¿ dziœ za 4.99$!" + '\n' + '\n'
				+ "P.S. Tak naprawde to dalsza czêœ bêdzie wkrotce dodana." + '\n'+ "A przynajmniej takie s¹ nadzieje. ");
		alert.showAndWait();
		while (alert.isShowing())
		{
		}
		setInputDisabled(false);

	}
}
