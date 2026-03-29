package File;

public class Link extends File{
    private File target;

    public Link(String name, target) {
        super(name);
        this.target=target;
    }

}
