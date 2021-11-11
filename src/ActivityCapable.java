import processing.core.PImage;

import java.util.List;

abstract class ActivityCapable extends Animatable {
    protected int actionPeriod;

    public ActivityCapable(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    abstract void executeActivity(WorldModel world,
                                  ImageStore imageStore,
                                  EventScheduler scheduler);

    public Activity createActivity(
            WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createActivity(world, imageStore),
                this.actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }
}
