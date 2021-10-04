import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An action that can be taken by an entity
 */
public final class Action
{
    public ActionKind kind;
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        switch (this.kind) {
            case ACTIVITY:
                this.executeActivityAction(scheduler);
                break;

            case ANIMATION:
                this.executeAnimationAction(scheduler);
                break;
        }
    }

    public void executeAnimationAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent( this.entity,
                    Action.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }
    }
    public void executeActivityAction(
             EventScheduler scheduler)
    {
        switch (this.entity.kind) {
            case SAPLING:
                executeSaplingActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case TREE:
                executeTreeActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case FAIRY:
                executeFairyActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case DUDE_NOT_FULL:
                executeDudeNotFullActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case DUDE_FULL:
                executeDudeFullActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        this.entity.kind));
        }
    }




    public void executeSaplingActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        entity.health++;
        if (!entity.transformPlant(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( entity,
                createActivityAction(entity, world, imageStore),
                entity.actionPeriod);
        }
    }

    public void executeTreeActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!entity.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
        }
    }

    public void executeFairyActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(entity.position, new ArrayList<>(Arrays.asList(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (entity.moveToFairy(world, fairyTarget.get(), scheduler)) {
                Entity sapling = Entity.createSapling("sapling_" + entity.id, tgtPos,
                        imageStore.getImageList(Entity.SAPLING_KEY));

                sapling.addEntity(world);
                scheduler.scheduleActions(sapling, world, imageStore);
            }
        }

        scheduler.scheduleEvent( entity,
            createActivityAction(entity, world, imageStore),
            entity.actionPeriod);
    }

    public void executeDudeNotFullActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(entity.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (!target.isPresent() || !entity.moveToNotFull(world,
                target.get(),
                scheduler)
                || !entity.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
        }
    }

    public void executeDudeFullActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler
    )
    {
        Optional<Entity> fullTarget =
                world.findNearest(entity.position, new ArrayList<>(Arrays.asList(EntityKind.HOUSE)));

        if (fullTarget.isPresent() && entity.moveToFull(world,
                fullTarget.get(), scheduler))
        {
            entity.transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
        }
    }

    public static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Action(ActionKind.ANIMATION, entity, null, null,
                repeatCount);
    }

    public static Action createActivityAction(
            Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
    }




}

