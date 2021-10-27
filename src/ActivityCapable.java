public interface ActivityCapable extends Animatable {



    void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);

    Activity createActivity(
            WorldModel world, ImageStore imageStore);
}
