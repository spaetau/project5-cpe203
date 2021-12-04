import jdk.jshell.spi.SPIResolutionException;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class VirtualWorld extends PApplet
{
    public static final int TIMER_ACTION_PERIOD = 100;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int WORLD_WIDTH_SCALE = 2;
    public static final int WORLD_HEIGHT_SCALE = 2;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static String LOAD_FILE_NAME = "world.sav";

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public static double timeScale = 1.0;

    public ImageStore imageStore;
    public WorldModel world;
    public WorldView view;
    public EventScheduler scheduler;

    public long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
            //System.out.println(entity.id + ": " + entity.getClass() + " : " + entity.health);
        }

       triggerEvent(pressed);
    }

    private void triggerEvent(Point pressed){
        String[] arrKeyCrater = new String[9];
        ArrayList<Point> pointCraters = new ArrayList<>();
        for (int y = -1; y <= 1; y++){
            for (int x = -1; x <= 1; x++){
                pointCraters.add(new Point(pressed.x + x, pressed.y + y));
            }
        }
        for (int i = 0; i < arrKeyCrater.length; i++){
            arrKeyCrater[i] = "crater" + (i + 1);
        }
        spawnCrater(pressed, arrKeyCrater, pointCraters);
        spawnAlien(pressed);
        spawnDudeRad(pressed);
    }

    private void spawnCrater(Point pt, String[] arrKey, ArrayList<Point> pointCraters){
        for (int i = 0; i < 9; i++){
            if (this.world.withinBounds(pointCraters.get(i))){
                Crater temp = new Crater(arrKey[i], pointCraters.get(i), this.imageStore.getImageList(arrKey[i]));
                this.world.removeEntityAt(pointCraters.get(i));
                this.world.addEntity(temp);
            }
        }
    }

    private void spawnAlien(Point pt){
        Point newPoint = new Point(pt.x, pt.y + 2);
        if (!this.world.withinBounds(newPoint)){
            newPoint = new Point(pt.x, pt.y - 2);
        }
        Alien entity = new Alien("alien-test", newPoint, this.imageStore.getImageList("alien"), 300, 300);
        this.world.addEntity(entity);
        entity.scheduleActions(this.scheduler, this.world, this.imageStore);
    }

    private void spawnDudeRad(Point pt){
        Optional<Entity> eventTarget =
                world.findNearest(pt, new ArrayList<Entity>(
                        Arrays.asList(new DudeNotFull(null,null, null, 0, 0, 0),
                                new DudeFull(null,null, null, 0, 0, 0))));

        Point newPoint = eventTarget.get().getPosition();

        DudeRadNotFull entity = new DudeRadNotFull("dude_rad", newPoint, this.imageStore.getImageList(Constants.DUDE_RAD), 200, 200, 20);
        this.world.removeEntityAt(newPoint);
        this.world.addEntity(entity);
        entity.scheduleActions(this.scheduler, this.world, this.imageStore);
    }

    private Point mouseToPoint(int x, int y)
    {
        return view.viewport.viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.load(in, world);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.entities) {
            if (entity instanceof Animatable){


            Animatable temp = (Animatable)entity;
            temp.scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
