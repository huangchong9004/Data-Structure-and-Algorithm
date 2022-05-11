package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class StagingArea implements Serializable {
    public TreeMap<String, File> addStage = new TreeMap<String, File>();
    public TreeMap<String, File> rmStage = new TreeMap<String, File>();
    public final File StagingFile = Utils.join(Repository.index, "StagingFile");

    public StagingArea(TreeMap<String, File> addStage, TreeMap<String, File> rmStage) {
        this.addStage = addStage;
        this.rmStage = rmStage;
    }
    public String getKey(File file) {
        Set<String> keys = this.addStage.keySet();
        for (String key : keys) {
            if(this.addStage.get(key).equals(file)) {
                return key;
            }
        }
        return null;
    }
    public String getRMKey(File file) {
        Set<String> keys = this.rmStage.keySet();
        for (String key : keys) {
            if(this.rmStage.get(key).equals(file)) {
                return key;
            }
        }
        return null;
    }
    public boolean addFileExists (File file) {
        return this.addStage.containsValue(file);
    }
    public boolean rmFileExists (File file) {
        if (this.rmStage == null) {
            return false;
        }
        return this.rmStage.containsValue(file);
    }
    public StagingArea fromStagingFile() {
        StagingArea sa;
        if (StagingFile.exists()){
            sa = Utils.readObject(StagingFile, StagingArea.class);
            return sa;
        }
        return null;
    }
    public void saveStagingFile() {
        Utils.writeObject(StagingFile, this);
    }
    public void updateStageFile(String hashcode, File file, boolean add) {

        //StagingArea sa = fromStagingFile();
        //TODO: check whether commit has the same file, if it has it, do not add it or remove it if there
        //if ()
        //TODO: if the file is in the rmStage, remove it from reStage;
        //TODO: replace files in staging area
        if (add) {
            if (this == null || !addFileExists(file)) {
                this.addStage.put(hashcode, file);
                File outFile = Utils.join(Repository.index, hashcode);
                String fileContent = Utils.readContentsAsString(file);
                Utils.writeContents(outFile, fileContent);
            } else if (getKey(file) != null) {
                this.addStage.remove(getKey(file));
                this.addStage.put(hashcode, file);
                File outFile = Utils.join(Repository.index, hashcode);
                String fileContent = Utils.readContentsAsString(file);
                Utils.writeContents(outFile, fileContent);
            }
            //TODO: need to remove from the commit folder
            if (rmFileExists(file)) {
                this.rmStage.remove(getRMKey(file));
                File outFile = Utils.join(Repository.index, hashcode);
                String fileContent = Utils.readContentsAsString(file);
                Utils.writeContents(outFile, fileContent);

            }
        } else { // remove file or remove it from staging area
            if (addFileExists(file)) {
                this.addStage.remove(getKey(file));
            } else {
                this.rmStage.put(hashcode, file);
            }
        }
        this.saveStagingFile();
    }
    public void rmStage(String hashcode) {

    }
    //Delete the files saved in the index folder named by key(hashcode), not the original place.
    public  void clear(){
        // delete the files saved in index folder associated with the staging area
        // Utils.restrictedDelete failed due to the folder structure.
        // it works without the file deletion, if error occurs, we can only clear the TreeMaps not the files.
        if (this.addStage != null) {
            Set<String> addKeys = this.addStage.keySet();
            for (String key : addKeys) {
                File outFile = Utils.join(Repository.index, key);
                outFile.delete();
            }
            this.addStage = null;
        }
        if (this.rmStage != null) {
            Set<String> rmKeys = this.rmStage.keySet();
            for (String key : rmKeys) {
                File outFile = Utils.join(Repository.index, key);
                outFile.delete();
            }
            this.rmStage = null;
        }
        this.saveStagingFile();
    }

}
