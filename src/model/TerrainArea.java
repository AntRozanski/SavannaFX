package model;

import java.awt.Point;
import java.util.ArrayList;

public class TerrainArea
{
	private ArrayList<Point> listOfFields = new ArrayList<Point>();
	private int numOfFields;
	private Size size;
	private Terrain terrain;

	public TerrainArea(ArrayList<Point> lof)
	{
		for (int i = 0; i < lof.size(); i++)
		{
			getListOfFields().add(i, lof.get(i));
		}
	}

	public ArrayList<Point> getListOfFields()
	{
		return listOfFields;
	}

	public int getNumOfFields()
	{
		return numOfFields;
	}

	public void setNumOfFields(int numOfFields)
	{
		this.numOfFields = numOfFields;
	}

	public void setListOfFields(ArrayList<Point> listOfFields)
	{
		this.listOfFields = listOfFields;
	}

	public Size getSize()
	{
		return size;
	}

	public void setSize(Size size)
	{
		this.size = size;
	}

	public Terrain getTerrain()
	{
		return terrain;
	}

	public void setTerrain(Terrain terrain)
	{
		this.terrain = terrain;
	}
}
