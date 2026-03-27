package filesystem;

import java.util.Date;
/*
 *   schrijfrechten [DEFENSIEF]
 *   tijdstippen [TOTAAL]
 *   bestandsgrootte [NOMINAAL]
 *   naamgeving [TOTAAL]
 */

/**
 * a basic File class simulating how files work in real applications
 * some behavior is not accurate to how it really is implemented
 *
 * @invar File.size should always be in between 0 and Integer.MAX_VALUE
 * @invar File.creationDate can't be null
 * @invar File.isValidName(File.name) should be true at all times
 *
 */
public class File {

    private String name;
    private long size;
    private static final int MAXFILESIZE = Integer.MAX_VALUE;
    private final Date creationTime;
    private Date modificationTime;
    private boolean writable;

    private Directory dir;
    private final FILETYPE type;

    /**
     * constructs a file using multiple parameters
     *
     * @pre this.isValidName(name) should return true
     *
     * @param name the name of the to-be-created file
     * @param size the original size of the file
     * @param writable sets the permission state for writing for everyone in that file
     *
     * @post a file is created, if the name doesn't respect the naming conventions it'll be
     * replaced with a dummy name
     */
    public File(Directory dir,String name, int size, boolean writable, FILETYPE type) {
        this.size = size;
        this.writable = writable;
        this.creationTime = new Date();
        this.dir = dir;

        if(isValidName(name)) {this.name = name;}
        else {this.name = "new_file";}

        if(isValidType(type)){this.type = type;}
        else {throw new IllegalArgumentException("Invalid file type");}
    }

    /**
     * constructs a new file using a single parameter
     *
     * @pre this.isValidName(name) should return true
     *
     * @param name the name of the to-be-created file
     *
     * @post an empty file is created
     * @post if the name doesn't respect the naming conventions it'll be replaced with a dummy name
     * @post the file is writable for everyone by default
     *
     */
    public File(Directory dir, String name, FILETYPE type) {
        this.size = 0;
        this.writable = true;
        this.creationTime = new Date();
        this.dir = dir;

        if(isValidName(name)) {this.name = name;}
        else {this.name = "new_file";}

        if(isValidType(type)){this.type = type;}
        else {throw new IllegalArgumentException("Invalid file type");}
    }

    public long getSize() { return size; }

    public Date getModificationTime() {
        return modificationTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public boolean isWritable(){
        return this.writable;
    }

    public void setWritable(boolean writable) {this.writable = writable;}

    public String getName(){return this.name;}

    public FILETYPE getType(){return this.type;}

    public String getNameAndExtension(){return this.name+"."+this.type;}

    /**
     * @param newName name that will be asigned to the file
     *
     * @pre this.isValidName(newName) has to return true in order for the rename to take effect.
     *
     * @throws FileNotWritableException if the file isn't writable when executing this function
     */
    public void renameFile(String newName){
        if (this.isValidName(newName) && this.isWritable()){
            this.name = newName;
            this.updateModificationTime();
        }

        else if (this.isValidName(newName) && !this.isWritable()) {
            throw new FileNotWritableException(this.name + " is not writable");}

        else {
            System.out.println(newName + " is not a valid name");}
    }

    /**
     * checks the validity of the filetype according to the conditions in the exercise
     * the function checks whether the filetype is known by checking if it is present in the Filesystem.EXTENSIONS list
     *
     * @param type the type that has to be checked
     *
     * @return the validity of the type in bool format
     */
    private boolean isValidType(FILETYPE type){
        return FILETYPE.returnExtensionsAsList().contains(type.getExtension());
    }

    /**
     * checks the validity of the string as filename according to the conditions in the exercise
     *
     * @param newName the string that has to be checked
     *
     * @return the validity of the string in bool format
     */
    private boolean isValidName(String newName){ // GETS BestandsSysteem.EXTENSIONS

        // list with all allowed characters in file name
        String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxz"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"1234567890";
        String ALLOWED_SYMBOLS = "._-";
        int symbolCounter = 0;

        for (int i = 0; i < newName.length(); i++){ // itereer doorheen de characters

            Character character = newName.charAt(i); // gebruik de wrapper class (vraag NIET wrm)

            if ((!ALLOWED_CHARS.contains(character.toString())) && (!ALLOWED_SYMBOLS.contains(character.toString()))) {
                return false; // if any of the chars NOT in ALLOWED_CHARS OR ALLOWED_SYMBOLS => return false
            }
            if (ALLOWED_SYMBOLS.contains(character.toString())){
                // als de character een symbool is, voeg 1 toe aan de teller
                symbolCounter++;
            }
        }
        return symbolCounter >= 1; // return true alleen als er minstens 1 symbooltje is
    }

    /**
     * method called upon change in file contents or name
     *
     * @post this.modificationTime will be set to the millisecond time upon function call
     *
    * */
    private void updateModificationTime() {
        this.modificationTime = new Date();
    }

    /**
     * method allowing for increase in the size of the file's contents
     *
     *  @pre parameter bytes can't be negative
     *  @pre parameter bytes can't be greater than this.maxFileSize - this.size
     *
     *  @post parameter bytes is added to this.size
     *  @post this.updateModificationTime() gets called
     */
    public void enlarge(long bytes){
        if (this.isWritable()) {this.size+=bytes;}
        this.updateModificationTime();
    }

    /**
     * @param bytes the changes in bytes of the file
     *
     *  @pre parameter bytes can't be negative
     *  @pre parameter bytes can't be greater than this.size
     *
     *  @post this.size is reduced by parameter bytes
     *  @post this.updateModificationTime() gets called
     */
    public void shorten(long bytes){
        if (this.isWritable()) {this.size-=bytes;}
        this.updateModificationTime();
    }

    /**
     * function that checks if two files have overlapping use periods
     *
     * @param otherFile the file we want to check for overlap with the current File object
     *
     * @return true if the two files have an overlapping use period, false if they don't
     */
    public boolean hasOverlappingUsePeriod(File otherFile) {

        Date start1 = this.getCreationTime();
        Date end1 = this.getModificationTime();

        Date start2 = otherFile.getCreationTime();
        Date end2 = otherFile.getModificationTime();

        // CHECK FOR NULL
        if (start1 == null) {
            System.out.println(this.getName()+" has no creation time");
            return false;
        }
        if (start2 == null) {
            System.out.println(otherFile.getName()+" has no creation time");
            return false;
        }
        if (end1 == null) {
            System.out.println(this.getName()+" has no modification time");
            return false;
        }
        if (end2 == null) {
            System.out.println(otherFile.getName()+" has no modification time");
            return false;
        }

        // CHECK FOR TIME TRAVEL AND REVERSE IT
        if (start1.after(end1)) {
            Date swap;
            swap = start1;
            start1 = end1;
            end1 = swap;
        }
        if (start2.after(end2)) {
            Date swap;
            swap = start2;
            start2 = end2;
            end2 = swap;
        }

        // ACTUALLY CHECK FOR OVERLAP
        if (start1.before(start2) && end1.before(start2)) {return false;}
        if (start2.before(start1) && end2.before(start1)) {return false;}
        else {return true;}
    }
}