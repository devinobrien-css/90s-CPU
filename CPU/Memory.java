/** This class is intended to mimic basic principles of CPU memory manipulaition
 * @author Devin O'Brien	
 * @version 1.0`
 */
public class Memory
{
	static Bit[] mem = new Bit[8192];

	/**Default Constructor
	 * instantiates all bits
	 */
	public Memory()
	{
		for(int i = 0; i < 8192; i++)
		{
			mem[i] = new Bit();
		}
	}
	/**Reads block of memory and returns LongWord
	 * @param address location of data being reteived
	 */
	public static LongWord read(LongWord address)
	{
		LongWord data = new LongWord();
		int start = (int)address.getUnsigned() * 8;
		for(int i = 0; i < 32; i ++)
		{
			data.setBit(i,mem[start+i]);
		}
		return data;
	}

	/**Writes block of data to memory
	 * @param address location to be written to
	 * @param value obj being assinged to address
	 */
	public static void write(LongWord address, LongWord value)
	{
		int start = (int)address.getUnsigned() * 8;
		for(int i = 0; i < 32; i++)
		{
			mem[start+i] = value.getBit(i);
		}
	}

	/**Dumps all of memory to console
	 */
	public static void dump()
	{
		for(int i = 0; i < 8192; i++ )
			System.out.print(mem[i]);
		System.out.println();
	}

	/**Prints all of memory to console orderly by LongWord
	 */
	public static void print()
	{
		int j = 0;
		for(int i = 0; i < 8192; i++ )
		{
			for(j = 0; j < 32; j++)
			{
				System.out.print(mem[j+i]);
			}
			System.out.println("\n"); i+=j-1;
		}
	}

	/**Clears memory bank
	*/
	public static void clear()
	{
		for(int i = 0; i < 8192; i++ )
			mem[i] = new Bit();
	}

	/**Checks if memory location is empty
	 * @param address location being evaluated
	 */
	public static boolean isEmpty(LongWord address)
	{
		int start = (int)address.getUnsigned() * 8;
		for(int i = start; i < start + 32; i++)
		{
			if(mem[i].getValue() != 0)
				return false;
		}
		return true;
	}

	
}