package File;

import java.util.ArrayList;
import java.util.List;

//
// DEZE KLASSE MOET NOG DIRECTORY GENOEMD WORDEN EN FOLDER BEST OOK NOG OVERAL VERANDEREN
//

public class Folder extends File{
    private List<File> contents;
    public Folder(String name, int size, boolean writable, List <File> contents) {
        super(name, size, writable);
        this.contents = new ArrayList<File>();
    }

    @override
    protected boolean isValidName(String newName){

        // list with all allowed characters in file name
        String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyz"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"1234567890";
        String ALLOWED_SYMBOLS = "_-";      // WITHOUT '.'
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

    public boolean contains(File file) {
        for (File f : contents) {
            if  (f.equals(file)) {
                return true;
            }

            if (f instanceof Folder) {
                ((Folder) f).contains(file);
            }
        }
        return false;
    }
    // needs to be a file, folder or link
    public void addElement(File element) {
        if (!(element instanceof File)){
            return;
        }
        if (element == null) {
            return;
        }
        if (!this.contains(element)) {
            this.contents.add(element);
        }
    }
    //
    // HIER MOET NOG WAT CODE KOMEN
    //

    public void makeRoot(Folder folder) {
        if (folder instanceof Folder) {
            folder= new Folder("folder",0,true,new ArrayList<File>);
        }
    }

    public int getNbItems(Folder folder) {
        int count=0;
        for (File f : folder.contents) {
            if (f instanceof Folder) {
                count+=1;
            }
        }
        return count;
    }
}
