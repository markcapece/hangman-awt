package hawt;

abstract class Gallows {
	
	// Print number of misses as hanged man
	public static String g = new String();
	public static String current_gallow(int misses) {	
		if(misses == 0)
		{
			g = "   -------- \n   |      | \n   |        \n   |        \n   |        \n   |        \n------      ";
		} else if(misses == 1)
		{
			g = "   -------- \n   |      | \n   |      O \n   |        \n   |        \n   |        \n------      ";
		} else if (misses == 2)
		{
			g = "   -------- \n   |      | \n   |      O \n   |      | \n   |        \n   |        \n------      ";
		} else if (misses == 3)
		{
			g = "   -------- \n   |      | \n   |      O/\n   |      | \n   |        \n   |        \n------      ";
		} else if (misses == 4)
		{
			g = "   -------- \n   |      | \n   |     \\O/\n   |      | \n   |        \n   |        \n------      ";
		} else if (misses == 5)
		{
			g = "   -------- \n   |      | \n   |     \\O/\n   |      | \n   |     /  \n   |        \n------      ";
		} else if (misses == 6)
		{
			g = "   -------- \n   |      | \n   |     \\O/\n   |      | \n   |     / \\\n   |        \n------      ";
		} else 
		{
			g = "Error";
		}
		return g;
		}
	}
