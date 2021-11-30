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

     public Point nextPosition(WorldModel world, Point destPos, PathingStrategy path) {
          List<Point> temp  = path.computePath(this.position, destPos,
                  (p) -> !world.isOccupied(p) && world.withinBounds(p),
                  (p1, p2) -> Point.adjacent(p1, p2) && world.withinBounds(p1) && world.withinBounds(p2),
                  path.CARDINAL_NEIGHBORS);
          if (temp.size() > 0){
//               System.out.println("pos" + this.position + " " + temp.get(0));
               return temp.get(0);
          }
          return this.position;
     }


     public boolean isObstacle(WorldModel world, Point pos){return world.isOccupied(pos);} //should call  world.isOccupied(newPos) and if it's a dude calling it
                                             //should also check if the entity at that point is a stump


}
