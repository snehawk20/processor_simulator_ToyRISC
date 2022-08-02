package processor.pipeline;

public class IF_EnableLatchType {

	boolean IF_enable;
	boolean RW_enable;
	boolean stalled;
	boolean IF_busy;

	public IF_EnableLatchType()
	{
		IF_enable = true;
		stalled = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public boolean getIfStall()
	{
		return stalled;
	}

	public void setIfStall(boolean stall)
	{
		stalled = stall;
	}

	public boolean isIF_busy()
	{
		return IF_busy;
	}

	public void setIF_busy(boolean if_busy)
	{
		IF_busy = if_busy;
	}

	public boolean isRW_enable()
	{
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable)
	{
		RW_enable = rW_enable;
	}
}