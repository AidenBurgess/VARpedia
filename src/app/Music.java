package app;

/**
* Handles access the background music files for the video player
*/
public class Music {

    // Field declarations
    private String title;
    private String altTitle;

    /**
     * Constructor to get the string representing the music
     * @param title
     * @param altTitle
     */
    public Music(String title, String altTitle) {
        this.title=title;
        this.altTitle=altTitle;
    }

    /**
     * get the official file name of the music
     * @return the file name of the song
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Find the actual name of the music file corresponding to the string input
     * @param name
     * @return the file name of the song
     */
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

    /**
     * Represent each music file available with a string
     * @return the String representation of a music file
     */
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
