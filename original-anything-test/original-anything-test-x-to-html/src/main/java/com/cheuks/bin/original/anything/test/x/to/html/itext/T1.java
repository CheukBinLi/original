package com.cheuks.bin.original.anything.test.x.to.html.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.FileOutputStream;

public class T1 {
	public static void main(String[] args) {
		try {

			Document document = new Document(PageSize.LETTER);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("d://testpdf.pdf"));
			document.open();
			document.addAuthor("test");
			document.addCreator("test");
			document.addSubject("test");
			document.addCreationDate();
			document.addTitle("XHTML to PDF");

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

//			worker.parseXHtml(pdfWriter, document, new FileInputStream(HTML), null, new XMLWorkerFontProvider());
			document.close();
			System.out.println("Done.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
