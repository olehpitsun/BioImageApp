package sample.controllers;

/**
 * Created by Rainbow-MRX on 11.02.2017.
 */

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import sample.libs.Hash;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.Timer;


public class PreviewPdf implements Initializable{
    @FXML
    private ImageView prev_img;
    @FXML
    private JFXButton exp_pdf;
    @FXML
    public void exp() {
        try {
            FileOutputStream fos = new FileOutputStream(Hash.hash(Hash.generateSalt(12), Hash.generateSalt(24)) + ".pdf");
            fos.write(PatientsController.BUFFER);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ByteBuffer buf = ByteBuffer.wrap(PatientsController.BUFFER);
        try {
            PDFFile pdfFile = new PDFFile(buf);
            PDFPage page = pdfFile.getPage(1);
            java.awt.Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
            java.awt.Image img = page.getImage(rect.width, rect.height,
                    rect,
                    null,
                    true,
                    true);

            prev_img.setImage(createFxImage(img));
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }



    public static javafx.scene.image.Image createFxImage(java.awt.Image image) throws IOException {
        if (!(image instanceof RenderedImage)) {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = bufferedImage;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) image, "png", out);
        out.flush();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new javafx.scene.image.Image(in);
    }

}
