package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook {
    private ArrayList<Folder> folders;

    public NoteBook() {
        this.folders = new ArrayList<Folder>();
    }

    public boolean createImageNote(String folderName, String title) {
        ImageNote newImgNote = new ImageNote(title);
        return insertNote(folderName, newImgNote);
    }

    public boolean createTextNote(String folderName, String title) {
        TextNote newTextNote = new TextNote(title);
        return insertNote(folderName, newTextNote);
    }

    public boolean createTextNote(String folderName, String title, String content) {
        TextNote newTextNote = new TextNote(title, content);
        return insertNote(folderName, newTextNote);
    }

    private boolean insertNote(String folderName, Note newNote) {
        for (Folder folder : this.folders) {
            if (folderName.equals(folder.getName())) {
                for (Note note : folder.getNotes()) {
                    if (note.equals(newNote)) {
                        System.out.println(
                                "Creating note " + newNote.getTitle() + " under folder " + folderName + " failed!");
                        return false;
                    }
                }
                folder.addNote(newNote);
                return true;
            }
        }
        Folder newFolder = new Folder(folderName);
        newFolder.addNote(newNote);
        this.folders.add(newFolder);
        return true;
    }

    public List<Note> searchNotes(String keywords) {
        List<Note> result = new ArrayList<Note>();
        for (Folder folder : this.folders) {
            result.addAll(folder.searchNotes(keywords));
        }
        return result;
    }

    public void sortFolders() {
        for (Folder folder : this.folders) {
            folder.sortNotes();
        }
        Collections.sort(this.folders);
    }

    /**
     * @return the folders
     */
    public ArrayList<Folder> getFolders() {
        return this.folders;
    }

}
