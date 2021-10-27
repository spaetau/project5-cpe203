import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fairy implements Movable{
    private String id;
    private Point position;
    private int actionPeriod;
    private int animationPeriod;
    private List<PImage> images;
    private int imageIndex;
    public void updatePosition(Point pos){
        this.position = pos;
    }

    public Fairy(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
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
    public int getAnimationPeriod(){
        return this.animationPeriod;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public void tryAddEntity(WorldModel world) {
        if (world.isOccupied(this.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(this);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Point.adjacent(this.position, target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Stump temp1 = new Stump(null, null, null);
        Optional<Entity> fairyTarget =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(temp1)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                ActivityCapable sapling = new Sapling("sapling_" + this.id, tgtPos,
                        imageStore.getImageList(Sapling.SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                this.createActivity(world, imageStore),
                this.actionPeriod);
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
