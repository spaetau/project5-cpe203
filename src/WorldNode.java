public class WorldNode {

    private Point point;
    private int distanceToStart;
    private int distanceToEnd;
    private WorldNode priorNode;
    private int totDistance;

    public int getTotDistance() {
        return totDistance;
    }

    public void change(Point point, int distanceToStart, int distanceToEnd, WorldNode priorNode){
        this.point = point;
        this.distanceToStart = distanceToStart;
        this.distanceToEnd = distanceToEnd;
        this.priorNode = priorNode;
        this.totDistance = distanceToEnd + distanceToStart;
    }


    public Point getPoint() {
        return point;
    }

    public int getDistanceToStart() {
        return distanceToStart;
    }

    public int getDistanceToEnd() {
        return distanceToEnd;
    }

    public WorldNode getPriorNode() {
        return priorNode;
    }

    public WorldNode(Point point, int distanceToStart, int distanceToEnd, WorldNode priorNode) {
        this.point = point;
        this.distanceToStart = distanceToStart;
        this.distanceToEnd = distanceToEnd;
        this.priorNode = priorNode;
        this.totDistance = distanceToEnd + distanceToStart;


    }
}
