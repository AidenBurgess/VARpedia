package app;

/**
* Handles access the background music files for the video player
*/
public class Music {

    private String title;
    private String altTitle;

    // Get the string representing the music
    public Music(String title, String altTitle) {
        this.title=title;
        this.altTitle=altTitle;
    }

    public String getTitle() {
        return this.title;
    }

    // Find the actual name of the music file corresponding to the string input
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

    // Represent each music file available with a string
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
