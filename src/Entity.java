import processing.core.PImage;

import java.util.List;

abstract class Entity {
    protected String id;
    protected Point position;
    protected List<PImage> images;
    protected int imageIndex;

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public void tryAddEntity(WorldModel world) {
        if (world.isOccupied(this.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(this);
    }
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex);
    }
    public Point getPosition() {
        return position;
    }

    public void updatePosition(Point pos){
        this.position = pos;
    }
}
