package Structures;
/** Node for lexer,parser Queue
 * @author Devin O'Brien
 * @version 1
 */
public class QueueNode
{
	QueueNode next;
	Package pkg;

	/**Creates new node with data
	 * @param data data being added to null-referenced node
	 */
	public QueueNode(String type,String data) throws InvalidInputException
	{
		this.next = null;
			if(type.equals("REG")) 
			{
				pkg = new Register(data);
			}
			else if(type.equals("ID"))
			{
				pkg = new Identifier(data);
			}
			else if(type.equals("CMD"))
			{
				pkg = new Command(data);
			}
			else
			{
				throw new InvalidInputException("Attempted constructor call QueueNode.QueueNode(String type, String data) line 14: Invalid type specification.");
			}
	}

	/**Creates a copy of an existent node
	 * @param node node to be copied
	 */
	public QueueNode(QueueNode node)
	{
		this.next = node.next;
		this.pkg = node.pkg;
	}

	/**Creates new node with data and reference
	 * @param next next reference to be set for this node
	 * @param data data to be set for this node
	 */
	public QueueNode(QueueNode next, Package data)
	{
		
		this.next = next;
		this.pkg = pkg;
	}

	/**Sets next reference
	 * @param next node to be forward reference of this node
	 */
	public void setNext(QueueNode next)
	{
		this.next = next;
	}

	/**Gets next reference
	 * @return forward reference of this node
	 */
	public QueueNode getNext()
	{
		return this.next;
	}

	/**Gets this nodes package
	 * @return  this nodes package
	 */
	public Package getPkg()
	{
		return this.pkg;
	}

	/**Returns this node as a string
	 * @return string representation of this node
	 */	
	public String toString()
	{
		return this.pkg.data();
	}
}