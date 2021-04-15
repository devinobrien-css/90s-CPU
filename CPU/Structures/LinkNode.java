package Structures;
/** Node for single line from file
 * @author Devin O'Brien
 * @version 1
 */
public class LinkNode
{
	LinkNode next;
	String data; //line from file

	/**Creates new empty node
	 */
	public LinkNode()
	{
		next = null;
		data = "";
	}

	/**Creates new node with data
	 * @param data data being added to null-referenced node
	 */
	public LinkNode(String data)
	{
		this.next = null;
		this.data = data;
	}

	/**Creates a copy of an existent node
	 * @param node node to be copied
	 */
	public LinkNode(LinkNode node)
	{
		this.next = node.next;
		this.data = node.data;
	}

	/**Creates new node with data and reference
	 * @param next next reference to be set for this node
	 * @param data data to be set for this node
	 */
	public LinkNode(LinkNode next,String data)
	{
		
		this.next = next;
		this.data = data;
	}

	/**Sets next reference
	 * @param next node to be forward reference of this node
	 */
	public void setNext(LinkNode next)
	{
		this.next = next;
	}

	/**Gets next reference
	 * @return forward reference of this node
	 */
	public LinkNode getNext()
	{
		return this.next;
	}

	/**Returns this node as a string
	 * @return string representation of this node
	 */	
	public String toString()
	{
		return this.data;
	}
}