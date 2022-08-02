package generic;

public class MemoryResponseEvent extends Event {

	int value;
	int val;
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setVal(int val) {
		this.val = value;
	}

	public int getVal() {
		return val;
	}

}
