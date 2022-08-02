package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfDataHazard;
	static int numWrongBranchPath;
	static int datastalls;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of data hazard stalls = " + datastalls);
			writer.println("Number of times instruction on wrong branch path is taken= " + numWrongBranchPath);
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int instructionNum) {
		Statistics.numberOfInstructions = instructionNum;
	}
	//public static int getNumberOfInstructions() {
	//	return number_of_wrong_path;
	//}

	public static void setNumberOfCycles(int CyclesNum) {
		Statistics.numberOfCycles = CyclesNum;
	}

	public static void setNumberOfDataHazard(int numberOfDataHazard)
	{
		Statistics.numberOfDataHazard = numberOfDataHazard;
	}

	public static int getNumWrongBranchPath()
	{
		return Statistics.numWrongBranchPath;
	}

	public static void setNumWrongBranchPath(int num)
	{
		Statistics.numWrongBranchPath = num;
	}


	public static void setdatastalls(int num) 
	{
		Statistics.datastalls = num;
	}

	public static int getdatastalls() 
	{
		return datastalls;
	}

	//
	public static int getInstructionNum(){
		return numWrongBranchPath;}
}

