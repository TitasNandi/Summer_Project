import java.util.*;
import java.io.*;
public class assign_1
{
	static int count = 0;
	public static void main(String args[])
	{
		/* Enter details in a file in the following format line by line:
		 * Source type (1 for fixed, 2 for Bursty)
		 * Switching type (1 for packet switch, 2 for circuit switch)
		 * Number of sources
		 * if Source is fixed enter packet_sending_rate(in packets/sec), else enter burst_interval(in sec)<space> burst_size (in packets/burst)
		 * Packet size (in Mb)
		 * Link Bandwidth from source to switch (in Mbps)
		 * Link Bandwidth from switch to sink (in Mbps)
		 * Simulation Time in seconds
		 * Source queue size
		 * Switch queue size
		 */
		ArrayList<String> a = new ArrayList<>();
		double band_source;
		int source_type;
		int switch_type;
		int num_sources;
		double psr = 0;
		double burst_interval;
		int burst_size;
		double packet_size;
		double band_switch;
		double sim_time;
		int sou_queue;
		int swi_queue;
		FileReader fr = null;
		EventComparator comparator = new EventComparator();
		PriorityQueue<packet> p = new PriorityQueue<>(comparator);
		try
		{
			fr = new FileReader("/mnt/Titas/1_QA_MODEL/Tools/Networks/src/Input.txt");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		BufferedReader br = null;
		String currentLine;
		try
		{
			br = new BufferedReader(fr);
			while((currentLine = br.readLine())!= null)
			{
				a.add(currentLine);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		int i = -1;
		source_type = Integer.parseInt(a.get(++i));
		switch_type = Integer.parseInt(a.get(++i));
		num_sources = Integer.parseInt(a.get(++i));
		source[] sou_arr= new source[num_sources];

		if(source_type == 1)
		{
			psr = Double.parseDouble(a.get(++i));
			for(int j=0;j<num_sources;j++)
			{

				double rand = myRandom();
				sou_arr[j] = new fixed(j, psr, rand, rand);
				p.add(new packet(j,0,rand));
				System.out.println("Packet 0 of source "+j+ " generated at "+rand);
			}
		}
		else
		{
			burst_interval = Double.parseDouble(a.get(++i).split(" ")[0]);
			burst_size = Integer.parseInt(a.get(i).split(" ")[1]);
			for(int j=0;j<num_sources;j++)
			{
				double rand = myRandom();
				sou_arr[j] = new bursty(j, burst_interval, burst_size, rand, rand);
				for(int k=0;k<burst_size;k++)
				{
					p.add(new packet(j,k,rand));
					System.out.println("Packet "+k+" of source "+j+ " generated at "+rand);
				
				}
			}
		}
		
		packet_size = Double.parseDouble(a.get(++i));
		band_source = Double.parseDouble(a.get(++i));
		band_switch = Double.parseDouble(a.get(++i));
		sim_time = Double.parseDouble(a.get(++i));
		sou_queue = Integer.parseInt(a.get(++i));
		swi_queue = Integer.parseInt(a.get(++i));
		double tr_time = packet_size/band_source;
		
		if(switch_type == 1)
		{
			packet_switch link = new packet_switch(packet_size, band_source, band_switch, swi_queue);
			packetSwitch(sim_time, sou_arr, link, p, source_type);
		}
		else
		{
			circuit_switch link = new circuit_switch(packet_size, band_source, band_switch, swi_queue, num_sources);
			circuitSwitch(sim_time, sou_arr, link, p, source_type, num_sources);
		}
//		
	}
	static double myRandom() {
	    Random generator = new Random();
		double number = generator.nextDouble();
		return number;
	}
	static void packetSwitch(double sim_time, source[] sou, packet_switch sw, PriorityQueue<packet> p, int packet_type)
	{
		while(!p.isEmpty())
		{
			packet pre_packet = p.poll();
			int s_id = pre_packet.s_id;
			double pre_time = pre_packet.timestamp;
			if(pre_packet.timestamp > sim_time)
			{
				System.out.println("Simulation over!");
				break;
			}
			source pre_source = sou[s_id];
			if(packet_type == 1)
			{
				pre_source = (fixed) sou[s_id];					
			}
			else
			{
				pre_source = (bursty) sou[s_id];
			}
			pre_source.next(p, s_id, sim_time);
			switch(pre_packet.status)
			{
				case 0: 
					{
						state0(pre_packet, pre_source, p, sw);
					}
				case 1:
					{
						state1(pre_packet, pre_source, p, sw, pre_time);
					}
				case 2:
					{
						state2(pre_packet, pre_source, p, sw);
					}
				case 3:
					{
						state3(pre_packet, pre_source, p, sw);
					}
				case 4:
					{
						state4(pre_packet, pre_source, p, sw, pre_time);
					}
			}
			
		}
	}
	static void circuitSwitch(double sim_time, source[] sou, circuit_switch sw, PriorityQueue<packet> p, int packet_type, int num_sources)
	{
		int round_robin = 0;
		double slot = sw.packet_size/sw.bandwidth_out;
		double interrupt = slot;
		while(!p.isEmpty())
		{
			source pre_source;
			if(interrupt < p.peek().timestamp)
			{
				interrupt+=slot;
				if(!sw.arr[round_robin].isEmpty())
				{
					packet l = sw.arr[round_robin].poll();
					l.timestamp = interrupt;
					l.status = 3;
					System.out.println("Packet "+l.p_id+" of source "+l.s_id+ " dropped at switch at "+ l.timestamp);
					p.add(l);
				}
				if(packet_type == 1)
				{
					pre_source = (fixed) sou[round_robin];					
				}
				else
				{
					pre_source = (bursty) sou[round_robin];
				}
				pre_source.next(p, round_robin, sim_time);
				round_robin = (round_robin+1)%num_sources;
			}
			else
			{
				packet pre_packet = p.poll();
				int s_id = pre_packet.s_id;
				if(pre_packet.timestamp > sim_time)
				{
					System.out.println("Simulation over!");
					break;
				}
				if(packet_type == 1)
				{
					pre_source = (fixed) sou[s_id];					
				}
				else
				{
					pre_source = (bursty) sou[s_id];
				}
				pre_source.next(p, s_id, sim_time);
				if(pre_packet.status == 0)
				{
					state0(pre_packet, pre_source, p, sw);
				}
				else if(pre_packet.status == 1)
				{
					pre_source.transmit = false;
					if(sw.arr[s_id].size() >= sw.queue_size)
					{
						System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " dropped at switch at "+ pre_packet.timestamp);
						return;
					}
					pre_packet.status = 4;
					sw.arr[s_id].add(pre_packet);
				}
				else if(pre_packet.status == 2)
				{
					state2(pre_packet, pre_source, p, sw);
				}
				else
				{
					System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " received at sink at "+ pre_packet.timestamp);
				}
			}
		}
	}
	static void state0(packet pre_packet, source pre_source, PriorityQueue<packet> p, router sw)
	{
		if(!pre_source.q.isEmpty())
		{
			if(pre_source.q.size() >= pre_source.q_fill)
			{
				System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " dropped at source at "+ pre_packet.timestamp);
				return;
			}
			pre_packet.status = 2;
			pre_packet.timestamp = pre_source.finish;
			p.add(pre_packet);
			pre_source.q.add(pre_packet);
			System.out.println("Packet "+pre_packet.p_id+ " queued at source "+pre_packet.s_id+ " at "+ pre_packet.timestamp);
		}
		else if(pre_source.transmit == false)
		{
			pre_packet.status = 1;
			pre_source.transmit = true;
			double trans = (sw.packet_size/sw.bandwidth_in);
			pre_packet.timestamp += trans;
			pre_source.finish = pre_packet.timestamp;
			p.add(pre_packet);
			System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " transmitted to switch at "+ pre_packet.timestamp);
				
		}
		else
		{
			pre_packet.status = 2;
			pre_packet.timestamp = pre_source.finish;
			p.add(pre_packet);
			pre_source.q.add(pre_packet);
			System.out.println("Packet "+pre_packet.p_id+ " queued at source "+pre_packet.s_id+ " at "+ pre_packet.timestamp);	
		}
	}
	static void state1(packet pre_packet, source pre_source, PriorityQueue<packet> p, packet_switch sw, double pre_time)
	{
		pre_source.transmit = false;
		if(!sw.Q.isEmpty())
		{
			if(sw.Q.size()>=sw.queue_size)
			{
				System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " dropped at switch at "+ pre_packet.timestamp);
				return;
			}
			pre_packet.status = 4;
			pre_packet.timestamp = sw.finish;
			pre_packet.change_time = pre_time;
			sw.Q.add(pre_packet);
			p.add(pre_packet);
			System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " queued at switch at "+ pre_packet.timestamp);
		}
		else if(sw.transmit == false)
		{
			pre_packet.status = 3;
			pre_source.transmit = true;
			double trans = (sw.packet_size/sw.bandwidth_out);
			pre_packet.change_time = pre_time;
			pre_packet.timestamp += trans;
			sw.finish = pre_packet.timestamp;
			p.add(pre_packet);
			System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " transmitted to sink at "+ pre_packet.timestamp);
				
		}
		else
		{
			pre_packet.status = 4;
			pre_packet.timestamp = sw.finish;
			pre_packet.change_time = pre_time;
			p.add(pre_packet);
			sw.Q.add(pre_packet);
			System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " queued at switch at "+ pre_packet.timestamp);
		}
		
	}
	static void state2(packet pre_packet, source pre_source, PriorityQueue<packet> p, router sw)
	{
		if(pre_source.transmit == false)
		{
			packet l = pre_source.q.poll();
			pre_packet.status = 1;
			pre_packet.timestamp += (sw.packet_size/sw.bandwidth_in);
			pre_source.finish = pre_packet.timestamp;
			pre_source.transmit = true;
			System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " transmitted to switch at "+ pre_packet.timestamp);
		}
		else
		{
			pre_packet.timestamp = pre_source.finish;
			System.out.println("Packet "+pre_packet.p_id+ " queued at source "+pre_packet.s_id+ " at &"+ pre_packet.timestamp);
		}
		p.add(pre_packet);
	}
	static void state3(packet pre_packet, source pre_source, PriorityQueue<packet> p, packet_switch sw)
	{
		sw.transmit = false;
		System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " received at sink at "+ pre_packet.timestamp);
	}
	static void state4(packet pre_packet, source pre_source, PriorityQueue<packet> p, packet_switch sw, double pre_time)
	{
		if(sw.transmit == false)
		{
			packet l = sw.Q.poll();
			pre_packet.status = 3;
			pre_packet.timestamp += (sw.packet_size/sw.bandwidth_out);
			pre_packet.change_time = pre_time;
			sw.finish = pre_packet.timestamp;
			sw.transmit = true;
			System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " transmitted to sink at "+ pre_packet.timestamp);
		}
		else
		{
			pre_packet.timestamp = sw.finish;
			pre_packet.change_time = pre_time;
			System.out.println("Packet "+pre_packet.p_id+" of source "+pre_packet.s_id+ " queued at switch at :"+ pre_packet.timestamp);
		}
		p.add(pre_packet);
	}
	
}

class source
{
	int s_id;
	double rand;
	Queue<packet> q = new LinkedList<packet>();
	int q_fill;
	double gen_time;
	boolean transmit;
	double finish;
	int num_packet;
	public source(int count, double rand, double gen_time)
	{
		
		this.num_packet = 1;
		s_id = count;
		this.rand = rand;
		this.gen_time = gen_time;
		this.transmit = false;
	}
	public void next(PriorityQueue<packet> p, int index, double sim_time)
	{
		// method to be extended
	}
}

class packet
{
	int s_id;
	double change_time;
	int p_id;
	double timestamp;
	int status;
	public packet(int s_id, int p_id, double timestamp)
	{
		this.s_id = s_id;
		this.p_id = p_id;
		this.timestamp = timestamp;
		this.status = 0;
		this.change_time = timestamp;
	}
//	public int compareTo(packet other) {
//        if(timestamp > other.timestamp)
//        {
//        	return -1;
//        }
//        else if(timestamp < other.timestamp)
//        {
//        	return 1;
//        }
//        else
//        {
//        	if(change_time > other.change_time)
//        	{
//        		return -1;
//        	}
//        	else if(change_time < other.change_time)
//        	{
//        		return 1;
//        	}
//        	return 0;
//        }
//    }
}

class router
{
	double bandwidth_in;
	double bandwidth_out;
	double packet_size;
	router(double bandwidth_in, double bandwidth_out,double packet_size)
	{
		this.bandwidth_out = bandwidth_out;
		this.bandwidth_in = bandwidth_in;
		this.packet_size = packet_size;
	}
}

class fixed extends source
{
	double sending_rate;
	public fixed(int s_id, double sending_rate, double rand, double gen_time)
	{
		super(s_id, rand, gen_time);
		this.sending_rate = sending_rate;
	}
	public void next(PriorityQueue<packet> p, int index, double sim_time)
	{
		this.gen_time += (1/sending_rate);
		if(this.gen_time <= sim_time)
		{
			p.add(new packet(s_id, num_packet++, gen_time));
			System.out.println("Packet "+ (num_packet-1)+" of source "+s_id+ " generated at "+gen_time);
		}
	}
}

class bursty extends source
{
	int burst_size;
	double burst_interval; 
	public bursty(int s_id, double burst_interval, int burst_size, double rand, double gen_time)
	{
		super(s_id, rand, gen_time);
		this.burst_interval = burst_interval;
		this.burst_size = burst_size;
	}
	public void next(PriorityQueue<packet> p, int index, double sim_time)
	{
		this.gen_time += burst_interval;
		if(this.gen_time <= sim_time)
		{
			for(int i=0; i< burst_size; i++)
			{
				p.add(new packet(s_id, this.num_packet++, gen_time));
				System.out.println("Packet "+ (num_packet-1)+" of source "+s_id+ " generated at "+gen_time);
			}
		}
	}
}

class packet_switch extends router
{
	Queue<packet> Q = new LinkedList<packet>();
	boolean transmit;
	int queue_size;
	double finish;
	public packet_switch(double packet_size, double bandwidth_in, double bandwidth_out, int queue_size)
	{
		super(bandwidth_in, bandwidth_out, packet_size);
		this.queue_size = queue_size;
		this.transmit = false;
	}
}
class circuit_switch extends router
{
	int num_sources;
	int queue_size;
	Queue<packet>[] arr;
	public circuit_switch(double packet_size, double bandwidth_in, double bandwidth_out, int queue_size, int num_sources)
	{
		super(bandwidth_in, bandwidth_out, packet_size);
		this.queue_size = queue_size;
		this.num_sources = num_sources;
		this.arr = new Queue[num_sources];
		for(int i=0;i<num_sources;i++)
		{
			arr[i] = new LinkedList<>();
		}
	}
}
class EventComparator implements Comparator<packet> {

    @Override
    public int compare(packet o1, packet o2) {
        if(o1.timestamp > o2.timestamp)
            return 1;
        else if(o1.timestamp==o2.timestamp){
        	if(o1.change_time > o2.change_time)
        		return 1;
        	else if(o1.change_time == o2.change_time)
        		return 0;
        	else
        		return -1;
        }
        return -1;
        
    }
}