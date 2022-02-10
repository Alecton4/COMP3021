package base;

import java.util.Date;

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
        return title;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    // @Override
    public boolean equals(Note obj) {
        // TODO Auto-generated method stub
        return title == obj.getTitle();
    }
}
