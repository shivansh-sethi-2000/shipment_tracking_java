package demo19081;
import base.*;
import java.util.*;

public class Network19081 extends Network{

    List<Hub> hubs = Collections.synchronizedList(new ArrayList<>());
    List<Truck> trucks = Collections.synchronizedList(new ArrayList<>());
    List<Highway> highways = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void add(Hub hub) {
        hubs.add(hub);
    }

    @Override
    public void add(Truck truck) {
        trucks.add(truck);
    }

    @Override
    public void add(Highway hwy) {
        highways.add(hwy);
    }

    @Override
    public void redisplay(Display disp) {
        for(Truck truck : trucks) {
            truck.draw(disp);
        }
        for(Hub hub:hubs) {
            hub.draw(disp);
        }
        for(Highway h:highways) {
            h.draw(disp);
        }
    }

    @Override
    public Hub findNearestHubForLoc(Location loc) {
        Hub res = null;
        int dis = (int)1e9;
        for(Hub h : hubs) {
            if(h.getLoc().distSqrd(loc) < dis) {
                res = h;
                dis = h.getLoc().distSqrd(loc);
            }
        }
        return res;
    }

    @Override
    public void start() {
        for(Truck truck : trucks) {
            truck.start();
        }
        for(Hub hub : hubs) {
            hub.start();
        }
    }

    public List<Highway> getHighways() {
        return highways;
    }
}
