package processor.pipeline;
import generic.Misc;
import processor.Processor;
import generic.Statistics;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	IF_EnableLatchType IF_EnableLatch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, IF_EnableLatchType iF_EnableLatch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
	}
	
	public boolean check_conflict(int rd1, int rs_1, int rd2, int rs_2, int rd_exma2, int rd_marw2, boolean ex_ma_stall, boolean ma_rw_stall)
	{
		return (rd1 == rs_1 && ex_ma_stall) || 
				(rs_1 == rd2 && ma_rw_stall) || 
				rs_2 == rd1 && ex_ma_stall  || 
				rs_2 == rd2 && ma_rw_stall || 
				(rd_exma2 == rs_1 && ex_ma_stall) || 
				(rs_1 == rd_marw2 && ma_rw_stall) || 
				rs_2 == rd_exma2 && ex_ma_stall  || 
				rs_2 == rd_marw2 && ma_rw_stall;
	}

	public void performOF()
	{
		OF_EX_Latch.setIfStall(IF_OF_Latch.getIfStall());
		if(IF_OF_Latch.isOF_enable() && IF_OF_Latch.getIfStall())
		{
			boolean ex_ma_stall = EX_MA_Latch.getIfStall();
			boolean ma_rw_stall = MA_RW_Latch.getIfStall();

			int rd1, rd2, opc1, opc2;
			//TODO
			
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

			rd1 = EX_MA_Latch.rdGet();
			opc1 = EX_MA_Latch.getOpcode();

			rd2 = MA_RW_Latch.rdGet();
			opc2 = MA_RW_Latch.getOpcode();

			int rd_exma2 = 0;
			int rd_marw2 = 0;
			if (opc1 < 0 || opc1 > 22)
				rd1 = -1;
			if ((opc1 >= 0 && opc1 <= 7) || (opc1 >= 16 && opc1 <= 21))
			{
				rd_exma2 = 31;
			}

			if (opc2 < 0 || opc2 > 22)
				rd2 = -1;
			if ((opc2 >= 0 && opc2 <= 7) || (opc2 >= 16 && opc2 <= 21))
			{
				rd_marw2 = 31;
			}

			
			int rs_1 = (instruction << 5) >>> 27;
			int rs_2 = (instruction << 10) >>> 27;

			if (check_conflict(rd1, rs_1, rd2, rs_2, rd_exma2, rd_marw2, ex_ma_stall, ma_rw_stall))
			{
				Statistics.setdatastalls(Statistics.getdatastalls() + 1);
				IF_EnableLatch.setIfStall(false);
				IF_OF_Latch.setIfStall(false);
				OF_EX_Latch.setIfStall(false);
			}

			else
			{
				OF_EX_Latch.setIfStall(true);
				OF_EX_Latch.setOpcode(opcode);
				int rs1 = containingProcessor.getRegisterFile().getValue((instruction << 5) >>> 27);


				if (opcode >= 0 && opcode <= 22)
				{
					OF_EX_Latch.rs1Set(rs1);
					//reg
					if (opcode % 2 == 0 && opcode != 22)
					{
						OF_EX_Latch.rs1Set(rs1);
						int rs2 = containingProcessor.getRegisterFile().getValue((instruction << 10) >>> 27);
						OF_EX_Latch.rs2Set(rs2);
						OF_EX_Latch.rdSet((instruction << 15) >>> 27);
					}

					//immediate & load
					else
					{
						OF_EX_Latch.rs1Set(rs1);
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
				OF_EX_Latch.pcSet(containingProcessor.getRegisterFile().getProgramCounter() - 1);
			}

			if(opcode == 29)
			{
				IF_EnableLatch.setIF_enable(false);
			}
				
		}
	}

}
