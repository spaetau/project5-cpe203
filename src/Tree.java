import processing.core.PImage;

import java.util.List;

public class Tree extends Healable implements Constants{

    public Tree(String id, Point position, int actionPeriod, int animationPeriod, int health, List<PImage> images) {
        super(id, position, images, animationPeriod, actionPeriod, health);
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(this.id,
                    this.position,
                    imageStore.getImageList(STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
//            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    this.createActivity(world, imageStore),
                    this.actionPeriod);
        }
    }
}
