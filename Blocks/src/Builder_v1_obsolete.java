import java.util.*;
import java.io.*;

public class Builder_v1_obsolete
{
	//	Declare and initialize variables
	private final static int NUM_BLOCKS = 100;
	private final static int MAXWEIGHT = 100;
	private final static String [] Colors = {"red", "orange", "yellow", "green", "blue", "purple"};
	private static ArrayList< ArrayList<Object> > pool, tower;
	
					//$
					//	Record number of times colorComparison is called
					private static int colorComparisonCounter = 0;
					private static int weightComparisonCounter = 0;
	
	//	Creates text file of randomly colored and weighted blocks
	private static void blocksGenerator ( int numberOfBlocks , int maximumWeight )  throws IOException
	{
		FileWriter writer = new FileWriter( "blocks.txt" );
		Random randomNum = new Random ();
		for ( int count = 0; count < numberOfBlocks; count++ )
		{
			writer.write ( Double.toString( randomNum.nextInt (maximumWeight) + 1 ) );
			for ( int index = 0; index < 6; index++ )
				writer.write ( "\t" + Colors [ randomNum.nextInt (Colors.length) ] );
			writer.write ( "\n" );
		}
		writer.close ();
		/*
		 * Let the 1st element in a line denote the weight of a block (0, MAXWEIGHT).
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
	
	// Calls blockGenerator, fills pool ArrayList,
	// sorts through pool & fills tower ArrayList, &
	// writes new text file containing the list of blocks arranged in a tower
	public static void main( String[] args ) throws IOException
	{
					//$
					//Scanner scan = new Scanner (System.in);
		
		// Call blockGenerator
		blocksGenerator ( NUM_BLOCKS, MAXWEIGHT );
		File blocks = new File ( "blocks.txt" );
		
		// Count number of blocks
		int size = 0;
		Scanner prelim_count = new Scanner ( blocks );
		prelim_count.useDelimiter ( "\\n" );
		while ( prelim_count.hasNext() )
		{
			prelim_count.next ();
			size++;
		}
		prelim_count.close ();
		
		// Prepare to fill pool ArrayList
		Scanner input = new Scanner ( blocks );
		input.useDelimiter ( "\\n" );
		pool = new ArrayList< ArrayList<Object> > ( size );
		
		// Fill pool ArrayList
		for ( int index = 0; input.hasNext(); index++ )
		{
			Scanner line = new Scanner ( input.next () );
			line.useDelimiter ( "\\t" );
			
			pool.add( new ArrayList<Object> (8) );
			/* Each block in pool has the following 8 elements (index):
			 *	weight (0)
			 *	side color (1-6)
			 *	face up side index (7) 
			 */
			while ( line.hasNext() )
				pool.get ( index ).add( line.next() );
			line.close ();
		}
		input.close ();
		
					//$ - special
					// Print pool ArrayList
					for ( ArrayList<Object> block : pool )
					{
						for ( Object element : block )
							System.out.print ( element + "\t");
						System.out.println ();
					}
					System.out.println ( "\n\n\n" );
		
		// Prepare to sort through pool and fill tower ArrayList
		int initialPoolSize = pool.size ();
		tower = new ArrayList< ArrayList<Object> > ( size );
		tower.add ( pool.get (0) );
		pool.remove (0);
		int finalPoolSize = pool.size ();
		
		// Record efficiency data
		int rounds = 0;
		long start = System.currentTimeMillis ();
		long end, sortingTime;
		
		// Sort through pool and fill tower ArrayList
		while ( finalPoolSize != initialPoolSize )
		{
			// Record initialPoolSize
			initialPoolSize = pool.size();
				
					//$
					System.out.println("Round Start. Initial Pool Size: " + initialPoolSize);
				
			// Declare variables
			boolean comparing, last, result;
			double p_block_WT, t_block_WT, t_nextBlock_WT;
			double t_index, old_t_index, temp_indexChanger;
			
					//$
					System.out.println("\tBegin iterating pool blocks.");
			
			// Iterate through blocks in pool
			for ( int pool_index = 0; pool_index < pool.size (); pool_index++ )
			{
					//$
					System.out.println("\t\tPool block #" + (pool_index+1));
				
				// Initialize variables
				comparing = true;
				t_index = tower.size () - 1.0;
				old_t_index = 0;
				
				// Preliminary weight comparison between pool block and first tower block to see if they will call the colorComparison method
				if (t_index != 0)	// (if tower size is not 1)
				{
					// Retrieve weights
					p_block_WT = Double.parseDouble ( (String) pool.get (pool_index).get (0) );
					t_block_WT = Double.parseDouble( (String) tower.get ( (int) old_t_index ).get (0) );
					t_nextBlock_WT =  Double.parseDouble ( (String) tower.get ( (int) old_t_index + 1 ).get (0) );
					
					// Check if the pool block is color comparable with the first block in Tower
					if ( p_block_WT >= t_block_WT || (p_block_WT < t_block_WT && p_block_WT > t_nextBlock_WT) )
					{
					//$
					System.out.println("\t\tPreliminary weight comparison showed pool block is color comparable to the first tower block (index: 0)."
									+ "\n\t\t(The colorComparison method will be called at index 0 of Tower.)");
					
					//$
					System.out.println("\t\tIndex changing.");
					System.out.println("\t\tOld Tower Index: 0 >> " + ( (int) (t_index + 0.5) ));
						
							temp_indexChanger = old_t_index;
							old_t_index = t_index;
							t_index = temp_indexChanger;
							
					//$
					System.out.println("\t\tCurrent Tower Index: " + ( (int) (old_t_index + 0.5) ) + " >> 0");
					
					//$
					// Increment number of weight comparisons made
					if (p_block_WT >= t_block_WT)
						weightComparisonCounter++;
					else
						weightComparisonCounter += 2;
					}	
				}
					//$
					System.out.println("\t\t\tBegin comparing with tower blocks.");
					
				// Compare pool block with each tower block 
				while (comparing)
				{
					//$
					System.out.println("\t\t\tTower block #" + ((int) t_index + 1));
					System.out.println("\t\t\tTower size: " + tower.size ());
					
					// Initialize variable
					last = (int) (t_index + 0.5) + 1 == tower.size ();
					
					//$
					String is_or_not = last ? "is" : "is not";
					System.out.println("\t\t\tTower block " + is_or_not + " last.");
					
					// Retrieve weights
					p_block_WT = Double.parseDouble ( (String) pool.get (pool_index).get (0) );
					t_block_WT = Double.parseDouble ( (String) tower.get ( (int) (t_index + 0.5) ).get (0) );
					
					//$
					System.out.println("\t\t\tPool Block Weight: " + p_block_WT
									+ "\n\t\t\tTower Block Weight: " + t_block_WT);
					
					if ( !last )
					{
					//$
					System.out.println("\t\t\t\tCurrent Tower Block (indx: " + ( (int) (t_index + 0.5) ) + ") counted as \"not last\" in Tower.");
						
						t_nextBlock_WT = Double.parseDouble ( (String) tower.get ( (int) (t_index + 0.5) + 1 ).get (0) );
						
					//$
					System.out.println("\t\t\t\tNext Block in Tower Weight: " + t_nextBlock_WT);
						
						// Compare weights
						if ( p_block_WT < t_block_WT && p_block_WT <= t_nextBlock_WT )
						{
							
					//$
					System.out.println("\t\t\t\t\tPool Block (wt: " + p_block_WT
									+ ") counted as < Current block (wt: " + t_block_WT
									+ ") & <= Next block (wt: " + t_nextBlock_WT + ") in Tower.");
							
					//$
					System.out.println("\t\t\t\t\tIndex changing.");
					System.out.println("\t\t\t\t\tOld Tower Index: " + ( (int) (old_t_index + 0.5) ) + " >> " + ( (int) (t_index + 0.5) ));
						
							temp_indexChanger = old_t_index;
							old_t_index = t_index;
							t_index = t_index + Math.abs ( temp_indexChanger - t_index ) / 2.0;
							
					//$
					System.out.println("\t\t\t\t\tCurrent Tower Index: " + ( (int) (old_t_index + 0.5) ) + " >> " + ( (int) (t_index + 0.5) ));
					
					//$
					// Increment number of weight comparisons made
					weightComparisonCounter += 2;
						
							continue;
						}
						else if ( p_block_WT == t_block_WT )
						{
					//$
					System.out.println("\t\t\t\t\tPool Block (wt: " + p_block_WT
									+ ") counted as == Current block (wt: " + t_block_WT
									+ ") in Tower.");
					
								// Call colorComparison
								// (Pool block weighs same as tower block at "t_index")
								result = colorComparison ( pool_index, (int) (t_index + 0.5), "equals" );
							
					//$
					// Increment number of weight comparisons made
					weightComparisonCounter += 3;
					
						}
						else if ( p_block_WT > t_block_WT )
						{
					//$
					System.out.println("\t\t\t\t\tPool Block (wt: " + p_block_WT
									+ ") counted as > Current block (wt: " + t_block_WT
									+ ") in Tower.");
							
							if ( (int) (t_index + 0.5) != 0 )
							{
					//$
					System.out.println("\t\t\t\t\t\tCurrent Tower Block (indx: " + ( (int) (t_index + 0.5) ) + ") counted as \"not first\" in Tower.");
					
					//$
					System.out.println("\t\t\t\t\t\tIndex changing.");
					System.out.println("\t\t\t\t\t\tOld Tower Index: " + ( (int) (old_t_index + 0.5) ) + " >> " + ( (int) (t_index + 0.5) ));
						
							temp_indexChanger = old_t_index;
							old_t_index = t_index;
							t_index = t_index - Math.abs ( temp_indexChanger - t_index ) / 2.0;
							
					//$
					System.out.println("\t\t\t\t\t\tCurrent Tower Index: " + ( (int) (old_t_index + 0.5) ) + " >> " + ( (int) (t_index + 0.5) ));
					
					//$
					// Increment number of weight comparisons made
					weightComparisonCounter +=4;
					
								continue;
							}
							else
							{
					//$
					System.out.println("\t\t\t\t\t\tCurrent Tower Block (indx: " + ( (int) (t_index + 0.5) ) + ") counted as \"first\" in Tower.");
					
								// Call colorComparison
								// (Pool block weighs more than first tower block)
								result = colorComparison ( pool_index, (int) (t_index + 0.5), "first" );

					//$
					// Increment number of weight comparisons made
					weightComparisonCounter += 4;
							}
						}
						else
						{
							assert !( p_block_WT < t_block_WT && p_block_WT > t_nextBlock_WT ) :
								"\n\tERROR: Some case besides the pool block weighing between the #" + ( (int) (t_index + 0.5) +1 )
								+ "\n\ttower block's weight and the #" + ( (int) (t_index + 0.5) + 2 ) + " tower block's"
								+ "\n\tweight occurred and passed all other case conditions.\n"
								+ "\n\tPool Block Weight: " + p_block_WT
								+ "\tTower Block Weight: " + t_block_WT;
							
					//$
					System.out.println("\t\t\t\t\tPool Block (wt: " + p_block_WT
									+ ") counted as < Current block (wt: " + t_block_WT
									+ ") & > Next Block (wt: " + t_nextBlock_WT + ") in Tower.");
							
							// Call colorComparison
							// (Pool block weighs something between the current tower block and the
							// next tower block's weight.)
							result = colorComparison ( pool_index, (int) (t_index + 0.5), "between" );
							
					//$
					// Increment number of weight comparisons made
					weightComparisonCounter += 4;
						}
							
					}
					else
					//$ - Remove brackets at end of testing ($)
						
					{
					//$
					System.out.println("\t\t\t\tCurrent Tower Block (indx: " + ( (int) (t_index + 0.5) ) + ") counted as \"last\" in Tower.");
					
						if ( tower.size () != 1 )
						{
					//$
					System.out.println("\t\t\t\t\tTower size (" + tower.size() + ") counted as \"!= 1\".");
					
							if ( p_block_WT > t_block_WT )
							{
					//$
					System.out.println("\t\t\t\t\t\tPool Block (wt: " + p_block_WT
									+ ") counted as > last block (wt: " + t_block_WT
									+ ") in Tower.");
					
					//$
					System.out.println("\t\t\t\t\t\t\tIndex changing.");
					System.out.println("\t\t\t\t\t\t\tOld Tower Index: " + ( (int) (old_t_index + 0.5) ) + " >> " + ( (int) (t_index + 0.5) ));
						
								temp_indexChanger = old_t_index;
								old_t_index = t_index;
								t_index = t_index - Math.abs ( temp_indexChanger - t_index ) / 2.0;
							
					//$
					System.out.println("\t\t\t\t\t\t\tCurrent Tower Index: " + ( (int) (old_t_index + 0.5) ) + " >> " + ( (int) (t_index + 0.5) ));
					
					//$
					// Increment number of weight comparisons made
					weightComparisonCounter++;
					
								continue;
							}
						 	else if ( p_block_WT == t_block_WT )
					//$ Remove brackets when done
						 		
						 	{
					//$
					System.out.println("\t\t\t\t\t\tPool Block (wt: " + p_block_WT
									+ ") counted as == last block (wt: " + t_block_WT
									+ ") in Tower.");

						 		result = colorComparison ( pool_index, (int) (t_index + 0.5), "equals" );

					 //$
					weightComparisonCounter += 2;
						 	}
							else
					//$ Remove brackets when done
								
							{
					//$
					System.out.println("\t\t\t\t\t\tPool Block (wt: " + p_block_WT
									+ ") counted as < last block (wt: " + t_block_WT
									+ ") in Tower.");

								result = colorComparison ( pool_index, (int) (t_index + 0.5), "last" );
								
					//$
					weightComparisonCounter += 2;
							}
						}
						else
					//$ - Remove brackets at end of testing ($)
							
						{
					//$
					System.out.println("\t\t\t\t\tTower size (" + tower.size() + ") counted as \"== 1\".");
					
							result = colorComparison ( pool_index, (int) (t_index + 0.5), "default" );
						}
					}
					
					//$
					if (weightComparisonCounter > 200000)
					{
						pool_index = pool.size();
						initialPoolSize = pool.size();
					}
					
					//$
					System.out.println("\t\t\tFinished comparing Pool Block #" + (pool_index+1) + " with tower blocks."
									//+ "\n\t\t\tContinue?"
									+ "\n\t\t\t");
				/*	if ( scan.nextLine().equalsIgnoreCase("no") )
					{
						pool_index = pool.size();
						initialPoolSize = pool.size();
						System.out.println("\t\t\tAcknowledged.");
					}
					else
						System.out.println("\t\t\tVery well. Continuing.");
				*/	
					comparing = false;
					
					// Adjustment to pool index if block removed
					pool_index = result ? (pool_index - 1) : pool_index;
				}
			}
			// Record finalPoolSize
			finalPoolSize = pool.size ();
			
			// Count round
			rounds++;
			
			//$
			System.out.println("Round finished."
					+ "\tInitial Pool Size: " + initialPoolSize
					+ "\tFinal Pool Size: " + finalPoolSize
					+ "\tChange: " + (finalPoolSize - initialPoolSize)
					+ "\n\n" );
			
		}
		
		System.out.println ( "\n" );
		
					//$
					//scan.close();
					
					//$ - Edit this part \/ to create a new text file instead of print when finished coding everything else
		
		// Report time & statistics and ($)print tower arrangement($)
		end = System.currentTimeMillis ();
		sortingTime = end - start;
		System.out.println ( "Today is\t" + convertTime (end, "Date & Time") + "\n"
							+ "Sort Time:\t" + convertTime (sortingTime, "Elapsed Time Minutes") + "\n"
							+ "Rounds taken:\t" + rounds + "\n"
							+ "Number of color comparisons:\t" + colorComparisonCounter + "\n"
							+ "Number of weight comparisons:\t" + weightComparisonCounter + "\n"
					/*$*/	+ "Tower arrangement (heaviest at bottom):\n\n" );	/*$*/
		
					//$
					for ( int token = tower.size() - 1; token > -1; token-- )
					{
						System.out.print ( tower.get(token).get (0) );
						for ( int index = 1; index < 7; index++ )
							System.out.print ( "\t" + (String) tower.get(token).get (index) );
						System.out.println ( "\t" + Integer.toString ( (Integer) tower.get(token).get(7) ) );
					}
	}
	
	// Compares blocks for color compatibility and moves the blocks if compatible
	private static boolean colorComparison ( int poolIndex, int towerIndex, String typeOfComparison )
	{
					//$
					if ( (typeOfComparison.equals("first") || towerIndex == tower.size() - 1) && (!typeOfComparison.equals("default")) )
						System.out.print("\t");
					System.out.println("\t\t\t\t\t\tColor comparison called.");

		// Declare variables
		boolean result, insertAtNext = false;
		int faceUp, faceUp_2ndTowerBlock, faceDown, p_block_newFaceUp, pool_faceUp;
		String faceUpColor, faceDownColor, pool_faceColor;
		
		// Comparison Types
		switch (typeOfComparison)
		{
			// Check the passed pool block for compatibility with the 1st tower block
			case "first" :
					//$
					System.out.println("\t\t\t\t\t\t\tUsing \"first\" method of comparison.");
					
				// Retrieves index and color of the tower block's faceDownColor
				faceUp = (Integer) tower.get (towerIndex).get (7);
				faceDown = faceUp + (int) Math.pow ( -1, faceUp + 1 );
				faceDownColor = (String) tower.get (towerIndex).get (faceDown);
				
				// Searches through pool block for color match
				p_block_newFaceUp = -1;
				for ( int index = 1; index <= 6; index++ )
					if ( faceDownColor.equals ( (String) pool.get (poolIndex).get (index) ) )
					{
						p_block_newFaceUp = index;
						break;
					}
			break;
				
			// Check the passed pool block for compatibility with the last tower block
			case "last" :
					//$
		 			if ( towerIndex == tower.size() -1 )
		 				System.out.print("\t");
					System.out.println("\t\t\t\t\t\tUsing \"last\" method of comparison.");
				
				// If pool block is compatible, it will always be inserted at (towerIndex + 1) in Tower.
				insertAtNext = true;
				
				// Retrieves index and color of the tower block's faceUpColor
				faceUp = (Integer) tower.get (towerIndex).get (7);
				faceUpColor = (String) tower.get (towerIndex).get (faceUp);
				
				// Searches through pool block for color match
				p_block_newFaceUp = -1;
				for ( int index = 1; index <= 6; index++ )
					if ( faceUpColor.equals ( (String) pool.get (poolIndex).get (index) ) )
					{
						p_block_newFaceUp = index + (int) Math.pow ( -1, index + 1 );
						break;
					}
			break;
				
			// Check the passed pool block for compatibility with the pair of tower blocks
			case "between" :
					//$
					System.out.println("\t\t\t\t\t\tUsing \"between\" method of comparison.");
				
				// If pool block is compatible, it will always be inserted at (towerIndex + 1) in Tower.
				insertAtNext = true;
				
				// Retrieves index and color of the 1st tower block's faceUpColor...
				faceUp = (Integer) tower.get (towerIndex).get (7);
				faceUpColor = (String) tower.get (towerIndex).get (faceUp);
				
				// ...and of the 2nd tower block's faceDownColor
				faceUp_2ndTowerBlock = (Integer) tower.get (towerIndex + 1).get (7);
				faceDown = faceUp_2ndTowerBlock + (int) Math.pow ( -1, faceUp_2ndTowerBlock + 1 );
				faceDownColor = (String) tower.get (towerIndex + 1).get (faceDown);
				
				// Searches through pool block for color match
				p_block_newFaceUp = -1;
				for ( int index = 1; index <= 6; index++ )
					if ( faceUpColor.equals ( (String) pool.get (poolIndex).get (index) ) )
					{
						pool_faceUp = index + (int) Math.pow ( -1, index + 1 );
						if ( faceDownColor.equals ( (String) pool.get (poolIndex).get (pool_faceUp) ) )
						{
							p_block_newFaceUp = pool_faceUp;
							break;
						}
					}
			break;
				
			// Check the passed pool block for compatibility with the tower block
			case "equals" :
					//$
		 			if ( towerIndex == tower.size() -1 )
		 				System.out.print("\t");
					System.out.println("\t\t\t\t\t\tUsing \"equals\" method of comparison.");
				
				// Retrieves index and color of the tower block's faceUpColor and faceDownColor
				faceUp = (Integer) tower.get (towerIndex).get (7);
				faceUpColor = (String) tower.get (towerIndex).get (faceUp);
				faceDown = faceUp + (int) Math.pow ( -1, faceUp + 1 );
				faceDownColor = (String) tower.get (towerIndex).get (faceDown);
				
				// Searches through pool block for pairs of matching colors on opposite sides
				// then checks to see if the color matches either faceUpColor or faceDownColor
				p_block_newFaceUp = -1;
				pool_faceColor = null;
				for ( int index = 1; index <= 6; index += 2 )
				{
					pool_faceColor = (String) pool.get (poolIndex).get (index);
					if ( pool_faceColor.equals( (String) pool.get (poolIndex).get (index + 1) ) )
						if ( pool_faceColor.equals (faceUpColor) || pool_faceColor.equals (faceDownColor) )
						{
							p_block_newFaceUp = index;
							
							// Decide whether to insert pool block at towerIndex or (towerIndex + 1)
							insertAtNext = ( pool_faceColor.equals (faceUpColor) ) ? true : false;
							
							break;
						}
				}
				
			break;
			
			// Handles pool block comparison when Tower has only 1 block
			default :
		 			//$
		 			System.out.println("\t\t\t\t\t\tUsing \"default\" method of comparison.");
		 		
		 		// Searches through pool block for a color that matches one side on the tower block
	 			// then assigns each block a new faceUp value
				p_block_newFaceUp = -1;
				OUTERLOOP :
				for ( int i = 1; i <= 6; i++ )
					for ( int j = 1; j <=6; j++ )
						if ( ( (String) tower.get (towerIndex).get (i) ).equals( (String) pool.get (poolIndex).get (j) ) )
						{
							if ( Double.parseDouble ( (String) tower.get (towerIndex).get (0) )
								>= Double.parseDouble ( (String) pool.get (poolIndex).get (0) ) )
							{
								tower.get (towerIndex).add (i);
								p_block_newFaceUp = j + (int) Math.pow ( -1, j + 1 );
								insertAtNext = true;
								break OUTERLOOP;
							}
							else
							{
								faceUp = i + (int) Math.pow (-1, i + 1 );
								tower.get (towerIndex).add (faceUp);
								p_block_newFaceUp = j;
								break OUTERLOOP;
							}
						}
			break;
		}
		
		// Adds pool block to Tower if compatible (removes block from Pool)
		if (p_block_newFaceUp != -1)
		{
			 		//$
			 		if ( towerIndex == tower.size() -1 )
			 			System.out.print("\t");
			 		System.out.print("\t\t\t\t\t\tColor comparison successful. Block inserted at index ");
	 		
			pool.get (poolIndex).add (p_block_newFaceUp);
			
			if ( !insertAtNext )
					//$ Remove brackets at end
				
			{
				tower.add ( towerIndex, pool.get (poolIndex) );
				
			 		//$
			 		System.out.println(towerIndex + ".");
			}
			else
					//$ Remove brackets at end
				
			{
				// Inserts pool block after indexed tower block for some "equals" & "default" cases,
				// as well as for all "last" & "between" cases
				tower.add ( towerIndex+1, pool.get (poolIndex) );
				
					//$
			 		System.out.println((towerIndex+1) + ".");
			}

			pool.remove (poolIndex);
			 		
			result = true;
		}
		else
					//$ Remove brackets at end
		
		{
					//$
			 		System.out.println("\t\t\t\t\t\tColor comparison unsuccessful.");
	 		
			result = false;
		}
		
		colorComparisonCounter++;
		return result;
	}
	
	
	
	
	//	** ** ** ** SHOULD BE MOVED TO ITS OWN CLASS ** ** ** **
	//	\/ \/ \/ \/	\/	\/	\/	\/	\/	\/	\/	\/	 \/ \/ \/ \/
	//	displays date and/or time in 4 formats, taking into account leap years.
	
	//	BEWARE, MIGHT HAVE GLITCHES IN PM TIMES.
	//	ALSO THIS COULD REALLY USE SOME STREAMLINING...
    static String convertTime(long millis, String option)
    {
    	String result = new String();
    	final int STARTING_YEAR = 1970;
    	
    	int year, month, day, hour, min, sec, millisec;
    	boolean leapyear = false;
    	
    	sec = (int) (millis/1000);
    	min = sec / 60;
    	hour = min / 60;
    		if (option.toLowerCase().equals("Date & Time".toLowerCase()) || option.toLowerCase().equals("Current Time".toLowerCase()))
    			hour -=6;	//	Time Zone: Central Time
    		//hour += 1;	//	When Daylight Savings Time is in effect
    	day = hour / 24;
    	year = day / 365;
    	
    	millisec = (int) (millis - (long) sec*1000);
    	sec -= min*60;
    	min -= hour * 60;
    		if (option.toLowerCase().equals("Date & Time".toLowerCase()) || option.toLowerCase().equals("Current Time".toLowerCase()))
    			min -= 6 * 60;	//	Time Zone: Central Time		THIS IF BLOCK IS HERE TWICE. SEE 9 LINES ABOVE	*/
    	hour -= day * 24;
    	day -= year * 365;
		
    	day -= (year - 2)/4 + 1 - ((year - 30)%400)/100;
    	day++;	//	I don't understand why this is needed but it is... I don't know if it will always work.
    	//	CHECK if starting date was in fact December 31, 1969 instead of January 1st, 1970.
    	//	And check if possible if time of start was 12:00 AM.
    	
    	if ((STARTING_YEAR + year)%400 == 0 || ((STARTING_YEAR + year)%4 == 0 && (STARTING_YEAR + year)%100 != 0))
    	{
    		leapyear = true;
    		day++;
    	}

    	if (day <= 31)
    		month = 1;
    	else
    		if (day <= (31 + 28))
    		{
    				month = 2;
    				day -= 31;
    		}
    		else
    			if (day <= (31 + 28 + 31))
    			{
    				month = 3;
    				day -= (31 + 28);
    			}
    			else
    				if (day <= (31 + 28 + 31 + 30))
    				{
    					month = 4;
    					day -= (31 + 28 + 31);
    				}
    				else
    					if (day <= (31 + 28 + 31 + 30 + 31))
    					{
    						month = 5;
    						day -= (31 + 28 + 31 + 30);
    					}
    					else
	    					if (day <= (31 + 28 + 31 + 30 + 31 + 30))
	    					{
	    						month = 6;
	    						day -= (31 + 28 + 31 + 30 + 31);
	    					}
    						else
    							if (day <= (31 + 28 + 31 + 30 + 31 + 30 + 31))
    							{
    								month = 7;
    								day -= (31 + 28 + 31 + 30 + 31 + 30);
    							}
    							else
    								if (day <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31))
    								{
    									month = 8;
    									day -= (31 + 28 + 31 + 30 + 31 + 30 + 31);
    								}
    								else
    									if (day <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30))
    									{
    										month = 9;
    										day -= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31);
    									}
    									else
    										if (day <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31))
    										{
    											month = 10;
    											day -= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30);
    										}
    										else
    											if (day <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30))
    											{
    												month = 11;
    												day -= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31);
    											}
    											else
    												if (day <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + 31))
    												{
    													month = 12;
    													day -= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30);
    												}
    												else
    													month = 13;
    	
    	if (leapyear && month == 3 && day == 1)
    	{
    		month = 2;
    		day = 29;
    	}
    	
    	String formattedDay, suffix, formattedMin, formattedSec, formattedMilliSec;
    	
    	if (option.toLowerCase().equals("Date & Time".toLowerCase()))	//	MM/DD/YYYY\tHH:MM:SS
    	{
    		if (month < 10)
    			result = "0";
    			
    		if (day < 10)
    			formattedDay = "0" + day;
    		else
    			formattedDay = Integer.toString(day);
    			
    		if (hour > 12)
    			suffix = "PM";
    		else
    			suffix = "AM";
    		
    		if (min < 10)
    			formattedMin = "0" + min;
    		else
    			formattedMin = Integer.toString(min);
    		
    		if (sec < 10)
    			formattedSec = "0" + sec;
    		else
    			formattedSec = Integer.toString(sec);
    		
    		millisec = (int) (millisec + 1)/10;
    		if (millisec < 10)
    			formattedMilliSec = "0" + millisec;
    		else
    			formattedMilliSec = Long.toString(millisec);
    		
    		result = month + "/" + formattedDay + "/" + (STARTING_YEAR + year) + "\t" + hour%12 + ":" + formattedMin + ":" + formattedSec + ":" + formattedMilliSec + " " + suffix;
    	}
    		else
		    	if (option.toLowerCase().equals("Current Time".toLowerCase()))	//	HH:MM:SS
		    	{
		    		if (hour > 12)
		    			suffix = "PM";
		    		else
		    			suffix = "AM";
		    		
		    		if (min < 10)
		    			formattedMin = "0" + min;
		    		else
		    			formattedMin = Integer.toString(min);
		    		
		    		if (sec < 10)
		    			formattedSec = "0" + sec;
		    		else
		    			formattedSec = Integer.toString(sec);
		    		
		    		result = hour + ":" + formattedMin + ":" + formattedSec + " " + suffix;
		    	}
		    	else
		    		if (option.toLowerCase().equals("Elapsed Time Hours".toLowerCase()))	//	Hhrs Mmin Ssec Lms
		    			result = hour%12 + "hrs " + min + "min " + sec + "sec " + millisec + "ms";
		    		else
		    			if (option.toLowerCase().equals("Elapsed Time Minutes".toLowerCase()))	//	Mmin Ssec Lms
		    				result = (60*hour + min) + "min " + sec + "sec " + millisec + "ms";
		  return result;
    }
}
