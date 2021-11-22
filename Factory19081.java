package demo19081;
import base.*;
public class Factory19081 extends Factory{

    @Override
    public Highway createHighway() {
        Highway19081 hwy = new Highway19081();
        return hwy;
    }

    @Override
    public Network createNetwork() {
        Network19081 nw = new Network19081();
        return nw;
    }

    @Override
    public Hub createHub(Location location) {
        Hub19081 hub = new Hub19081(location);
        return hub;
    }

    @Override
    public Truck createTruck() {
        Truck19081 truck = new Truck19081();
        return truck;
    }
}
