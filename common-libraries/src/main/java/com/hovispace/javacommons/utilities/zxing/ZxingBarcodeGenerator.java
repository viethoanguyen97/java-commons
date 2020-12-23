package com.hovispace.javacommons.utilities.zxing;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

import static com.google.zxing.BarcodeFormat.*;
import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

public class ZxingBarcodeGenerator {

    public static BufferedImage generateUPCABarcodeImage(String barcodeText) throws Exception {
        UPCAWriter barcodeWriter = new UPCAWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, UPC_A, 300, 150);
        return toBufferedImage(bitMatrix);
    }

    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, EAN_13, 300, 150);
        return toBufferedImage(bitMatrix);
    }

    public static BufferedImage generateCode128BarcodeImage(String barcodeText) throws Exception {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, CODE_128, 300, 150);
        return toBufferedImage(bitMatrix);
    }

    public static BufferedImage generatePDF417BarcodeImage(String barcodeText) throws Exception {
        PDF417Writer barcodeWriter = new PDF417Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, PDF_417, 700, 700);
        return toBufferedImage(bitMatrix);
    }

    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, QR_CODE, 200, 200);
        return toBufferedImage(bitMatrix);
    }
}
