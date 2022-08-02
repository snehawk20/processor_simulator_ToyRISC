package processor.pipeline;

public class EX_IF_LatchType {

	boolean IF_enable;
	boolean BranchTaken;
	private boolean isBranchTaken;
	private int pcBranch;

	public EX_IF_LatchType()
	{
		IF_enable = false;
	}
	public boolean isIF_enable()
	{
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable)
	{
		IF_enable = iF_enable;
	}

	public void setIsBranchTaken(boolean isBranchTaken) 
	{
		this.isBranchTaken = isBranchTaken;
	}

	public boolean getIsBranchTaken()
	{
		return this.isBranchTaken;
	}

	public void setBranchPC(int pcBranch) 
	{
		this.pcBranch = pcBranch;
	}

	public int getBranchPC() {
		return pcBranch;
	}
}

