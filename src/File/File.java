package File;

import java.util.Date;

public class File {

    //===  naamgeving [TOTAAL]  ===//
    private String name; //hoofdlettergevoelig en minstens 1 teken

    //===  bestandsgrootte [NOMINAAL]  ===//
    private long size; //staat in bytes en kan nul zijn
    private static final int MAXFILESIZE = Integer.MAX_VALUE;

    //===  tijdstippen [TOTAAL]  ===//
    private final Date creationTime;
    private Date modificationTime; //grootte of naam bestand

    //===  schrijfrechten [DEFENSIEF]  ===//
    private boolean writable;

    // CONSTRUCTOREN

    public File(String name, int size, boolean writable) {
        this.name = name;
        this.size = size;
        this.writable = writable;
        this.creationTime = new Date(); //[WIP]
    }

    public File(String name) {
        this.name = name;
        this.size = 0;
        this.creationTime = new Date();
    }

    // GETTERS-SETTERS

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

    public void renameFile(String newName){
        if (this.isValidName(newName) && this.isWritable()){
            this.name = newName;
            this.updateModificationTime();
        }

        else if (this.isValidName(newName) && !this.isWritable()){System.out.println(this.name + " is not writable");}

        else {System.out.println(newName + " is not a valid name");}
    }

    // METHODES

    /**
     * checks the validity of the string as filename according to the conditions in the excercise
     *
     * @param newName the string that has to be checked
     *
     * @return the validity of the string in bool format
     */
    private boolean isValidName(String newName){

        // list with all allowed characters in file name
        String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxz"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"1234567890";
        String ALLOWED_SYMBOLS = "._-";
        int symbolcounter = 0;

        for (int i = 0; i < newName.length(); i++){ // itereer doorheen de characters

            Character character = newName.charAt(i); // gebruik de wrapper class (vraag NIET wrm)

            if ((!ALLOWED_CHARS.contains(character.toString())) && (!ALLOWED_SYMBOLS.contains(character.toString()))) {
                return false; // if any of the chars NOT in ALLOWED_CHARS OR ALLOWED_SYMBOLS => return false
            }
            if (ALLOWED_SYMBOLS.contains(character.toString())){
                // als de character een symbool is, voeg 1 toe aan de teller
                symbolcounter++;
            }
        }
        return symbolcounter >= 1; // return true alleen als er minstens 1 symbooltje is
    }

    private void updateModificationTime() {
        this.modificationTime = new Date();
    }

    /**
     *  @pre int bytes mag niet negatief zijn,
     *      mag niet groter zijn dan maxFileSize-size
     *
     *  @post het argument bytes wordt toegevoegd aan het attribuut size
     *      en wordt opgeslagen in het attribuut size
     *      de functie setLastModified wordt opgeropen
     */
    public void enlarge(long bytes){
        if (this.isWritable()) {this.size+=bytes;}
        this.updateModificationTime();
    }

    /**
     * @param bytes the changes in bytes of the file
     *
     *  @pre int bytes mag niet negatief zijn,
     *      mag niet groter zijn dan maxFileSize-size
     *
     *  @post het argument bytes wordt afgetrokken van het attribuut size
     *      en wordt opgeslagen in het attribuut size
     *      de functie setLastModified wordt opgeropen
     */
    public void shorten(long bytes){
        if (this.isWritable()) {this.size-=bytes;}
        this.updateModificationTime();
    }

    /**
     * function that checks if two files have overlapping use periods
     *
     * @param otherFile the file we want to check for overlap
     *
     * @return whether the files do have overlapping use periods or not
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