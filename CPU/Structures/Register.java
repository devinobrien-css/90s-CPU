package Structures;
/**Register is a Package of numeric type
 */
public class Register extends Package
{
	int index;//holds the register's index

	/**Specifies register's index
	 * @param str holds the name of this register - ex: 'R0'
	 */
	public Register(String str)
	{
		super("REG");

		if(str.length() > 2)
			index = Integer.parseInt(""+str.charAt(1)+""+str.charAt(2));
		else
			index = Integer.parseInt(""+str.charAt(1));

	}

	/**Returns the type of this register
	 * @return this register's type
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
		return ""+this.index;
	}
}