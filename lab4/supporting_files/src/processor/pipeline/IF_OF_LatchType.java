package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	boolean IF_enable;
	int instruction;
	private boolean stalled;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
		stalled = true;
		//IF_enable = true;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean getIF_enable()
	{
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable)
	{
		this.IF_enable = iF_enable;
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
