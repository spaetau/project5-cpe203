import java.util.*;

/**
 * Keeps track of events that have been scheduled.
 */
public final class EventScheduler
{
    public PriorityQueue<Event> eventQueue;
    public Map<Entity, List<Event>> pendingEvents;
    public double timeScale;

    public EventScheduler(double timeScale) {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.timeScale = timeScale;
    }

    public void scheduleActions(
            Entity entity,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (entity.kind) {
            case DUDE_FULL:
                this.scheduleEvent( entity,
                    Action.createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
                this.scheduleEvent( entity,
                    Action.createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
                break;

            case DUDE_NOT_FULL:
                this.scheduleEvent(entity,
                    Action.createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
                this.scheduleEvent( entity,
                    Action.createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
                break;

            case OBSTACLE:
                this.scheduleEvent( entity,
                    Action.createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
                break;

            case FAIRY:
                this.scheduleEvent( entity,
                    Action.createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
                this.scheduleEvent( entity,
                    Action.createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
                break;

            case SAPLING:
                this.scheduleEvent( entity,
                    Action.createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
                this.scheduleEvent(entity,
                    Action.createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
                break;

            case TREE:
                this.scheduleEvent( entity,
                    Action.createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
                this.scheduleEvent( entity,
                    Action.createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
                break;

            default:
        }
    }

    public void scheduleEvent(
            Entity entity,
            Action action,
            long afterPeriod)
    {
        long time = System.currentTimeMillis() + (long)(afterPeriod
                * this.timeScale);
        Event event = new Event(action, time, entity);

        this.eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = this.pendingEvents.getOrDefault(entity,
                new LinkedList<>());
        pending.add(event);
        this.pendingEvents.put(entity, pending);
    }

    public void unscheduleAllEvents(Entity entity)
    {
        List<Event> pending = this.pendingEvents.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                this.eventQueue.remove(event);
            }
        }
    }

    public void removePendingEvent(Event event) {
        List<Event> pending = this.pendingEvents.get(event.entity);

        if (pending != null) {
            pending.remove(event);
        }
    }

    public void updateOnTime(long time) {
        while (!this.eventQueue.isEmpty()
                && this.eventQueue.peek().time < time) {
            Event next = this.eventQueue.poll();

            this.removePendingEvent(next);

            next.action.executeAction(this);
        }
    }

}

