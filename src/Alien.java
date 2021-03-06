import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Alien extends Movable{
    public Alien(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Point.adjacent(this.position, target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
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
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> alienTarget =
                world.findNearest(this.position, new ArrayList<Entity>(
                        Arrays.asList(new DudeNotFull(null,null, null, 0, 0, 0),
                                new DudeFull(null,null, null, 0, 0, 0))));

        if (alienTarget.isPresent()) {
            Point tgtPos = alienTarget.get().getPosition();

            if (this.moveTo(world, alienTarget.get(), scheduler)) {
                world.removeEntityAt(tgtPos);
                Animatable explosion = new DeathExplosion("explosion_" + this.id, tgtPos,
                        imageStore.getImageList(Constants.DEATH_EXPLOSION), 51, 51);

                world.addEntity(explosion);
                explosion.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                this.createActivity(world, imageStore),
                this.actionPeriod);
    }
}
