/**Testing class for Assembler.java
 */
public class CPU_Test3
{
	public static void runTests()
	{
		System.out.println("\t\tTEST:\n" + 
						   "–––––––––––––––––––––––––––––––––––––––––––––––––");
		String[] instructions = Assembler.assemble("input.txt");

		/* TO RUN */
		Computer cpu = new Computer();

		cpu.preload(instructions);

		cpu.run();

		System.out.println("\n\t\tRegister Values: \n"+ 
						   "–––––––––––––––––––––––––––––––––––––––––––––––––");
		cpu.print_register_values();

		System.out.println("\n\t\tExpected Values On Success: \n"+ 
						   "–––––––––––––––––––––––––––––––––––––––––––––––––\n" + 
						    "Register[0]: null\n"+
							"Register[1]: 5\n"+
							"Register[2]: 10\n"+
							"Register[3]: 15\n"+
							"Register[4]: null\n"+
							"Register[5]: 5\n"+
							"Register[6]: 10\n"+
							"Register[7]: 15\n"+
							"Register[8]: 25\n"+
							"Register[9]: 15\n"+
							"Register[10]: 10\n"+
							"Register[11]: null\n"+
							"Register[12]: null\n"+
							"Register[13]: null\n"+
							"Register[14]: 42\n"+
							"Register[15]: 2");

	}
}