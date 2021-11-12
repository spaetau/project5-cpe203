import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends DudeAbstract {

    public DudeFull(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod, 0, resourceLimit);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Point.adjacent(this.position, target.getPosition())) {
            return true;
        } else {
            return super.moveTo(world, target, scheduler);
        }
    }

    public void executeActivity( //will cleanup later
         WorldModel world,
         ImageStore imageStore,
         EventScheduler scheduler) {
        House temp = new House(null, null, null);
        Optional<Entity> fullTarget =
                world.findNearest(this.position, new ArrayList<Entity>(Arrays.asList(temp)));

        if (fullTarget.isPresent() && this.moveTo(world,
                fullTarget.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    this.createActivity(world, imageStore),
                    this.actionPeriod);

        }
    }


    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        DudeNotFull miner = new DudeNotFull(this.id,
                this.position, this.images, this.animationPeriod, this.actionPeriod,
                this.resourceLimit);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
        return true;
    }

}
