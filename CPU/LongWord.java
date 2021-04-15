import java.lang.Math;
/** Longword Class implements functions from ILongWord for operating on a 32-bit bitString
 * @author Devin O'Brien
 * @version 4 - updated or (why did i have it carry) - add instead of or in twos comp
 */
public class LongWord implements ILongWord
{
	//array for storing bit String
	Bit[] bit_string;

	/**Default Constructor initializes all bits to 0
	 */
	public LongWord()
	{
		bit_string = new Bit[32];
		for(int i = 0; i < 32; i++)
			bit_string[i] = new Bit(0);
	}

	/**Specific LongWord  copies given LongWord into this LongWord
	 */
	public LongWord(LongWord other)
	{
		bit_string = new Bit[32];
		for(int i = 0; i < 32; i++)
			bit_string[i].set(other.getBit(i).getValue());
	}

	/**Specific LongWord  copies given String LongWord into this LongWord
	 */
	public LongWord(String other)
	{
		bit_string = new Bit[32];
		for(int i = 0; i < 32; i++)
			bit_string[i] = new Bit((Integer.parseInt(String.valueOf(other.charAt(i)))));
	}

	/**Constructor sets bit string to value
	 */
	public LongWord(int value)
	{
		bit_string = new Bit[32];
		for(int i = 0; i < 32; i++)
			bit_string[i] = new Bit(0);


		boolean negative = false;
		if(value < 0)
		{
			negative = true;
			value *= (-1);
		}

		int p = 31; //power of two - for retreiving bit value
		int current_val = 0; //current value of binary string
		int bit_val = 0; //current value of bit at index
		for(int i = 0; i < 32; i++) //go through every bit in binary string
		{
			bit_val = (int)Math.pow(2,p);
			if(current_val == value)
				i = 33; //break loop
			else if(bit_val == value)
			{
				bit_string[i] = new Bit(1);
				i = 33; //break loop
			}
			else if(bit_val < value)
			{
				if((current_val+bit_val) == value)
				{
					bit_string[i] = new Bit(1);
					i = 33;  //break loop
				}
				else if((current_val+bit_val) < value)
				{
					bit_string[i] = new Bit(1);
					current_val += bit_val;
				}
			}
			p--;
		}
		if(negative == true)
		{
			LongWord temp = new LongWord();
			temp.set(value);

			LongWord neg_bit_str = twos_comp(temp);
			for(int i = 0; i < 32; i++)
			{
				bit_string[i] = neg_bit_str.getBit(i);
			}
		}
	}

	/**Returns true if this LongWord is negative
	 * @return true if this LongWord is negative
	 */
	public boolean isNegative()
	{
		if(bit_string[0].getValue() == 1)
			return true;
		else
			return false;
	}

	/**Find and retreive bit at index
	 * @param index location of bit requested
	 * @return Bit object at requested index
	 */
	public Bit getBit(int index)
	{
		return bit_string[index];
	}

	/**Set bit value at specified index. index range [0-31]
	 * @param index location of assignment
	 * @param value 0 or 1 being assigned to Bit at index
	 */
	public void setBit(int index, Bit value)
	{
		bit_string[index].set(value.getValue());
	}

	/**Perform Binary AND on two longwords(32 bits)
	 * @return longword from result of AND
	 */
	public LongWord and(LongWord other)
	{
		LongWord new_word = new LongWord();
		for(int i = 0; i < 32; i++)
		{
			if(bit_string[i].getValue() == 1 && other.getBit(i).getValue() == 1) 
			{
				new_word.setBit(i,new Bit(1));
			}
			else
			{
				new_word.setBit(i,new Bit(0));
			}
		}
		return new_word;
	}

	/**Perform Binary OR on two longwords(32 bits)
	 * @return longword from result of OR
	 */
	public LongWord or(LongWord other)
	{
		LongWord new_word = new LongWord();
		int carry = 0;
		for(int i = 31; i >= 0; i--)
		{
			if(this.getBit(i).getValue() == 1 || other.getBit(i).getValue() == 1)
				new_word.setBit(i,new Bit(1));
		}
		return new_word;
	}

	/**Perform Binary XOR on two longwords(32 bits)
	 * @return longword from result of XOR
	 */
	public LongWord xor(LongWord other)
	{
		LongWord new_word = new LongWord();
		for(int i = 0; i <=31; i++ )
		{
			if(bit_string[i].getValue() == 1)
			{
				if(other.getBit(i).getValue() == 0)
				{
					new_word.setBit(i,new Bit(1));
				}
			}
			else
			{
				if(other.getBit(i).getValue() == 1)
				{
					new_word.setBit(i,new Bit(1));
				}
			}
		}
		return new_word;
	}

	/**Perform Binary NOT on this longword(32 bits)
	 * @return result from NOT of this LongWord
	 */
	public LongWord not()
	{
		LongWord new_word = new LongWord();
		for(int i = 0; i < 32; i++)
		{
			if(bit_string[i].getValue() == 0)
			{
				new_word.setBit(i,new Bit(1));
			}
			else
			{
				new_word.setBit(i,new Bit(0));
			}
		}
		return new_word;
	}

	/**Rightshift this LongWord by specified index and return new LongWord
	 * @param amount number of digits to shift right
	 */
	public LongWord rightShift(int amount)
	{
		LongWord newWord = new LongWord();
		for(int i = 0, j = amount; i < (32-amount); i++,j++)
		{
			newWord.setBit(j,bit_string[i]);
		}
		return newWord;
	}

	/**Leftshift this LongWord by specified index and return new LongWord
	 * @param amount number of digits to shift left
	 */
	public LongWord leftShift(int amount)
	{
		LongWord newWord = new LongWord();
		for(int i = amount, j = 0; i < 32; i++,j++)
		{
			newWord.setBit(j,bit_string[i]);
		}

		return newWord;
	}

	/**Prints out 32 Bit LongWord as String
	 * @return String representation of this LongWord
	 */
	public String toString()
	{
		String str = "";
		for(Bit bit : bit_string)
		{
			str = str.concat(Integer.toString(bit.getValue()));
		}
		return str;
	}

	/**Returns this LongWord as a long
	 * @return long of this LongWord
	 */
	public long getUnsigned()
	{
		long total = 0;
		int p = 31; //exponent for 2^p
		int bit_val = 0; //current value of bit at index
		for(int i = 0; i < 32; i++)
		{
			bit_val = (int)Math.pow(2,p); p--;
			if(bit_string[i].getValue() == 1)
			{
				total += bit_val;
			}
		}
		return total;
	}

	/**Returns this LongWord as a int
	 * @return int of this LongWord
	 */
	public int getSigned()
	{
		int value = 0; //value returned
		int p = 31; //exponent for 2^p
		int bit_val = 0; //current value of bit at index

		if(bit_string[0].getValue() == 1)
		{
			LongWord this_word = twos_comp(new LongWord(this.toString()));

			for(int i = 0; i < 32; i++)
			{
				bit_val = (int)Math.pow(2,p); p--;

				if(this_word.getBit(i).getValue() == 1)
				{
					value += bit_val;
				}			
			}

			value *= (-1);
		}
		else
		{
			for(int i = 0; i < 32; i++)
			{
				bit_val = (int)Math.pow(2,p); p--;

				if(bit_string[i].getValue() == 1)
				{
					value += bit_val;
				}
			}
		}
		
		return value;
	}

	/**Copies another LongWord into this LongWord
	 * @param other LongWord to be copied into this LongWord
	 */
	public void copy(LongWord other)
	{
		for(int i = 0; i < 32; i++)
		{
			bit_string[i] = other.getBit(i);
		}
	}

	/**Set all bit values of this LongWord to value
	 * @param value to be assigned to all Bits of this LongWord
	 */
	public void set(int value)
	{
		//key for detecting negative numbers
		boolean negative = false;
		if(value < 0)
		{
			negative = true;
			value *= (-1);
		}

		int p = 31; //power of two - for retreiving bit value
		int current_val = 0; //current value of binary string
		int bit_val = 0; //current value of bit at index
		for(int i = 0; i < 32; i++) //go through every bit in binary string
		{
			bit_val = (int)Math.pow(2,p);
			if(current_val == value)
			{
				i = 33; //break loop
			}
			else if(bit_val == value)
			{
				bit_string[i].set(1);
				i = 33; //break loop
			}
			else if(bit_val < value)
			{
				if((current_val+bit_val) == value)
				{
					bit_string[i].set(1);
					i = 33;  //break loop
				}
				else if((current_val+bit_val) < value)
				{
					bit_string[i].set(1);
					current_val += bit_val;
				}
			}
			p--;
		}
		if(negative == true)
		{
			LongWord temp = new LongWord();
			temp.set(value);

			LongWord neg_bit_str = twos_comp(temp);
			for(int i = 0; i < 32; i++)
			{
				bit_string[i] = neg_bit_str.getBit(i);
			}
		}
	}

	/**Performs two's complement on a LongWord
	 * @param word bit string to be converted to negative
	 */
	public static LongWord twos_comp(LongWord word)
	{
		LongWord new_word = word.not();
		new_word = RippleAdder.add(new_word,(new LongWord(1)));
		return new_word;
	}
}























