package base;

import java.util.Date;
import java.util.Objects;

public class Note {
    private String title;
    private Date date;

    public Note(String title) {
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    // @Override
    public boolean equals(Note note) {
        // TODO Auto-generated method stub
        return note.getTitle().equals(this.title);
    }

    // @Override
    // public boolean equals(Object obj) {
    //     // TODO Auto-generated method stub
    //     if (obj == this) {
    //         return true;
    //     }
    //     if (!(obj instanceof Note)) {
    //         return false;
    //     }
    //     Note note = (Note) obj;
    //     return Objects.equals(note.title, this.title);
    // }

}
