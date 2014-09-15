package pdf;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

public class Test_driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PDDocument doc = ProcessPDF.createPDF("C:\\Users\\Ben\\Desktop\\216\\lecture sides", "tester.c");
	
		try {
			doc.save("C:\\Users\\Ben\\Desktop\\test_create.pdf");
		} catch (COSVisitorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
