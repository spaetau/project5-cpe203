import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DudeRadNotFull extends DudeAbstract {
    private int saplingCounter;

    public DudeRadNotFull(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod, 0, resourceLimit);
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
            return super.moveTo(world,
                    target,
                    scheduler);
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
            if(target.isPresent()) {
                Healable tgt = (Healable) target.get();
                if(Point.adjacent(this.position, tgt.position) && tgt.getHealth() == 0) {
                    List<Point> neighbors = new ArrayList<>();
                    neighbors.add(new Point(tgt.getPosition().x - 1, tgt.getPosition().y));
                    neighbors.add(new Point(tgt.getPosition().x + 1, tgt.getPosition().y));
                    neighbors.add(new Point(tgt.getPosition().x, tgt.getPosition().y - 1));
                    neighbors.add(new Point(tgt.getPosition().x, tgt.getPosition().y + 1));

                    neighbors = neighbors.stream().filter(n -> !world.isOccupied(n) && world.withinBounds(n)).collect(Collectors.toList());
                    if (neighbors.isEmpty()) {
                        world.removeEntity(tgt);
                        DudeNotFull miner = new DudeNotFull("rad_spawned",
                                tgt.getPosition(), imageStore.getImages(Constants.DUDE_KEY), 100, 100,
                                4);
                        world.addEntity(miner);
                        miner.scheduleActions(scheduler, world, imageStore);
                    }
                    else {
                        DudeNotFull miner = new DudeNotFull("rad_spawned",
                                neighbors.get(0), imageStore.getImages(Constants.DUDE_KEY), 100, 100,
                                4);
                        world.addEntity(miner);
                        miner.scheduleActions(scheduler, world, imageStore);
                    }
                }
            }
            if(saplingCounter >= 7){
                List<Point> neighbors = new ArrayList<>();
                neighbors.add(new Point(this.position.x - 2, this.position.y));
                neighbors.add(new Point(this.position.x + 2, this.position.y));
                neighbors.add(new Point(this.position.x, this.position.y - 2));
                neighbors.add(new Point(this.position.x, this.position.y + 2));

                neighbors = neighbors.stream().filter(n -> !world.isOccupied(n) && world.withinBounds(n)).collect(Collectors.toList());

                if(neighbors.size() > 0)
                {
                    ActivityCapable sapling = new Sapling("sapling_" + this.id, neighbors.get(0),
                            imageStore.getImageList(Sapling.SAPLING_KEY));

                    world.addEntity(sapling);
                    sapling.scheduleActions(scheduler, world, imageStore);
                    saplingCounter = 0;
                }
            }
            saplingCounter++;
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
            DudeRadFull miner = new DudeRadFull(this.id,
                    this.position, this.images, this.animationPeriod, this.actionPeriod,
                    this.resourceLimit);


            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


}
