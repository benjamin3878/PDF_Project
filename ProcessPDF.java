package pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdfviewer.PDFPagePanel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;


public class ProcessPDF {

	
	/**********************************************************************************
	 * This method extracts the text from a PDF. The results are output as an array.
	 * THe text from each page of the PDF will be stored in
	 * @param pdf
	 * @return
	 *********************************************************************************/
	public static String[] getTexted(File pdf){
		if(pdf == null){
			return null;
		}
		
		PDFParser parser = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		PDFTextStripper pdfStripper;
		int numPages;
		String parsedText; 
		String[] result = null;
		
		try {
			parser = new PDFParser(new FileInputStream(pdf));
			parser.parse();
			cosDoc = parser.getDocument();
			pdDoc = new PDDocument(cosDoc);
			numPages = pdDoc.getNumberOfPages();
			result = new String[numPages];
		
			for(int i = 0; i < numPages; i++){
				
				pdfStripper = new PDFTextStripper();
	    		pdfStripper.setStartPage(i);
	    		pdfStripper.setEndPage(i);
	    		parsedText = pdfStripper.getText(pdDoc);
	    		result[i] = parsedText;
			}
			parser.clearResources();
			cosDoc.close();
			pdDoc.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("error: Opening file");
		}
		
		return result;
	}
	
	/*********************************************************************************
	 * This method searches a list of strings and output the String which contain the
	 * argument toBeFound within a given string. True for case sensitive and false 
	 * otherwise.
	 * The results are output in an ArrayList
	 * @param input
	 * @param toBeFound
	 * @return
	 *********************************************************************************/
	public static LinkedList<Pair<Integer, String>> getSubSet(String[] input, 
			String toBeFound){
		
		String[] subString;
		LinkedList<Pair<Integer, String>> result = 
				new LinkedList<Pair<Integer, String>>(); 
		
		for(int i = 0; i < input.length; i++){
			subString = input[i].split(" ");
			for(int j = 0; j < subString.length; j++){
				subString[j] = subString[j].replace(",", "");
				if(subString[j].contains(toBeFound)){
					result.add(new Pair<Integer, String>(i, subString[j]));
				}
			}
		}
		
		return result;
	}
	

	/**
	 *               
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public static PDDocument createPDF(String dir, String fileName){
		if(dir == null || fileName == null){
			return null;
		}
		PDDocument pdDoc = null;
		String line = null;
		PDFont font = PDType1Font.HELVETICA_BOLD;
		PDPageContentStream cStream;
		PDPage pdPage;
		
		int i = 0, fontSize = 12;
		try {
			FileReader text = new FileReader(dir + "\\" + fileName);
			BufferedReader bufText = new BufferedReader(text);
			pdDoc = new PDDocument();
			pdPage = new PDPage();
			pdDoc.addPage(pdPage);
			cStream = new PDPageContentStream(pdDoc, pdPage);
			cStream.setFont(font, fontSize);
			
			/*read each line of file, and add to pdf*/
			while((line = bufText.readLine()) != null){
				/*if the line exceeds the current pdf page, a new page is created
				 * and the index for positioning the text is reset*/
//				System.out.println(line);
				if(740 -(i * 12) < 100){
					i = 0;
					cStream.close();
					pdDoc.addPage(pdPage);
					pdPage = new PDPage();
					cStream = new PDPageContentStream(pdDoc, pdPage);
					cStream.beginText();
					cStream.setFont(font, fontSize);
				}else{
					cStream.beginText();
				}
				/*move text position and add it to pdf*/
				cStream.moveTextPositionByAmount(50,  740 - (i * 12));
				cStream.drawString(line);
				cStream.endText();
				i++;
			}
			
			cStream.close();
			bufText.close();
//			pdDoc.addPage(pdPage);
		} catch (FileNotFoundException e) {
			/*unable to find File*/
			infoBox("Unable to find file: " + fileName, dir);
			pdDoc = null;
		} catch (IOException r){
			pdDoc = null;
		}
		
		return pdDoc;
	}
	
	private static void infoBox(String infoMessage, String location){
        JOptionPane.showMessageDialog(null, infoMessage, "@ location: " + location, JOptionPane.INFORMATION_MESSAGE);
    }
	
	public static PDDocument insertPage(LinkedList<Pair<Integer, PDDocument>> list, 
			File orgFile){
		System.out.println("in create!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		PDDocument og = null, newPDF = null;
		List<PDPage> listOfPages, curList;
		Pair<Integer, PDDocument> current;
		if(list == null || orgFile == null){
			return null;
		}
		
		try {
			og = PDDocument.load(orgFile);
			listOfPages = og.getDocumentCatalog().getAllPages();
			newPDF = new PDDocument();
			current = list.removeFirst();
			
			//outer loop loops through the original PDF doc
			for(int i = 0; i < listOfPages.size(); i++){
//				newPDF.addPage(listOfPages.get(i));
				//while loop adds new pages if its index matches the index of the
				//outer loop
				while(current != null && current.getValue() == i && current.getElement() != null){
					curList = current.getElement().getDocumentCatalog().getAllPages();
					//if the new pages are more than one, add all of them
					for(int j = 0; j < curList.size(); j++){
						newPDF.addPage(curList.get(j));
					}
					try{
						current = list.removeFirst();
					}catch (NoSuchElementException e){
						current = null;
					}
					
				}
				newPDF.addPage(listOfPages.get(i));
			}
//			newPDF.close();
//			og.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newPDF;
	}
	
	/**
	 * 
	 * @param pdfDoc
	 * @return
	 */
	public static JPanel createPanelWithAllPages(PDDocument pdfDoc) {
	    JPanel docPanel = new JPanel();
	    docPanel.setLayout(new BoxLayout(docPanel, BoxLayout.Y_AXIS));
	    List<PDPage> docPages = pdfDoc.getDocumentCatalog().getAllPages();

	    for (PDPage page : docPages) {
	        PDFPagePanel pagePanel;
			try {
				pagePanel = new PDFPagePanel();
				pagePanel.setPage(page);
		        docPanel.add(pagePanel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }

	    return docPanel;
	}
	

}
