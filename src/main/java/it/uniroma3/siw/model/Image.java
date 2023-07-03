package it.uniroma3.siw.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public class Image {
    private byte[] imageData;
    private String fileName;

    public Image(String fileName) {
        this.fileName = fileName;
        loadImage();
    }

    private void loadImage() {
        try {
            Path imagePath = Path.of("images", fileName);
            imageData = Files.readAllBytes(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        try {
            BufferedImage image = ImageIO.read(getImageFile());
            return image.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getHeight() {
        try {
            BufferedImage image = ImageIO.read(getImageFile());
            return image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void save(String outputFilePath) {
        String[] allowedFormats = ImageIO.getWriterFormatNames();

        if (outputFilePath.toLowerCase().endsWith(".jpg")) {
            try {
                File outputFile = new File(outputFilePath);
                Files.write(outputFile.toPath(), imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Il formato del file di output non è supportato. Utilizzare solo l'estensione .jpg");
        }
    }

    private File getImageFile() {
        return new File("images", fileName);
    }

}
