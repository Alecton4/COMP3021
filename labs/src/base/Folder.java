package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Folder implements Comparable<Folder>, Serializable {
    private String name;
    private ArrayList<Note> notes;
    private static final long serialVersionUID = 1L;

    public Folder(String name) {
        this.name = name;
        this.notes = new ArrayList<Note>();
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
        this.notes.add(newNote);
    }

    public void sortNotes() {
        Collections.sort(this.notes);
    }

    public List<Note> searchNotes(String keywords) {
        // maintain collections of two kinds of targets
        // !!!can NOT handle consecutive ORs
        String[] wordArr = keywords.split("\\s+");
        Stack<String> targetsOfAND = new Stack<String>();
        Stack<String[]> targetPairsOfOR = new Stack<String[]>();
        String previousTargetOfOR = "";
        boolean ofOR = false;
        for (String word : wordArr) {
            if (ofOR) {
                String[] targetPair = { previousTargetOfOR, word };
                targetPairsOfOR.push(targetPair);
                ofOR = false;
            } else {
                if (StringHelper.containsIgnoreCase(word, "or")) {
                    previousTargetOfOR = targetsOfAND.pop();
                    ofOR = true;
                } else {
                    targetsOfAND.push(word);
                }
            }
        }

        // search for notes containing targets
        List<Note> result = new ArrayList<Note>();
        for (Note note : this.notes) {
            boolean isResult = true;
            if (note instanceof TextNote) {
                TextNote textNote = (TextNote) note;
                // search for AND targets
                for (String target : targetsOfAND) {
                    if (!textNote.containsKeyword(target)) {
                        isResult = false;
                        break;
                    }
                }
                // skip the current note and start checking the next note
                if (!isResult) {
                    continue;
                }
                // search for OR targets
                for (String[] targetPair : targetPairsOfOR) {
                    if (!(textNote.containsKeyword(targetPair[0]) || textNote.containsKeyword(targetPair[1]))) {
                        isResult = false;
                        break;
                    }
                }
                // skip the current note and start checking the next note
                if (!isResult) {
                    continue;
                }
            } else if (note instanceof ImageNote) {
                ImageNote imgNote = (ImageNote) note;
                // search for AND targets
                for (String target : targetsOfAND) {
                    if (!imgNote.containsKeyword(target)) {
                        isResult = false;
                        break;
                    }
                }
                // skip the current note and start checking the next note
                if (!isResult) {
                    continue;
                }
                // search for OR targets
                for (String[] targetPair : targetPairsOfOR) {
                    if (!(imgNote.containsKeyword(targetPair[0]) || imgNote.containsKeyword(targetPair[1]))) {
                        isResult = false;
                        break;
                    }
                }
                // skip the current note and start checking the next note
                if (!isResult) {
                    continue;
                }
            } else {
                isResult = false;
            }

            // if (isResult) {
            result.add(note);
            // }
        }
        return result;
    }

    @Override
    public String toString() {
        int numTextNote = 0;
        int numImageNote = 0;
        for (Note note : this.notes) {
            if (note instanceof ImageNote) {
                ++numImageNote;
            } else if (note instanceof TextNote) {
                ++numTextNote;
            }
        }
        return this.name + ":" + numTextNote + ":" + numImageNote;
    }

    @Override
    public int hashCode() {
        // REVIEW Auto-generated method stub
        return super.hashCode();
    }

    // @Override
    public boolean equals(Folder anotherFolder) {
        // REVIEW Auto-generated method stub
        return this.name.equals(anotherFolder.getName());
    }

    @Override
    public int compareTo(Folder anotherFolder) {
        return this.name.compareTo(anotherFolder.getName());
    }
}
