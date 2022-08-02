package processor.pipeline;

public class MA_RW_LatchType {

	boolean RW_enable;
	private int opcode;
	private int result;
	private int rd;
	private int excess;
	private boolean stalled;
	public MA_RW_LatchType()
	{
		rd = -1;
		RW_enable = false;
		stalled = true;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public void setOpcode (int opcode)
	{
		this.opcode = opcode;
	}

	public int getOpcode ()
	{
		return opcode;
	}

	public void setResult (int result)
	{
		this.result = result;
	}

	public int getResult ()
	{
		return result;
	}

	public void setExcess(int excess)
	{
		this.excess = excess;
	}

	public int getExcess() {
		return excess;
	}

	public void rdSet (int rd)
	{
		this.rd = rd;
	}

	public int rdGet ()
	{
		return rd;
	}

	public boolean getIfStall()
	{
		return stalled;
	}

	public void setIfStall(boolean stall)
	{
		stalled = stall;
	}

}