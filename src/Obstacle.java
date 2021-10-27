import processing.core.PImage;

import java.util.List;

public class Obstacle implements Animatable{

    private String id;
    private Point position;
    private int animationPeriod;
    private List<PImage> images;
    private int imageIndex;

    public void updatePosition(Point pos){
        this.position = pos;
    }

    public Obstacle(String id, Point position, int animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.animationPeriod = animationPeriod;
        this.images = images;
        this.imageIndex = 0;

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
                this.createAnimation(0),
                this.getAnimationPeriod());
    }



    @Override
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public Animation createAnimation(int repeatCount) {
        return new Animation(this, null, null,
                repeatCount);
    }
}
