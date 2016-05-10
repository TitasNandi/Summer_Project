package elsevier;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;                                                       
import org.apache.pdfbox.util.PDFTextStripper;

public class pdf_To_word
{
    static int count = 0;
    static String root_folder = "/mnt/Titas/1_QA_MODEL/Documents";
    static void pdftoText(String fileName)throws IOException, Exception
    {
        PDFParser parser = null;
        String parsedText = null;
        PDFTextStripper pdfStripper;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = new File(fileName);
        if (!file.isFile()) {
            System.err.println("File " + fileName + " does not exist.");
            
        }
        try {
           // System.out.println("hello");
            parser = new PDFParser(new FileInputStream(file));
        } catch (IOException e) {
            System.err.println("Unable to open PDF Parser. " + e.getMessage());
           // return null;
        }
        try {
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (Exception e) {
            System.err.println("An exception occured in parsing the PDF Document." + e.getMessage());
        }
        finally {
            try {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        print_to_Text(parsedText);
    }
    static void print_to_Text(String s) throws IOException, Exception
    {
        try
        {
            String output_name = "file"+Integer.toString(count)+".txt";
            String path = root_folder+output_name;
            File file  = new File(path);
            if (!file.exists())
            {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            String dummy = s.toLowerCase();
            String pattern = "reference";
            Search obj = new Search();
            int index = obj.search(dummy,pattern );
            if(index == -1)
            {
                System.out.println("References not found");
                return;
            }
            String temp = s.substring(index,s.length());
            String ref =  "file"+Integer.toString(count)+"reference.txt";
            path = root_folder+ref;
            file = new File(path);
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(temp);
            System.out.println(temp);
            bw.close();
        }
        catch (IOException e) {}
    }
}
