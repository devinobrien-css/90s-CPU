package Structures;
/**Exception handler for empty list access
 */
 public class EmptyElementException extends NullPointerException 
 {
 	public EmptyElementException(String error)
 	{
 		System.out.println("Empty Element Error: " + error);
 	}
 }