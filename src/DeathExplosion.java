import processing.core.PImage;

import java.util.List;

public class DeathExplosion extends ActivityCapable{
    public DeathExplosion(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod){
        super(id, position, images,  animationPeriod, actionPeriod);
    }

    @Override
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if(imageIndex > 12){
            scheduler.unscheduleAllEvents(this);
            world.removeEntity(this);
        }
        else{
            scheduler.scheduleEvent( this,
                    this.createActivity(world, imageStore),
                    this.actionPeriod);
        }
    }
}
