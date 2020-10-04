package com.example.SOS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
public class FTPCommunication {
    private static String server = "sos.komza.com";
    private static int port = 21;
    private static String pass = "G!10w!ceStr0ma822";
    private static String user = "ftp.komza.com|lkomza";
    private static FTPClient ftpClient = new FTPClient();
    public static void connect() throws IOException{
        ftpClient.connect(server, port);
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
        }
        boolean success = ftpClient.login(user, pass);
        if (!success) {
        }
        ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
    }

    public static void addMedia(String local, String remote, boolean isBinary) throws IOException{
        connect();
        File file = new File(local);
        InputStream is = new FileInputStream(file);
        if (isBinary) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        }
        if (!ftpClient.storeFile(remote, is)) {
            //TODO
        }

    }
    public static void addMedia(InputStream is, String remote, boolean isBinary) throws IOException{
        connect();
        if (isBinary) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        }
        if (!ftpClient.storeFile(remote, is)) {
            //TODO
        }
    }
    public static String addDirectory(String dirName, String remote) throws IOException{
        connect();
        if (!ftpClient.makeDirectory(remote + "\\" + dirName)) {
            //TODO
        }
        return remote + "\\" + dirName;
    }
    public static OutputStream retrieveFile(String remote, boolean isBinary) throws IOException{
        connect();
        if (isBinary) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        }
        OutputStream os = new ByteArrayOutputStream();
        ftpClient.retrieveFile(remote, os);
        return os;

    }
}

