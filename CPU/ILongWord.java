/** Longword interface provides functions for operating on a 32-bit bitString
 * @author Devin O'Brien
 * @version 1
 */
public interface ILongWord
{
	/**Find and retreive bit at index
	 * @param index location of bit requested
	 * @return Bit object at requested index
	 */
	public Bit getBit(int index);

	/**Set bit value at specified index
	 * @param index location of assignment
	 * @param value 0 or 1 being assigned to Bit at index
	 */
	public void setBit(int index, Bit value);

	/**Perform Binary AND on two longwords(32 bits)
	 * @return longword from result of AND
	 */
	public LongWord and(LongWord other);

	/**Perform Binary OR on two longwords(32 bits)
	 * @return longword from result of OR
	 */
	public LongWord or(LongWord other);

	/**Perform Binary XOR on two longwords(32 bits)
	 * @return longword from result of XOR
	 */
	public LongWord xor(LongWord other);

	/**Perform Binary NOT on this longword(32 bits)
	 * @return result from NOT of this LongWord
	 */
	public LongWord not();

	/**Rightshift this LongWord by specified index and return new LongWord
	 * @param amount number of digits to shift right
	 */
	public LongWord rightShift(int amount);

	/**Leftshift this LongWord by specified index and return new LongWord
	 * @param amount number of digits to shift left
	 */
	public LongWord leftShift(int amount);

	/**Prints out 32 Bit LongWord as String
	 * @return String representation of this LongWord
	 */
	public String toString();

	/**Returns this LongWord as a long
	 * @return long of this LongWord
	 */
	public long getUnsigned();

	/**Returns this LongWord as a int
	 * @return int of this LongWord
	 */
	public int getSigned();

	/**Copies another LongWord into this LongWord
	 * @param other LongWord to be copied into this LongWord
	 */
	public void copy(LongWord other);

	/**Set all bit values of this LongWord to value
	 * @param value to be assigned to all Bits of this LongWord
	 */
	public void set(int value);
}
