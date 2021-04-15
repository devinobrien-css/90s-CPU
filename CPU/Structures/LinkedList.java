package Structures;
/**Buffer for holding lines from a file
 */
public class LinkedList
{
	LinkNode head;
	int size;

	/**Default constructor
	 */
	public LinkedList() 
	{
		head = null;
		size = 0;
	}

	/**Copies LinkedList into this List
	 * @param list list to be copied
	 */
	public LinkedList(LinkedList list)
	{
		this.head = list.head;
		this.size =list.size;
	}

	/**Adds element to the end of this list
	 * @param node element to be added
	 */	
	public void add(LinkNode node)
	{
		if(isEmpty())
			head = node;
		else
		{
			LinkNode temp = tail();
			temp.setNext(node);
		}
		size++;
	}

	/**Retrieves ending node of list
	 * @return the tail of this list
	 * @throws EmptyElementException if this list is empty
	 */	
	public LinkNode tail() throws EmptyElementException
	{
		if(isEmpty())
			throw new EmptyElementException("\nAttempted method call LinkedList.tail() line 45: List is empty");
		else
		{
			LinkNode temp = head;
			while(temp.getNext() != null)
				temp = temp.getNext();
			return temp;
		}
	}

	/**Returns and removes the next node in line
	 * @return a copy of the leading node
	 * @throws EmptyElementException if this list is empty
	 */
	public LinkNode next() throws EmptyElementException
	{
		if(isEmpty())
			throw new EmptyElementException("\nAttempted method call LinkedList.next() line 62: List is empty");
		else
		{
			LinkNode temp = new LinkNode(head);

			head = head.getNext();

			temp.setNext(null);

			return temp;
		}
	}

	/**Returns true if next node isn't null
	 * @return true if next node isn't null
	 */
	public boolean hasNext() 
	{
		return !isEmpty();
	}

	/**Prints list to console
	 */	
	public void print()
	{
		LinkNode temp = head;
		while(temp.getNext() != null)
		{
			System.out.println(temp.toString());
			temp = temp.getNext();
		}
	}

	/**Returns true if this list is empty
	 * @return true if this list is empty
	 */
	public boolean isEmpty()
	{
		return (head == null);
	}
}