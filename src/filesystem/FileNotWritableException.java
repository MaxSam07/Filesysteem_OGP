package filesystem;

public class FileNotWritableException extends RuntimeException {
    public FileNotWritableException(String message) {
        super(message);
    }
}
