package processor.pipeline;

import processor.Processor;
import processor.memorysystem.MainMemory;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if (EX_MA_Latch.isMA_enable())
		{
			//transferring the values to the next latch
			int rd = EX_MA_Latch.rdGet();
			MA_RW_Latch.rdSet(rd);

			int result = EX_MA_Latch.getResult();
			
			int opcode = EX_MA_Latch.getOpcode();
			MA_RW_Latch.setOpcode(opcode);

			int excessBits = EX_MA_Latch.getExcess();
			MA_RW_Latch.setExcess(excessBits);

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
			}
			MA_RW_Latch.setResult(result);

			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);			
		}
	}

}

