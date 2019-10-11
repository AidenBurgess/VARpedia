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
        return this.newName;
    }

}
