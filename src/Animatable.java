import processing.core.PImage;

import java.util.List;

abstract class Animatable extends Entity {
    protected int animationPeriod;

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public Animatable(String id, Point position, List<PImage> images, int animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public Animation createAnimation(int repeatCount) {
        return new Animation(this, null, null,
                repeatCount);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createAnimation(0),
                this.getAnimationPeriod());
    }
}
