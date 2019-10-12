package app;

public class Music {

    private String title;
    private String altTitle;

    public Music(String title, String altTitle) {
        this.title=title;
    }

    public String getTitle() {
        return this.title;
    }

    public static String findMusic(String name) {
        if (name.equals("Mattioli Prelude")) {
            return "musica";
        } else if (name.equals("Piano and Cello")) {
            return "musicpiano";
        } else if (name.equals("Entre Les Murs")) {
            return "french";
        } else {
            return "musica";
        }
    }

    @Override
    public String toString() {
        if (this.title.equals("musica")) {
            return "Mattioli Prelude";
        } else if (this.title.equals("musicpiano")) {
            return "Piano and Cello";
        } else if (this.title.equals("french")) {
            return "Entre Les Murs";
        } else {
            return this.altTitle;
        }
    }

}
