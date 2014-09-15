package pdf;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;

import pdf.*;

import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JProgressBar;
import javax.swing.JInternalFrame;
import javax.swing.JList;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.Dimension;

public class GUI_PDF extends JFrame {
	
	/*GUI variables*/
	private JTextPane textIntsru = null;
	private JButton start;
	private JPanel contentPane;
	private JTextField textField;
	
	/*Processing variables*/
	private File file;
	private LinkedList<Pair<Integer, String>> list;
	private LinkedList<Pair<Integer, PDDocument>> pdDoc = 
			new LinkedList<Pair<Integer, PDDocument>>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_PDF frame = new GUI_PDF();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_PDF() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		
		/*
		 * File - open
		 */
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result;
				JFileChooser openFile = new JFileChooser();
				if((result = openFile.showOpenDialog(null)) == JFileChooser.APPROVE_OPTION){
                	file = openFile.getSelectedFile();
                	textIntsru.setText("Please enter the suffix file \r\nextension to be found.");
					textField.setVisible(true);
					start.setVisible(true);
                }else if(result == JFileChooser.ERROR_OPTION){
                	/*process error*/
                	System.out.println("error opening");
                	return;
                }else if(result == JFileChooser.CANCEL_OPTION){
                	System.out.println("Canel");
                	return;
                }
			}
			
		});
		
		/*
		 * File - save
		 */
		mnFile.add(mntmOpen);
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser saveFile = new JFileChooser();
                saveFile.showSaveDialog(null);
			}
		});
		
		mnFile.add(mntmSave);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		textIntsru = new JTextPane();
		textIntsru.setEditable(false);
		contentPane.add(textIntsru);
		
		textField = new JTextField();
		textField.setVisible(false);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.setText(".c");
		
		/*
		 * perform PDF action
		 */
		start = new JButton("Start");
		start.setVisible(false);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] textFromPDF = ProcessPDF.getTexted(file);
				String text = textField.getText(), dir = file.getParent();
				list = ProcessPDF.getSubSet(textFromPDF, text);
				PDDocument processedPDF;
				Integer i = 1;
				for(Pair<Integer, String> s: list){
					pdDoc.add(new Pair<Integer, PDDocument>(s.getValue(), 
							ProcessPDF.createPDF(dir, s.getElement())));
				}
//				for(Pair<Integer, PDDocument> p: pdDoc){
//					try {
//						if(p != null && p.getElement() != null){
//							p.getElement().save("C:\\Users\\Ben\\Desktop\\Hello World_" + i.toString() + ".pdf");
//						}
//						i++;
//					} catch (COSVisitorException | IOException e) {
//						// TODO Auto-generated catch block
//						//e.printStackTrace();
//						System.out.println("error");
//					}
//				}
				
				
				processedPDF = ProcessPDF.insertPage(pdDoc, file);
//				JPanel pan = ProcessPDF.createPanelWithAllPages(processedPDF);
//				JFrame fram = new JFrame();
//				DisplayPDF genPDF = new DisplayPDF(ProcessPDF.createPanelWithAllPages(processedPDF));
				
				
//				try {
//					processedPDF.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
				try {
					processedPDF.save("C:\\Users\\Ben\\Desktop\\Hello World.pdf");
				} catch (COSVisitorException | IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("save Failed");
				}
			}
		});
		contentPane.add(start);
	}

}
