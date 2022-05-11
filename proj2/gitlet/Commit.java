package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Chongwen Huang
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    //something to track files
    //Shall I sava files into TreeMap use hashcode with filename or File
    public TreeMap<String, File> files;
    public TreeMap<String, String> commitLog = new TreeMap<>();
    public String parent; //store parent's hashcode
    /**
    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            Date original = new Date(0);
            this.timestamp = original.toString();
        } else {
            Date now = new Date();
            this.timestamp = now.toString();
        }
    }*/
    public Commit(String message, String parent, TreeMap<String, File> files) {
        this.message = message;
        this.parent = parent;
        this.files = files;
        if (this.parent == null) {
            Date original = new Date(0);
            String dateFormat = original.toString();
            dateFormat = dateFormat.substring(0, 19) + dateFormat.substring(23,28);
            this.timestamp = dateFormat;
        } else {
            Date now = new Date();
            String dateFormat = now.toString();
            dateFormat = dateFormat.substring(0, 19) + dateFormat.substring(23,28);
            this.timestamp = dateFormat;
        }
    }
    public String getMessage() {
        return this.message;
    }
    public String getKey(File file) {
        Set<String> keys = this.files.keySet();
        for (String key : keys) {
            if(this.files.get(key).equals(file)) {
                return key;
            }
        }
        return null;
    }
    //Get Commit documents from SHA code
    public Commit fromSHA(String commitSHA) {
        File commitFile = Utils.join(Repository.commits, commitSHA);
        if (commitFile.exists()) {
            return Utils.readObject(commitFile, Commit.class);
        } else {
            return null;
        }
    }
    public String printMessage() {
        String newText;
        String hashcode = this.commitHashcode();
        if (this.parent != null && this.parent.contains(" ")){
            newText = "===\n" +"commit " + hashcode +"\n" + "Merge: " + this.parent +"\n"
                    + "Date: " + this.timestamp + "\n" + this.message +"\n\n";
        } else {
            newText = "===\n" +"commit " + hashcode +"\n" + "Date: " + this.timestamp + "\n" +
                    this.message +"\n\n";
        }
        return newText;
    }
    // add commit message to logs/head file and commitLog TreeMap;
    public void addLog() {
        File f = Utils.join(Repository.logs, "HEAD");
        String hashcode = this.commitHashcode();
        String newText = printMessage();
        if (f.exists()) {
            String original = Utils.readContentsAsString(f);
            newText = original + newText;
        }
        Utils.writeContents(f, newText);
        commitLog.put(hashcode, this.message);
    }
    public void removeFile(File file) {
        Set<String> keys = this.files.keySet();
        for (String key : keys) {
            if(this.files.get(key).equals(file)) {
                this.files.remove(key);
            }
        }
    }
    public String commitHashcode() {
        return Utils.sha1(Utils.serialize(this));
    }
    //TODO: need to consider the files when there is parent Commit available
    //TODO: need to consider remove stage area as well
    public void saveCommit() {

        String commitSHA = this.commitHashcode();

        File outFile = Utils.join(Repository.commits, commitSHA);
        Utils.writeObject(outFile, this);
        File outFile2 = Utils.join(Repository.refsHeads, Repository.currentHead());
        Utils.writeContents(outFile2, commitSHA);
        this.addLog();
    }
    public Commit getParent() {
        if (this.parent != null) {
            return fromSHA(this.parent);
        }
        return null;
    }
    //TODO: needs to update for the case of merge
    public void printString() {
        String newText = printMessage();
        System.out.println(newText);
    }
    /* TODO: fill in the rest of this class. */
}
