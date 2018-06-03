package com.cheuks.bin.original.anything.test.x.to.html.poi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class T1 {

	public static void main(String[] args) throws Exception {
		// new T1().docToHtml();
		// new T1().xlsToHtml();
		// new T1().doPPTtoImage();
		// new T1().doPPTXtoImage();
		// new T1().docxToHtml();
		// new POIExcelToHtml().excelToHtml("D:/Desktop/16线程总结报告.xls.xls",
		// "D:/Desktop/1-doc-image/excel1.html", true);
		// new POIExcelToHtml().excelToHtml("D:/Desktop/副本 压测接口.xlsx",
		// "D:/Desktop/1-doc-image/excel1.html", true);
		StringBuilder sb = new StringBuilder();
		String encode = "utf-8";
		sb.append(
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"")
				.append(null == encode ? "UTF-8" : encode.toUpperCase()).append("\"></head><body>");
		System.out.println(sb.toString());
	}

	// doc转换为html
	@SuppressWarnings("restriction")
	void docToHtml() throws Exception {
		String sourceFileName = "D:/Desktop/1.doc";
		String targetFileName = "D:/Desktop/1-doc-image/1-doc.html";
		String imagePathStr = "D:/Desktop/1-doc-image/image/";
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
		// 保存图片，并返回图片的相对路径
		wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
			try (FileOutputStream out = new FileOutputStream(imagePathStr + name)) {
				out.write(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "image/" + name;
		});
		wordToHtmlConverter.processDocument(wordDocument);
		Attr attr = wordToHtmlConverter.getDocument().createAttribute("onLoad");
		attr.setValue("alert('123');");

		// wordToHtmlConverter.getDocument().getElementsByTagName("body").item(0).appendChild(attr);

		NodeList nodeList = wordToHtmlConverter.getDocument().getElementsByTagName("html");
		Node node = nodeList.item(0);
		org.w3c.dom.Element script = wordToHtmlConverter.getDocument().createElement("script");
		script.setAttribute("src", "pattern.js");
		script.setAttribute("type", "text/javascript");
		node.appendChild(script);

		Document htmlDocument = wordToHtmlConverter.getDocument();
		DOMSource domSource = new DOMSource(htmlDocument);

		// NodeList nodes =
		// domSource.getNode().getChildNodes().item(0).getChildNodes();
		// for (int i = 0, len = nodes.getLength(); i < len; i++) {
		// System.out.println(nodes.item(i));
		// }
		// ElementImpl node1 = (ElementImpl)nodes.item(0);
		// System.err.println(nodes.item(1).getClass().getName());
		//// node1.setTextContent("aaaaaaaaaaaaaaaaaaaaaaaa");
		// org.w3c.dom.Element
		// script=wordToHtmlConverter.getDocument().createElement("script");
		// script.setAttribute("src", "pattern.js");
		// script.setAttribute("type", "text/javascript");
		//// node1.appendChild()
		//// node1.setAttribute("onLoad", "alert('123');");
		// node1.appendChild(script);
		//
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult(result);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);

	}

	// docx转换为html
	public void docxToHtml() throws Exception {
		String filepath = "D:/Desktop/1-doc-image/";
		String sourceFileName = filepath + "MCA-初步压力测试报告.docx";
		String targetFileName = filepath + "2-doc.html";
		String imagePathStr = filepath + "image/";
		OutputStreamWriter outputStreamWriter = null;
		try {
			XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
			XHTMLOptions options = XHTMLOptions.create();
			// 存放图片的文件夹
			options.setExtractor(new FileImageExtractor(new File(imagePathStr)));
			// html中图片的路径
			options.URIResolver(new IURIResolver() {
				private String baseUrl = "image/";

				@Override
				public String resolve(String uri) {
					// 改为上传的地址
					return this.baseUrl + uri.substring(uri.lastIndexOf("/") + 1);
				}
			});
			outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
			XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
			xhtmlConverter.convert(document, outputStreamWriter, options);
		} finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
		}
	}

	void xlsToHtml() throws Exception {
		String path = "D:/Desktop/1-doc-image/";
		String file = "10线程总结报告.xls";
		FileInputStream input = new FileInputStream(path + file);
		HSSFWorkbook excelBook = new HSSFWorkbook(input);
		ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		excelToHtmlConverter.processWorkbook(excelBook);
		List pics = excelBook.getAllPictures();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				HSSFPictureData pic = (HSSFPictureData) pics.get(i);
				try {
					// FileWriter writer = new FileWriter(new File(path +
					// pic.suggestFileExtension()));
					OutputStream writer = new FileOutputStream(new File(path + pic.suggestFileExtension()));
					writer.write(pic.getData());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Document htmlDocument = excelToHtmlConverter.getDocument();
		DOMSource domSource = new DOMSource(htmlDocument);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult(new File(path, "exportExcel.html"));

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		outStream.close();

		// String content = new String(outStream.toByteArray());
		//
		// FileUtils.writeStringToFile(new File(path, "exportExcel.html"),
		// content, "utf-8");

		// @Deprecated
		// String saveXlsToHtml(InputStream in, Storage storage, String name)
		// throws Exception {
		// // throw new Exception("not implement.");
		//
		// ExcelToHtmlConverter excelToHtmlConverter = new
		// ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		// excelToHtmlConverter.setOutputColumnHeaders(false);
		// excelToHtmlConverter.setOutputRowNumbers(false);
		//
		// // 创建POI工作薄对象
		// Workbook workbook = WorkbookFactory.create(in);
		// if (workbook instanceof XSSFWorkbook) {
		// XSSFWorkbook xWb = (XSSFWorkbook) workbook;
		// // htmlExcel = POIReadExcelToHtml.getExcelInfo(xWb, true);
		// } else if (workbook instanceof HSSFWorkbook) {
		// HSSFWorkbook hWb = (HSSFWorkbook) workbook;
		// // htmlExcel = POIReadExcelToHtml.getExcelInfo(hWb, true);
		// }
		// HSSFWorkbook hssfWorkbook = (HSSFWorkbook) workbook;
		// excelToHtmlConverter.processWorkbook(hssfWorkbook);
		//
		// Document htmlDocument = excelToHtmlConverter.getDocument();
		// ByteArrayOutputStream out = new ByteArrayOutputStream();
		//
		// DOMSource domSource = new DOMSource(htmlDocument);
		//
		// StreamResult streamResult = new StreamResult(out);
		//
		// TransformerFactory tf = TransformerFactory.newInstance();
		// Transformer serializer = tf.newTransformer();
		// serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		// serializer.setOutputProperty(OutputKeys.METHOD, "html");
		// serializer.transform(domSource, streamResult);
		//
		// return null;
		//
		// }

	}

	public void doPPTtoImage() {
		String filePath = "D:/Desktop/1-doc-image/";
		String file = "附件1：新项目交楼评估报告（模板）.ppt";
		try {

			FileInputStream is = new FileInputStream(filePath + file);
			HSLFSlideShow ppt = new HSLFSlideShow(is);
			is.close();

			Dimension pgsize = ppt.getPageSize();

			int idx = 1;
			FileOutputStream out;
			FileOutputStream html = new FileOutputStream(filePath + "ppt.html");
			html.write("<html><body>".getBytes("utf-8"));
			String pic;
			for (HSLFSlide slide : ppt.getSlides()) {// XSLFSheet

				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				// clear the drawing area
				graphics.setPaint(Color.WHITE);

				// graphics.setPaint();
				graphics.setBackground(slide.getBackground().getFill().getBackgroundColor());
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

				// render
				slide.draw(graphics);

				// save the output
				out = new FileOutputStream(pic = (filePath + "html/" + idx + ".png"));
				javax.imageio.ImageIO.write(img, "png", out);
				html.write(("<p><img src=\"" + pic + "\" alt=\"\" width=\"" + pgsize.width + "\" height=\""
						+ pgsize.height + "\"/></p>").getBytes("utf-8"));
				out.close();
				idx++;
			}
			html.write("</body></html>".getBytes("utf-8"));
			html.close();
			System.out.println("success!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPPTXtoImage() {
		String filePath = "D:/Desktop/1-doc-image/";
		String file = "附件1：新项目交楼评估报告（模板）.ppt";
		try {

			FileInputStream is = new FileInputStream(
					"D:/工作清单/项目经理运营工作清单/项目经理运营工作清单支持文件/案场/08收楼准备/3收楼前15天/2物业查验与移交/支持文件/关于提交新项目交楼评估报告的通知/附件1：新项目交楼评估报告（模板）.pptx");
			XMLSlideShow ppt = new XMLSlideShow(is);
			is.close();

			Dimension pgsize = ppt.getPageSize();

			int idx = 1;
			FileOutputStream out;
			FileOutputStream html = new FileOutputStream(filePath + "pptx.html");
			html.write("<html><body>".getBytes("utf-8"));
			String pic;
			for (XSLFSlide slide : ppt.getSlides()) {// XSLFSheet

				BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				// clear the drawing area
				graphics.setPaint(Color.WHITE);

				// graphics.setPaint();
				graphics.setBackground(slide.getBackground().getFillColor());
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

				// render
				slide.draw(graphics);

				// save the output
				out = new FileOutputStream(pic = (filePath + "html2/" + idx + ".png"));
				javax.imageio.ImageIO.write(img, "png", out);
				html.write(("<p><img src=\"" + pic + "\" alt=\"\" width=\"" + pgsize.width + "\" height=\""
						+ pgsize.height + "\"/></p>").getBytes("utf-8"));
				out.close();
				idx++;
			}
			html.write("</body></html>".getBytes("utf-8"));
			html.close();
			System.out.println("success!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
