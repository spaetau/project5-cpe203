import processing.core.PImage;

import java.util.List;

public class Tree implements ActivityCapable, Healable {


    public static final String STUMP_KEY = "stump";
    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;

    private String id;
    private Point position;
    private int actionPeriod;
    private int animationPeriod;
    private int health;
    private List<PImage> images;
    private int imageIndex;


    public Tree(String id, Point position, int actionPeriod, int animationPeriod, int health, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.images = images;
        this.imageIndex = 0;

    }

    public void tryAddEntity(WorldModel world) {
        if (world.isOccupied(this.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(this);
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
    public void updateHealth(int i) {
        this.health += i;
    }
    public Point getPosition() {
        return this.position;
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

    public int getAnimationPeriod(){
        return this.animationPeriod;
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


    public Activity createActivity(
            WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }
    public Animation createAnimation(int repeatCount) {
        return new Animation(this, null, null,
                repeatCount);
    }

}
