
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;


public class Copy extends Thread {

    private static HashSet<FileTime> logsServer = new HashSet<>();
   // private static String CPSLogsPath;
    private static String ServerLogsPath = "/mnt/vasbackup/"; // pass to store logs
    private static Long SpaceLimit = 1479707417856L;
    private static File CPSlog = new File("/var/log/broadhop/consolidated-engine.2.log.gz"); // file that will be stored
    //private static File CEL = new File("/var/log/broadhop/consolidated-engine.log");


    public FileTime lastModTime(File file) {
        FileTime LMT = null;

        try {
             LMT = Files.readAttributes(file.toPath(), PosixFileAttributes.class).lastModifiedTime();
            // LMT = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return LMT;
    }


    public void run() {
        System.out.println("Program started!  " + (new File(ServerLogsPath).getFreeSpace())+ " bytes are available!");
        while (true) {
           // if (CEL.length() == 0) {
                try (
                        DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(ServerLogsPath)))

                {
                    for (Path file : stream) {

                        if (!file.toFile().isDirectory()) {

                            logsServer.add(Files.readAttributes(file, PosixFileAttributes.class).lastModifiedTime());
                            //logsServer.add(Files.readAttributes(file, BasicFileAttributes.class).lastModifiedTime());


                        }
                    }


                } catch (IOException | DirectoryIteratorException x) {
                    System.err.println(x);
                }
                //System.out.println(logsServer);
                //System.out.println(lastModTime(CPSlog));

                if (!logsServer.contains(lastModTime(CPSlog))) {
                    File dest = new File(ServerLogsPath + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".gz");


                    if (!dest.exists()) {
                        if ((new File(ServerLogsPath).getFreeSpace() - SpaceLimit) > 0) {

                            //if (CPSlog.length() > 12000000) {

                                try {
                                    Files.copy(CPSlog.toPath(), dest.toPath());
                                    Files.setLastModifiedTime(dest.toPath(), lastModTime(CPSlog));
                                    System.out.println("New Log has been being copied! Size: " + dest.length());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                           // }
                        } else {
                            System.out.println("No free space  available!");
                            // System.out.println(CPSlog.length());
                            System.exit(0);

                        }
                    }
                }


            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}





