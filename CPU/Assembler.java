/**Assember class for CPU
 * @author Devin O'Brien
 * @version 1.0
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;
import Structures.*;

/** Assembler - lexer & parser
 *	- Reads lines from file input.txt and puts into Linked List buffer
 *  - Linked List buffer is sent to the lexer and pushed into a Queue of Packages
 *		• A Package can be of types Command, Register or Identifier
 *	- Queue of Packages is then sent to the parser, which validates the input,
 *     converts it to binary, and assembles a list of instructions.
 *	- List of instructions is returned by assemble method
 *
 *
 * NAME:  | CODE:  | BITS:
 *--------+--------+------
 * Halt   |  HLT   | 0000
 * Move   |  MOV   | 0001
 * Dump   |  REG   | 0010 ... 0000
 * Dump   |  MEM   | 0010 ... 0001
 * And    |  AND   | 1000
 * Or     |  OR    | 1001
 * Xor    |  XOR   | 1010
 * Not    |  NOT   | 1011
 * LeftS  |  LSH   | 1100
 * Right  |  RSH   | 1101
 * Add    |  ADD   | 1110
 * Subtr  |  SUB   | 1111
 * Multi  |  MUL   | 0111
 * Jump   |  JMP   | 0011
 * Compr  |  CMP   | 0100
 * IfEq   |	 BIE   | 010101
 * If!Eq  |  BINE  | 010100
 * IfGr   |  BIGT  | 010110
 * IfGrEq |  BIGE  | 010111
 */
public class Assembler
{
	/**Initiates assembler by opening specified file and reading into buffer
	 * @param filename name of assembly file
	 */
	public static String[] assemble(String filename)
	{
		try
		{
			File in_file = new File(filename);
			Scanner input = new Scanner(in_file);

			LinkedList buffer = new LinkedList();			

			//READ LINE BY LINE
			while(input.hasNextLine())
			{
				buffer.add(new LinkNode(input.nextLine())); //adds line from file to buffer
			}
			input.close();

			PackageQueue pkgs = new PackageQueue();
				lex(buffer,pkgs);

			String[] instruction_list = new String[256];
				parse(pkgs,instruction_list);

			return instruction_list;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error in Assembler.assemble() : file not found in directory");
		}

		return null;
	}

	/**Reads buffer line by line, assembles list of separated commands, registers and values
	 * @param buffer lines pulled from file
	 * @param pkgs queue of packages - each package is a command, identifier or register
	 * @throws InvalidInputException if object type unknown 
	 */
	public static void lex(LinkedList buffer,PackageQueue pkgs) throws InvalidInputException
	{
		String line = ""; //each line of file

		/** This loop scans through a linked list containing lines from the input file
		 */
		while(buffer.hasNext())
		{
			//FETCH LINE FROM LIST
			line = buffer.next().toString();
			if(line.charAt(line.length()-1) != ' ') //add space as terminator if needed
				line = line + " ";
			
			String object = ""; //each word in the current line
			char c = ' '; //each character in the current line 

			/** This loop takes each line and packages the objects into the queue
			 */
			for(int i = 0; i < line.length(); i++)
			{
				c = line.charAt(i);
				
				if(Character.isDigit(c) || Character.isLetter(c) || c == '-')
					object = object + c;
				else
				{	
					if(is_register(object))
						pkgs.add(new QueueNode("REG",object));
					else if(is_move(object) || is_operation(object) || is_interrupt(object) || is_jump(object) || is_compare(object) || is_branch(object) || is_stack_function(object) || is_call(object) || is_return(object))
						pkgs.add(new QueueNode("CMD",object));
					else if(is_identifier(object))
						pkgs.add(new QueueNode("ID",object));
					else
						throw new InvalidInputException("Attempted to add to PackageQueue in Assembler.lex(LinkedList buffer, PackageQueue pkgs) : Invalid object " + object + ".");

					object = "";//reset current word value
				}
			}
		}
	}

	/**Inputs list of packages, validates input for instructions, exports list of bitstring commands
	 * @param pkgs queue of packages - each package is a command, identifier or register
	 * @param 
	 */
	public static void parse(PackageQueue pkgs,String[] instruction_list) throws InvalidInputException
	{
		//SET OF INSTRUCTIONS OF LENGTH 16bit
		String[] instructions = new String[512];
		int count = 0; 

		QueueNode current_pkg;

		/** This loop traverses through the lexical queue of packages
		 */
		while(pkgs.hasNext())
		{
			current_pkg = pkgs.next(); //current pkg is current object(word) in an instruction

			/** This if/else branch ensures th leading package must be a command followed by operands
			 */
			if(current_pkg.getPkg().type().equals("CMD"))
			{
				/** The following if/else branch system validates that the syntax and order of instructions in the queue is correct
				 */
				if(is_operation(current_pkg.toString()))
				{	
					//AN OPERATION TAKES IN THREE REGISTERS
					if(is_register(current_pkg.getNext()) && is_register(current_pkg.getNext().getNext()) && is_register(current_pkg.getNext().getNext().getNext()));
					{
						int op1 = Integer.parseInt(pkgs.next().toString()),op2 = Integer.parseInt(pkgs.next().toString()),dst = Integer.parseInt(pkgs.next().toString());
						instructions[count] = convert(current_pkg.toString(),op1,op2,dst);
						count++;
					}
				}
				else if(is_move(current_pkg.toString()))
				{
					//A MOVE TAKES IN A DESTINATION REGISTER AND A VALUE
					if(is_register(current_pkg.getNext()) && is_identifier(current_pkg.getNext().getNext()))
					{
						int dst = Integer.parseInt(pkgs.next().toString());
						int value = Integer.parseInt(pkgs.next().toString());
						instructions[count] = convert(current_pkg.toString(),dst,value);
						count++;
					}
					else 
						throw new InvalidInputException("Attempted method call at parse(PackageQueue pkgs, LongWord[] instruction_list) line 89: Unexpected input for interrupt: " + current_pkg.toString() + ".");
				}
				else if(is_interrupt(current_pkg.toString()))
				{
					if(current_pkg.toString().equals("MEM"))
					{
						instructions[count] = convert(current_pkg.toString());
						count++;
					}
					else if(current_pkg.toString().equals("REG"))
					{
						instructions[count] = convert(current_pkg.toString());
						count++;
					}
					else if(current_pkg.toString().equals("HLT")) 
					{
						instructions[count] = convert(current_pkg.toString());
						count++;
					}
					else 
						throw new InvalidInputException("Attempted method call at parse(PackageQueue pkgs, LongWord[] instruction_list) line 89: Unexpected input for interrupt: " + current_pkg.toString() + ".");
				}
				else if(is_jump(current_pkg.toString()))
				{
					//A JUMP TAKES IN A DESTINATION ADDRESS
					if(is_identifier(current_pkg.getNext()))
					{
						int address = Integer.parseInt(pkgs.next().toString());
						instructions[count] = convert(current_pkg.toString(),address);
						count++;
					}
				}
				else if(is_compare(current_pkg.toString()))
				{
					//A COMPARISON TAKES IN TWO REGISTERS
					if(is_register(current_pkg.getNext()) && is_register(current_pkg.getNext().getNext()))
					{		
						int op1 = Integer.parseInt(pkgs.next().toString()),op2 = Integer.parseInt(pkgs.next().toString());
						instructions[count] = convert(current_pkg.toString(),op1,op2);
						count++;
					}
				}
				else if(is_branch(current_pkg.toString()))
				{
					//A BRANCH TAKES IN A POSITIVE OR NEGATIVE INTEGER
					if(is_identifier(current_pkg.getNext()))
					{
						int address = Integer.parseInt(pkgs.next().toString());
						
						if(address < 0)
						{
							address *= (-1);
							instructions[count] = convert(current_pkg.toString(),"1",address);
							count++;
						}
						else
						{
							instructions[count] = convert(current_pkg.toString(),"0",address);
							count++;
						}
					}
				}
				else if(is_stack_function(current_pkg.toString()))
				{
					if(is_pop(current_pkg.toString()) && is_register(current_pkg.getNext()))
					{
						int index = Integer.parseInt(pkgs.next().toString());
						instructions[count] = convert(current_pkg.toString(),index);
						count++;

					}
					else if(is_push(current_pkg.toString()) && is_register(current_pkg.getNext()))
					{
						int index = Integer.parseInt(pkgs.next().toString());
						instructions[count] = convert(current_pkg.toString(),index);
						count++;

					}
				}
				else if(is_call(current_pkg.toString()))
				{
					if(is_identifier(current_pkg.getNext()))
					{
						int address = Integer.parseInt(pkgs.next().toString());
						instructions[count] = convert(current_pkg.toString(),address);
						count++;
					}
				}
				else if(is_return(current_pkg.toString()))
				{
					instructions[count] = "0110110000000000";
					count++;
				}
			}
			else
				throw new InvalidInputException("Attempted method call at parse(PackageQueue pkgs, LongWord[] instruction_list) line 89: First object in instruction is not a command: " + current_pkg.toString() + ".");
		}

		//CHECK IF ODD NUMBER OF INSTRUCTIONS - IF ODD, ADD TAILING HALT
		if(count % 2 != 0)
		{
			instructions[count] = "0000000000000000";
			count++;
		}

		instructions[count] = "EOD";//end of list signifier 

		int j = 0;
		for(int i = 0; !instructions[i].equals("EOD"); i+=2,j++)
		{
			instruction_list[j] = (instructions[i] + instructions[i+1]);
		}
	}


	/*************************************************************************************************************/
	/** IDENTITY FUNCTIONS
	 *  the following functions are used to determine the type of an object. 
	 */

	/**Returns true if the given word is a operation
	 * @return true if the given word is a operation
	 */
	public static boolean is_operation(String object)
	{
		return  (object.equals("AND") || object.equals("OR") || object.equals("XOR") || object.equals("NOT") || object.equals("LSH") || object.equals("RSH") || object.equals("ADD") || object.equals("SUB") || object.equals("MUL"));
	}

	/**Returns true if the given word is a comparison
	 * @return true if the given word is a comparison
	 */
	public static boolean is_compare(String object)
	{
		return (object.equals("CMP"));
	}

	/**Returns true if the given word is a branch
	 * @return true if the given word is a branch
	 */
	public static boolean is_branch(String object)
	{
		return (object.equals("BIE") || object.equals("BINE") || object.equals("BIGT") || object.equals("BIGE"));
	}

	/**Returns true if the given word is a move
	 * @return true if the given word is a move
	 */
	public static boolean is_move(String object)
	{
		return (object.equals("MOV"));
	}

	/**Returns true if the given word is a call
	 * @return true if the given word is a call
	 */
	public static boolean is_call(String object)
	{
		return (object.equals("CALL"));
	}

	/**Returns true if the given word is a return
	 * @return true if the given word is a return
	 */
	public static boolean is_return(String object)
	{
		return (object.equals("RET"));
	}

	/**Returns true if the given word is a stack function
	 * @return true if the given word is a stack function
	 */
	public static boolean is_stack_function(String object)
	{
		return ((object.equals("PSH")) || (object.equals("POP")));
	}

	/**Returns true if the given word is a pop
	 * @return true if the given word is a pop
	 */
	public static boolean is_pop(String object)
	{
		return ((object.equals("POP")));
	}

	/**Returns true if the given word is a push
	 * @return true if the given word is a push
	 */
	public static boolean is_push(String object)
	{
		return ((object.equals("PSH")));
	}

	/**Returns true if the given word starts with R and ends with a digit
	 * @return true if the given word starts with R and ends with a digit
	 */    
	public static boolean is_register(String object)
	{
		return (object.charAt(0) == 'R') && (Character.isDigit(object.charAt(1)));
	}

	/**Returns true if the given QueueNode is a register
	 * @return true if the given QueueNode is a register
	 */    
	public static boolean is_register(QueueNode node)
	{
		return (node.getPkg().type().equals("REG"));
	}

	/**Returns true if the given object is an interrupt
	 * @return true if the given object is an interrupt
	 */    
	public static boolean is_interrupt(String object)
	{
		return (object.equals("MEM") || object.equals("REG") || object.equals("HLT"));
	}

	/**Returns true if the given object is a jump
	 * @return true if the given object is a jump
	 */    
	public static boolean is_jump(String object)
	{
		return (object.equals("JMP"));
	}

	/**Returns true if the given object is a number
	 * @return true if the given object is a number
	 */    
	public static boolean is_identifier(String object)
	{
		try
      	{
         	Integer.parseInt(object);
         	return true;
      	}
      	catch (NumberFormatException ex)
      	{
         	return false;
      	}
	}

	/**Returns true if the given QueueNode is an identifier
	 * @return true if the given QueueNode is an identifier
	 */    
	public static boolean is_identifier(QueueNode node)
	{
		return (node.getPkg().type().equals("ID"));
	}

	/*************************************************************************************************************/
	/** OVERRIDEN CONVERT FUNCTIONS
	 *	the following functions convert a package of objects to a runnable 16 bit CPU instruction
	 */

	/**Convert to binary instruction 
	 */    
	public static String convert(String operator,int op1, int op2, int dst)
	{
		String bit_string = "";

		if(operator.equals("AND"))
		{
			bit_string = bit_string + "1000" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("OR"))
		{
			bit_string = bit_string + "1001" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("XOR"))
		{
			bit_string = bit_string + "1010" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("NOT"))
		{
			bit_string = bit_string + "1011" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("LSH"))
		{
			bit_string = bit_string + "1100" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("RSH"))
		{
			bit_string = bit_string + "1101" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("ADD"))
		{
			bit_string = bit_string + "1110" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("SUB"))
		{
			bit_string = bit_string + "1111" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else if(operator.equals("MUL"))
		{
			bit_string = bit_string + "0111" + int_to_four_bit(op1) + int_to_four_bit(op2) + int_to_four_bit(dst);
		}
		else 
			throw new InvalidInputException("Attempted method call at convert(String,int,int,int) line 247: Unexpected input for operator: " + operator + ".");

		return bit_string;
	}

	/**Convert to binary instruction
	 */    
	public static String convert(String operator,int op1, int op2)
	{
		String bit_string = "";

		if(operator.equals("MOV"))
		{
			bit_string = bit_string + "0001" + int_to_four_bit(op1) + int_to_byte(op2);
		}
		else if(operator.equals("CMP"))
		{
			bit_string = bit_string + "01000000" + int_to_four_bit(op1) + int_to_four_bit(op2);
		}
		else 
			throw new InvalidInputException("Attempted method call at convert(String,int,int) line 295: Unexpected input for operator: " + operator + ".");

		return bit_string;
	}

	/**Convert to binary instruction
	 */    
	public static String convert(String operator,int address)
	{
		String bit_string = "";

		if(operator.equals("JMP"))
		{
			bit_string = bit_string + "0011" + int_to_twelve_bit(address);
		}
		else if(operator.equals("PSH"))
		{
			bit_string = bit_string + "011000000000" + int_to_four_bit(address);
		}
		else if(operator.equals("POP"))
		{
			bit_string = bit_string + "011001000000" + int_to_four_bit(address);
		}
		else if(operator.equals("CALL"))
		{
			bit_string = bit_string + "011010" + int_to_ten_bit(address);
		}
		else 
			throw new InvalidInputException("Attempted method call at convert(String,int,int) line 295: Unexpected input for operator: " + operator + ".");

		return bit_string;
	}

	/**Convert to binary instruction
	 */    
	public static String convert(String operator, String sign, int address)
	{
		String bit_string = "";

		if(operator.equals("BIE"))
		{
			bit_string = bit_string + "010101" + sign + int_to_nine_bit(address);
		}
		else if(operator.equals("BINE"))
		{
			bit_string = bit_string + "010100" + sign + int_to_nine_bit(address);
		}
		else if(operator.equals("BIGT"))
		{
			bit_string = bit_string + "010110" + sign + int_to_nine_bit(address);
		}
		else if(operator.equals("BIGE"))
		{
			bit_string = bit_string + "010111" + sign + int_to_nine_bit(address);
		}
		else 
			throw new InvalidInputException("Attempted method call at convert(String,int,int) line 295: Unexpected input for operator: " + operator + ".");

		return bit_string;
	}

	/**Convert to binary instruction
	 */    
	public static String convert(String operator)
	{
		if(operator.equals("MEM"))
		{
			return "0010000000000001";
		}
		else if(operator.equals("REG"))
		{
			return "0010000000000000";
		}
		else if(operator.equals("HLT"))
		{
			return "0000000000000000";
		}
		else 
			throw new InvalidInputException("Attempted method call at convert(String,int,int) line 295: Unexpected input for operator: " + operator + ".");
	}

	/*************************************************************************************************************/
	/** INT TO BINARY STRING FUNCTIONS
	 *  take in an integer value, convert it to a binary string
	 */

	/**Convert to binary
	 * @param num number in range [0-15]
	 */
	public static String int_to_four_bit(int num)
	{
		char[] bit_string = {'0','0','0','0'};

		int pow = 8;

		for(int i = 0; i < 4; i++)
		{
			if((num - pow) >= 0)
			{
				bit_string[i] = '1';
				num -= pow;
				pow /= 2;
			}
			else
				pow /= 2;
		}

		return String.valueOf(bit_string);
	}

	/**Convert to binary
	 * @param num number in range [0-127]
	 */
	public static String int_to_byte(int num)
	{
		char[] bit_string = {'0','0','0','0','0','0','0','0'};

		int pow = 128;

		for(int i = 0; i < 8; i++)
		{
			if((num - pow) >= 0)
			{
				bit_string[i] = '1';
				num -= pow;
				pow /= 2;
			}
			else
				pow /= 2;
		}

		return String.valueOf(bit_string);
	}

	/**Convert to binary
	* @param num number in range [0-2048]
	 */
	public static String int_to_twelve_bit(int num)
	{
		char[] bit_string = {'0','0','0','0','0','0','0','0','0','0','0','0'};

		int pow = 2048;

		for(int i = 0; i < 12; i++)
		{
			if((num - pow) >= 0)
			{
				bit_string[i] = '1';
				num -= pow;
				pow /= 2;
			}
			else
				pow /= 2;
		}

		return String.valueOf(bit_string);
	}

	/**Convert to binary
	* @param num number in range [0-2048]
	 */
	public static String int_to_nine_bit(int num)
	{
		char[] bit_string = {'0','0','0','0','0','0','0','0','0'};

		int pow = 256;

		for(int i = 0; i < 9; i++)
		{
			if((num - pow) >= 0)
			{
				bit_string[i] = '1';
				num -= pow;
				pow /= 2;
			}
			else
				pow /= 2;
		}

		return String.valueOf(bit_string);
	}

	/**Convert to binary
	* @param num number in range [0-2048]
	 */
	public static String int_to_ten_bit(int num)
	{
		char[] bit_string = {'0','0','0','0','0','0','0','0','0','0'};

		int pow = 512;

		for(int i = 0; i < 10; i++)
		{
			if((num - pow) >= 0)
			{
				bit_string[i] = '1';
				num -= pow;
				pow /= 2;
			}
			else
				pow /= 2;
		}

		return String.valueOf(bit_string);
	}

}