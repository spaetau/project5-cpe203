import processing.core.PImage;

import java.util.List;
import java.util.Optional;

abstract class DudeAbstract extends Movable{
    protected int resourceCount;
    protected int resourceLimit;

    public DudeAbstract(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceCount, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if ((vert == 0 || world.isOccupied(newPos)) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world,
                          Entity target,
                          EventScheduler scheduler){
        Point nextPos = this.nextPosition(world, target.getPosition());

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
