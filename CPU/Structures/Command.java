package Structures;
/**Command is a Package of String type
 */
public class Command extends Package
{
	String cmd_name;

	/**Specifies register's index
	 * @param str holds the name of this command - ex: 'AND','ADD'
	 */
	public Command(String str)
	{
		super("CMD");
		cmd_name = str;
	}

	/**Returns the type of this register
	 * @return this commands's name
	 */
	public String type()
	{
		return super.type;
	}

	/**Returns the name of this command
	 * @return this command's name
	 */
	public String data()
	{
		return this.cmd_name;
	}
}