package Structures;
/**Identifier is a Package of numeric type
 */
public class Identifier extends Package
{
	int value; //holds numerical value

	/**Specifies register's index
	 * @param num holds the numerical value being stored
	 */
	public Identifier(String num)
	{
		super("ID");
		value = Integer.parseInt(num);
	}

	/**Returns the type of this register
	 * @return this commands's name
	 */
	public String type()
	{
		return super.type;
	}

	/**Returns the index value of this register
	 * @return this register's index
	 */
	public String data()
	{
		return ""+this.value;
	}
}