import processing.core.PImage;

import java.util.List;

public class Sapling implements WoodPlant, Entity, ActivityCapable {
    private String id;
    private Point position;
    private List<PImage> images;
    private int health;
    private int actionPeriod;
    private int imageIndex;


    public Sapling(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.health = SAPLING_HEALTH;
        this.imageIndex = 0;


    }

    @Override
    public void updateHealth(int i) {
        this.health += i;
    }

    public void updatePosition(Point pos){
        this.position = pos;
    }

    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex);
    }
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
    public Point getPosition() {
        return this.position;
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
    public int getAnimationPeriod(){
        return SAPLING_ACTION_ANIMATION_PERIOD;
    }

    public void tryAddEntity(WorldModel world) {
        if (world.isOccupied(this.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(this);
    }
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createActivity(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimation(0),
                this.getAnimationPeriod());
    }


    public Action createActivity(
            WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }
    public Action createAnimation(int repeatCount) {
        return new Animation(this, null, null,
                repeatCount);
    }

}

