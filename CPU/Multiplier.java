/**This class provides multiplying functions on two LongWords
 * @author Devin O'Brien
 * @version 3 - fixed mistakes from last implementation(RippleAdder updates)
 */
public class Multiplier
{
	/**This method multipliplies two LongWords
	 * @param a operand one
	 * @param b operand two
	 * @return result of a times b
	 */
	public static LongWord multiply(LongWord a, LongWord b)
	{
		if(a.getBit(0).getValue() == 1 && b.getBit(0).getValue() == 1)
		{
			a = LongWord.twos_comp(a);
			b = LongWord.twos_comp(b);
		}

		LongWord total = new LongWord();
		int count = 0;
		for(int i = 31; i > 0; i--)
		{
			if(b.getBit(i).getValue() == 1)
			{
				total = RippleAdder.add(total,a.leftShift(count));
				count++; 
			}
			else
			{
				count++;
			}
		}
		return total;
	}
}