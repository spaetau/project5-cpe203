import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{

    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        Comparator<WorldNode> comp = Comparator.comparingInt(WorldNode::getTotDistance);
        PriorityQueue<WorldNode> openList = new PriorityQueue<>(comp);
        HashMap<Point, WorldNode> openListMap = new HashMap<>();
        HashSet<Point> closedList = new HashSet<>();
        WorldNode curr = new WorldNode(start, 0, manhattanDist(start, end), null);
        boolean targetReached = false;
        List<Point> path = new LinkedList<>();
        openList.add(curr);

        while (!openList.isEmpty()) {
            curr = openList.remove();
            Point currPoint = curr.getPoint(); //.getPoint used frequently, might as well just make it its own variable.
            closedList.add(currPoint);


        if (withinReach.test(currPoint, end)) {
            targetReached = true;
            break;
        }

            List<Point> temp = CARDINAL_NEIGHBORS.apply(currPoint)
                    .filter(canPassThrough)
                    .filter(point -> !closedList.contains(point))
                    .collect(Collectors.toList());
            for (Point p : temp){ //wouldnt let me use non-final variable in the lambda, curr
               WorldNode node = new WorldNode(p, curr.getDistanceToStart() + 1, manhattanDist(p, end), curr);

               if(openListMap.containsKey(p)){
                    if(openListMap.get(p).getDistanceToStart() > node.getDistanceToStart()){
                        openList.removeIf(worldNode -> worldNode.getPoint().equals(p));
                        openList.add(node);
                    }//this is the most disgusting thing i have ever written in my life, i am sorry.
               }
               else{
                   openListMap.put(p, node);
                   openList.add(node);
               }
            }
        }
        if (targetReached){
            while(curr.getPriorNode() != null){
            path.add(0, curr.getPoint());
            curr = curr.getPriorNode();
            }
        }
        return path;
    }

//    private boolean withinReach(Point p1, Point p2) {
//        return manhattanDist(p1, p2) == 1;
//    }

    private int manhattanDist(Point p1, Point p2){
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}
