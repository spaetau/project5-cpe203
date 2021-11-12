import processing.core.PImage;

import java.util.List;

abstract class Movable extends ActivityCapable {
     public Movable(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
          super(id, position, images, animationPeriod, actionPeriod);
     }
     abstract boolean moveTo( //fairy would be only one to implement, so am going to make it abstract
                              WorldModel world,
                              Entity target,
                              EventScheduler scheduler);

     public Point nextPosition(WorldModel world, Point destPos) {
          int horiz = Integer.signum(destPos.x - this.position.x);
          Point newPos = new Point(this.position.x + horiz, this.position.y);

          if (horiz == 0 || isObstacle(world, newPos)) {
               int vert = Integer.signum(destPos.y - this.position.y);
               newPos = new Point(this.position.x, this.position.y + vert);

               if (vert == 0 || isObstacle(world, newPos))  {
                    newPos = this.position;
               }
          }

          return newPos;
     }


     public boolean isObstacle(WorldModel world, Point pos){return world.isOccupied(pos);} //should call  world.isOccupied(newPos) and if it's a dude calling it
                                             //should also check if the entity at that point is a stump


}
