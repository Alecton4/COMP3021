package base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;

import javafx.scene.text.Text;

public class TextNote extends Note {
    private String content;

    public TextNote(String title) {
        super(title);
    }

    public TextNote(String title, String content) {
        super(title);
        this.content = content;
    }

    /**
     * load a TextNote from File f
     *
     * the tile of the TextNote is the name of the file
     * the content of the TextNote is the content of the file
     *
     * @param File f
     */
    public TextNote(File file) {
        super(file);
        this.content = this.getTextFromFile(file.getAbsolutePath());
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * get the content of a file
     *
     * @param absolutePath of the file
     * @return the content of the file
     */
    private String getTextFromFile(String absolutePath) {
        String content = "";
        File file = new File(absolutePath);

        try (FileReader fr = new FileReader(file);) {

            int nextChar = 0;
            while (nextChar != -1) {
                content += (char) nextChar;
                nextChar = fr.read();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return content;
    }

    @Override
    public boolean containsKeyword(String target) {
        return (StringHelper.containsIgnoreCase(this.getTitle(), target)
                || StringHelper.containsIgnoreCase(content, target));
    }

    public Character countCharacters() {
        HashMap<Character, Integer> count = new HashMap<Character, Integer>();
        String totalStr = this.getTitle() + this.getContent();
        Character maxOccrChar = '\0';
        int maxOccr = 0;
        for (int i = 0; i < totalStr.length(); i++) {
            Character c = totalStr.charAt(i);
            if (c <= 'Z' && c >= 'A' || c <= 'z' && c >= 'a') {
                if (count.containsKey(c)) {
                    count.put(c, count.get(c) + 1);
                    if (count.get(c) > maxOccr) {
                        maxOccr = count.get(c);
                        maxOccrChar = c;
                    }
                } else {
                    count.put(c, 1);
                }
            }
        }
        return maxOccrChar;
    }

    public void exportTextToFile(String dirPath) {
        // handle working directory
        if (dirPath.equals("")) {
            dirPath = ".";
        }
        File file = new File(dirPath + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");

        // try-with-resource
        try (PrintWriter pw = new PrintWriter(file);) {
            // FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
            // OutputStreamWriter osw = new OutputStreamWriter(fos);
            pw.write(this.getContent());
            // osw.write(this.getContent());
            // osw.close();
            // fos.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("Cannot export to the specified directory!");
        }
    }

}
