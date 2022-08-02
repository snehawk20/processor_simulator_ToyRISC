package processor.memorysystem;
import generic.*;
import processor.Processor;
import processor.Clock;
import configuration.Configuration;

public class Cache implements Element{

    Processor containingProcessor;
    public Element returningEl;
    int CLsize, start,end, current;
    int Cline;
    int hit, miss;
    int address, valueToWrite, valueToRead;
    long latency;
    CacheLine[] L1Cache;

    public Cache(int size,long latency,Element element, Processor processor  ){
        this.CLsize = size;
        size = CLsize + 1;
        this.L1Cache = new CacheLine[size];

        this.containingProcessor = processor;
        this.returningEl = element;
        this.start = -1;
        this.end = -1;
        this.current = 0;
        this.Cline = 0;
        this.hit = 0;
        this.miss = 0;

        this.address = -1;
        this.valueToWrite = 0;
        this.valueToRead = 0;
        this.latency = latency;

        for(int i =0; i< size; i++){
            L1Cache[i] = new CacheLine();
        }
    }


    public void cacheRead(int address,MemoryReadEvent e){
        hit = 0;
        for(int i = 0; i<current ;i++){
            Cline = L1Cache[(start+ i) % CLsize].tag;
            if(Cline == address){
                hit = 1;
                this.address = address;
                valueToRead = L1Cache[(start + i)%CLsize].data;
                MemoryReadEvent event = (MemoryReadEvent) e;
                MemoryResponseEvent MRE = new MemoryResponseEvent(Clock.getCurrentTime()  ,this,event.getRequestingElement(),valueToRead);
                MRE.setVal(0);
                Simulator.getEventQueue().addEvent(MRE);
                return;

            }
        }
        if(hit == 0){
            handleCacheMiss(address,e);
        }
    }

    public void handleCacheMiss(int address, Event e){
        miss = 1;
        this.address = address;

        MemoryReadEvent events = new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency,this,containingProcessor.getMainMemory(),address);
        Simulator.getEventQueue().addEvent(events);
    }

    public void cacheWrite(int address, int value){
        this.address = address;
        valueToWrite = value;
        hit = 0;
        for(int i = 0; i< current; i++){
            Cline = L1Cache[(start+ i) % CLsize].tag;
            if(Cline == address){
                hit = 1;
                //write through policy
                MemoryWriteEvent event =new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,this,containingProcessor.getMainMemory(),address,valueToWrite);
                Simulator.getEventQueue().addEvent(event);

            }
        }
        if(hit == 0) {
            MemoryWriteEvent event = new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency ,this,containingProcessor.getMainMemory(),address,valueToWrite);

            Simulator.getEventQueue().addEvent(event);
        }
    }

    public void handleResponse(MemoryResponseEvent e)
    {
        MemoryResponseEvent MRE = new MemoryResponseEvent(Clock.getCurrentTime(),this, returningEl, e.getValue());
        MRE.setVal(e.getVal());
        if (e.getVal() == 0 && miss == 1)
        {
            if (current >= CLsize)
            {
                L1Cache[start].tag = address;
                L1Cache[start].data = MRE.getValue();
                start = (start + 1) % CLsize;
                end = (end + 1) % CLsize;
            }
            else if (current == 0)
            {
                start = 0;
                end = 0;
                current += 1;
                L1Cache[end].tag = address;
                L1Cache[end].data = MRE.getValue();
            }
            else
            {
                end = (end + 1) % CLsize;
                current += 1;
                L1Cache[end].tag = address;
                L1Cache[end].data = MRE.getValue();
            } 
        }
        else if (e.getVal() == 1 && miss == 0)
        {
            for (int i = 0; i < current; i++)
            {
                if (L1Cache[(start + i) % CLsize].tag == address)
                {
                    L1Cache[(start + i) % CLsize].data = valueToWrite;
                }
            }
            miss = 1;
        }
        Simulator.getEventQueue().addEvent(MRE);

    }

    @Override
    public void handleEvent(Event e)
    {
        if (e.getEventType() == Event.EventType.MemoryRead)
        {
            MemoryReadEvent event = (MemoryReadEvent) e;
            cacheRead((event.getAddressToReadFrom()), event);
        }
        else if (e.getEventType() == Event.EventType.MemoryWrite)
        {
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            cacheWrite(event.getAddressToWriteTo(), event.getValue());
        }
        else
        {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            handleResponse(event);
        }
    }
}
