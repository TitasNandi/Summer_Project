package elsevier;
import static elsevier.pdf_To_word.pdftoText;
import java.io.FileInputStream;
import java.io.IOException;

public class Elsevier
{
    String root_folder = "/mnt/Titas/1_QA_MODEL/Documents";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception
    {
        try
        {
            String input_path = "/mnt/Titas/1_QA_MODEL/Documents/41222.pdf";
            pdftoText(input_path);
        }
        catch(IOException e){
        // TODO code application logic here
        }
    }
}
