import processing.core.PImage;

import java.util.List;

public class Sapling extends Healable implements Constants {

    public Sapling(String id, Point position, List<PImage> images) {
        super(id, position, images, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        if (!this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( this,
                    this.createActivity(world, imageStore),
                    SAPLING_ACTION_ANIMATION_PERIOD);
        }
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
        } else if (this.health >= SAPLING_HEALTH_LIMIT) {
            ActivityCapable tree = new Tree("tree_" + this.id,
                    this.position,
                    Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
                    imageStore.getImageList(TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}

