package AutoReport;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
//iText API
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class reportGenPDF {
	static String IMAGE_PATH = "/Users/Abraxas/Documents/drJeong/img/";
	static String PY_PATH = "/Users/Abraxas/Documents/drJeong/clean.py";
	static String CSV_NAME = "processed.csv";
	static String REPORT_COLLECTION = "/Users/Abraxas/Documents/drJeong/collection/";
	
	private static void Line(Graphics2D g, Double[] x, int xLength, int yLength, int xPos, int yPos) {
	  int midX = (xLength / 2) + xPos; int midY = (yLength / 2) + yPos; int length = 210; int angularDiff = 149;
	  //eight coordinate with respect to the image size, starting from vertical upward, going clockwise.
	  int[] coordinateSet = {midX, midY - length, midX + angularDiff, midY - angularDiff,midX + length, 
			  				midY,midX + angularDiff, midY + angularDiff,midX, midY + length,midX - angularDiff, 
			  				midY + angularDiff,midX - length, midY,midX - angularDiff, midY - angularDiff};
	  //I will draw the lines that propagate from the center, saving the coordinates in save array, and draw
	  //the lines that connect the edges later to complete the edges - let Duke know if this confuses you
	  int[] save = new int[16]; int i = 0; int c = 0; int d = 0;
	      g.setStroke(new BasicStroke(2));
	  while (i < 8) {
		  c = coordinateSet[2*i]; d = coordinateSet[2*i + 1];
		  double coX = midX + (c - midX) * (x[i] / 5.0); double coY = midY + (d - midY) * (x[i] / 5.0);
		  g.setColor(Color.ORANGE);
		  g.drawLine(midX, midY, (int) coX, (int) coY);
		  //saving the coordinates of the ends of the 8 lines, to draw the connecting edges later
	  save[2*i] = (int) coX; save[2*i + 1] = (int) coY;			  
	  //draw the double string
	  String scoreString = Double.toString(x[i]);
	  g.setColor(Color.BLACK);
	  //extend the coordinates by 2 percent to draw the scoreStrings
		  coX = midX + (c - midX) * (0.1 + (x[i] / 5.0)); coY = midY + (d - midY) * (0.1 + (x[i] / 5.0));
		  g.drawString(scoreString, (int) coX - 7, (int) coY + 3);
		  i += 1;
	  }
	  i = 0;
	  g.setColor(Color.ORANGE);
	  while (i < 13) {
		  //drawing connecting edges
		  g.fillRoundRect(save[i] - 4, save[i + 1] - 3, 7, 7, 7, 7);
		  g.drawLine(save[i], save[i + 1], save[i + 2], save[i + 3]);
		  i += 2;
	  }
	  //last connecting line between northwest and vertical up end
		  g.fillRoundRect(save[14] - 4, save[15] - 3, 7, 7, 7, 7);
		  g.drawLine(save[14], save[15], save[0], save[1]);
	  }
	  
	public static void makePDF(String name, Double[] scores, String namePDF) throws IOException, DocumentException {
		// Create a new PDF document with a width of 1275 and a height of 1650
		BufferedImage template;BufferedImage one; BufferedImage two; BufferedImage three;
		BufferedImage four; BufferedImage strength1; BufferedImage strength2; 
		BufferedImage strength3; BufferedImage strength4;
		//draw the background
		template = ImageIO.read(new File(IMAGE_PATH+"template.jpg"));
		one = ImageIO.read(new File(IMAGE_PATH+"one.png"));
		two = ImageIO.read(new File(IMAGE_PATH+"two.png"));
		three = ImageIO.read(new File(IMAGE_PATH+"three.png"));
		four = ImageIO.read(new File(IMAGE_PATH+"four.png"));
		//let's make use of reportGist object
		reportGist rg = new reportGist(name, scores);
		String[] imgs = rg.topFourImages();
		strength1 = ImageIO.read(new File(IMAGE_PATH+imgs[0]));
		strength2 = ImageIO.read(new File(IMAGE_PATH+imgs[1]));
		strength3 = ImageIO.read(new File(IMAGE_PATH+imgs[2]));
		strength4 = ImageIO.read(new File(IMAGE_PATH+imgs[3]));
		
		
		Document document = new Document(PageSize.A4, 0, 0, 0, 0);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(REPORT_COLLECTION + namePDF+".pdf"));
		writer.setCompressionLevel(0);
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		float width = PageSize.A4.getWidth();
		float height = PageSize.A4.getHeight() / 2;
		Graphics2D g2 = new PdfGraphics2D(cb, height, width, new DefaultFontMapper());			
			
		//All images are called. We now place them on the right place in the template
		g2.drawImage(template, 10, 10, null);
		
		//write the name and top strength
		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		g2.drawString(name, 500, 176);
		String[] str = rg.topFourNames();
		String topStrengths = str[0] + ", " + str[1] + ", " + str[2] + ", " + str[3];
		g2.drawString(topStrengths, 500, 205);
		
		//draw top 4 candidate strengths
		g2.drawImage(one, 223, 408, null);
		g2.drawImage(strength1, 333, 410, null);
		g2.drawImage(two, 223, 447, null);
		g2.drawImage(strength2, 333, 447, null);
		g2.drawImage(three, 223, 486, null);
		g2.drawImage(strength3, 333, 484, null);
		g2.drawImage(four, 223, 525, null);
		g2.drawImage(strength4, 333, 525, null);
		//draw the graph
		BufferedImage circle = ImageIO.read(new File(IMAGE_PATH + "circle.png"));
		int xLength = circle.getWidth(null); int yLength = circle.getHeight(null); 
		g2.drawImage(circle, 200, 635, null);
		//Line function draws the graph on the image
		Line(g2, scores, xLength, yLength, 200, 635);
		// Write the PDF output to a file
		g2.dispose();
		document.close();
		
	}
	public static void main(String[] args) throws IOException, InterruptedException, DocumentException {

		ProcessBuilder pb = new ProcessBuilder("python",PY_PATH+"clean.py");
		Process p = pb.start();
		 
		// -define .csv file in app
        String fileNameDefined = PY_PATH + CSV_NAME;
        // -File class needed to turn stringName to actual file
        File file = new File(fileNameDefined);
        try{
            // -read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);
            //throw away the column name row
            inputStream.nextLine();
            // hashNext() loops line-by-line
            while(inputStream.hasNextLine()){
            	//read single line, put in string
                String thisLine = inputStream.nextLine();
            	String[] tokens = thisLine.split(",");
            	String namePDF = tokens[0].replace(" ", "_");
            	String name = tokens[0];
            	Double[] score = new Double[8];
            	for (int i = 1; i < 9;i++) {
            		score[i-1] = Double.parseDouble(tokens[i]);
            	}
            	makePDF(name, score, namePDF);
            	inputStream.close();
		}
		}catch (FileNotFoundException e){
            e.printStackTrace();
        }
	}
}

