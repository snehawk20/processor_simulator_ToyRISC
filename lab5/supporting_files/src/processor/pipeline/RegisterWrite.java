package processor.pipeline;

import generic.Simulator;
import generic.Statistics;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
	}

	public void performRW()
	{
		int opcode = MA_RW_Latch.getOpcode();
		if (opcode > 22 && opcode < 29)
		{
			IF_EnableLatch.setIF_enable(true);
		}

		if (MA_RW_Latch.isRW_enable() && MA_RW_Latch.getIfStall())
		{
			int instructions = Statistics.getInstructionNum()+1;
			Statistics.setNumberOfInstructions(instructions);
				//TODO

			//store reminder or any kind of overflow
			MA_RW_Latch.setRW_enable(false);

			int excess = MA_RW_Latch.getExcess();
			RegisterFile registerFile = containingProcessor.getRegisterFile();
			registerFile.setValue(31, excess);

			if (opcode <= 22)
			{
				registerFile.setValue(MA_RW_Latch.rdGet(), MA_RW_Latch.getResult());
				containingProcessor.setRegisterFile(registerFile);
				IF_EnableLatch.setIF_enable(true);
			}

			MA_RW_Latch.rdSet(-100);
		}
		else if (MA_RW_Latch.getIfStall() == false)
		{
			IF_EnableLatch.setIfStall(true);
			IF_EnableLatch.setIF_enable(true);
			IF_OF_Latch.setIfStall(true);
		}

		if (opcode == 29)
		{
			Simulator.setSimulationComplete(true);
		}
	}

}