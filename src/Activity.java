public class Activity implements Action{
    public ActivityCapable entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Activity(ActivityCapable entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void execute(EventScheduler scheduler) {
        this.entity.executeActivity(this.world,
                this.imageStore, scheduler);
    }
}
