package base;

import java.util.ArrayList;

public class NoteBook {
    private ArrayList<Folder> folders;

    public NoteBook() {
        this.folders = new ArrayList<Folder>();
    }

    /**
     * @return the folders
     */
    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public boolean createImageNote(String folderName, String title) {
        ImageNote imgNote = new ImageNote(title);
        return insertNote(folderName, imgNote);
    }

    public boolean createTextNote(String folderName, String title) {
        TextNote txtNote = new TextNote(title);
        return insertNote(folderName, txtNote);
    }

    private boolean insertNote(String folderName, Note newNote) {
        for (Folder folder : folders) {
            if (folder.getName() == folderName) {
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
        folders.add(newFolder);
        return true;
    }
}
