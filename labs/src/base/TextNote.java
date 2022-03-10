package base;

public class TextNote extends Note {
    private String content;

    public TextNote(String title) {
        super(title);
    }

    public TextNote(String title, String content) {
        super(title);
        this.content = content;
    }

    @Override
    public boolean containsKeyword(String target) {
        return (StringHelper.containsIgnoreCase(this.getTitle(), target)
                || StringHelper.containsIgnoreCase(content, target));
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
