package filesystem;

public class Link extends File {

    private final File linkedItem;

    Link(Directory dir, String name, File linkedItem) {
        super(dir,name);

        if (linkedItem == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }

        if (linkedItem instanceof Link) {
            throw new IllegalArgumentException("Link cannot refer to another link");
        }

        this.linkedItem = linkedItem;

        if (!isActive()) {
            throw new IllegalStateException("Link must be active at creation");
        }
    }

    /**
     * Returns the linked item without direct access to it's properties
     */
    public File getLinkedItem() {
        return linkedItem;
    }

    /**
     * A link is active if its target still exists.
     */
    public boolean isActive() {
        return linkedItem.getDir() != null;
    }

    public double getTotalDiskUsage() {
        return 0;
    }

}
