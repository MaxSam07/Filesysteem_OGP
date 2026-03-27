package filesystem;

import java.util.ArrayList;
import java.util.List;

public enum FILETYPE {
    TXT("txt"),
    PDF("pdf"),
    JAVA("java"),;


    private final String extension;

    FILETYPE(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }

    public static List<String> returnExtensionsAsList() {
        List<String> extensions = new ArrayList<>();
        for (FILETYPE t : FILETYPE.values()) {
            extensions.add(t.extension);
        }
        return extensions;
    }
}
