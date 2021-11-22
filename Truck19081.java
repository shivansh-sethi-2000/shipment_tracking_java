package demo19081;
import base.*;

public class Truck19081 extends Truck{

    private Highway highway = null;
    private int curr_time = 0;
    private boolean onroad = true;
    private boolean on_highway = false;
    private boolean towards_des = false;
    private int left_last = (int)1e9;
    private double lastX = -1;
    private double lastY = -1;

    @Override
    public String getTruckName() {
		return "Truck19081";
	}
    
    public Hub getLastHub() {
        if(highway == null)
            return null;
        return highway.getStart();
    }

    public synchronized void enter(Highway h) {
        highway = h;
        on_highway = true;
    }

    public void update(int time) {
        curr_time += time;
        Location loc = getLoc();
        if(curr_time > getStartTime()) {
            if(lastY == -1) {
                lastY = getSource().getY();
            }
            if(lastX == -1) {
                lastX = getSource().getX();
            }
            // after reaching the destination
            if(towards_des && (loc.distSqrd(Network.getNearestHub(getDest()).getLoc()) > Network.getNearestHub(getDest()).getLoc().distSqrd(getDest()) || left_last == 0)) {
                System.out.println();
                System.out.println(getTruckName() + " finally reached........at " + curr_time);
                System.out.println();
                setLoc(getDest());
                left_last = 0;
                return ;
            }
            // going from last hub towards destination
            if(towards_des) {
                left_last = loc.distSqrd(getDest());
                System.out.println(getTruckName() + " on road......dis left to des.... " + left_last);
                Location des = getDest();
                double theta = Math.atan2((des.getY() - loc.getY()), (des.getX() - loc.getX()));
                double speed = 10;
                double speedX = speed*Math.cos(theta);
                double speedY = speed*Math.sin(theta);

                lastX += speedX*time/1000;
                lastY += speedY*time/1000;
                int X = (int)lastX;
                int Y = (int)lastY;
                loc.setPos(X,Y);
                setLoc(loc);
                return ;
            }
            // reaching at a hub in the route
            if(on_highway && (loc.distSqrd(highway.getStart().getLoc()) > highway.getStart().getLoc().distSqrd(highway.getEnd().getLoc()) || left_last == 0)) {
                left_last = (int)1e9;
                if(highway.getEnd().add(this)) {
                    highway.remove(this);
                    on_highway = false;
                }
                int X = highway.getEnd().getLoc().getX();
                int Y = highway.getEnd().getLoc().getY();
                lastX = X;
                lastY = Y;
                loc.setPos(X,Y);
                if(loc.getX() == Network19081.getNearestHub(getDest()).getLoc().getX() && loc.getY() == Network19081.getNearestHub(getDest()).getLoc().getY()) {
                    left_last = (int)1e9;
                    towards_des = true;
                }
                return ;
            }
            // reaching at the first hub from source in the route
            if(onroad && (loc.distSqrd(getSource()) > getSource().distSqrd(Network19081.getNearestHub(loc).getLoc()) || left_last == 0)) {
                left_last = (int)1e9;
                if(Network19081.getNearestHub(loc).add(this)) {
                    on_highway = false;
                }
                int X = Network19081.getNearestHub(loc).getLoc().getX();
                int Y = Network19081.getNearestHub(loc).getLoc().getY();
                lastX = X;
                lastY = Y;
                loc.setPos(X,Y);
                setLoc(loc);
                onroad = false;
                return ;
            }
            // moving on the highway towards the hub
            if(on_highway) {
                left_last = loc.distSqrd(highway.getEnd().getLoc());
                System.out.println(getTruckName() + " on highway.......dis left to hub.... "+left_last);
                double theta = Math.atan2((highway.getEnd().getLoc().getY() - loc.getY()), (highway.getEnd().getLoc().getX() - loc.getX()));
                int speed = highway.getMaxSpeed();
                double speedX = speed*Math.cos(theta);
                double speedY = speed*Math.sin(theta);

                lastX += speedX*time/1000;
                lastY += speedY*time/1000;
                int X = (int)lastX;
                int Y = (int)lastY;
                loc.setPos(X,Y);
                setLoc(loc);
                return ;
            }
            // moving on road towards the first hub
            if(onroad){
                left_last = loc.distSqrd(Network19081.getNearestHub(loc).getLoc());
                System.out.println(getTruckName() + " on road......dis left to hub.... " + left_last);
                Location des = Network19081.getNearestHub(loc).getLoc();
                double theta = Math.atan2((des.getY() - loc.getY()), (des.getX() - loc.getX()));
                double speed = 10;
                double speedX = speed*Math.cos(theta);
                double speedY = speed*Math.sin(theta);

                lastX += speedX*time/1000;
                lastY += speedY*time/1000;
                int X = (int)lastX;
                int Y = (int)lastY;
                loc.setPos(X,Y);
                setLoc(loc);
                return ;
            }
        }
    }
}
