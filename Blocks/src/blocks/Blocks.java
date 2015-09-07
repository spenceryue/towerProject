import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Blocks {
	
	private final static int NUM_BLOCKS = 10000;
	private final static int MAXWEIGHT = 100;
	private final static String [] Colors = {"red", "orange", "yellow", "green", "blue", "purple", /*"black", "white", "grey", "rainbow", "supercolor1", "peanutButter"*/};

	//Creates text file of randomly colored and weighted blocks
	public static void generate ( int numberOfBlocks , int maximumWeight, String [] poolOfColors, String fileName)  throws IOException
	{
		FileWriter writer = new FileWriter( fileName );
		Random randomNum = new Random ();
		
		for ( int count = 0; count < numberOfBlocks; count++ )
		{
			// Random weight from (0, maximumWeight] with two decimal places.
			writer.write ( String.format ( "%.2f", maximumWeight - randomNum.nextInt ( maximumWeight*100 )/100.0 ) );
			for ( int index = 0; index < 6; index++ )
				writer.write ( "\t" + Colors [ randomNum.nextInt (Colors.length) ] );
			writer.write ( "\n" );
		}
		writer.close ();
		/*
		 * Let the 1st element in a line denote the weight of a block (0, maximumWeight].
		 * Let the 2nd through 7th elements denote the colors of the sides of the block
		 * in the following way:
		 * 		2nd = Top
		 * 		3rd = Bottom
		 * 		4th = Left
		 * 		5th = Right
		 * 		6th = Front
		 * 		7th = Back
		 * */
	}
	
	// Returns a Blocks array containing the data for each block in file "blocks.txt"
	public static ArrayList <Block> readList ( File fileName ) throws IOException
	{
		ArrayList <Block> pool = new ArrayList <Block> ();
		
		// Prepare to fill pool array
		Scanner input = new Scanner ( fileName );
		input.useDelimiter ( "\\n" );
		
		// Fill pool array
		while ( input.hasNext() )
		{
			Scanner line = new Scanner ( input.next () );
			line.useDelimiter ( "\\t" );
			
			String [] colors = new String[6];
			double weight = line.nextDouble();
			
			for (int side = 0; line.hasNext(); side++ )
				colors [side] = line.next();
			line.close ();
			
			pool.add( new Block (weight, colors));
		}
		input.close ();
		
		return pool;
	}

	// Sorts the blocks in pool by weight using Selection Sort algorithm. (greatest to least)
	public static void weightSort ( ArrayList <Block> pool )
	{
		for ( int index = 0; index < pool.size() - 1; index++ )
		{
			int greatest = index;
			Block temp;
			
			for ( int scan = index + 1; scan < pool.size(); scan++ )
			{
				if ( pool.get(scan).getWeight() > pool.get(greatest).getWeight() )
					greatest = scan;
			}
			
			if ( greatest != index )
			{
				temp = pool.get(greatest);
				pool.set(greatest, pool.get(index));
				pool.set(index, temp);
			}
		}
	}
	
	// Find longest subsequence of color compatible blocks
	public static ArrayList <ArrayList <Block>> analyze ( ArrayList <Block> pool )
	{
		ArrayList<ArrayList<Block>> length = new ArrayList<ArrayList<Block>> ();
		for (int i = 0; i < pool.size() ; i++)
		{
			for (int l = length.size(); l > 0; l--)
			{
				for (Block aPreviousBlock : length.get(l - 1))
				{
					int sidesAdded = 0;
					for (String aPreviousColor : aPreviousBlock.getPossibleSideUpColors ())
					{
						for (int aSideIndex = 0; aSideIndex < 6; aSideIndex++)
						{
							if (pool.get(i).getColorAt(aSideIndex).equals(aPreviousColor))
							{
								if (sidesAdded == 0)
									pool.get(i).addPreviousBlock(pool.indexOf(aPreviousBlock));
								sidesAdded = pool.get(i).addPossibleSideUp(pool.indexOf(aPreviousBlock), pool.get(i).flipSide(aSideIndex));
							}
						}
					}
				}
				if (!pool.get(i).getPossibleSidesUp().isEmpty())
				{
					if (l == length.size())
						length.add(new ArrayList<Block> ());
					length.get(l).add(pool.get(i));
					l = 1;
				}
			}
			if (pool.get(i).getPossibleSidesUp().isEmpty())
			{
				//System.out.println("isEmpty " + i);
				pool.get(i).addPreviousBlock(-1);
				for (int index = 0; index < 6; index++)
					pool.get(i).addPossibleSideUp(-1, index);
				if (length.size() == 0)
					length.add(new ArrayList<Block> ());
				length.get(0).add(pool.get(i));
			}
		}
		
	/*	for (ArrayList<Block> array : length)
		{
			for (Block aBlock : array)
				System.out.print(aBlock + "\t");
			System.out.println();
		}	*/
		
		return length;
	}
	
	public static Block [] build ( ArrayList <Block> pool, ArrayList <ArrayList <Block>> length )
	{
		int l = length.size();
		int i = pool.indexOf(length.get(l - 1).get(0));
		int m = pool.get(i).getPreviousBlocks().get(0);

		Block [] tower = new Block [l];

		pool.get(i).setSideUp(pool.get(i).getPossibleSidesUp().get(0).get(0)); // First ".get(0) matches ".get(0)" in 3rd line (assigning m).

	/*	System.out.println("\nPool");
		for (Block aBlock : pool)
				System.out.println(aBlock);
		System.out.println("\nLength");
		for (ArrayList<Block> array : length)
		{
			for (Block aBlock : array)
				System.out.print(aBlock + "\t");
			System.out.println();
		}

		System.out.println("Index of Longest Length block:\t\t\t\t " + i);
		System.out.println("Index of 2nd Longest Length block:\t\t\t " + m);
		System.out.println("Set of possible 2nd Longest length blocks to precede the longest: " + pool.get(i).getPreviousBlocks());
		System.out.println("pool size:\t " + pool.size());
		System.out.println("length:\t " + length.size());	*/
		
		for (int count = 0; count < l; count++)
		{
		/*	System.out.print("\nBegin round.");
			System.out.println("\t\t\t\t\t\t\t\tcount: " + count);
			System.out.println("The longest length block\n" + pool.get(i) + "\n");	*/
			
			tower [count] = pool.get(i);
			String bottomSide = tower [count].getColorAt(tower [count].flipSide(tower [count].getSideUp()));
			int matchingSide = -1;
			for (int side = 0; side < 6; side++)
			{
				//System.out.println("A Side Index: " + side + "\t" + pool.get(m).getColorAt(side));
				if (bottomSide.equals(pool.get(m).getColorAt(side)))
				{
					matchingSide = side;
					side = 5;
				}
			}
			//System.out.println();
			
			if (matchingSide == -1)
				throw new RuntimeException("Something… went wrong\n"
				+ "A matching side on block " + m + " could not be found.\n"
				+ "(Color wanted: \"" + bottomSide + "\")");
			
			i = m;
			ArrayList<ArrayList<Integer>> s = pool.get(i).getPossibleSidesUp();
			String sideUpColor = pool.get(i).getColorAt(matchingSide);
			
		/*	System.out.println("Possible Sides up of block " + i);
			for (ArrayList<Integer> blockGroup : s)
			{
				for (int aSide : blockGroup)
					System.out.print ("Side: " + aSide + " Color: " + pool.get(i).getColorAt(aSide) + "\t");
				System.out.println();
			}
			System.out.println();	*/
			
			FINDING_NEXT_M :
				for (int blockGroup = 0; blockGroup < s.size(); blockGroup++)
				{
					for (int aSide : s.get(blockGroup))
					{
						if (sideUpColor.equals(pool.get(i).getColorAt(aSide)))
						{
							pool.get(i).setSideUp(aSide);
							m = pool.get(i).getPreviousBlocks().get(blockGroup);
						/*	System.out.println("Block " + i + " New Side Up: " + aSide + "\t Color: " + pool.get(i).getColorAt(aSide));
							System.out.println("Found an m: " + m);	*/
							break FINDING_NEXT_M;
						}
					}
				}
			
			//System.out.println("End round.\ti: " + i + "\tm: " + m + "\n");
			if (count != l - 2)
			{
				if (i == m)
					throw new RuntimeException("Sigh -.-, somehow the next block couldn't be found.\n"
					+ "Our current count is " + count + ".\n"
					+ "The matchingSide we wanted was: " + matchingSide + ".");
				
			/*	System.out.println("Tower so far: ");
				for (Block aBlock : tower)
					System.out.println(aBlock);	*/
			}
			else
			{
				tower[l - 1] = pool.get(i);
				count++;
				//System.out.println("End of building.");
			}
		}
		
		return tower;
	}
	
	public static void main (String [] args) throws IOException
	{
		long start = System.currentTimeMillis ();
		long end, RunTime;
                String theDate = LocalDateTime.now().toString();
                String fileName = NUM_BLOCKS + " sample blocks (" + theDate.substring(0, theDate.indexOf(":")-3) + ").txt";
		
		generate ( NUM_BLOCKS, MAXWEIGHT, Colors, fileName );
		ArrayList <Block> pool = readList ( new File (fileName) );
		weightSort ( pool );
		
		ArrayList <ArrayList <Block>> length = analyze ( pool );
		Block [] tower = build ( pool, length );
		
		//System.out.println("\n\n");
		for (Block aBlock : tower)
		{
			System.out.println(aBlock);
		}
		
		end = System.currentTimeMillis ();
		RunTime = end - start;
		System.out.println ( "\nToday is\t" + TimeReporter.convertTime (end, "Date & Time") + "\n"
		+ "Run Time:\t" + TimeReporter.convertTime (RunTime, "Elapsed Time Minutes") + "\n\n"
		+ "Height of Tower: " + tower.length + " of " + NUM_BLOCKS + ((tower.length > pool.size()/2) ? "\t:)" : ""));
	}
}