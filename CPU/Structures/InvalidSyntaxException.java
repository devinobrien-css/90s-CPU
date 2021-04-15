package Structures;
/**Exception handler for invalid syntax
 */
public class InvalidSyntaxException extends Exception 
{
	public InvalidSyntaxException(String error)
	{
		System.err.println("Invalid Syntax Exception: " + error);
	}
}