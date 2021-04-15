/**To be continued - Computer class
 * @author Devin O'Brien
 * @version 2.0
 */
public class Computer
{
	LongWord PC,SP,current_instruction; //program counter, current instruction(16 bits)
	LongWord mask,src,dst,literal_value,address; //mask, source register addressholder, destination register address holder, literal value being assigned to register(move)
	Memory memory; //memory bank of 8192 bits
	LongWord[] register; //16 assignable registers
	LongWord op1,op2; //operands of equation
	LongWord op_result; //result of operation on operands op1 and op2
	Bit[] opcode; //code of operator (first 4 bits from current instrucion)
	Bit status; //status of state (keeps loop running)
	Bit[] cmp_result,condition_code;
	boolean condition;

	/**General Constructor for computer
	 */
	public Computer()
	{
		current_instruction = PC = new LongWord(0);
		literal_value = new LongWord(0);
		register = new LongWord[16];
		op1 = op2 = new LongWord(0);
		op_result = new LongWord(0);
		memory = new Memory();
		opcode = new Bit[4];
		status = new Bit(0);
		cmp_result = new Bit[2];
		condition_code = new Bit[2];
		condition = false;
		SP = new LongWord(1020);
	}

	/**Runs all four functions of CPU
	 */
	public void run()
	{
		while(status.getValue() == 0)
		{
			fetch();
			decode();
			execute();
			store();
		}
	}

	/**Fetch next instruction from memory using 
	 *  the program counter
	 */
	public void fetch()
	{
		current_instruction = memory.read(PC); //read next instruction from memory
		PC = RippleAdder.add(new LongWord(2),PC); //increment program counter
	}

	/**Breaks down current instruction and differentiates the 
	 *  objects according to specified opcode.
	 */
	public void decode()
	{
		//FILL opcode ARRAY
		for(int i = 0; i < 4; i++)
			opcode[i] = current_instruction.getBit(i);

		if(is_move(opcode))
		{
			//FILL dst(DESTINATION) REGISTER VALUE
			mask = new LongWord("00001111000000000000000000000000");
			dst = (mask.and(current_instruction)).rightShift(24);

			//FILL literal_value
			if(current_instruction.getBit(8).getValue() == 1)//negative case
			{
				LongWord tmp = new LongWord();//temporary holder for 8 bit value
				literal_value = new LongWord("11111111111111111111111111111111");//longword of ones (last 8 bits to be replaced by last 8 bits of tmp)

				//FILL 8BITS INTO tmp FROM current_instruction
				mask = new LongWord("00000000111111110000000000000000");
				tmp = ((mask.and(current_instruction)).rightShift(16));

				//FILL LAST 8BITS FROM tmp INTO LAST 8BITS OF literal_value
				for(int i = 24; i < 32; i++)
					literal_value.setBit(i,tmp.getBit(i));
			}
			else//positive case
			{
				LongWord tmp = new LongWord();//temporary holder for 8 bit value
				literal_value = new LongWord();

				//FILL 8BITS INTO tmp FROM current_instruction
				mask = new LongWord("00000000111111110000000000000000");
				tmp = ((mask.and(current_instruction)).rightShift(16));
				
				//FILL LAST 8BITS FROM tmp INTO LAST 8BITS OF literal_value
				for(int i = 24; i < 32; i++)
					literal_value.setBit(i,tmp.getBit(i));
			}

		}
		else if(is_operation(opcode))
		{
			//FILL FIRST OP
			mask = new LongWord("00001111000000000000000000000000");
			src = (mask.and(current_instruction)).rightShift(24); //masking desired bits from instruction and storing for operand 1
			op1 = register[(int)src.getUnsigned()];

			//FILL SECOND OP
			mask = new LongWord("00000000111100000000000000000000");
			src = (mask.and(current_instruction)).rightShift(20); //masking desired bits from instruction and storing for operand 2
			op2 = register[(int)src.getUnsigned()];

			//FILL DESTINATION REGISTER VALUE
			mask = new LongWord("00000000000011110000000000000000");
			dst = (mask.and(current_instruction)).rightShift(16);
		}
		else if(is_jump(opcode))
		{
			mask = new LongWord("00001111111111110000000000000000");
			address = (mask.and(current_instruction)).rightShift(16);
		}
		else if(is_compare(opcode))
		{
			mask = new LongWord("00000000111100000000000000000000");
			src = (mask.and(current_instruction)).rightShift(20); //masking desired bits from instruction and storing for operand 2
			op1 = register[(int)src.getUnsigned()];

			mask = new LongWord("00000000000011110000000000000000");
			src = (mask.and(current_instruction)).rightShift(16); //masking desired bits from instruction and storing for operand 2
			op2 = register[(int)src.getUnsigned()];
		}
		else if(is_branch(opcode))
		{
			mask = new LongWord("00000001111111110000000000000000");
			address = (mask.and(current_instruction)).rightShift(16);

			condition_code[0] = current_instruction.getBit(4);
			condition_code[1] = current_instruction.getBit(5);
		}
		else if(is_stack_function(opcode))
		{
			//POP
			if((current_instruction.getBit(4).getValue() == 0) && (current_instruction.getBit(5).getValue() == 1))
			{
				mask = new LongWord("00000000000011110000000000000000");
				dst = (mask.and(current_instruction)).rightShift(16);
				
			}//PUSH
			else if((current_instruction.getBit(4).getValue() == 0) && (current_instruction.getBit(5).getValue() == 0))
			{
				mask = new LongWord("00000000000011110000000000000000");
				src = (mask.and(current_instruction)).rightShift(16);
				op1 = register[(int)src.getUnsigned()];
			}//CALL
			else if((current_instruction.getBit(4).getValue() == 1) && (current_instruction.getBit(5).getValue() == 0))
			{
				mask = new LongWord("00000011111111110000000000000000");
				address = (mask.and(current_instruction)).rightShift(16);
			}
		}
	}

	/**Execute with opcode and operands derived from decode
	*/
	public void execute()
	{
		if(is_halt(opcode))
			this.halt();
		else if(is_operation(opcode))
			op_result = ALU.doOp(opcode,op1,op2);
		else if(is_interrupt(opcode))
		{
			if(current_instruction.getBit(15).getValue() == 1)//dump memory
				memory.dump();
			else
				print_registers();
		}
		else if(is_compare(opcode))
		{
			int value = (int)(op1.getUnsigned() - op2.getUnsigned());

			if(value == 0)
			{
				cmp_result[0] = new Bit(0);
				cmp_result[1] = new Bit(1);
			}
			else if(value > 0)
			{
				cmp_result[0] = new Bit(1);
				cmp_result[1] = new Bit(0);
			}
			else if(value < 0)
			{
				cmp_result[0] = new Bit(0);
				cmp_result[1] = new Bit(0);
			}
		}
		else if(is_branch(opcode))
		{
			if((condition_code[0].getValue() == 0) && (condition_code[1].getValue() == 1)) //EQUALS
			{
				if(cmp_result[1].getValue() == 1)
					condition = true;
				else
					condition = false;
			}
			else if((condition_code[0].getValue() == 0) && (condition_code[1].getValue() == 0))//!EQUALS
			{
				if(cmp_result[1].getValue() == 0)
					condition = true;
				else
					condition = false;
			}
			else if((condition_code[0].getValue() == 1) && (condition_code[1].getValue() == 0))//GREATER THAN
			{

				if((cmp_result[0].getValue() == 1) && (cmp_result[1].getValue() == 0))
					condition = true;
				else
					condition = false;
			}
			else if((condition_code[0].getValue() == 1) && (condition_code[1].getValue() == 1))//GREATER THAN OR EQUAL
			{
				if((cmp_result[0].getValue() == 1) || (cmp_result[1].getValue() == 1))
					condition = true;
				else 
					condition = false;
			}
		}
		else if(is_stack_function(opcode))
		{
			//POP
			if((current_instruction.getBit(4).getValue() == 0) && (current_instruction.getBit(5).getValue() == 1))
			{
				if((int)(SP.getUnsigned()) == 1020)
				{
					register[(int)dst.getUnsigned()] = memory.read(SP);
					memory.write(SP,new LongWord("00000000000000000000000000000000"));//clear mem
				}
				else
				{
					register[(int)dst.getUnsigned()] = memory.read(SP);
					memory.write(SP,new LongWord("00000000000000000000000000000000"));//clear mem
					SP = RippleAdder.add(SP,new LongWord(4));
				}
			}//PUSH
			else if((current_instruction.getBit(4).getValue() == 0) && (current_instruction.getBit(5).getValue() == 0))
			{
				if(((int)(SP.getUnsigned()) == 1020) && (memory.read(SP).toString().equals("00000000000000000000000000000000")))
				{
					memory.write(SP,op1);
				}
				else
				{
					SP = RippleAdder.subtract(SP,new LongWord(4));
					memory.write(SP,op1);
				}
			}
		}
	}

	/**Stores into register specified by destination
	 */
	public void store() throws IndexOutOfBoundsException
	{
		if(is_operation(opcode))
			register[(int)dst.getUnsigned()] = op_result;
		else if(is_move(opcode))
			register[(int)dst.getUnsigned()] = literal_value;
		else if(is_jump(opcode))
		{
			int index = (int)address.getUnsigned();

			PC = new LongWord(0);

			for(int i = 1; i < index; i++)
			{
				PC = RippleAdder.add(new LongWord(2),PC);
			}
		}
		else if((is_branch(opcode)) && (condition == true))
		{
			if(current_instruction.getBit(6).getValue() == 1)
			{
				int index = (int)address.getUnsigned();

				/*BOUNDS CHECK
				 */
				int count = 0;
				for(int i = 1; i < index; i++)
				{
					count += 2;
				}

				if(count > PC.getUnsigned())
				{
					throw new IndexOutOfBoundsException("Attempted method call at store() Line 243 : jump is greater than lines available");
				}
				else
				{
					for(int i = 0; i < index; i++)
					{
						PC = RippleAdder.subtract(PC,new LongWord(2));
					}
				}
			}
			else
			{
				int index = (int)address.getUnsigned();

				for(int i = 1; i < index; i++)
				{
					PC = RippleAdder.add(new LongWord(2),PC);
				}
			}
		}
		else if(is_stack_function(opcode))
		{
			//RETURN
			if((current_instruction.getBit(4).getValue() == 1) && (current_instruction.getBit(5).getValue() == 1))
			{
				int index = (int)memory.read(SP).getUnsigned();

				if(((int)(SP.getUnsigned()) == 1020) && (memory.read(SP).toString().equals("00000000000000000000000000000000")))
				{
					memory.write(SP,new LongWord("00000000000000000000000000000000"));
				}
				else
				{
					memory.write(SP,new LongWord("00000000000000000000000000000000"));
					SP = RippleAdder.add(SP,new LongWord(4));
				}

				PC = new LongWord(index);
			}
			//CALL
			else if((current_instruction.getBit(4).getValue() == 1) && (current_instruction.getBit(5).getValue() == 0))
			{
				if(((int)(SP.getUnsigned()) == 1020) && (memory.read(SP).toString().equals("00000000000000000000000000000000")))
				{
					memory.write(SP,PC);
				}
				else
				{
					SP = RippleAdder.subtract(SP,new LongWord(4));
					memory.write(SP,PC);
				}

				PC = new LongWord(0);

				for(int i = 1; i < (int)address.getUnsigned(); i++)
				{
					PC = RippleAdder.add(new LongWord(2),PC);
				}
			}
		}
	}

	/*Changes status bit to halt CPU
	*/
	public void halt()
	{
		status.set(1);
	}

	/**prints all registers to console
	 */
	public void print_registers()
	{
		for(int i = 0; i < 16; i++)
			System.out.println("Register[" + i + "]: " + register[i]);
	}

	/**prints all registers' values to console
	 */
	public void print_register_values()
	{
		for(int i = 0; i < 16; i++)
		{
			if(register[i] != null)
				System.out.println("Register[" + i + "]: " + register[i].getUnsigned());
			else
				System.out.println("Register[" + i + "]: Empty");
		}
	}


	/**Preloads strings into cpu memory
	 * @param instruction_pkg holds two CPU instructions
	 */
	public void preload(String[]instruction_pkg)
	{
		for(int i = 0; instruction_pkg[i] != null; i++)
		{
			memory.write(new LongWord((i*4)),new LongWord(instruction_pkg[i]));
		}
	}

	/*************************************************************************************************************/
	/** IDENTITY FUNCTIONS
	 *  the following functions are used to determine the type of an object. 
	 */

	/**Checks if opcode is jump  
	 * @return true if the opcode is signalling an operation
	 * @return false if the opcode is not signalling an operation
	 */
	public boolean is_compare(Bit[] opcode)
	{
		return ((opcode[0].getValue() == 0) && (opcode[1].getValue() == 1) && (opcode[2].getValue() == 0) && (opcode[3].getValue() == 0));
	}

	/**Checks if opcode is halt  
	 * @return true if the opcode is signalling a halt
	 * @return false if the opcode is not signalling a halt
	 */
	public boolean is_halt(Bit[] opcode)
	{
		if((opcode[0].getValue() + opcode[1].getValue() + opcode[2].getValue() + opcode[3].getValue()) == 0)//validates all bits are zero
			return true;//opcode of a halt is all zeros
		else
			return false;
	}

	/**Checks if opcode is jump  
	 * @return true if the opcode is signalling an operation
	 * @return false if the opcode is not signalling an operation
	 */
	public boolean is_jump(Bit[] opcode)
	{
		return ((opcode[0].getValue() == 0) && (opcode[1].getValue() == 0) && (opcode[2].getValue() == 1) && (opcode[3].getValue() == 1));
	}

	/**Checks if opcode is operation  
	 * @return true if the opcode is signalling an operation
	 * @return false if the opcode is not signalling an operation
	 */
	public boolean is_operation(Bit[] opcode)
	{
		//GET NUMERIC VALUE OF OPCODE
		int value = ((opcode[0].getValue()*8) + (opcode[1].getValue()*4) + (opcode[2].getValue()*2) + (opcode[3].getValue()*1));
		
		if(value > 6 && value < 16)
			return true;//operation codes of ALU run from 7-15
		else
			return false;
	}

	/**Checks if opcode is stack function  
	 * @return true if the opcode is signalling a stack function 
	 * @return false if the opcode is not signalling a stack function
	 */
	public boolean is_stack_function(Bit[]opcode)
	{
		return ((opcode[0].getValue() == 0) && (opcode[1].getValue() == 1) && (opcode[2].getValue() == 1) && (opcode[3].getValue() == 0));
	}

	/**Checks if opcode is move  
	 * @return true if the opcode is signalling a move 
	 * @return false if the opcode is not signalling a move
	 */
	public boolean is_move(Bit[] opcode)
	{
		if((opcode[3].getValue() == 1) && ((opcode[0].getValue() + opcode[1].getValue() + opcode[2].getValue()) == 0))//validates all bits are zero except last
			return true;//only the last bit of opcode for move is one
		else
			return false;
	}

	/**Checks if opcode is interrupt  
	 * @return true if the opcode is signalling an interrupt 
	 * @return false if the opcode is not signalling an interrupt
	 */
	public boolean is_interrupt(Bit[] opcode)
	{
		if((opcode[2].getValue() == 1) && ((opcode[0].getValue() + opcode[1].getValue() + opcode[3].getValue()) == 0))
			return true;//only the 3rd bit of opcode for interrupt is one
		else
			return false;
	}

	/**Checks if opcode is a branch  
	 * @return true if the opcode is a branch 
	 * @return false if the opcode is not a branch
	 */
	public boolean is_branch(Bit[] opcode)
	{
		if((opcode[0].getValue() == 0) && (opcode[1].getValue() == 1) && (opcode[2].getValue() == 0) && (opcode[3].getValue() == 1))
			return true;
		else
			return false;
	}

	
}