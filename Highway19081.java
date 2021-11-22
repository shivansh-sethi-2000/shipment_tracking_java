package demo19081;
import base.*;
import java.util.*;

public class Highway19081 extends Highway{

    private int current = 0;
    ArrayList<Truck> trucks = new ArrayList<>();

    @Override
    public synchronized boolean hasCapacity() {
        if(current < getCapacity())
            return true;
        return false;
    }

    @Override
    public synchronized boolean add(Truck truck) {
        if(current < getCapacity()) {
            if(trucks.contains(truck)) {
                return true;
            }
            truck.enter(this);
            trucks.add(truck);
            current++;
            return true;
        }
        return false;
    }

    @Override
    public synchronized void remove(Truck truck) {
        try {
            trucks.remove(truck);
            current--;
        } catch (Exception e){
            System.out.println("truck not in highway.....");
        }

    }

}
