package app;

/**
 * Class that handles the computer's available voices and how they are represented on the GUI
 */
public class Voice {
    // Field declarations
    private String originalName;
    private String newName;

    /**
     * Constructor to create a voice object representing both the string representation of it and it's file name
     * @param ogName
     * @param newName
     */
    public Voice(String ogName, String newName) {
        this.originalName=ogName;
        this.newName=newName;
    }

    /**
     * @return the official file name of the voice
     */
    public String getOriginalName() {
        return this.originalName;
    }

    /**
     * Take a string representation of a voice and find it's official file name
     * @param name
     * @return the file name of the voice
     */
    public static String findVoice(String name) {
        if (name.equals("Default/Normal")) {
            return "kal_diphone";
        } else if (name.equals("NZ voice")) {
            return "akl_nz";
        } else {
            return "kal_diphone";
        }
    }

    /**
     * Provide a string representation of the voice
     * @return the string representation
     */
    @Override
    public String toString() {
        if (this.originalName.equals("kal_diphone")) {
            return "Default/Normal";
        } else if (this.originalName.equals("akl_nz")) {
            return "NZ voice";
        } else {
            return this.newName;
        }
        }

}
