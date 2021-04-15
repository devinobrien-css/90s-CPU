/**Interface representing Bit manipulation methods
 * @author Devin O'Brien
 * @version 1.0
 */
public interface IBit
{
	/**sets value of the bit
	 * @param value 0 or 1 for bit assignment
	 */
	void set(int value);

	/**change value to 0 or 1
	 */
	void toggle();

	/**sets bit value to 1
	 */
	void set();

	/**sets bit value to 0
	 */
	void clear();

	/**returns current bit value
	 * @return 0 or 1 depending on value
	 */
	int getValue();

	/**performs binary AND
	 * @param other comparison bit for AND
	 * @return result of this bit AND other
	 */
	Bit and(Bit other);

	/**performs binary OR
	 * @param other comparison bit for OR 
	 * @return result of this bit OR other
	 */
	Bit or(Bit other);

	/**performs binary XOR
	 * @param other comparison bit for XOR 
	 * @return result of this bit XOR other
	 */
	Bit xor(Bit other);

	/**performs bit negation
	 * @return negation of bit
	 */
	Bit not();

	/**returns 0 or 1
	 * @return 0 or 1 as string
	 */
	String toString();

}