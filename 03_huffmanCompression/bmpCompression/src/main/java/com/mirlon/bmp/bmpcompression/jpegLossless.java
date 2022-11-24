/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mirlon.bmp.bmpcompression;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

/**
 *
 * @author jaynam
 */
public class jpegLossless {
    public static long beginJCompression(File selectedBmp) {
        File ofile = new File(selectedBmp.getAbsolutePath() + ".jpeglossless");
        try{
            BufferedImage selImg = ImageIO.read(selectedBmp);
            ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(param.MODE_EXPLICIT);
            param.setCompressionType("JPEG");
            writer.setOutput(ImageIO.createImageOutputStream(ofile));
            writer.write(null, new IIOImage(selImg, null, null), param);
        } catch (IOException e) {
            System.out.println(" > Unhandled IO Exception - Jpeg Lossless");
        }
        return ofile.length();
    }
}
