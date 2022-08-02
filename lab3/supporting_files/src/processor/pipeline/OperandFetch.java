// package processor.pipeline;
// import generic.Misc;
// import processor.Processor;

// public class OperandFetch {
// 	Processor containingProcessor;
// 	IF_OF_LatchType IF_OF_Latch;
// 	OF_EX_LatchType OF_EX_Latch;
	
// 	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
// 	{
// 		this.containingProcessor = containingProcessor;
// 		this.IF_OF_Latch = iF_OF_Latch;
// 		this.OF_EX_Latch = oF_EX_Latch;
// 	}
	
// 	public void performOF()
// 	{
// 		if(IF_OF_Latch.isOF_enable())
// 		{
// 			//TODO
// 			int instruction = 0;
// 			try
// 			{
// 				instruction = this.IF_OF_Latch.getInstruction();
// 			}
// 			catch (Exception e)
// 			{
// 				e.printStackTrace();
// 				Misc.printErrorAndExit("File size could not be computed\n" + e.toString());
// 			}

// 			int opcode = instruction;
// 			opcode >>>= 27;

// 			OF_EX_Latch.setOpcode(opcode);
// 			if (opcode >= 0 && opcode <= 22)
// 			{
// 				int rs1 = containingProcessor.getRegisterFile().getValue((instruction << 5) >>> 27);
// 				OF_EX_Latch.rs1Set(rs1);
// 				//reg
// 				if (opcode % 2 == 0 && opcode != 22)
// 				{
// 					int rs2 = containingProcessor.getRegisterFile().getValue((instruction << 10) >>> 27);
// 					OF_EX_Latch.rs1Set(rs2);
// 					OF_EX_Latch.rdSet((instruction << 15) >>> 27);
// 				}

// 				//immediate
// 				else
// 				{
// 					OF_EX_Latch.rdSet((instruction << 10) >>> 27);
// 					OF_EX_Latch.immSet((instruction << 15) >> 15);
// 				}
// 			}

// 			else if (opcode == 24 || opcode == 29)
// 			{
// 				int rd = containingProcessor.getRegisterFile().getValue((instruction << 5) >>> 27);
// 				OF_EX_Latch.rdSet(rd);
// 				OF_EX_Latch.immSet((instruction << 10) >> 10);
// 			}

// 			else
// 			{
// 				int rs1 = containingProcessor.getRegisterFile().getValue((instruction << 5) >>> 27);
// 				OF_EX_Latch.rs1Set(rs1);
// 				int rd = containingProcessor.getRegisterFile().getValue((instruction << 10) >>> 27);
// 				OF_EX_Latch.rdSet(rd);
// 				OF_EX_Latch.immSet((instruction << 15) >> 15);
// 			}

			
// 			IF_OF_Latch.setOF_enable(false);
// 			OF_EX_Latch.setEX_enable(true);
// 		}
// 	}

// }

package processor.pipeline;
import generic.Misc;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			OF_EX_Latch.pcSet(containingProcessor.getRegisterFile().getProgramCounter() - 1);
			int instruction = 0;
			try
			{
				instruction = this.IF_OF_Latch.getInstruction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Misc.printErrorAndExit("File size could not be computed\n" + e.toString());
			}

			int opcode = instruction;
			opcode >>>= 27;

			OF_EX_Latch.setOpcode(opcode);
			int rs1 = containingProcessor.getRegisterFile().getValue((instruction << 5) >>> 27);

			if (opcode >= 0 && opcode <= 22)
			{
				OF_EX_Latch.rs1Set(rs1);
				//reg
				if (opcode % 2 == 0 && opcode != 22)
				{
					int rs2 = containingProcessor.getRegisterFile().getValue((instruction << 10) >>> 27);
					OF_EX_Latch.rs2Set(rs2);
					OF_EX_Latch.rdSet((instruction << 15) >>> 27);
				}

				//immediate
				else
				{
					OF_EX_Latch.rdSet((instruction << 10) >>> 27);
					OF_EX_Latch.immSet((instruction << 15) >> 15);
				}
			}

			else if (opcode == 24 || opcode == 29)
			{
				int rd = containingProcessor.getRegisterFile().getValue((instruction << 5) >>> 27);
				OF_EX_Latch.rdSet(rd);
				OF_EX_Latch.immSet((instruction << 10) >> 10);
			}

			else
			{
				OF_EX_Latch.rs1Set(rs1);
				int rd = containingProcessor.getRegisterFile().getValue((instruction << 10) >>> 27);
				OF_EX_Latch.rdSet(rd);
				OF_EX_Latch.immSet((instruction << 15) >> 15);
			}

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
