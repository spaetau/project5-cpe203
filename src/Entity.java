import processing.core.PImage;

public interface Entity {

    void tryAddEntity(WorldModel world);
    void nextImage();
    PImage getCurrentImage();
    Point getPosition();

    void updatePosition(Point pos);
    //public void nextImage() {
    //        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    //    }



}
