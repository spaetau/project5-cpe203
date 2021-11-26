import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{

    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        PriorityQueue<WorldNode> openList;
        HashSet<Point> closedList;
        WorldNode curr = new WorldNode(start, 0, manhattanDist(start, end), null);
        
        return null;
    }

    private int manhattanDist(Point p1, Point p2){
        return 0;
    }
}
