package processor.memorysystem;
import generic.*;
import processor.Clock;
public class MainMemory implements Element{
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event e) {
		if (e.getEventType() == Event.EventType.MemoryRead || e.getEventType() == Event.EventType.MemoryWrite)
		{
			MemoryResponseEvent MRE;
			if (e.getEventType() == Event.EventType.MemoryRead)
			{
				MemoryReadEvent event = (MemoryReadEvent) e;
				MRE = new MemoryResponseEvent(Clock.getCurrentTime(),
								this,
								event.getRequestingElement(),
								getWord(event.getAddressToReadFrom()));
				MRE.setVal(0);
			}
			else
			{
				MemoryWriteEvent event = (MemoryWriteEvent) e;
				setWord(event.getAddressToWriteTo(),event.getValue());
				MRE = new MemoryResponseEvent(Clock.getCurrentTime(),
								this,
								event.getRequestingElement(),
								0);
				MRE.setVal(1);
			}
			Simulator.getEventQueue().addEvent(MRE);
		}
	}
}
