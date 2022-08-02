package processor.pipeline;

import processor.Processor;
import processor.memorysystem.MainMemory;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_Enable;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, IF_EnableLatchType iF_Enable, MA_RW_LatchType mA_RW_Latch, IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_Enable = iF_Enable;
	}

	public void performMA()
	{
		//TODO
		boolean stall = EX_MA_Latch.getIfStall();
		MA_RW_Latch.setIfStall(stall);
		if (EX_MA_Latch.isMA_enable() && stall)
		{
			//transferring the values to the next latch
			int rd = EX_MA_Latch.rdGet();
			MA_RW_Latch.rdSet(rd);

			int result = EX_MA_Latch.getResult();
			MA_RW_Latch.setResult(result);

			int opcode = EX_MA_Latch.getOpcode();
			MA_RW_Latch.setOpcode(opcode);

			int excessBits = EX_MA_Latch.getExcess();
			MA_RW_Latch.setExcess(excessBits);

			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);

			MainMemory mainMemory = containingProcessor.getMainMemory();
			//load
			if (opcode == 22)
			{
				result = mainMemory.getWord(result);
			}
			//store
			else if (opcode == 23)
			{
				mainMemory.setWord(result, rd);
				containingProcessor.setMainMemory((mainMemory));
				if (result == -1)
				{
					IF_OF_Latch.setIfStall(true);
				}
			}
			else if (opcode > 23 && opcode < 29 && result == -1)
			{
				IF_OF_Latch.setIfStall(true);
			}

			if (opcode == 29)
			{
				IF_Enable.setIF_enable(false);
			}
			
			MA_RW_Latch.setResult(result);	
		}
	}

}
