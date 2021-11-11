import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends DudeAbstract {

    public DudeNotFull(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod, 0, resourceLimit);

    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Point.adjacent(this.position, target.getPosition())) {
                this.resourceCount += 1;
                Healable temp = (Healable) target;
                temp.updateHealth(-1);
                return true;

            }
        else {
            return super.moveTo(world,
                    target,
                    scheduler);
        }
    }

    public void executeActivity( //will cleanup later
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Tree temp1 = new Tree(null, null, 0,0, 0, null);
        Sapling temp2 = new Sapling(null, null, null);
            Optional<Entity> target =
                    world.findNearest(this.position, new ArrayList<Entity>(Arrays.asList(temp1, temp2)));

            if (!target.isPresent() || !this.moveTo(world,
                    target.get(),
                    scheduler)
                    || !this.transform(world, scheduler, imageStore)) {
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

        if (this.resourceCount >= this.resourceLimit) {
            DudeFull miner = new DudeFull(this.id,
                    this.position, this.images, this.animationPeriod, this.actionPeriod,
                    this.resourceLimit);


            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


}
