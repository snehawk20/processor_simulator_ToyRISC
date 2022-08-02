package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	private int type;
	private int opcode;
	private int immx;
	private int rs1;
	private int rs2;
	private int rd;
	private int branchTarget;
	private int imm;
	private int pc;
	boolean stalled;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
		rd = -1;
		stalled = true;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public void setOpcode (int opcode)
	{
		this.opcode = opcode;
	}

	public int getOpcode ()
	{
		return this.opcode;
	}

	public void setImmx(int immx)
	{
		this.immx = immx;
	}

	public int getImmx ()
	{
		return this.immx;
	}

	public void rs1Set (int rs1)
	{
		this.rs1 = rs1;
	}

	public void rs2Set (int rs2)
	{
		this.rs2 = rs2;
	}

	public void rdSet (int rd)
	{
		this.rd = rd;
	}

	public int rs1Get ()
	{
		return this.rs1;
	}

	public int rs2Get ()
	{
		return this.rs2;
	}

	public int rdGet ()
	{
		return this.rd;
	}

	public void setInstructionClass (int type)
	{
		this.type = type;
	}

	public int getInstructionClass ()
	{
		return this.type;
	}

	public void setBranchTarget(int branchTarget) 
	{
		this.branchTarget = branchTarget;
	}

	public int getBranchTarget() 
	{
		return this.branchTarget;
	}

	public void immSet(int imm) 
	{
		this.imm = imm;
	}

	public int immGet() 
	{
		return this.imm;
	}
	
	public void pcSet(int op)
	{
		this.pc = op;
	}

	public int pcGet()
	{
		return pc;
	}

	public void set_instype(int op)
	{
		this.type = op;
	}
	
	public int get_instype(int op)
	{
		return type;
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