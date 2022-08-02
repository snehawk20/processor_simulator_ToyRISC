package processor.pipeline;

import java.lang.Math;
import processor.Processor;
import configuration.Configuration;
import processor.Clock;
import generic.*;

public class Execute implements Element{
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	int result, rd, opcode, shift;
	long overflow;
	long latency;
	boolean EX_busy;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
				   EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		result = 0;
		EX_busy = false;
	}

	public void performEX()
	{
		//TODO
		boolean stall = OF_EX_Latch.getIfStall();

		if(EX_MA_Latch.isMA_busy() == false)
			EX_MA_Latch.setIfStall(stall);

		if(EX_busy == false){
			OF_EX_Latch.setEXbusy(EX_MA_Latch.isMA_busy());
		}
		if (OF_EX_Latch.isEX_enable() && stall) {
			opcode = OF_EX_Latch.getOpcode();

			if(OF_EX_Latch.isEXbusy()){
				return;
			}

			EX_MA_Latch.pcSet(OF_EX_Latch.pcGet());
			EX_MA_Latch.setOpcode(opcode);
			System.out.printf("EX %d\n",opcode);

			EX_IF_Latch.setIsBranchTaken(false);
			OF_EX_Latch.setEX_enable(false);

			opcode = OF_EX_Latch.getOpcode();

			switch (opcode) {
				case 0:
					result = OF_EX_Latch.rs1Get() + OF_EX_Latch.rs2Get();
					overflow = (long) OF_EX_Latch.rs1Get() + (long) OF_EX_Latch.rs2Get();
					overflow >>= 32;
					if (overflow != 0)
						EX_MA_Latch.setExcess((int) overflow);
					break;
				case 1:
					result = OF_EX_Latch.rs1Get() + OF_EX_Latch.immGet();
					overflow = (long) OF_EX_Latch.rs1Get() + (long) OF_EX_Latch.immGet();
					overflow >>= 32;
					if (overflow != 0)
						EX_MA_Latch.setExcess((int) overflow);
					break;
				case 2:
					result = OF_EX_Latch.rs1Get() - OF_EX_Latch.rs2Get();
					overflow = (long) OF_EX_Latch.rs1Get() - (long) OF_EX_Latch.rs2Get();
					overflow >>= 32;
					if (overflow != 0)
						EX_MA_Latch.setExcess((int) overflow);
					break;
				case 3:
					result = OF_EX_Latch.rs1Get() - OF_EX_Latch.immGet();
					overflow = (long) OF_EX_Latch.rs1Get() - (long) OF_EX_Latch.immGet();
					overflow >>= 32;
					if (overflow != 0)
						EX_MA_Latch.setExcess((int) overflow);
					break;
				case 4:
					result = OF_EX_Latch.rs1Get() * OF_EX_Latch.rs2Get();
					overflow = (long) OF_EX_Latch.rs1Get() * (long) OF_EX_Latch.rs2Get();
					overflow >>= 32;
					if (overflow != 0)
						EX_MA_Latch.setExcess((int) overflow);
					break;
				case 5:
					result = OF_EX_Latch.rs1Get() * OF_EX_Latch.immGet();
					overflow = (long) OF_EX_Latch.rs1Get() * (long) OF_EX_Latch.immGet();
					overflow >>= 32;
					if (overflow != 0)
						EX_MA_Latch.setExcess((int) overflow);
					break;
				case 6:
					if (OF_EX_Latch.rs1Get() % OF_EX_Latch.rs2Get() != 0)
						EX_MA_Latch.setExcess((int) (OF_EX_Latch.rs1Get() % OF_EX_Latch.rs2Get()));
					result = OF_EX_Latch.rs1Get() / OF_EX_Latch.rs2Get();
					break;
				case 7:
					if (OF_EX_Latch.rs1Get() % OF_EX_Latch.immGet() != 0)
						EX_MA_Latch.setExcess((int) (OF_EX_Latch.rs1Get() % OF_EX_Latch.immGet()));
					result = OF_EX_Latch.rs1Get() / OF_EX_Latch.immGet();
					break;
				case 8:
					result = OF_EX_Latch.rs1Get() & OF_EX_Latch.rs2Get();
					break;
				case 9:
					result = OF_EX_Latch.rs1Get() & OF_EX_Latch.immGet();
					break;
				case 10:
					result = OF_EX_Latch.rs1Get() | OF_EX_Latch.rs2Get();
					break;
				case 11:
					result = OF_EX_Latch.rs1Get() | OF_EX_Latch.immGet();
					break;
				case 12:
					result = OF_EX_Latch.rs1Get() ^ OF_EX_Latch.rs2Get();
					break;
				case 13:
					result = OF_EX_Latch.rs1Get() ^ OF_EX_Latch.immGet();
					break;
				case 14:
					if (OF_EX_Latch.rs1Get() < OF_EX_Latch.rs2Get())
						result = 1;
					else
						result = 0;
					break;
				case 15:
					if (OF_EX_Latch.rs1Get() < OF_EX_Latch.immGet())
						result = 1;
					else
						result = 0;
					break;
				case 16:
					result = OF_EX_Latch.rs1Get() << OF_EX_Latch.rs2Get();
					overflow = (long) OF_EX_Latch.rs1Get() << (long) OF_EX_Latch.rs2Get();
					overflow >>= 32;
					// if (overflow != 0)
					EX_MA_Latch.setExcess((int) overflow);
					break;
				case 17:
					result = OF_EX_Latch.rs1Get() << OF_EX_Latch.immGet();
					overflow = (long) OF_EX_Latch.rs1Get() << (long) OF_EX_Latch.immGet();
					overflow >>= 32;
					// if (overflow != 0)
					EX_MA_Latch.setExcess((int) overflow);
					break;
				case 18:
					result = OF_EX_Latch.rs1Get() >>> OF_EX_Latch.rs2Get();
					shift = OF_EX_Latch.rs1Get() & ((int) Math.pow((double) 2, (double) OF_EX_Latch.rs2Get()) - 1);
					// if (shift != 0)
					EX_MA_Latch.setExcess((int) shift);
					break;
				case 19:
					result = OF_EX_Latch.rs1Get() >>> OF_EX_Latch.immGet();
					shift = OF_EX_Latch.rs1Get() & ((int) Math.pow((double) 2, (double) OF_EX_Latch.immGet()) - 1);
					// if (shift != 0)
					EX_MA_Latch.setExcess((int) shift);
					break;
				case 20:
					result = OF_EX_Latch.rs1Get() >> OF_EX_Latch.rs2Get();
					shift = OF_EX_Latch.rs1Get() & ((int) Math.pow((double) 2, (double) OF_EX_Latch.rs2Get()) - 1);
					// if (shift != 0)
					EX_MA_Latch.setExcess((int) shift);
					break;
				case 21:
					result = OF_EX_Latch.rs1Get() >> OF_EX_Latch.immGet();
					shift = OF_EX_Latch.rs1Get() & ((int) Math.pow((double) 2, (double) OF_EX_Latch.immGet()) - 1);
					// if (shift != 0)
					EX_MA_Latch.setExcess((int) shift);
					break;
				case 22:
					result = OF_EX_Latch.rs1Get() + OF_EX_Latch.immGet();
					break;
				case 23:
					result = OF_EX_Latch.rdGet() + OF_EX_Latch.immGet();
					break;
				case 24:
					OF_EX_Latch.setIfStall(false);
					IF_OF_Latch.setIfStall(false);
					containingProcessor.getRegisterFile().setProgramCounter(
							(OF_EX_Latch.pcGet() + OF_EX_Latch.rdGet() + OF_EX_Latch.immGet()) % (1 << 22));
					break;
				case 25:
					if (OF_EX_Latch.rdGet() == OF_EX_Latch.rs1Get()) {
						EX_IF_Latch.setIsBranchTaken(true);
						OF_EX_Latch.setIfStall(false);
						IF_OF_Latch.setIfStall(false);
						containingProcessor.getRegisterFile()
								.setProgramCounter(OF_EX_Latch.pcGet() + OF_EX_Latch.immGet());
					}
					break;
				case 26:
					if (OF_EX_Latch.rdGet() != OF_EX_Latch.rs1Get()) {
						EX_IF_Latch.setIsBranchTaken(true);
						OF_EX_Latch.setIfStall(false);
						IF_OF_Latch.setIfStall(false);
						containingProcessor.getRegisterFile()
								.setProgramCounter(OF_EX_Latch.pcGet() + OF_EX_Latch.immGet());
					}
					break;
				case 27:
					if (OF_EX_Latch.rdGet() > OF_EX_Latch.rs1Get()) {
						EX_IF_Latch.setIsBranchTaken(true);
						OF_EX_Latch.setIfStall(false);
						IF_OF_Latch.setIfStall(false);
						containingProcessor.getRegisterFile().setProgramCounter(OF_EX_Latch.pcGet() + OF_EX_Latch.immGet());
					}
					break;
				case 28:
					if (OF_EX_Latch.rdGet() < OF_EX_Latch.rs1Get()) {
						EX_IF_Latch.setIsBranchTaken(true);
						OF_EX_Latch.setIfStall(false);
						IF_OF_Latch.setIfStall(false);
						containingProcessor.getRegisterFile()
								.setProgramCounter(OF_EX_Latch.pcGet() + OF_EX_Latch.immGet());
					}
					break;
				case 29:
					// end
					break;
				default:
					Misc.printErrorAndExit("Unknown instruction\n");
			}

			if (opcode <= 28 && opcode >= 24)
			{
				Simulator.getEventQueue().deleteEvent();
				Statistics.setNumWrongBranchPath(Statistics.getNumWrongBranchPath() + 1);
				result = -1;
			}

			int latency;
			rd = OF_EX_Latch.rdGet();

				if (opcode == 4 || opcode == 5)
				{
					latency = Configuration.multiplier_latency;
					EX_busy = true;
					Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime() + latency, this, this));
					OF_EX_Latch.setEXbusy(true);
					return;
				}
				else if (opcode == 6 || opcode == 7)
				{
					latency = Configuration.divider_latency;
					EX_busy = true;
					Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime() + latency, this, this));
					OF_EX_Latch.setEXbusy(true);
					return;
				}
				else if (opcode >= 0 && opcode < 22)
				{
					latency = Configuration.ALU_latency;
					EX_busy = true;
					Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime() + latency, this, this));
					OF_EX_Latch.setEXbusy(true);
					return;
				}
				else if (opcode == 23)
				{
					EX_MA_Latch.rdSet(OF_EX_Latch.rs1Get());
				}
				else
				{
					EX_MA_Latch.rdSet(rd);
				}

			EX_MA_Latch.setResult(result);
			EX_MA_Latch.pcSet(OF_EX_Latch.pcGet());
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);

		}
	}

	@Override
	public void handleEvent(Event e)
	{
		if(EX_MA_Latch.isMA_busy())
		{
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else
		{
			EX_MA_Latch.setResult(result);
			EX_MA_Latch.rdSet(rd);

			EX_busy = false;
			OF_EX_Latch.setEXbusy(false);
			EX_MA_Latch.setMA_enable(true);
			OF_EX_Latch.setEX_enable(false);
		}
	}

}