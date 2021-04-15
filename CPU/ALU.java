/**This class handles the basic functions of an ALU operating on two LongWords
 * @author Devin O'Brien
 * @version 1
 */
public class ALU
{
	/**Accepts two LongWords and performs specified operation
	 * @param operation bit string of 4 specifying which operation to be performed
	 * @param a operand 1
	 * @param b operand 2
	 * @return result of operation on operand(s)
	 */
	public static LongWord doOp(Bit[] operation, LongWord a, LongWord b)
	{
		if((operation[0].getValue() == 1) && (operation[1].getValue() == 0) && (operation[2].getValue() == 0) && (operation[3].getValue() == 0)) //AND
		{
			return a.and(b);
		}
		else if((operation[0].getValue() == 1) && (operation[1].getValue() == 0) && (operation[2].getValue() == 0) && (operation[3].getValue() == 1)) //OR
		{
			return a.or(b);
		}
		else if((operation[0].getValue() == 1) && (operation[1].getValue() == 0) && (operation[2].getValue() == 1) && (operation[3].getValue() == 0)) //XOR
		{
			return a.xor(b);
		}
		else if((operation[0].getValue() == 1) && (operation[1].getValue() == 0) && (operation[2].getValue() == 1) && (operation[3].getValue() == 1)) //NOT
		{
			return a.not();
		}
		else if((operation[0].getValue() == 1) && (operation[1].getValue() == 1) && (operation[2].getValue() == 0) && (operation[3].getValue() == 0)) //LEFTSHIFT
		{
			return a.leftShift((int)b.getUnsigned());
		}
		else if((operation[0].getValue() == 1) && (operation[1].getValue() == 1) && (operation[2].getValue() == 0) && (operation[3].getValue() == 1)) //RIGHTSHIFT
		{
			return a.rightShift((int)b.getUnsigned());
		}
		else if((operation[0].getValue() == 1) && (operation[1].getValue() == 1) && (operation[2].getValue() == 1) && (operation[3].getValue() == 0)) //ADD
		{
			return RippleAdder.add(a,b);
		}
		else if((operation[0].getValue() == 1) && (operation[1].getValue() == 1) && (operation[2].getValue() == 1) && (operation[3].getValue() == 1)) //SUBTRACT
		{
			return RippleAdder.subtract(a,b);
		}
		else if((operation[0].getValue() == 0) && (operation[1].getValue() == 1) && (operation[2].getValue() == 1) && (operation[3].getValue() == 1)) //MULTIPLY
		{
			return Multiplier.multiply(a,b);
		}
		else
		{
			return a;
		}
	}
}