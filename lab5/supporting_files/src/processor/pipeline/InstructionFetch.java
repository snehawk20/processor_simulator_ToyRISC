package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class InstructionFetch implements Element{

	Processor containingProcessor;
	public IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable() && IF_EnableLatch.getIfStall())
		{
			if(IF_EnableLatch.isIF_busy())
			{
				return;
			}
			else
			{
				Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency ,
												this,
												containingProcessor.getMainMemory(),
												containingProcessor.getRegisterFile().getProgramCounter()));
				IF_EnableLatch.setIF_busy(true);
			}	
		}
	}

	@Override
	public void handleEvent(Event e) {
		MemoryResponseEvent event;
		if(IF_OF_Latch.isOF_busy()||!IF_OF_Latch.getIfStall()||!IF_OF_Latch.get_completed())
		{
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else
		{
			event = (MemoryResponseEvent) e;
			containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() + 1);
			IF_OF_Latch.setInstruction(event.getValue());									
			IF_OF_Latch.setOF_enable(true);
			if (IF_OF_Latch.get_completed() == true)
				IF_OF_Latch.set_completed(false);
			IF_EnableLatch.setIF_busy(false);
		}
	}

}

