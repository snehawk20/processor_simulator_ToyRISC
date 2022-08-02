package processor.memorysystem;
public class CacheLine {
    public int tag;
    public int data;

    public void CacheLine()
    {
        this.tag = -1;
        this.data = -1;
    }
}
