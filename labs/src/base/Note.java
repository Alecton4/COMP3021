package base;

import java.util.Date;

public class Note implements Comparable<Note> {
    private String title;
    private Date date;

    public Note(String title) {
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    public boolean containsKeyword(String target) {
        return StringHelper.containsIgnoreCase(this.title, target);
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return this.date.toString() + "\t" + this.getTitle();
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    // @Override
    public boolean equals(Note anotherNote) {
        // TODO Auto-generated method stub
        return this.title.equals(anotherNote.getTitle());
    }

    // REF:
    // @Override
    // public boolean equals(Object obj) {
    // if (obj == this) {
    // return true;
    // }
    // if (!(obj instanceof Note)) {
    // return false;
    // }
    // Note note = (Note) obj;
    // return Objects.equals(note.title, this.title);
    // }

    /**
     *
     * @param anotherNote
     * @return <code>0</code> if two motes are created at the same time;
     *         a value less than <code>0</code> if this note is newer than
     *         anotherNote;
     *         a value greater than <code>0</code> if this note is older than
     *         anotherNote.
     */
    @Override
    public int compareTo(Note anotherNote) {
        return anotherNote.getDate().compareTo(this.date);
    }
}
