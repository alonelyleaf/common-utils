package com.alonelyleaf.spring.javaio;

import com.spire.doc.Document;
import com.spire.doc.documents.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * spire 为付费功能，需要购买license
 *
 * @author bijl
 * @date 2020/5/15
 */
public class FileConvertUtils {

    public static void docxPageToImage() throws IOException {
        //create a Document object
        Document doc = new Document();

        //load a Word file
        doc.loadFromFile("F:\\fileconvert\\doctopdf\\录播服务器-录播管理服务设计文档(v1.6.0).docx");

        //save the first page to a BufferedImage
        BufferedImage image = doc.saveToImages(0, ImageType.Bitmap);

        //write the image data to a .png file
        File file = new File("F:\\fileconvert\\doctopdf\\output\\ToPNG.png");

        ImageIO.write(image, "PNG", file);

    }

    public static void docxToImage() throws IOException {

        //create a Document object
        Document doc = new Document();

        //load a Word file
        doc.loadFromFile("F:\\fileconvert\\doctopdf\\录播服务器-录播管理服务设计文档(v1.6.0).docx");

        //loop through the pages
        for (int i = 0; i < doc.getPageCount(); i++) {

            //save the specific page to a BufferedImage
            BufferedImage image = doc.saveToImages(i, ImageType.Bitmap);

            //write the image data to a .png file
            File file = new File("F:\\fileconvert\\doctopdf\\" + String.format(("Img-%d.png"), i));

            ImageIO.write(image, "PNG", file);

        }

    }

    public static void main(String[] args) {

        try {
            docxPageToImage();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
