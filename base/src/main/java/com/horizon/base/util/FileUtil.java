

package com.horizon.base.util;


import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static final int BUFFER_SIZE = 1024 * 4;

    /**
     * check if exist directory <br/>
     * make directory if not exist.
     */
    public static boolean makeDirIfNotExist(File dir) {
        if (dir == null) {
            return false;
        }

        if (dir.exists()) {
            return true;
        } else {
            for (int i = 0; i < 5; i++) {
                if (dir.mkdirs()) {
                    break;
                }
            }
        }

        return dir.exists();
    }

    /**
     * check if exist file <br/>
     * make file if not exist.
     */
    public static boolean makeFileIfNotExist(File file) throws IOException {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.exists()) {
                    if (makeDirIfNotExist(parent)) {
                        //noinspection ResultOfMethodCallIgnored
                        file.createNewFile();
                    } else {
                        return false;
                    }
                } else {
                    //noinspection ResultOfMethodCallIgnored
                    file.createNewFile();
                }
            }
        }

        return file.exists();
    }

    public static void copyFile(File src, File des) throws IOException {
        RandomAccessFile srcFile = new RandomAccessFile(src, "r");
        RandomAccessFile desFile = new RandomAccessFile(des, "rw");
        copyFile(srcFile, desFile);
    }

    public static void copyFile(String srcPath, String desPath) throws IOException {
        RandomAccessFile srcFile = new RandomAccessFile(srcPath, "r");
        RandomAccessFile desFile = new RandomAccessFile(desPath, "rw");
        copyFile(srcFile, desFile);
    }

    private static void copyFile(RandomAccessFile srcFile, RandomAccessFile desFile) throws IOException {
        try {
            long srcLen = srcFile.length();
            desFile.setLength(srcLen);
            if (srcLen != 0) {
                int count;
                byte[] b = new byte[srcLen < BUFFER_SIZE ? (int) srcLen : BUFFER_SIZE];
                desFile.seek(0);
                while ((count = srcFile.read(b)) > 0) {
                    desFile.write(b, 0, count);
                }
            }
        } finally {
            IOUtil.closeQuietly(srcFile);
            IOUtil.closeQuietly(desFile);
        }
    }

    public static void saveBytes(File file, byte[] bytes) throws IOException {
        if (file == null || !makeFileIfNotExist(file) || bytes == null || bytes.length == 0) {
            return;
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        try {
            accessFile.write(bytes);
        } finally {
            IOUtil.closeQuietly(accessFile);
        }
    }

    public static byte[] loadBytes(File file) throws IOException {
        if (file == null || !file.isFile() || !file.exists()) {
            return null;
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");
        int len = (int) accessFile.length();
        byte[] bytes = new byte[len];
        try {
            accessFile.read(bytes);
        } finally {
            IOUtil.closeQuietly(accessFile);
        }
        return bytes;
    }

    /**
     * append string
     * note：this method will append a "\n" after append string
     *
     * @param file
     * @param str
     */
    public static void appendStr(File file, String str) throws IOException {
        if (file == null || !file.exists() || !file.isFile() || TextUtils.isEmpty(str)) {
            return;
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        accessFile.seek(accessFile.length());
        accessFile.write(str.getBytes());
        accessFile.writeBytes("\n");
        accessFile.close();
    }

    /**
     * load file content to string
     *
     * @param file file to load
     * @return
     */
    public static String fileToString(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            throw new FileNotFoundException("");
        }

        int fileLen = (int) file.length();
        if (fileLen > 0) {
            byte[] buffer = new byte[fileLen];
            InputStream inputStream = new FileInputStream(file);
            try {
                inputStream.read(buffer);
            } finally {
                IOUtil.closeQuietly(inputStream);
            }
            return new String(buffer);
        } else {
            return "";
        }
    }

    /**
     * save string to file
     *
     * @param file file to save
     * @param str  content
     * @throws IOException
     */
    public static void stringToFile(File file, String str) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return;
        }
        OutputStream outputStream = new FileOutputStream(file);
        try {
            if (!TextUtils.isEmpty(str)) {
                outputStream.write(str.getBytes());
            }
        } finally {
            IOUtil.closeQuietly(outputStream);
        }
    }

    private static void geFiles(File parent, List<File> fileList) {
        File[] files = parent.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    geFiles(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }

    public static List<File> getFiles(String path) {
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            geFiles(file, fileList);
        }
        return fileList;
    }
}