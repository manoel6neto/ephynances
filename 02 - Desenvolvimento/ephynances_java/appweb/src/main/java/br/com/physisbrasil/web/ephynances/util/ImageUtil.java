/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.physisbrasil.web.ephynances.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Thadeu
 */
public class ImageUtil {

    public static BufferedImage progressiveResizeImage(BufferedImage originalImage, int destWidth, int destHeight) {

        BufferedImage resizeImage;

        int halfWidth = originalImage.getWidth(null) / 2;
        int halfHeight = originalImage.getHeight(null) / 2;

        int originalSize = halfWidth * halfHeight;
        int destSize = destWidth * destHeight;
        int proporcao = originalSize / destSize;

        while (proporcao > 2) {
            originalImage = resizeImage(originalImage, halfWidth, halfHeight);//reduz à metade do tamanho;

            halfWidth = originalImage.getWidth(null) / 2;
            halfHeight = originalImage.getHeight(null) / 2;

            originalSize = halfWidth * halfHeight;
            proporcao = originalSize / destSize;
        }

        resizeImage = resizeImage(originalImage, destWidth, destHeight);

        return resizeImage;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int destWidth, int destHeight) {

        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int imageWidth = originalImage.getWidth(null);
        int imageHeight = originalImage.getHeight(null);
        double aspectRatio = (double) imageWidth / (double) imageHeight;
        double thumbRatio = (double) destWidth / (double) destHeight;

        if (thumbRatio < aspectRatio) {
            destHeight = (int) (destWidth / aspectRatio);
        } else {
            destWidth = (int) (destHeight * aspectRatio);
        }

        BufferedImage resizedImage = new BufferedImage(destWidth, destHeight, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, destWidth, destHeight, null);
        g.dispose();

        return resizedImage;
    }
}
