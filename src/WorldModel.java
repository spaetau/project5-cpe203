import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel implements Constants
{
    public int numRows;
    public int numCols;
    public Background background[][];
    public Entity occupancy[][];
    public Set<Entity> entities;




    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.getPosition());
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
//            Movable temp = (Movable) entity;
            entity.updatePosition(pos);
        }
    }

    public void addEntity(Entity entity) {
        if (this.withinBounds(entity.getPosition())) {
            this.setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }
    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }


    }

    public void setBackgroundCell(Point pos, Background backgroundInput)
    {
        this.background[pos.y][pos.x] = backgroundInput;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Entity getOccupancyCell(Point pos)
    {
        return this.occupancy[pos.y][pos.x];
    }

    public Optional<Entity> getOccupant (Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public  boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0
                && pos.x < this.numCols;
    }



    public void setBackground(
            Point pos, Background background)
    {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    public  Optional<PImage> getBackgroundImage(
           Point pos)
    {
        if (this.withinBounds(pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public  void removeEntityAt(Point pos) {
        if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.updatePosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public Optional<Entity> findNearest(
            Point pos, List<Entity> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity kind: kinds)
        {
            for (Entity entity : this.entities) {
                if (entity.getClass().equals(kind.getClass())) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }

    public static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = Point.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = Point.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }


    public boolean parseBackground(
            String[] properties, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            this.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public boolean parseSapling(
            String[] properties, ImageStore imageStore) {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SAPLING_COL]),
                    Integer.parseInt(properties[SAPLING_ROW]));
            String id = properties[SAPLING_ID];
            Entity entity = new Sapling(id, pt, imageStore.getImageList(SAPLING_KEY));
            entity.tryAddEntity(this);
        }

        return properties.length == SAPLING_NUM_PROPERTIES;
    }

    public boolean parseDude(
            String[] properties,  ImageStore imageStore) {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DUDE_COL]),
                    Integer.parseInt(properties[DUDE_ROW]));
            Entity entity = new DudeNotFull(properties[DUDE_ID],
                    pt,
                    imageStore.getImageList(DUDE_KEY),
                    Integer.parseInt(properties[DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[DUDE_LIMIT]));
            entity.tryAddEntity(this);
        }

        return properties.length == DUDE_NUM_PROPERTIES;
    }

    public boolean parseFairy(
            String[] properties, ImageStore imageStore) {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FAIRY_COL]),
                    Integer.parseInt(properties[FAIRY_ROW]));
            Entity entity = new Fairy(properties[FAIRY_ID],
                    pt, imageStore.getImageList(FAIRY_KEY),Integer.parseInt(properties[FAIRY_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[FAIRY_ACTION_PERIOD])
                   );
            entity.tryAddEntity(this);
        }

        return properties.length == FAIRY_NUM_PROPERTIES;
    }

    public boolean parseTree(
            String[] properties, ImageStore imageStore) {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            Entity entity = new Tree(properties[TREE_ID],
                    pt,
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]),
                    imageStore.getImageList(TREE_KEY));
            entity.tryAddEntity(this);
        }

        return properties.length == TREE_NUM_PROPERTIES;
    }

    public boolean parseObstacle(
            String[] properties, ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = new Obstacle(properties[OBSTACLE_ID], pt,
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(OBSTACLE_KEY));
            entity.tryAddEntity(this);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseHouse(
            String[] properties, ImageStore imageStore) {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[HOUSE_COL]),
                    Integer.parseInt(properties[HOUSE_ROW]));
            Entity entity = new House(properties[HOUSE_ID], pt,
                    imageStore.getImageList(HOUSE_KEY));
            entity.tryAddEntity(this);
        }

        return properties.length == HOUSE_NUM_PROPERTIES;
    }
}
