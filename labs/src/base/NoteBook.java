package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook implements Serializable {
    private ArrayList<Folder> folders;

    public NoteBook() {
        this.folders = new ArrayList<Folder>();
    }

    /**
     * Constructor of an object NoteBook from an object serialization on disk
     *
     * @param filePath, the path of the file for loading the object
     *                  serialization
     */
    public NoteBook(String filePath) {
        // try-with-resource
        try (FileInputStream fis = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fis);) {

            NoteBook n = (NoteBook) ois.readObject();
            this.folders = n.getFolders();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * @return the folders
     */
    public ArrayList<Folder> getFolders() {
        return this.folders;
    }

    public boolean insertFolder(String folderName) {

        for (Folder folder : folders) {
            if (folder.equals(new Folder(folderName))) {
                return false;
            }

        }
        folders.add(new Folder(folderName));
        return true;

    }

    public void sortFolders() {
        for (Folder folder : this.folders) {
            folder.sortNotes();
        }
        Collections.sort(this.folders);
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

    public void editNote(String folderName, Note note, String content) {
        for (Folder folder : folders) {
            if (folder.equals(new Folder(folderName))) {
                for (Note transNote : folder.getNotes()) {
                    if (note.equals(transNote)) {
                        ((TextNote) transNote).setContent(content);
                    }
                }
            }
        }
    }

    public boolean removeNote(String folderName, Note note) {
        for (Folder folder : folders) {
            if (folder.equals(new Folder(folderName))) {
                for (Note transNote : folder.getNotes()) {
                    if (note.equals(transNote)) {
                        folder.removeNote(transNote);
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public List<Note> searchNotes(String keywords) {
        List<Note> result = new ArrayList<Note>();
        for (Folder folder : this.folders) {
            result.addAll(folder.searchNotes(keywords));
        }
        return result;
    }

    /**
     * method to save the NoteBook instance to file
     *
     * @param filePath, the path of the file where to save the object
     *                  serialization
     * @return true if save on file is successful, false otherwise
     */
    public boolean save(String filePath) {
        // try-with-resource
        try (FileOutputStream fos = new FileOutputStream(filePath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
