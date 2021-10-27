import processing.core.PImage;

import java.util.List;

/**
 * Represents a background for the 2D world.
 */
public final class Background
{
    public String id;
    public List<PImage> images;
    public int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public PImage getCurrentImage(){//mioght not work might need to make static again, unsure
        return this.images.get(
                this.imageIndex);
    }
}
