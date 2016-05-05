import java.util.Comparator;

public class ByLastColumn implements Comparator<String[]>
{
	public int compare(String[] stringArray1, String[] stringArray2)
	{
		String s1 = stringArray1[stringArray1.length - 1];
		String s2 = stringArray2[stringArray2.length - 1];
		return s1.compareTo(s2);
	}
}