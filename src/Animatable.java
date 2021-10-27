public interface Animatable extends Entity {
    int getAnimationPeriod();;
    Action createAnimation(int repeatCount);
    void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);
}
