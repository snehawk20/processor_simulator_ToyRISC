package generic;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStream;

import processor.Clock;
import processor.Processor;
//trying to push
//its troublesome
//import processor.memorysystem.MainMemory;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
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
		 *     x2 = 65535
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
		try
		{
			long remainingSize = 0;
			try
			{
				remainingSize = dis.available();
			}
			catch (IOException e)
			{
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
					Simulator.processor.getMainMemory().setWord(address, instruction);
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
		 
	}
	
	public static void simulate()
	{
		int numberOfCycles = 0;
		int numberOfInstruction = 0;
		try
		{
			while(simulationComplete == false)
			{
				processor.getIFUnit().performIF();
				Clock.incrementClock();
				processor.getOFUnit().performOF();
				Clock.incrementClock();
				processor.getEXUnit().performEX();
				Clock.incrementClock();
				processor.getMAUnit().performMA();
				Clock.incrementClock();
				processor.getRWUnit().performRW();
				Clock.incrementClock();
				numberOfInstruction++;
				numberOfCycles++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("error in clock incrementing\n" + e.toString());
		}

		try
		{
			// TODO
			// set statistics

			//numberOfCycles = (int) Clock.getCurrentTime();
			/* 
			the number of cycles are equal to the number of instructions executed
			this is a case with no pipelining
			*/
			Statistics.setNumberOfInstructions(numberOfInstruction);
			Statistics.setNumberOfCycles(numberOfCycles);
			
			//Statistics.printStatistics();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Misc.printErrorAndExit("error in creating stat file\n" + e.toString());
		}

	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
