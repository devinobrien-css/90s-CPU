package Structures;
/**Parent class for Register, Identifier and Command
 */
public abstract class Package 
{
	String type;//package type

	/**Instantiates new empty package
	 */
	public Package()
	{
		type = "";
	}

	/**Instantiates new package with given type and data
	 * @param type specifies the type of this package
	 * @param data specifies the data of this package
	 * @throws InvalidInputException if type or data entry is corrupt
	 */
	public Package(String type) throws InvalidInputException
	{
		if(((!type.equals("REG")) && (!type.equals("CMD")) && (!type.equals("ID"))) && (!type.equals("INTR")) && (!type.equals("CALL")) && (!type.equals("RET")) && (!type.equals("POP")) && (!type.equals("PSH")))
			throw new InvalidInputException("Attempted constructor call Package.Package(String type) line 22: Incorrect Type Specification: " + type + ".");
		else
		{
			this.type = type;
		}
	}

	/**Copies package into this package
	 * @param pkg package to be copied
	 */ 
	public Package(Package pkg)
	{
		this.type = pkg.type;
	}

	/**Returns the type of this object - overridden in subclasses
	 * @return this object's type
	 */
	public abstract String type();

	/**Returns the data of this object - overridden in subclasses
	 * @return this object's data
	 */
	public abstract String data();

}