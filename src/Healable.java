import processing.core.PImage;

import java.util.List;

abstract class Healable extends ActivityCapable implements Transformable{
    protected int health;

    public Healable(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
    }

    public void updateHealth(int i){
        this.health += i;
    }
}
