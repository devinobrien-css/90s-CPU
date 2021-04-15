/** Bit class implements the IBit class for bit conversions
 * @author Devin O'Brien
 * @version 2
 */
public class Bit implements IBit
{
	int bit;

	/**General Constructor
	 * initializes bit to 0
	 */
	Bit()
	{
		this.bit = 0;
	}

	/**Param Constructor
	 * initializes bit to param value
	 * ensures value is 0 or 1
	 */
	Bit(int value)
	{
		if(value > 1 || value < 0)
		{
			System.out.println("Error: invalid input - auto assigning 0 to bit");
			this.bit = 0;
		}
		else
			this.bit = value;
	}

	/**sets value of the bit
	 * @param value 0 or 1 for bit assignment
	 * ensures value is 0 or 1
	 */
	public void set(int value)
	{
		if(value > 1 || value < 0)
		{

			System.out.println("Error: invalid input - auto assigning 0 to bit");
			this.bit = value;
		}
		else
			this.bit = value;
	}

	/**change value to 0 or 1
	 */
	public void toggle()
	{
		if(this.bit == 0)
			this.bit = 1;
		else
			this.bit = 0;
	}

	/**sets bit value to 1
	 */
	public void set()
	{
		this.bit = 1;
	}

	/**sets bit value to 0
	 */
	public void clear()
	{
		this.bit = 0;
	}

	/**returns current bit value
	 * @return 0 or 1 depending on value
	 */
	public int getValue()
	{
		return this.bit;
	}

	/**performs binary AND
	 * @param other comparison bit for AND
	 * @return result of this bit AND other
	 */
	public Bit and(Bit other)
	{
		int value = other.getValue();

		if(this.bit == 0)
			return new Bit(0);
		else //bit == 1
		{
			if(value == 1)
				return new Bit(1);
			else //other == 0
				return new Bit(0);
		}
	}

	/**performs binary OR
	 * @param other comparison bit for OR 
	 * @return result of this bit OR other
	 */
	public Bit or(Bit other)
	{
		int value = other.getValue();

		if(this.bit == 1)
			return new Bit(1);
		else //bit == 0 
		{
			if(value == 1)
				return new Bit(1);
			else //other == 0 
				return new Bit(0);
		}
	}

	/**performs binary XOR
	 * @param other comparison bit for XOR 
	 * @return result of this bit XOR other
	 */
	public Bit xor(Bit other)
	{
		int value = other.getValue();

		if(this.bit == 0)
		{
			if(value == 1)
				return new Bit(1);
			else //other == 0
				return new Bit(0);
		}
		else //bit == 1
		{
			if(value == 0)
				return new Bit(1);
			else //other == 1
				return new Bit(0);
		}
	}

	/**performs bit negation
	 * @return negation of bit
	 */
	public Bit not()
	{
		if(this.bit == 0)
			return new Bit(1);
		else //bit == 1
			return new Bit(0);
	}

	/**returns 0 or 1
	 * @return 0 or 1 as string
	 */
	public String toString()
	{
		return "" + this.bit;
	}
}