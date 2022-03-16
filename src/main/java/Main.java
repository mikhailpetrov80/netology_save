import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void saveGame(String path, GameProgress obj) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String pathZip, String[] pathFile) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathZip))) {
            for (int i = 0; i < pathFile.length; i++) {
                try (FileInputStream fis = new FileInputStream(pathFile[i])) {
                    ZipEntry entry = new ZipEntry("save" + (i + 1) + ".dat");
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openZip(String pathZip, String pathFile) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(pathFile + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String pathProgress) {
        GameProgress objProgress = null;
        try (FileInputStream fis = new FileInputStream(pathProgress);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            objProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return objProgress;
    }

    public static void main(String[] args) {

        GameProgress obj1 = new GameProgress(4, 2, 5, 4.6);
        GameProgress obj2 = new GameProgress(2, 1, 2, 5.2);
        GameProgress obj3 = new GameProgress(2, 3, 1, 2.4);

        String path1 = "/Users/vodnik/Documents/Games/savegames/save1.dat";
        String path2 = "/Users/vodnik/Documents/Games/savegames/save2.dat";
        String path3 = "/Users/vodnik/Documents/Games/savegames/save3.dat";

        saveGame(path1, obj1);
        saveGame(path2, obj2);
        saveGame(path3, obj3);

        String pathZip = "/Users/vodnik/Documents/Games/savegames/saveZip.zip";

        String[] pathFile = {path1, path2, path3};
        zipFiles(pathZip, pathFile);

        File path1_1 = new File("/Users/vodnik/Documents/Games/savegames/save1.dat");
        path1_1.delete();
        File path1_2 = new File("/Users/vodnik/Documents/Games/savegames/save2.dat");
        path1_2.delete();
        File path1_3 = new File("/Users/vodnik/Documents/Games/savegames/save3.dat");
        path1_3.delete();

        String pathFileOpen = "/Users/vodnik/Documents/Games/savegames/";

        openZip(pathZip, pathFileOpen);

        System.out.println(openProgress(path3));


    }
}
