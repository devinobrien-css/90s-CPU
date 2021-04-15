/** This class implements LongWord addition and subtraction 
 * @author Devin O'Brien
 * @version 3 - fixed mistakes from original implementation(using XOR and fixed OR in longword)
 */
public class RippleAdder
{
	/**Works with the functions of the LongWord class to add two numbers together
	 * @param a first operand
	 * @param b second operand
	 * @return addition operation on the two operands
	 */
	public static LongWord add(LongWord a, LongWord b)
	{
		LongWord new_word = new LongWord();
		Bit carry_bit = new Bit(0);
		for(int i = 31; i >= 0; i--)
		{
			// carry bit xor a bit xor b bit
			new_word.setBit(i,carry_bit.xor(a.getBit(i).xor(b.getBit(i))));

			//set carry bit
			if((a.getBit(i).getValue() + b.getBit(i).getValue() + carry_bit.getValue()) > 1)
				carry_bit.set(1);
			else
				carry_bit.set(0);
		}
		return new_word;
	}

	/**Works with the functions of the LongWord class to subtract two numbers 
	 * @param a first operand
	 * @param b second operand
	 * @return subtraction operation on the two operands
	 */
	public static LongWord subtract(LongWord a, LongWord b)
	{
		//negate b
		b = LongWord.twos_comp(b);

		return add(a,b);
	}
}










