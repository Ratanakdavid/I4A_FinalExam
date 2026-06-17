package com.example.terrain_rentals.service;

import com.example.terrain_rentals.model.Profile;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Service
public class PdfService {

    public void generateProfilePdf(Profile profile, OutputStream os) throws Exception {
        PdfWriter writer = new PdfWriter(os);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("ID Card Information").setFontSize(18));
        document.add(new Paragraph("Name: " + profile.getFullName()));
        document.add(new Paragraph("Reg No: " + profile.getRegistrationNumber()));

        // QR Code
        var matrix = new MultiFormatWriter().encode(profile.getRegistrationNumber(), BarcodeFormat.QR_CODE, 150, 150);
        ByteArrayOutputStream qrStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", qrStream);
        document.add(new Image(ImageDataFactory.create(qrStream.toByteArray())));

        document.close();
    }
}