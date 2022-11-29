/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mirlon.bmp.bmpcompression;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import com.aspose.imaging.*;
import com.aspose.imaging.Image;
import com.aspose.imaging.imageoptions.BmpOptions;
import com.aspose.imaging.fileformats.bmp.BmpImage;
import com.aspose.imaging.fileformats.jpeg.JpegCompressionColorMode;
import com.aspose.imaging.fileformats.jpeg.JpegCompressionMode;
import com.aspose.imaging.fileformats.jpeg.JpegImage;
import com.aspose.imaging.imageoptions.BmpOptions;
import com.aspose.imaging.imageoptions.JpegOptions;
import com.aspose.imaging.imageoptions.PngOptions;

/*
 *
 * @author jaynam
 */
public class jpegLossless {
    public static long beginJCompression(File selectedBmp) {
        File ofile = new File(selectedBmp.getAbsolutePath() + ".ljpeg");
        try{
            FileOutputStream ostream = new FileOutputStream(ofile);
            BmpImage image = (BmpImage) Image.load(selectedBmp.getAbsolutePath());
            JpegOptions options = new JpegOptions();
            options.setCompressionType(JpegCompressionMode.Lossless);
            image.save(ostream, options);
            
//            BufferedImage selImg = ImageIO.read(selectedBmp);
//            ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            param.setCompressionMode(param.MODE_EXPLICIT);
//            param.setCompressionType("JPEG-LOSSLESS");
//            writer.setOutput(ImageIO.createImageOutputStream(ofile));
//            writer.write(null, new IIOImage(selImg, null, null), param);
        } catch (Exception e) {
            System.out.println(" > Unhandled IO Exception - Jpeg Lossless");
        } 
        return ofile.length();
    }
}
