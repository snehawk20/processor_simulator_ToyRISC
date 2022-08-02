package processor.pipeline;
//import processor.pipeline.RegisterFile;
//import org.graalvm.compiler.lir.Opcode;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{	
			//TODO
			int opcode = MA_RW_Latch.getOpcode();

			//store reminder or any kind of overflow
			int excess = MA_RW_Latch.getExcess();
			RegisterFile registerFile = containingProcessor.getRegisterFile();
			registerFile.setValue(31, excess);

			if (opcode <= 22)
			{
				registerFile.setValue(MA_RW_Latch.rdGet(), MA_RW_Latch.getResult());
				containingProcessor.setRegisterFile(registerFile);
			}

			else if (opcode == 29)
			{
				Simulator.setSimulationComplete(true);
			}
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}