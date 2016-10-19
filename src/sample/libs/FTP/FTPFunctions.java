package sample.libs.FTP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Created by oleh_pi on 16.10.2016.
 */

/**
 * Клас для роботи з FTP
 */
public class FTPFunctions {

    FTPClient ftp = null; // Creating FTP Client instance

    /**
     *
     * @param host - хост
     * @param port - порт
     * @param username - користувач
     * @param password - пароль користувача
     * @throws Exception
     */
    public FTPFunctions(String host, int port, String username, String password) throws Exception {
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host,port);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(username, password);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }

    /**
     * Завантажити файл на FTP
     * @param localFileFullName  - шлях до файлу на локалі
     * @param fileName - назва файла (з розширенням)
     * @param hostDir - розміщення файлу на віддаленому сервері
     * @throws Exception
     */
    public void uploadFTPFile(String localFileFullName, String fileName, String hostDir)
            throws Exception
    {
        try {
            InputStream input = new FileInputStream(new File(localFileFullName));

            this.ftp.storeFile(hostDir + fileName, input);
        }
        catch(Exception e){

        }
    }

    /**
     * Отримати список директорій
     * @param path - шлях до директорії на віддаленому сервері
     */
    public void getDirectoriesList(String path){

        FTPFile[] files = new FTPFile[0];
        try {
            files = ftp.listDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (FTPFile file : files) {
            String details = file.getName();
            if (file.isDirectory()) {
                details = "[" + details + "]";
            }

            System.out.println(details);
        }
    }

    /**
     *
     * @param currentPath - шлях , де буде створено директорію на сервері
     * @param newDirName - назва нової директорії
     */
    public void createDirectory(String currentPath, String newDirName){
        boolean created = false;
        try {
            created = this.ftp.makeDirectory(currentPath + newDirName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (created)
            System.out.println("Directory created");
    }



    /**
     * Download the FTP File from the FTP Server
     * @param source - назва файлу на FTP сервері
     * @param destination - директорія призначення (на локальному ПК)
     */
    public void downloadFTPFile(String source, String destination) {
        try (FileOutputStream fos = new FileOutputStream(destination)) {
            this.ftp.retrieveFile(source, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отримати список файлів в директорії
     * @param directory - директорія FTP з якої вибираються файли
     * @return List<String>
     * @throws IOException
     */
    public List<String> listFTPFiles(String directory) throws IOException {

        List<String> imageList = new ArrayList<String>();
        FTPFile[] files = ftp.listFiles(directory);
        for (FTPFile file : files) {
            imageList.add(file.getName().toString());
        }

        return imageList;
    }

    /**
     * // Disconnect the connection to FTP
     */
    public void disconnect(){
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }
}
