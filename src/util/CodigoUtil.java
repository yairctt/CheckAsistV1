package util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.nio.file.Path;

public class CodigoUtil {

    public static void generarCodigoQR(String data, String path) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300);
        Path archivo = new File(path).toPath();
        MatrixToImageWriter.writeToPath(matrix, "PNG", archivo);
    }

    public static void generarCodigoBarras(String data, String path) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 400, 150);
        Path archivo = new File(path).toPath();
        MatrixToImageWriter.writeToPath(matrix, "PNG", archivo);
    }
}

