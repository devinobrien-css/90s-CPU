package Structures;
/**Exception handler for invalid input
 */
public class InvalidInputException extends RuntimeException 
{
	public InvalidInputException(String error)
	{
		System.out.println("Invalid Input Error: " + error);
	}
}