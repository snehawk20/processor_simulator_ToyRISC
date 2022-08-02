package generic;

import java.io.FileInputStream;
import generic.Operand.OperandType;
import generic.ParsedProgram.*;
import generic.Instruction.*;
import java.io.*;
import java.lang.*;

public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	private static String destinationFile;
	
	public static void setupSimulation(String assemblyProgramFile, String objectProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
		
		//output file path
		destinationFile = objectProgramFile;
	}
    
    //method to return a binary string which is the twos complement of the imput string
    public static String twosComplement(String input) 
    {
        String onesComplement = "";
        
        int len = input.length();
        for (int i = 0; i < len; i++) 
            onesComplement = onesComplement + bitFlip(input.charAt(i));
            
//         int twosComplement = (int)Long.parseLong(onesComplement, 2) + 1;
//         String twosComplementStr = null;
//         if (twosComplement >= 0)
//             twosComplementStr = String.format("%4s", Integer.toBinaryString(twosComplement)).replace(' ','0');
//         else
//             twosComplementStr = String.format("%4s", Integer.toBinaryString(twosComplement)).replace(' ','1');
            

        StringBuilder builder = new StringBuilder(onesComplement);
        int check = 0;
        for (int i = onesComplement.length() - 1; i > 0; i--) 
        {
            if (onesComplement.charAt(i) == '1') 
                builder.setCharAt(i, '0');
            else if (onesComplement.charAt(i) == '0')
            {
                builder.setCharAt(i, '1');
                check = 1;
                break;
            }
            else
            {
                System.out.println("Input string is not a binary string");
                System.exit(0);
            }
        }
        
        if (check == 0)
            builder.append("1", 0, 7);
        String twosComplement = "";
        twosComplement = builder.toString();

        return twosComplement;
    }

    public static char bitFlip(char c) 
    {
        if (c == '0')
            return '1';
        else
            return '0';
    }
	
	public static void assemble()

	{
		//TODO your assembler code

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		DataOutputStream dos = null;
		
		try
		{
            //open the output file in binary mode
            fos = new FileOutputStream(destinationFile);
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);

		}
		catch (FileNotFoundException e)
		{
            e.printStackTrace();
			Misc.printErrorAndExit("error in opening objectProgramFile\n" + e.toString());
		}
		
		try
        {
            //obtain the header, i.e; where the first instruction is
            int firstCodeAddress = ParsedProgram.symtab.get("main");
            //write the header into the output file
            dos.writeInt(firstCodeAddress);
        }
		catch (IOException e) {
            e.printStackTrace();
            Misc.printErrorAndExit("error in writing program counter of .text section\n" + e.toString());
        }
		
		
		try
		{
            //write the data section into the file
            for (int i = 0; i < ParsedProgram.symtab.get("main"); i++)
            {
                dos.writeInt(ParsedProgram.data.get(i));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Misc.printErrorAndExit("error in writing data section contents\n" + e.toString());
        }
        
        try
        {
            int textBlockSize = ParsedProgram.code.size();
            int programCounter = ParsedProgram.mainFunctionAddress;

            Instruction nextInstruction = new Instruction();

            
            while(textBlockSize > 0)
            {
                try
                {
                    nextInstruction = ParsedProgram.getInstructionAt(programCounter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Misc.printErrorAndExit("error in accessing the next instruction\n" + e.toString());
                }
                //store the opcode of all instructions in binaryString
                String binaryString = null;
                //use toggle to toggle between all the instructions
                int toggle = 0;
                
               try
               {

                    switch (nextInstruction.getOperationType())
                {
                    case add : 
                    {
                        binaryString = "00000";
                        break;
                    }
					case sub : 
					{
                        binaryString = "00010";
                        break;
					}
					case mul : 
					{
                        binaryString = "00100";
                        break;
					}
					case div : 
					{
                        binaryString = "00110";
                        break;
					}
					case and :
					{
                        binaryString = "01000";
                        break;
					}
					case or  : 
					{
                        binaryString = "01010";
                        break;
					}
					case xor : 
					{
                        binaryString = "01100";
                        break;
					}
					case slt :
					{
                        binaryString = "01110";
                        break;
					}
					case sll : 
					{
                        binaryString = "10000";
                        break;
					}
					case srl : 
					{
                        binaryString = "10010";
                        break;
					}
					case sra :	
					{
                        binaryString = "10100";
                        break;
					}
                    case addi :
                    {
                        binaryString = "00001";
                        toggle = 1;
                        break;
                    }
                    case subi :
                    {
                        binaryString = "00011";
                        toggle = 1;
                        break;
                    }
                    case muli :
                    {
                        binaryString = "00101";
                        toggle = 1;
                        break;
                    }
                    case divi : 
                    {
                        binaryString = "00111";
                        toggle = 1;
                        break;
                    }
                    case andi : 
                    {
                        binaryString = "01001";
                        toggle = 1;
                        break;
                    }
                    case ori : 
                    {
                        binaryString = "01011";
                        toggle = 1;
                        break;
                    }
                    case xori : 
                    {
                        binaryString = "01101";
                        toggle = 1;
                        break;
                    }
                    case slti : 
                    {
                        binaryString = "01111";
                        toggle = 1;
                        break;
                    }
                    case slli : 
                    {
                        binaryString = "10001";
                        toggle = 1;
                        break;
                    }
                    case srli : 
                    {
                        binaryString = "10011";
                        toggle = 1;
                        break;
                    }
                    case srai :
                    {
                        binaryString = "10101";
                        toggle = 1;
                        break;
                    }
                    case load :
                    {
                        binaryString = "10110";
                        toggle = 1;
                        break;
                    }
                    case store :
                    {
                        binaryString = "10111";
                        toggle = 1;
                        break;
                    }
                    case beq : 
                    {
                        binaryString = "11001";
                        toggle = 2;
                        break;
                    }
                    case bne : 
                    {
                        binaryString = "11010";
                        toggle = 2;
                        break;
                    }
                    case blt :
                    {
                        binaryString = "110011";
                        toggle = 2;
                        break;
                    }
                    case bgt :
                    {
                        binaryString = "11100";
                        toggle = 2;
                        break;
                    }
                    case jmp :
                    {
                        binaryString = "11000";
                        toggle = 3;
                        break;
                    }
                    case end :
                    {
                        binaryString = "11101";
                        toggle = 4;
                        break;
                    }
                    //invalid instruction
                    default :
                    {
                     	Misc.printErrorAndExit("Wrong instruction");
                     	System.exit(0);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Misc.printErrorAndExit("couldn't access next instruction'\n" + e.toString());
            }
            
            //binaryStringInstruction will store the entire instruction
            //we will concatenate the operands, immediates and 
            String binaryStringInstruction = null;
                 
                if (toggle == 0)
                {
                 	//R3-type instructions
                 	//rd = rs1 + rs2
                 	String rs1 = String.format("%5s", Integer.toBinaryString(nextInstruction.getSourceOperand1().getValue())).replace(' ','0');
                    String rs2 = String.format("%5s", Integer.toBinaryString(nextInstruction.getSourceOperand2().getValue())).replace(' ','0');
                    String rd = String.format("%5s", Integer.toBinaryString(nextInstruction.getDestinationOperand().getValue())).replace(' ','0');
                    //the remaining 12 bits are unused, they can hold any value, let's set them to 0
                    binaryStringInstruction = binaryString + rs1 + rs2 + rd + "000000000000";
                }
                
                else if (toggle == 1)
                {
                    // R2I-Type instructions
                    //rd = rs1 + imm
                    int op1 = nextInstruction.getSourceOperand1().getValue();
                	String rs1 = String.format("%5s", Integer.toBinaryString(op1)).replace(' ','0');
                    String rd = null;
                    String imm = null;

                    if(nextInstruction.getSourceOperand2().getOperandType() == OperandType.valueOf("Label"))
                    {
                        if(ParsedProgram.symtab.get(String.valueOf(nextInstruction.getSourceOperand2().getLabelValue())) >= 0)
                            imm = String.format("%17s",Integer.toBinaryString(ParsedProgram.symtab.get(String.valueOf(nextInstruction.getSourceOperand2().getLabelValue())))).replace(" ","0");
                        else
                            imm = twosComplement(String.format("%17s",Integer.toBinaryString(-ParsedProgram.symtab.get(String.valueOf(nextInstruction.getSourceOperand2().getLabelValue())))).replace(" ","0"));	
                    }	
                    else
                    {
                        if(nextInstruction.getSourceOperand2().getValue() >= 0)
                            imm = String.format("%17s",Integer.toBinaryString(nextInstruction.getSourceOperand2().getValue())).replace(" ","0");
			
                        else
                            imm = twosComplement(String.format("%17s",Integer.toBinaryString(-nextInstruction.getSourceOperand2().getValue())).replace(" ","0"));	
                    }
                    rd = String.format("%5s",Integer.toBinaryString(nextInstruction.getDestinationOperand().getValue())).replace(" ","0");
                 	

                 	binaryStringInstruction = binaryString + rs1 + rd + imm;

                }
                
                else if (toggle == 2)
                {                    
					String rs1 = String.format("%5s",Integer.toBinaryString(nextInstruction.getSourceOperand1().getValue())).replace(" ","0");
					String rd = String.format("%5s",Integer.toBinaryString(nextInstruction.getSourceOperand2().getValue())).replace(" ","0");
					String imm = null;

					if(nextInstruction.getDestinationOperand().getOperandType()==OperandType.valueOf("Label"))
					{
					int offset = ParsedProgram.symtab.get(String.valueOf(nextInstruction.getDestinationOperand().getLabelValue()))-programCounter;
						if(offset >= 0)
						{
							imm = String.format("%17s",Integer.toBinaryString(ParsedProgram.symtab.get(String.valueOf(nextInstruction.getDestinationOperand().getLabelValue())) - programCounter)).replace(" ","0");
						}
						else
						{	
							imm = twosComplement(String.format("%17s",Integer.toBinaryString(-ParsedProgram.symtab.get(String.valueOf(nextInstruction.getDestinationOperand().getLabelValue()))+programCounter)).replace(" ","0"));	
						}
					}
					else
					{
						if(nextInstruction.getDestinationOperand().getValue()>=0)
						{
							imm = String.format("%17s",Integer.toBinaryString(nextInstruction.getDestinationOperand().getValue())).replace(" ","0");
						}
						else
						{
							imm = twosComplement(String.format("%17s",Integer.toBinaryString(-nextInstruction.getDestinationOperand().getValue())).replace(" ","0"));	
						}
					}
		
                    binaryStringInstruction = binaryString + rs1 + rd + imm;

                }
                
                else if (toggle == 3)
                {
                    String rd = null;
                    String imm = null;
                    if(nextInstruction.getDestinationOperand().getOperandType() == OperandType.valueOf("Immediate"))
                    {
                        if(nextInstruction.getDestinationOperand().getValue() >= 0)
                            imm = String.format("%22s",Integer.toBinaryString(nextInstruction.getDestinationOperand().getValue())).replace(' ', '0');
                        else
                            imm = twosComplement(String.format("%22s",Integer.toBinaryString(-nextInstruction.getDestinationOperand().getValue())).replace(" ","0"));	
                        rd = "00000";
                    }
                    else if(nextInstruction.getDestinationOperand().getOperandType() == OperandType.valueOf("Label"))
                    {
                        int offset = ParsedProgram.symtab.get(String.valueOf(nextInstruction.getDestinationOperand().getLabelValue())) - programCounter;
                        if(offset >= 0)
                            imm = String.format("%22s",Integer.toBinaryString(ParsedProgram.symtab.get(String.valueOf(nextInstruction.getDestinationOperand().getLabelValue()))-programCounter)).replace(' ', '0');
                        else
                            imm = twosComplement(String.format("%22s",Integer.toBinaryString(-ParsedProgram.symtab.get(String.valueOf(nextInstruction.getDestinationOperand().getLabelValue()))+programCounter)).replace(" ","0"));
                        rd = "00000";
                    }
                    else
                    {
                        rd = String.format("%5s",Integer.toBinaryString(nextInstruction.getDestinationOperand().getValue())).replace(" ","0");
                        imm = "0000000000000000000000";
                    }
		
                binaryStringInstruction = binaryString + rd + imm;

                }
                
                else if (toggle == 4)
                 	binaryStringInstruction = binaryString + "000000000000000000000000000";  

                
                int instruction = (int)Long.parseLong(binaryStringInstruction, 2);
                dos.writeInt(instruction);
                //increment program counter
                programCounter += 1;

                textBlockSize--;
            }	
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Misc.printErrorAndExit("error in writing code section contents\n" + e.toString());
        }
        
        try
		{
            dos.close();
            fos.close();
		}
		catch (Exception e)
		{
            e.printStackTrace();
			Misc.printErrorAndExit("error in closing objectProgramFile\n" + e.toString());
		}
	}
}
