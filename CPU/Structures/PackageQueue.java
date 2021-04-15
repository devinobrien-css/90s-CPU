package Structures;
/**Queue for lexer and parser
 * @author Devin O'Brien
 * @version 1
 */
public class PackageQueue
{
	QueueNode head;
	int size;

	/**Instantiates an empty list
	 */
	public PackageQueue()
	{
		head = null;
		size = 0;
	}

	/**Copys an existent queue into this queue
	 * @param queue being copied
	 */
	public PackageQueue(PackageQueue q)
	{
		this.head = q.head;
		this.size = q.size;
	}

	/**Returns size
	 * @return size of this list
	 */ 
	public int size()
	{
		return this.size;
	}

	/**Adds element to the end of this list
	 * @param node element to be added
	 */	
	public void add(QueueNode node)
	{
		if(isEmpty())
			head = node;
		else
		{
			QueueNode temp = tail();
			temp.setNext(node);
		}
		size++;
	}

	/**Retrieves ending node of list
	 * @return the tail of this list
	 * @throws EmptyElementException if this list is empty
	 */	
	public QueueNode tail() throws EmptyElementException
	{
		if(isEmpty())
			throw new EmptyElementException("\nAttempted method call PackageQueue.tail() line 46:  List is empty.");
		else
		{
			QueueNode temp = head;
			while(temp.getNext() != null)
				temp = temp.getNext();
			return temp;
		}
	}

	/**Returns and removes the next node in line
	 * @return a copy of the leading node
	 * @throws EmptyElementException if this list is empty
	 */
	public QueueNode next() throws EmptyElementException
	{
		if(isEmpty())
			throw new EmptyElementException("\nAttempted method call PackageQueue.next() line 63:  List is empty.");
		else
		{
			QueueNode temp = new QueueNode(head);

			head = head.getNext();

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

	/**Returns true if this list is empty
	 * @return true if this list is empty
	 */
	public boolean isEmpty()
	{
		return (head == null);
	}

	/**Prints entire queue's values to screen for testing
	 */
	public void print()
	{
		QueueNode temp = head;
		while(temp != null)
		{
			System.out.println(temp.toString());
			temp = temp.getNext();
		}
	}
}