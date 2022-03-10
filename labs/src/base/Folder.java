package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Folder implements Comparable<Folder> {
    private String name;
    private ArrayList<Note> notes;

    public Folder(String name) {
        this.name = name;
        this.notes = new ArrayList<Note>();
    }

    public void addNote(Note newNote) {
        this.notes.add(newNote);
    }

    public void sortNotes() {
        Collections.sort(this.notes);
    }

    public List<Note> searchNotes(String keywords) {
        // maintain collections of two kinds of keywords
        String[] keywordsArr = keywords.split("\\s+");
        Stack<String> wordsInAND = new Stack<String>();
        Stack<String[]> pairsInOR = new Stack<String[]>();
        String previousWordIntoOR = "";
        boolean intoOR = false;
        for (String str : keywordsArr) {
            if (intoOR) {
                String[] pair = { previousWordIntoOR, str };
                pairsInOR.push(pair);
                intoOR = false;
            } else {
                if (StringHelper.containsIgnoreCase(str, "or")) {
                    previousWordIntoOR = wordsInAND.pop();
                    intoOR = true;
                } else {
                    wordsInAND.push(str);
                }
            }
        }

        List<Note> result = new ArrayList<Note>();
        for (Note note : this.notes) {
            boolean isResult = true;
            if (note instanceof TextNote) {
                TextNote textNote = (TextNote) note;
                // check AND words
                for (String target : wordsInAND) {
                    if (!textNote.containsKeyword(target)) {
                        isResult = false;
                        break;
                    }
                }
                // skip checking current note and start checking the next note
                if (!isResult) {
                    continue;
                }
                // check OR words
                for (String[] pair : pairsInOR) {
                    if (!(textNote.containsKeyword(pair[0]) || textNote.containsKeyword(pair[1]))) {
                        isResult = false;
                        break;
                    }
                }
                // skip checking current note and start checking the next note
                if (!isResult) {
                    continue;
                }
            } else if (note instanceof ImageNote) {
                ImageNote imgNote = (ImageNote) note;
                // check AND words
                for (String target : wordsInAND) {
                    if (!imgNote.containsKeyword(target)) {
                        isResult = false;
                        break;
                    }
                }
                // skip checking current note and start checking the next note
                if (!isResult) {
                    continue;
                }
                // check OR words
                for (String[] pair : pairsInOR) {
                    if (!(imgNote.containsKeyword(pair[0]) || imgNote.containsKeyword(pair[1]))) {
                        isResult = false;
                        break;
                    }
                }
                // skip checking current note and start checking the next note
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
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    // @Override
    public boolean equals(Folder anotherFolder) {
        // TODO Auto-generated method stub
        return this.name.equals(anotherFolder.getName());
    }

    @Override
    public int compareTo(Folder anotherFolder) {
        return this.name.compareTo(anotherFolder.getName());
    }

}
