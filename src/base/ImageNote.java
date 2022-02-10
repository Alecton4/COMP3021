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
}
