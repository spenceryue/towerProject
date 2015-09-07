
public class TimeReporter {

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
