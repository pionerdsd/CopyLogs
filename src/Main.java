
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;


/**
 * Created by dmurashko on 12.01.2018.
 */
public class Main {

    public static void main(String[] args) {
        Thread thread = new Copy();
        //Copy.setCPSLogsPath(args[0]);
        //Copy.setServerLogsPath(args[1]);
        thread.start();
    }

    }
