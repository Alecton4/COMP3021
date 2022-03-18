package base;

import java.io.File;

public class ImageNote extends Note {
    private File image;

    public ImageNote(String title) {
        super(title);
    }

    public ImageNote(String title, File image) {
        super(title);
        this.image = image;
    }

    /**
     * @return the image
     */
    public File getImage() {
        return image;
    }

    @Override
    public boolean containsKeyword(String target) {
        return StringHelper.containsIgnoreCase(this.getTitle(), target);
    }

}
