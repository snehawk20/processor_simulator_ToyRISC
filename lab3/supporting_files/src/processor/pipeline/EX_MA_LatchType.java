package processor.pipeline;
import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int aluResult;
	Instruction Instruction;

	private int opcode;
	private int pc;
	private int rd;
	private int result;
	private int excess;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public void setOpcode (int opcode)
	{
		this.opcode = opcode;
	}

	public int getOpcode ()
	{
		return this.opcode;
	}

	public void pcSet (int pc)
	{
		this.pc = pc;
	}

	public int pcGet ()
	{
		return this.pc;
	}

	public void rdSet (int rd)
	{
		this.rd = rd;
	}

	public int rdGet ()
	{
		return this.rd;
	}

	public int getaluResult()
	{
		return aluResult;
	}

	public void setaluResult(int aluResult)
	{
		this.aluResult= aluResult;
	}

	public void setResult (int result)
	{
		this.result = result;
	}

	public int getResult ()
	{
		return this.result;
	}

	public void setExcess(int excess) 
	{
		this.excess = excess;
	}

	public int getExcess () 
	{
		return excess;
	}
}


