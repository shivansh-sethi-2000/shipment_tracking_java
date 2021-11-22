package demo19081;
import base.*;

import java.util.*;

public class Hub19081 extends Hub{

    HashMap<Truck,Highway> queue = new HashMap<>();
    Hub19081(Location loc) {
        super(loc);
    }

    @Override
    public synchronized boolean add(Truck truck) {
        if(getCapacity() == 0) {
            System.out.println();
            System.out.println(truck.getTruckName() +" waiting to enter......." + this.getLoc().toString());
            return false;
        }
        System.out.println();
        System.out.println(truck.getTruckName() + " reached....." + this.getLoc().toString());
        setCapacity(getCapacity()-1);
        queue.put(truck,getNextHighway(truck.getLastHub(), Network19081.getNearestHub(truck.getDest())));
        return true;
    }

    public synchronized void remove(Truck truck) {
        try {
            queue.remove(truck);
            int cap = getCapacity();
            setCapacity(cap+1);
        } catch(Exception e) {
            System.out.print("No truck in the Hub to remove..");
        }
    }

    public Highway getNextHighway(Hub last, Hub des) {
        ArrayList<Highway> highways = getHighways();
        Highway hwy = null;
        double time = 1e9;
        if(this == des) {
            return hwy;
        }
        for (Highway h : highways) {
            double total = bfs(h.getEnd(),des);
            if(total == -1)
                continue;

            total += this.getLoc().distSqrd(h.getEnd().getLoc())/h.getMaxSpeed();
            if(total < time) {
                hwy = h;
                time = total;
            }
        }
        return hwy;
    }


    public double bfs(Hub hub, Hub des) {
        HashMap<Hub,Boolean> vis = new HashMap<>();
        HashMap<Hub,Double> time = new HashMap<>();
        Queue<Hub> hubs = new LinkedList<>();
        time.put(hub,0d);
        hubs.add(hub);

        while(!hubs.isEmpty()) {
            Hub from = hubs.remove();
            if(vis.getOrDefault(from,false)) {
                continue;
            }
            vis.put(from,true);
            for(Highway high : from.getHighways()) {
                double speed = high.getMaxSpeed();
                double dis = from.getLoc().distSqrd(high.getEnd().getLoc());
                double time_taken = dis/speed;
                if(time.get(from)+time_taken < time.getOrDefault(high.getEnd(),1e9)) {
                    time.put(high.getEnd(), time.get(from)+time_taken);
                    hubs.add(high.getEnd());
                }
            }
        }

        return time.getOrDefault(des,-1d);
    }

    public void processQ(int delta) {

        for(Map.Entry<Truck,Highway> map : queue.entrySet()) {
//            System.out.println(map.getKey().getTruckName() + " " + map.getValue().hasCapacity());
            if(map.getValue() == null) {
                Truck truck = map.getKey();
                remove(truck);
                queue.remove(truck);
            }
            else if(map.getValue().hasCapacity() == true) {
                Truck truck = map.getKey();
                Highway highway = map.getValue();
                highway.add(truck);
                truck.enter(highway);
                System.out.println();
                System.out.println(truck.getTruckName() + "going to...... " +highway.getEnd().getLoc().toString());
                remove(truck);
                queue.remove(truck);
            }
        }
    }
}
