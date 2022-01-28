package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Example {
    public static void main(String[] args) throws IOException {
        File f = new File("dummy.txt");

        f.createNewFile();
        Utils.writeContents(f, "Hello JAVA");
        f.exists();
    }
}
