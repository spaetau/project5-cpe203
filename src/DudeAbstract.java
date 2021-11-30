import processing.core.PImage;

import java.util.List;
import java.util.Optional;

abstract class DudeAbstract extends Movable implements Transformable{
    protected int resourceCount;
    protected int resourceLimit;

    public DudeAbstract(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceCount, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public Point nextPosition(WorldModel world, Point destPos, PathingStrategy path) {
        List<Point> temp  = path.computePath(this.position, destPos,
                (p) -> world.withinBounds(p) && (!world.isOccupied(p) || world.getOccupant(p).get() instanceof Stump),
                (p1, p2) -> Point.adjacent(p1, p2) && world.withinBounds(p1) && world.withinBounds(p2),
                path.CARDINAL_NEIGHBORS);
        if (temp.size() > 0){
            return temp.get(0);
        }
        return this.position;
    }

    public boolean moveTo(WorldModel world,
                          Entity target,
                          EventScheduler scheduler){
        Point nextPos = this.nextPosition(world, target.getPosition(), new AStarPathingStrategy());

        if (!this.position.equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
        }
        return false;
    }

    @Override
    public boolean isObstacle(WorldModel world, Point pos){
        return world.isOccupied(pos) && !(world.getOccupancyCell(pos) instanceof Stump);
    }
}
