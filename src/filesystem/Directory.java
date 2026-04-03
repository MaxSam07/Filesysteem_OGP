package filesystem;

import be.kuleuven.cs.som.annotate.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import be.kuleuven.cs.som.annotate.*;

public class Directory extends File{




    /**********************************************************
     * Constructors
     **********************************************************/

    public Directory(Directory dir, String name, int size, boolean writable) {
        super(dir, name, 0,  writable);
        this.contents = new ArrayList<File>();
    }

    public double getTotalDiskUsage() {
        double total = 0;

        for (File item : this.getContents()) {
            total += item.getTotalDiskUsage();
        }

        return total;
    }

    /**********************************************************
     * Directory
     **********************************************************/

    @override
    private boolean isValidDir(Directory dir){
        return true;
    }

    @override
    public void move(Directory newDir) {

        Directory oldDir = super.getDir();

        if (oldDir != null) {
            oldDir.removeItem(this);
        }

        newDir.addItem(this);
    }

    /**********************************************************
     * contents - total programming
     **********************************************************/

    /**
     * List of objects keeping track of which files are present in this instance of the directory
     */
    private List<File> contents; //[WIP] mag zichzelf niet bevatten

    /**
     * Function to return all files present in the directory
     *
     * @return The contents of the directory
     */
    public List<File> getContents() {
        return contents;
    }

    /**
     * returns the number of items present in the Directory
     *
     * @return number of items (Files, Directories and links)
     */
    public int getNbItems() {
        return this.contents.size();
    }

    public void addItem(File item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (!super.isWritable()) {
            throw new FileNotWritableException(this);
        }
        if (item.getDir() != null) {
            throw new IllegalArgumentException("Item is already in a directory");
        }
        if (containsDiskItemWithName(item.getName())) {
            throw new IllegalArgumentException("Duplicate name");
        }
        if (item instanceof Directory) {
            Directory dir = (Directory) item;
            Directory current = this;
            while (current != null) {
                if (current == dir) {
                    throw new IllegalArgumentException("Cycle present in structure");
                }
                current = current.getDir();
            }
        }
        this.contents.add(item);
        item.setDir(this);
        this.sort();
    }

    /**
     * Check whether the given file is present within the directory's contents.
     *
     * @param  	file
     *         	The file to look for
     * @return 	True if and only if the given file present in the List of items
     *         	| result == file in this.contents()
     */
    public boolean contains(File file) {
        for (File f : this.getContents()) {
            if (f.equals(file)) {
                return true;
            }
            if (f instanceof Directory) {
                return ((Directory) f).contains(file);
            }
        }
        return false;
    }

    /**
     * Return specific item from the directory's content by searching for name.
     *
     * @param  	name
     *         	The name to look for.
     * @return 	A file if and only if the given name is the name of a file present in the
     *          directory's contents
     *
     * @throws IllegalArgumentException if the argument is null
     * @throws FileNotFoundException if file not present in contents
     */
    public File getItem(String name) throws FileNotFoundException {
        if (name == null){throw new IllegalArgumentException("name cannot be null");}
        for (File item : this.getContents()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        throw new FileNotFoundException("no file named "+name);
    }

    /**
     * Returns the item present at given index from the item list of the directory starting from 1
     *
     * @param  	index
     *         	the index to look for (starting from 1)
     * @return 	The file at given index in directory's content
     *
     * @throws  IndexOutOfBoundsException if the index exceeds the number of files present in the directory
     */
    public File getItemAt(int index) throws IndexOutOfBoundsException {
        if (index < 1 || index > this.getNbItems()) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        return getContents().get(index - 1);
    }

    /**
     * Check whether the given date is a valid creation time.
     *
     * @param  	name
     *          The name to look for.
     * @return  True if the given name is the name of a file present in the directory's contents
     *         	| result == getItem()!=null
     *
     * @throws IllegalArgumentException if argument is null
     */
    public boolean containsDiskItemWithName(String name) throws IllegalArgumentException {
        File item;

        try {
            item = this.getItem(name);
        }
        catch (FileNotFoundException e) {return false;}

        return item != null;
    }

    /**
     * Returns index of given file in (sorted) directory's content
     *
     * @param  	item
     *         	The file to search the index of
     * @return 	the index of the file
     */
    public int getIndexOf(File item) {
        if (item == null) return -1;

        for (int i = 0; i < getContents().size(); i++) {
            if (getContents().get(i) == item) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * check whether given file is present in directory's content
     * @param item
     *        The file to check
     * @return True if and only if the given file is present in this.getContents
     */
    public boolean hasAsItem(File item) {
        return getIndexOf(item) != -1;
    }

    public void sort() {
        for (int i = 1; i < this.getNbItems(); i++) {
            File key = this.getContents().get(i);
            int j = i - 1;

            while (j >= 0 &&
                    this.getContents().get(j).getName()
                            .compareToIgnoreCase(key.getName()) > 0) {

                this.contents.set(j + 1, this.getContents().get(j));
                j--;
            }

            this.contents.set(j + 1, key);
        }
    }

    public void removeItem(File item){
        this.contents.remove(item);
    }

    public void makeRoot() {
        if (getDir() == null) {
            return;
        }

        Directory oldParent = getDir();

        if (!oldParent.isWritable()) {
            throw new IllegalStateException("Parent directory not writable");
        }

        oldParent.removeItem(this);

        this.setDir(null);
    }

    /**
     * Check whether the given name is a legal name for a directory.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits, dots,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z0-9_-]+")
     */


    /**********************************************************
     * name - total programming
     **********************************************************/

    @override
    public static boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z0-9_-]+"));
    }
}