package app;

public class Voice {

    private String originalName;
    private String newName;

    public Voice(String ogName, String newName) {
        this.originalName=ogName;
        this.newName=newName;
    }

    protected String getOriginalName() {
        return this.originalName;
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