/**
 * An event is made up of an Entity that is taking an
 * Action a specified time.
 */
public final class Event
{
    public Action action;
    public long time;
    public Entity entity;

    public Event(Action action, long time, Entity entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }
}
