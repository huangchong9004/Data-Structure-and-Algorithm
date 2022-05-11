package gitlet;

import java.io.File;
import java.io.IOException;
import static gitlet.Utils.*;
import static java.lang.System.*;
import java.util.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Chongwen Huang
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static File objects = Utils.join(GITLET_DIR, "objects");
    public static File refs = Utils.join(GITLET_DIR, "refs");
    public static File refsHeads = Utils.join(refs, "heads");
    public static File logs = Utils.join(GITLET_DIR, "logs");
    public static File commits = Utils.join(logs, "commits");
    public static File index = Utils.join(GITLET_DIR, "index");

    /* TODO: fill in the rest of this class. */
    public static void setupPersistence() {
        GITLET_DIR.mkdir();
        objects.mkdir();
        refs.mkdir();
        refsHeads.mkdir();
        logs.mkdir();
        commits.mkdir();
        index.mkdir();
        File outFile = Utils.join(Repository.GITLET_DIR, "HEAD");
        Utils.writeContents(outFile, "ref: refs/heads/master");
    }
    public static void init() {
        if (GITLET_DIR.exists()) {
            throw Utils.error("A Gitlet version-control system already exists in the current directory");
        } else {
            setupPersistence();
            Commit initial = new Commit("initial commit", null, null);
            initial.saveCommit();
        }
    }
    public static void add(String fileName) {
        File newFile = Utils.join(Repository.CWD, fileName);
        if (!newFile.exists()) {
            Utils.message("File does not exist");
            System.exit(0);
        }
        String hashcode = Utils.sha1(Utils.serialize(newFile));
        StagingArea sa = fromFile();
        Commit cm = fromSHA(headHashcode());
        //if staged file already exists in the commit, stop add
        if ((cm.files != null) && (cm.files.containsKey(hashcode))) {
            //sa.updateStageFile(hashcode, newFile, false);
            System.exit(0);
        } else if (sa == null) {
            TreeMap<String, File> temp = new TreeMap<String, File>();
            temp.put(hashcode, newFile);
            sa = new StagingArea(temp, null); // set up new staging area
            sa.updateStageFile(hashcode, newFile, true);
            sa.saveStagingFile();
        } else if (sa.rmFileExists(newFile)){
            sa.updateStageFile(hashcode, newFile, true);
        } else {
            sa.updateStageFile(hashcode, newFile, true);
        }
    }
    public static StagingArea fromFile() {
        StagingArea sa;
        File StagingFile = Utils.join(Repository.index, "StagingFile");
        if (StagingFile.exists()){
            sa = Utils.readObject(StagingFile, StagingArea.class);
            return sa;
        }
        return null;
    }

    public static void commit(String msg) {
        //System.out.println(headHashcode());
        String hashcode = headHashcode();
        Commit cm = fromSHA(hashcode);

        Commit newCM = new Commit(msg, hashcode, cm.files);//New Commit;
        StagingArea sa = fromFile(); //read current sa
        if (sa.addStage != null) {
            Set<String> addKeys = sa.addStage.keySet();
            for (String key: addKeys) {
                File outFile = Utils.join(Repository.objects, key);
                File original = sa.addStage.get(key);
                String fileContent = Utils.readContentsAsString(original);
                Utils.writeContents(outFile, fileContent);
                if (newCM.files == null) {
                    newCM.files = new TreeMap<>();
                    newCM.files.put(key, original);
                } else {
                    newCM.removeFile(original); // remove file with old key
                    newCM.files.put(key, original); //update file with new key
                }
            }
        }
        if (sa.rmStage != null) {
            Set<String> rmKeys = sa.rmStage.keySet();
            for (String key: rmKeys) {
                cm.files.remove(key);
            }
        }
        newCM.saveCommit();
        if (sa != null) {
            sa.clear();
        }
    }
    //TODO: use the restrictedDelete in Utils
    public static void rm(String fileName) {
        StagingArea sa = fromFile(); //read current sa
        Commit cm = fromSHA(headHashcode()); // read current commit
        File rmFile = Utils.join(CWD, fileName);
        if (sa.addStage != null && sa.addFileExists(rmFile)) {
            sa.updateStageFile(sa.getKey(rmFile), rmFile, false);
        } else if (cm.files != null && cm.files.containsValue(rmFile)) {

            if (sa.rmStage == null) {
                sa.rmStage = new TreeMap<>();
            }
            sa.rmStage.put(cm.getKey(rmFile), rmFile); // add to stagingArea
            //save to staging area
            File outFile = Utils.join(Repository.index, cm.getKey(rmFile));
            String fileContent = Utils.readContentsAsString(rmFile);
            Utils.writeContents(outFile, fileContent);
            // save to staging file
            sa.updateStageFile(cm.getKey(rmFile), rmFile, false);
            Utils.restrictedDelete(rmFile);
        } else {
            Utils.message("No reason to remove the file.");
        }
    }

    public static String headHashcode() {
        File head = Utils.join(refsHeads, currentHead());
        return Utils.readContentsAsString(head);
    }
    public static Commit fromSHA(String commitSHA) {
        File commitFile = Utils.join(Repository.commits, commitSHA);
        return Utils.readObject(commitFile, Commit.class);
    }
    //TODO: update the merge comment when having two parents
    public static void log() {
        Commit cm = currentCM();
        while (cm.parent != null) {
            cm.printString();
            String parentCode = cm.parent;
            cm = fromSHA(matchHashcode(parentCode));
        }
        cm.printString();
    }
    public static void globalLog() {
        File f = Utils.join(Repository.logs, "HEAD");
        String original = Utils.readContentsAsString(f);
        System.out.println(original);
    }
    public static void find(String msg) {
        List<String> fileList = plainFilenamesIn(commits);
        boolean exists = false;
        for (String hashcode : fileList) {
            File path = Utils.join(commits, hashcode);
            Commit temp = Utils.readObject(path, Commit.class);
            if (temp.getMessage().equals(msg)) {
                System.out.println(hashcode);
                exists = true;
            }
        }
        if (!exists) {
            Utils.message("Found no commit with that message");
        }
    }

    public static void status() {
        List<String> fileList = plainFilenamesIn(refsHeads);
        String printText = "=== Branches ===\n";
        for (String name : fileList) {
            if (name.equals(currentHead())) {
                printText += "*" + name + "\n";
            } else {
                printText += name + "\n";
            }
        }
        printText += "\n";
        printText += "=== Staged Files ===\n";
        StagingArea sa = fromFile();
        if (sa != null && sa.addStage != null) {
            Set<String> keys = sa.addStage.keySet();
            String[] files = new String[keys.size()];
            int size = 0;
            for (String key : keys) {
                files[size++] = sa.addStage.get(key).getName();
            }
            Arrays.sort(files);
            for (int i = 0; i < size; i++) {
                printText += files[i] + "\n";
            }
        }
        //TODO: need to update the StagingArea issues.
        printText += "\n";
        printText += "=== Removed Files ===\n";
        if (sa != null && sa.rmStage != null) {
            Set<String> keys = sa.rmStage.keySet();
            String[] files = new String[keys.size()];
            int size = 0;
            for (String key : keys) {
                files[size++] = sa.rmStage.get(key).getName();
            }
            Arrays.sort(files);
            for (int i = 0; i < size; i++) {
                printText = printText + files[i] + "\n";
            }
        }
        printText += "\n";
        printText += "=== Modifications Not Stage For Commit ===\n\n";
        printText += "=== Untracked Files ===\n\n";
        System.out.println(printText);
    }
    //TODO: what is the working directory? I think is where the .gitlet folder locates
    public static void checkoutHashcode(String hashcode, String fileName) {
        Commit cm = fromSHA(hashcode); // read commit with hashcode
        File file= Utils.join(CWD, fileName);// because File is based on CWD + fileName
        if (!cm.files.containsValue(file)) {
            Utils.message("File does not exist in that commit.");
            System.exit(0);
        } else {
            String fileHashcode = cm.getKey(file);
            File newFile = Utils.join(objects, fileHashcode);
            String fileContent = Utils.readContentsAsString(newFile);
            Utils.writeContents(file, fileContent);
        }
    }
    public static void checkout(String fileName) {
        String hashcode = headHashcode(); // read current commit
        checkoutHashcode(hashcode, fileName);
    }
    public static String matchHashcode(String hashcode) {
        List<String> fileList = plainFilenamesIn(commits);
        //extract the first hashcode if the parent has two hashcode
        if (hashcode.contains(" ")) {
            hashcode = hashcode.substring(0, hashcode.indexOf(" "));
        }
        int n = hashcode.length();
        boolean hashcodeExists = false;
        for (String file : fileList) {
            if (file.substring(0, Math.min(file.length(), n)).equals(hashcode)) {
                hashcode = file;
                hashcodeExists = true;
            }
        }
        if (!hashcodeExists) {
            Utils.message("No commit with that id exists");
            System.exit(0);
        }
        return hashcode;
    }
    public static void checkout(String hashcode, String fileName) {
        String fullHashcode = matchHashcode(hashcode);
        checkoutHashcode(fullHashcode, fileName);
    }
    public static String currentHead() {
        String temp = Utils.readContentsAsString(Utils.join(Repository.GITLET_DIR, "HEAD"));
        String currentHead = temp.substring(temp.lastIndexOf("/") + 1);
        return currentHead;
    }
    public static Commit currentCM() {
        String headHashcode = headHashcode();
        return fromSHA(headHashcode);
    }
    //TODO: how to check whether we will overwrite untracted files in CWD????
    public static void overwrite(Commit cm) {
        Commit currentCM = currentCM();
        if (cm.files != null) {
            Set<String> keys = cm.files.keySet();
            for (String key : keys) {
                File file= Utils.join(CWD, cm.files.get(key).getName());
                if (file.exists() && ((currentCM == null) ||!currentCM.files.containsValue(file))) {
                    Utils.message("There is an untracked file in the way; " +
                            "delete it, or add it and commit it first");
                    System.exit(0);
                } else {
                    File savedFile = Utils.join(objects, key);
                    String fileContent = Utils.readContentsAsString(savedFile);
                    Utils.writeContents(file, fileContent);
                }
            }
        }
    }
    public static void removeUntracked(Commit cm) {
        Commit currentCM = currentCM();
        Set<String> currentKeys = currentCM.files.keySet();
        for (String key: currentKeys) {
            File currentFile = currentCM.files.get(key);
            if (!cm.files.containsValue(currentFile)) {
                Utils.restrictedDelete(currentFile);
            }
        }
    }

    public static void checkoutBranch(String branchName) {
        File branchFile = Utils.join(refsHeads, branchName);
        StagingArea sa = fromFile();

        if (!branchFile.exists()) {
            Utils.message("No such branch exists.");
            System.exit(0);
        } else if (branchName.equals(currentHead())) {
            Utils.message("No need to checkout the current branch.");
            System.exit(0);
        } else {
            String hashcode = Utils.readContentsAsString(branchFile);
            Commit cm = fromSHA(hashcode);
            overwrite(cm);
            if (cm.files != null) {
                Set<String> keys = cm.files.keySet();
                for (String key : keys) {
                    File file= cm.files.get(key);
                    String fileHashcode = cm.getKey(file);
                    File newFile = Utils.join(objects, fileHashcode);
                    String fileContent = Utils.readContentsAsString(newFile);
                    Utils.writeContents(file, fileContent);
                }
            }
            // delete files tracked in current branch but not in new branch
            removeUntracked(cm);
        }
        //change current head to branch
        File outFile = Utils.join(Repository.GITLET_DIR, "HEAD");
        Utils.writeContents(outFile, "ref: refs/heads/" + branchName);
        if (sa != null) {
            sa.clear();
        }
    }
    public static void branch(String name) {
        String hashcode = headHashcode();
        File branchFile = Utils.join(refsHeads, name);
        if (branchFile.exists()) {
            Utils.message("A branch with that name already exists.");
            System.exit(0);
        }
        Utils.writeContents(branchFile, hashcode);
    }
    //remove pointer to branch
    public static void rmBranch(String name) {
        File branchFile = Utils.join(refsHeads, name);
        if (!branchFile.exists()) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        } else if (name.equals(currentHead())) {
            Utils.message("Cannot remove the current branch.");
            System.exit(0);
        } else {
            branchFile.delete();
        }
    }
    //TODO: how to check whether we will overwrite untracted files in CWD????
    // shall we need to overwrite files in CWD after reset?
    //same with checkoutBranch method, need additional method to track untracted files
    public static void reset(String hashcode) {
        String fullHashcode = matchHashcode(hashcode);
        Commit cm = fromSHA(fullHashcode);
        overwrite(cm);
        removeUntracked(cm);
        File currentHead = Utils.join(refsHeads, currentHead());
        Utils.writeContents(currentHead, fullHashcode);
        StagingArea sa = fromFile();
        if (sa != null) {
            sa.clear();
        }
    }
    public static List<String> getParentHashcode(String branchName) {
        List<String> parentList = new ArrayList<>();
        File file = Utils.join(refsHeads, branchName);
        String hashcode = Utils.readContentsAsString(file);
        Commit cm = fromSHA(hashcode);
        //TODO: need to finish this part, additional routes
        while (cm.parent != null) {
            parentList.add(hashcode);
            hashcode = cm.parent;
            cm = fromSHA(matchHashcode(hashcode));
        }
        parentList.add(hashcode);
        return parentList;
    }
    public static String getSplitPoint(List<String> branch, List<String> current) {
        for (String hashcode : branch) {
            if (current.contains(hashcode)) {
                return hashcode;
            }
        }
        return null;
    }
    public static String combineFiles(String key, String branchKey) {
        String fromCurrent;
        String fromBranch;
        if (key != null) {
            File currentFile = Utils.join(objects, key);
            fromCurrent = Utils.readContentsAsString(currentFile);
        } else {
            fromCurrent ="";
        }
        if (branchKey != null) {
            File branchFile = Utils.join(objects, branchKey);
            fromBranch = Utils.readContentsAsString(branchFile);
        } else {
            fromBranch ="";
        }
        String newText = "<<<<<<< HEAD\n" + fromCurrent + "=======" + fromBranch + ">>>>>>>";
        Utils.message("Encountered a merge conflict.");
        return newText;
    }
    public static void merge(String branchName) {
        File branchCommit = Utils.join(refsHeads, branchName);
        String hashcode = Utils.readContentsAsString(branchCommit);
        Commit branch = fromSHA(hashcode);
        Commit current = currentCM();
        StagingArea sa = fromFile();
        //overwrite(branch);
        if (sa.addStage != null || sa.rmStage != null) {
            Utils.message("You have uncommitted changes.");
            System.exit(0);
        } else if (!branchCommit.exists()) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        } else if (currentHead().equals(branchName)) {
            Utils.message("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Set<String> keys = branch.files.keySet();
        for (String key : keys) {
            File file = Utils.join(CWD, branch.files.get(key).getName());
            if (file.exists() && ((current == null) || !current.files.containsValue(file))) {
                Utils.message("There is an untracked file in the way; " +
                            "delete it, or add it and commit it first");
                System.exit(0);
            }
        }
        String splitPoint = getSplitPoint(getParentHashcode(currentHead()), getParentHashcode(branchName));
        Commit split = fromSHA(splitPoint);
        if (splitPoint.equals(hashcode)) {
            Utils.message("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (splitPoint.equals(headHashcode())){
            checkoutBranch(branchName);
            Utils.message("Current branch fast-forwarded.");
            System.exit(0);
        }
        // consider each and every case in the merge.
        String msg = "Merged " +branchName + " into "+ currentHead();
        // set the parent point to a string with combination of 2 hashcode
        String parent = headHashcode().substring(0, 7) + " " +hashcode.substring(0, 7);
        Commit merge = new Commit(msg, parent,current.files);
        Set<File> fileSet = new HashSet<>();
        Set<String> currentSet = current.files.keySet();
        Set<String> branchSet = branch.files.keySet();
        Set<String> splitSet = split.files.keySet();
        for (String key : currentSet) {
            File file = current.files.get(key);
            fileSet.add(file);
            if (splitSet.contains(key) && !branchSet.contains(key) && branch.files.containsValue(file)) {
                String newHashcode = branch.getKey(file);
                File newFile = Utils.join(objects,newHashcode);
                merge.files.remove(key);
                String fileContent = Utils.readContentsAsString(newFile);
                Utils.writeContents(file, fileContent);
                merge.files.put(newHashcode, file);
            } else if (splitSet.contains(key) && !branch.files.containsValue(file)) {
                merge.files.remove(key);
                file.delete();
            } else if (!splitSet.contains(key) && !branchSet.contains(key)
                     && split.files.containsValue(file)) {
                String newText;
                if (!branch.files.containsValue(file)) {
                    newText = combineFiles(key, null);
                } else {
                    String branchKey = branch.getKey(file);
                    newText = combineFiles(key, branchKey);
                }
                merge.files.remove(key);
                Utils.writeContents(file, newText);
                String newHashcode = Utils.sha1(serialize(file));
                File newFile = Utils.join(objects, newHashcode);
                Utils.writeContents(newFile, newText);
                merge.files.put(newHashcode, file);
            }
        }
        // TODO: need to update file writing
        for (String key : branchSet) {
            File file = branch.files.get(key);
            if (!fileSet.contains(file)) {
                fileSet.add(file);
                //if file exists in branch, but not in current or split, check out and add to merge.
                if (!current.files.containsValue(file) && !split.files.containsValue(file)) {
                    String fileContent = Utils.readContentsAsString(file);
                    Utils.writeContents(file, fileContent);
                    merge.files.put(key, file);
                } else if (!current.files.containsValue(file) && split.files.containsValue(file)
                && !splitSet.contains(key)) {
                    String newText = combineFiles(null, key);
                    Utils.writeContents(file, newText);
                    String newHashcode = Utils.sha1(serialize(file));
                    File newFile = Utils.join(objects, newHashcode);
                    Utils.writeContents(newFile, newText);
                    merge.files.put(newHashcode, file);
                }
            }
        }
        merge.saveCommit();
    }
}
