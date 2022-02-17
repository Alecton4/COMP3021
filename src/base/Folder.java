package base;

import java.util.ArrayList;

public class Folder {
    private String name;
    private ArrayList<Note> notes;

    public Folder(String name) {
        this.name = name;
        notes = new ArrayList<Note>();
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the notes
     */
    public ArrayList<Note> getNotes() {
        return this.notes;
    }

    public void addNote(Note newNote) {
        notes.add(newNote);
    }

    @Override
    public String toString() {
        int numImage = 0;
        int numText = 0;
        for (Note note : notes) {
            if (note instanceof ImageNote) {
                ++numImage;
            } else if (note instanceof TextNote) {
                ++numText;
            }
        }
        return this.name + ":" + numText + ":" + numImage;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    // @Override
    public boolean equals(Folder folder) {
        // TODO Auto-generated method stub
        return this.name == folder.getName();
    }
}
