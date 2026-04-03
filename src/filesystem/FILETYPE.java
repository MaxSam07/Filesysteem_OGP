package filesystem;

import be.kuleuven.cs.som.annotate.*;

public enum FILETYPE {

    /**********************************************************
     * ALLOWED FILETYPES
     **********************************************************/

    TXT("txt"),
    PDF("pdf"),
    JAVA("java"),;

    /**********************************************************
     * Constructors
     **********************************************************/

    FILETYPE(String extension) {
        this.extension = extension;
    }

    /**********************************************************
     * Extension
     **********************************************************/

    private final String extension;

    public String getExtension() {
        return this.extension;
    }

    public static FILETYPE getFiletypeOfExtension(String extension) throws IllegalArgumentException {

        for (FILETYPE t : values()) {
            if (t.getExtension().equals(extension)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown extension "+extension);
    }

    public static boolean isFiletype(String extension) {
        try {
            getFiletypeOfExtension(extension);
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
