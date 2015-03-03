/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ipsoftbrasil.web.agility.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 *
 * @author Thadeu
 */
public class FileUtil {

    /**
     * Copia o conteudo do arquivo <b>src</b> para o caminho indicado em <b>dst</b>
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copyFileContents(InputStream in, File dst) throws IOException {

        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static boolean copyFileContents(File sourceFile, File destFile) {

        boolean status = false;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {

            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(destFile);

            sourceChannel = fileInputStream.getChannel();
            destinationChannel = fileOutputStream.getChannel();

            long count = 0;
            long size = sourceChannel.size();

            while (count < size) {
                count += destinationChannel.transferFrom(sourceChannel, count,
                        size - count);
            }

            status = true;

        } catch (IOException e) {
        } finally {
            if (sourceChannel != null) {
                try {
                    sourceChannel.close();
                    fileInputStream.close();
                } catch (Exception e) {
                }
            }
            if (destinationChannel != null) {
                try {
                    destinationChannel.close();
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }

        return status;
    }

    public static String getFileExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            extension = fileName.substring(i + 1);
        }

        return extension;
    }
    
    public static String getCriptoFileName(String fileName) {
        try {
            return Criptografia.criptografar(fileName);           
        } catch (Exception e) {
            return null;
        }
    }
    
}
