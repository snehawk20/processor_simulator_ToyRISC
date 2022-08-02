package generic;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import processor.memorysystem.Cache;
import processor.Clock;
import processor.pipeline.InstructionFetch;

public class EventQueue {

	PriorityQueue<Event> queue;

	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
	}

	public void addEvent(Event event)
	{
		queue.add(event);
	}

	public void deleteEvent() 
	{
		// Iterator<Event> it = queue.iterator();
		// while(it.hasNext() == true) {
		// 	Event e = it.next();
		// 	if (e.requestingElement instanceof InstructionFetch == true && e.processingElement instanceof InstructionFetch == true)
		// 	{
		// 		((InstructionFetch)e.requestingElement).IF_EnableLatch.setIF_busy(false);
		// 		((InstructionFetch)e.processingElement).IF_EnableLatch.setIF_busy(false);
		// 		it.remove();
		// 		it.remove();
		// 	}
		// 	else if (e.requestingElement instanceof InstructionFetch == true && e.processingElement instanceof InstructionFetch == false) 
		// 	{
		// 		((InstructionFetch)e.requestingElement).IF_EnableLatch.setIF_busy(false);
		// 		it.remove();
		// 	}
		// 	else if (e.processingElement instanceof InstructionFetch == true && e.requestingElement instanceof InstructionFetch == false) 
		// 	{
		// 		((InstructionFetch)e.processingElement).IF_EnableLatch.setIF_busy(false);
		// 		it.remove();
		// 	}

		// 	if(e.requestingElement instanceof InstructionFetch == true && e.processingElement instanceof InstructionFetch == true) {


			
		// 		if (((Cache) e.requestingElement).returningEl instanceof InstructionFetch) {
		// 			((InstructionFetch) ((Cache) e.requestingElement).returningEl).IF_EnableLatch.setIF_busy(false);
		// 			it.remove();
		// 			continue;
		// 		}

		// 		if (((Cache) e.processingElement).returningEl instanceof InstructionFetch) {
		// 			((InstructionFetch) ((Cache) e.processingElement).returningEl).IF_EnableLatch.setIF_busy(false);
		// 			it.remove();
		// 			continue;
		// 		}
		// 	}
		// 	else if(e.requestingElement instanceof InstructionFetch == true && e.processingElement instanceof InstructionFetch == false){
		// 		if(((Cache)e.requestingElement).returningEl instanceof InstructionFetch) {
		// 			((InstructionFetch) ((Cache) e.requestingElement).returningEl).IF_EnableLatch.setIF_busy(false);
		// 			it.remove();
		// 			continue;
		// 		}
		// 	}
		// 	else if(e.requestingElement instanceof InstructionFetch == false && e.processingElement instanceof InstructionFetch == true){
		// 		if(((Cache)e.processingElement).returningEl instanceof InstructionFetch) {
		// 			((InstructionFetch) ((Cache) e.processingElement).returningEl).IF_EnableLatch.setIF_busy(false);
		// 			it.remove();
		// 			continue;
		// 		}
		// 	}
		// }

		//public void delete_IF() {
			Iterator<Event> itr=queue.iterator();
			Event e;
			while(itr.hasNext()) {
				e = itr.next();
				if(e.requestingElement instanceof InstructionFetch) {
					((InstructionFetch)e.requestingElement).IF_EnableLatch.setIF_busy(false);
					itr.remove();
					continue;
				}
				if(e.processingElement instanceof InstructionFetch) {
					((InstructionFetch)e.processingElement).IF_EnableLatch.setIF_busy(false);
					itr.remove();
					continue;
				}
				if(e.processingElement instanceof Cache) {
					if(((Cache)e.processingElement).returningEl instanceof InstructionFetch){
						((InstructionFetch)((Cache)e.processingElement).returningEl).IF_EnableLatch.setIF_busy(false);
						itr.remove();
						continue;
					}
				}
				if(e.requestingElement instanceof Cache) {
					if(((Cache)e.requestingElement).returningEl instanceof InstructionFetch){
						((InstructionFetch)((Cache)e.requestingElement).returningEl).IF_EnableLatch.setIF_busy(false);
						itr.remove();
						continue;
					}
				}
				
			}
		//}
	}

	public void processEvents()
	{
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
	}
}

class EventComparator implements Comparator <Event>
{
	@Override
	public int compare(Event x, Event y)
	{
		long t1 = x.getEventTime();
		long t2 = y.getEventTime();
		if (t1 > t2)
			return 1;
		else if (t1 < t2)
			return -1;
		else
			return 0;
	}
}