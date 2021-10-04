import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import processing.core.PImage;

public final class ImageStore
{
    public Map<String, List<PImage>> images;
    public List<PImage> defaultImages;

    public ImageStore(PImage defaultImage) {
        this.images = new HashMap<>();
        defaultImages = new LinkedList<>();
        defaultImages.add(defaultImage);
    }
    public List<PImage> getImageList(String key) {
        return this.images.getOrDefault(key, this.defaultImages);
    }
}
