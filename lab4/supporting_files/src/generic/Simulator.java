package generic;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import processor.Clock;
import processor.Processor;
//trying to push
//its troublesome
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	//static int numberOfInstruction;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535x
		 */
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		try
		{
			fis = new FileInputStream(assemblyProgramFile);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("File could not be found\n" + e.toString());

		}
		try
		{
			if (fis.getChannel().size() % 4 != 0)
			{
				Misc.printErrorAndExit("File is broken\n");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("File size could not be computed\n" + e.toString());
		}

		int instruction = 0, programCounter = -1;
		MainMemory mem = new MainMemory();
		try {

			long remainingSize = 0;
			try {
				remainingSize = dis.available();
			} catch (IOException e) {
				e.printStackTrace();
				Misc.printErrorAndExit("Error in reading contents\n" + e.getClass().getName());
			}

			if (remainingSize != 0)
			{
				try
				{
					programCounter = dis.readInt();
					Simulator.processor.getRegisterFile().setProgramCounter(programCounter);
				}
				catch (RuntimeException e)
				{
					e.printStackTrace();
					Misc.printErrorAndExit("Error in setting PC\n" + e.getClass().getName());
				}
			}
			int address = 0;
			//MainMemory memory = new MainMemory();
			while (dis.available() > 0)
			{
				try
				{
					instruction = dis.readInt();
					mem.setWord(address, instruction);
					address++;
				}
				catch (RuntimeException e)
				{
					e.printStackTrace();
					Misc.printErrorAndExit("Error in loading instructions into mainMemory\n" + e.getClass().getName());
				}

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("error in loading p\n" + e.toString());
		}

		try
		{
			dis.close();
			bis.close();
			fis.close();
			processor.setMainMemory(mem);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("error in closing assemblyProgramFile\n" + e.toString());
		}

		try
		{
			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("Error in setting the registers\n" + e.getClass().getName());
		}



		try
		{
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("Error in setting the registers\n" + e.getClass().getName());
		}

	}

		public static void simulate()
		{


			int numberOfDataHazard = 0;
			int numWrongBranchPath = 0;
			try
			{
				int numberOfInstruction = 0;
				while(simulationComplete == false)
				{
					processor.getRWUnit().performRW();
					processor.getMAUnit().performMA();
					processor.getEXUnit().performEX();
					processor.getOFUnit().performOF();
					processor.getIFUnit().performIF();
					Clock.incrementClock();
					numberOfInstruction++;
				}


				// TODO
				// set statistics

				Statistics.setNumberOfInstructions(numberOfInstruction);
				int numOfCycles = (int) Clock.getCurrentTime();
				Statistics.setNumberOfCycles(numOfCycles);
				/*
				the number of cycles are equal to the number of instructions executed
				this is a case with no pipelining
				*/
				//Statistics.setdatastalls(numberOfDataHazard);

				//Statistics.setNumWrongBranchPath(numWrongBranchPath);
				//Statistics.setNumberOfCycles(numberOfCycles);

				//Statistics.printStatistics();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Misc.printErrorAndExit("error in clock incrementing\n" + e.toString());
			}


	}
//		public static int getNumberOfInstruction(){
//			return numberOfInstruction;
//		}
//

	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
