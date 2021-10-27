public interface ActivityCapable extends Animatable {



    void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);

    Action createActivity(
            WorldModel world, ImageStore imageStore);
}
