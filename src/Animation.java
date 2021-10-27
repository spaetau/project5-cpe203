public class Animation implements Action{
    public Animatable entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Animation(Animatable entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void execute(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent( this.entity,
                    this.entity.createAnimation(
                            Math.max(this.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }
    }
}
