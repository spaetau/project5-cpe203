public interface Animatable extends Entity {
    int getAnimationPeriod();;
    Animation createAnimation(int repeatCount);
    void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);
}
