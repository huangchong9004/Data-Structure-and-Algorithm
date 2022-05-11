package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?

        if (args.length == 0) {
            Utils.error("Please enter a command.");
            Utils.message("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        //String firstArg = "init";
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                //validateNumArgs("init", args, 1);
                Repository.init();
                break;

            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs("add", args, 2);
                Repository.add(args[1]);
                break;

            case "commit":
                if (args.length == 1) {
                    Utils.message("Please enter a commit message");
                    System.exit(0);
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;

            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.globalLog();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;

            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;

            case "checkout":
                if (args.length == 3) {
                    Repository.checkout(args[2]);
                } else if (args.length == 4) {
                    //validateNumArgs("checkout", args, 5);
                    Repository.checkout(args[1], args[3]);
                } else {
                    //validateNumArgs("checkout", args, 2);
                    Repository.checkoutBranch(args[1]);
                }
                break;

            case "branch":
                validateNumArgs("branch", args, 2);
                Repository.branch(args[1]);
                break;

            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                Repository.rmBranch(args[1]);
                break;

            case "reset":
                validateNumArgs("rest", args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                validateNumArgs("merge", args, 2);
                Repository.merge(args[1]);
                break;
            default:
                Utils.error("No command with that name exists.");
                Utils.message("No command with that name exists.");
                break;


        }

    }
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
