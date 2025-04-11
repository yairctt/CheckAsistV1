package util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.github.sarxos.webcam.Webcam;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QRScanner {

    public static String scanQRCode() {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            System.out.println("No se detectó ninguna webcam");
            return null;
        }

        try {
            webcam.setViewSize(new Dimension(640, 480)); // Resolución más alta
            webcam.open();
            QRCodeReader reader = new QRCodeReader();

            // Bucle para intentar leer el QR varias veces
            for (int i = 0; i < 30; i++) { // Intenta durante 30 iteraciones (ajustable)
                BufferedImage image = webcam.getImage();
                if (image == null) {
                    System.out.println("No se pudo obtener la imagen de la webcam");
                    continue;
                }

                BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = reader.decode(bitmap);
                    System.out.println("QR detectado: " + result.getText());
                    return result.getText();
                } catch (NotFoundException e) {
                    System.out.println("Intento " + (i + 1) + ": No se encontró código QR");
                    Thread.sleep(500); // Espera 500ms antes del próximo intento
                } catch (ChecksumException e) {
                    System.out.println("Error en la suma de verificación del QR");
                    return null;
                } catch (FormatException e) {
                    System.out.println("Error en el formato del QR");
                    return null;
                }
            }
            System.out.println("No se detectó QR después de varios intentos");
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            webcam.close();
        }
    }

    public static void main(String[] args) {
        String result = scanQRCode();
        if (result != null) {
            System.out.println("Contenido del QR: " + result);
        } else {
            System.out.println("No se pudo leer el QR");
        }
    }
}