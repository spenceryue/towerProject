import java.util.ArrayList;

public class Builder_v2
{
	private final double weight;
	private final String [] sideColors;
	private int sideUp;
	
	private ArrayList<Integer> previousBlocks;
	private ArrayList<ArrayList<Integer>> possibleSidesUp;
	
	public Builder_v2(double wt, String [] colors)
	{
		weight = wt;
		sideColors = colors;
		previousBlocks = new ArrayList<Integer> ();
		possibleSidesUp = new ArrayList<ArrayList<Integer>> ();
	}
	
	public void setSideUp (int whichSide)
	{
		sideUp = whichSide;
	}
	
	public int getSideUp ()
	{
		return sideUp;
	}
	
	public int flipSide (int aSide)
	{
		return aSide + (int) Math.pow(-1, aSide%2);
	}
	
	public String [] getSideColors ()
	{
		return sideColors;
	}
	
	public String getColorAt (int whichSide)
	{
		return sideColors [whichSide];
	}
	
	public double getWeight ()
	{
		return weight;
	}
	
	public ArrayList<Integer> getPreviousBlocks ()
	{
		return previousBlocks;
	}
	
	public int addPreviousBlock (int theBlockIndex)
	{
		previousBlocks.add(theBlockIndex);
		possibleSidesUp.add(new ArrayList<Integer> ());
		return previousBlocks.size();
	}
	
	public ArrayList<ArrayList<Integer>> getPossibleSidesUp ()
	{
		return possibleSidesUp;
	}
	
	public ArrayList<String> getPossibleSideUpColors ()
	{
		ArrayList<String> instances = new ArrayList<String> ();
		for (ArrayList<Integer> sideGroup : possibleSidesUp)
			for (int aSideIndex : sideGroup)
			{
				String aSideColor = sideColors[aSideIndex];
				boolean duplicate = false;
				for (String aPreviousColor : instances)
					if (aPreviousColor.equals(aPreviousColor))
						duplicate = true;
				if (!duplicate)
					instances.add(aSideColor);
			}
		return instances;
	}
	
	public int addPossibleSideUp (int previousBlockGroup, int sideIndex)
	{
		int group = previousBlocks.indexOf(previousBlockGroup);
		possibleSidesUp.get(group).add(sideIndex);
		return possibleSidesUp.get(group).size();
	}
	
	public String toString ()
	{
		String line = Double.toString(weight);
		for (int side = 0; side < 6; side++)
		{
			if (side == sideUp)
				line += "\t" + "[" + getColorAt(side) + "]";
			else
				line += "\t" + getColorAt(side);
		}
		return line;
	}
}