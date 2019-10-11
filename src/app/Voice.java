package app;

public class Voice {

    private String originalName;
    private String newName;

    public Voice(String ogName, String newName) {
        this.originalName=ogName;
        this.newName=newName;
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public static String findVoice(String name) {
        if (name.equals("Default/Normal")) {
            return "kal_diphone";
        } else if (name.equals("NZ voice")) {
            return "akl_nz";
        } else {
            return "kal_diphone";
        }
    }

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
