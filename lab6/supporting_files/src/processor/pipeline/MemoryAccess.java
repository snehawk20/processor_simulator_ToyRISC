package processor.pipeline;

import processor.Processor;
import generic.*;
import processor.Clock;
import configuration.Configuration;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_Enable;

	int result, opcode, excessBits, rd;

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
		// //TODO
		boolean stall = EX_MA_Latch.getIfStall();
		MA_RW_Latch.setIfStall(stall);


		if (EX_MA_Latch.isMA_enable() && stall)
		{
			if(EX_MA_Latch.isMA_busy())
			{
				return;
			}
			//transferring the values to the next latch
			rd = EX_MA_Latch.rdGet();

			result = EX_MA_Latch.getResult();

			opcode = EX_MA_Latch.getOpcode();
			MA_RW_Latch.setOpcode(opcode);

			excessBits = EX_MA_Latch.getExcess();
			MA_RW_Latch.setExcess(excessBits);

			//load
			if (opcode == 22)
			{
				Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + 
													Configuration.L1d_latency,
													this,
													containingProcessor.getL1d() ,
													result));
				EX_MA_Latch.setMA_busy(true);
				return;
			}
			//store
			else if (opcode == 23)
			{
				Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.L1d_latency , this, containingProcessor.getMainMemory(), result, rd));
				EX_MA_Latch.setMA_busy(true);
				return;
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
			MA_RW_Latch.rdSet(rd);
			EX_MA_Latch.rdSet(-2);	
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			//testing testing testing
		}

		MA_RW_Latch.setIfStall(EX_MA_Latch.getIfStall());
	}

	@Override
	public void handleEvent(Event e) 
	{
		// TODO Auto-generated method stub
		MemoryResponseEvent event = (MemoryResponseEvent) e;
		MA_RW_Latch.rdSet(rd);
		MA_RW_Latch.setResult(event.getValue());
		EX_MA_Latch.setMA_enable(false);
		EX_MA_Latch.setMA_busy(false);
		MA_RW_Latch.setRW_enable(true);

		EX_MA_Latch.rdSet(-100);
	}
}
