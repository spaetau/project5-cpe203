import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull implements Movable {

    private String id;
    private Point position;
    private int actionPeriod;
    private int animationPeriod;
    private int resourceLimit;
    private List<PImage> images;
    private int resourceCount;
    public int imageIndex;


    public DudeNotFull(String id, Point position, int actionPeriod, int animationPeriod, int resourceLimit, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.resourceLimit = resourceLimit;
        this.images = images;
        this.imageIndex = 0;

    }
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex);
    }
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    @Override
    public void updatePosition(Point pos) {
        this.position = pos;
    }

    public void tryAddEntity(WorldModel world) {
        if (world.isOccupied(this.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(this);
    }

    public Point getPosition() {
        return this.position;
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos) instanceof Stump) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos) instanceof Stump) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (Point.adjacent(this.position, target.getPosition())) {
                this.resourceCount += 1;
                Healable temp = (Healable) target;
                temp.updateHealth(-1);
                return true;

            }
        else {
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

    public void executeActivity( //will cleanup later
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Tree temp1 = new Tree(null, null, 0,0, 0, null);
        Sapling temp2 = new Sapling(null, null, null);
            Optional<Entity> target =
                    world.findNearest(this.position, new ArrayList<Entity>(Arrays.asList(temp1, temp2)));

            if (!target.isPresent() || !this.moveTo(world,
                    target.get(),
                    scheduler)
                    || !this.transform(world, scheduler, imageStore)) {
                scheduler.scheduleEvent(this,
                        this.createActivity(world, imageStore),
                        this.actionPeriod);
            }
    }


    public boolean transform(
                                     WorldModel world,
                                     EventScheduler scheduler,
                                     ImageStore imageStore)
    {

        if (this.resourceCount >= this.resourceLimit) {
            DudeFull miner = new DudeFull(this.id,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.resourceLimit,
                    this.images);


            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
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
