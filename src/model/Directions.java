package model;

import java.awt.Point;

public enum Directions
{
	N(true, true, new Point(0, -1)), NE(false, false, new Point(1, -1)), E(true, false, new Point(1, 0)), SE(
			false, false, new Point(1, 1)), S(true, true, new Point(0, 1)), SW(false, false,
					new Point(-1, 1)), W(true, false, new Point(-1, 0)), NW(false, false, new Point(-1, -1));

	private final boolean horizontal;
	private final boolean mainDir;
	private final Point unitVector;

	Directions(boolean mDir, boolean h, Point p)
	{
		this.mainDir = mDir;
		this.horizontal = h;
		this.unitVector = p;
	}

	public boolean isMainDir()
	{
		return mainDir;
	}

	public boolean isHorizontal()
	{
		/*
		 * if (!isMainDir()) return false; else
		 */
		return horizontal;
	}

	public Point getUnitVector()
	{
		return unitVector;
	}
}
