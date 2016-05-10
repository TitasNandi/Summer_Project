package elsevier;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class Xml_parser {
	static ArrayList<ArrayList<String>>   all_references=new ArrayList<>();
	public static void main(String[] args) {
	      try {
	    	 // String[] inputFiles = {"1-s2.0-S000437021500106X-main.txt","1-s2.0-S0004370215000776-main.txt","1-s2.0-S0004370215000818-main.txt","1-s2.0-S0004370215000867-main.txt","1-s2.0-S0004370215000879-main.txt","1-s2.0-S0004370215000880-main.txt","1-s2.0-S0004370215000909-main.txt","1-s2.0-S0004370215000922-main.txt","1-s2.0-S0004370215000995-main.txt","1-s2.0-S0004370215001034-main.txt","1-s2.0-S0004370215001046-main.txt","1-s2.0-S0004370215001058-main.txt","1-s2.0-S0004370215001071-main.txt","1-s2.0-S0004370215001162-main.txt","1-s2.0-S0004370215001198-main.txt","1-s2.0-S0004370215001204-main.txt","1-s2.0-S0004370215001216-main.txt","1-s2.0-S0004370215001332-main.txt","1-s2.0-S0004370215001344-main.txt","1-s2.0-S0004370215001393-main.txt","1-s2.0-S0004370215001447-main.txt"};
	         //for(int i=0; i<21; i++)
	         //{
	        	// System.out.println(inputFiles[i]);
	        	 double[] t = new double[3];
		    	 File inputFile = new File("/mnt/Titas/Elsevier/Training_new/Accepted_papers/accepted papers/1-s2.0-S0004370215000776-main.pdf.xml");
		    	 //File inputFile = new File("/mnt/Titas/Elsevier/Training_Set/XML_files/1-s2.0-S0004370215000776-main.txt");
		         SAXReader reader = new SAXReader();
		         Document document = reader.read( inputFile );
		         String m = "/mnt/Titas/Elsevier/Training_new/Accepted_papers/accepted papers/1-s2.0-S0004370215000776-main.pdf.xml";
		         All all = new All(m);
		         // iterate through child elements of root
		   //       Map uris = new HashMap();
				 // uris.put("pqr", "http://www.tei-c.org/ns/1.0");
				 // XPath xpath = document.createXPath("/pqr:TEI/pqr:teiHeader");
				 // xpath.setNamespaceURIs(uris);
				 // List<Node> nodes = xpath.selectNodes(document);
		   //       Element classElement = document.getRootElement();
		         System.out.println("--------------------------------------------------");
		         System.out.println("The abstract wordcount: "+ get_Abstract_WC(document));
		         System.out.println("--------------------------------------------------");
		         double n = (get_Abstract_WC(document)*1.0)/get_Rel_length(document);
		         System.out.println("--------------------------------------------------");
		         System.out.println("Abstract Relative length: "+ n);
		         System.out.println("--------------------------------------------------");
		         System.out.println("Check_Template: "+check_template(document));
		         System.out.println("--------------------------------------------------");
		         int z = get_Rel_length(document);
		         get_section_WC(document,z,t);
		         
		         total_references(document);
		         System.out.println("--------------------------------------------------");
		        // System.out.println("number of self citations are: "+number_of_self_citations(document));
		         System.out.println("--------------------------------------------------");
		         //all.Mathematical_Formula(m);
		         //all.Uncited_Reference(m);
		         //all.Table_Figures(m);
	      //   }
	         
//	        List<Node> nodes = document.selectNodes("TEI/teiHeader/profileDesc/abstract" );
//	         System.out.println(nodes.size());
//	         System.out.println("----------------------------");
//	         for (Node node : nodes) 
//	         {
//	            System.out.println("\nCurrent Element :" 
//	               + node.getName());
//	            //System.out.println("Student roll no : " 
//	             //  + node.valueOf("@rollno") );
//	            System.out.println("Data : " + node.selectSingleNode("p").getText());
//	            String l = node.selectSingleNode("p").getText();
//	            System.out.println(getNum(l));
//	         } 
//	          //  System.out.println("Last Name : " + node.selectSingleNode("lastname").getText());
//	           // System.out.println("First Name : " + node.selectSingleNode("nickname").getText());
//	           // System.out.println("Marks : " + node.selectSingleNode("marks").getText());
//	            nodes = document.selectNodes("TEI/text/body/div");
//	            System.out.println(nodes.size());
//	            for (int i=0; i<nodes.size(); i++) 
//	            {
//	            	System.out.println(nodes.get(i).selectSingleNode("head").getText());
//	            	List<Node> nodes_1 = nodes.get(i).selectNodes("p");
//	            	for (int j=0; j<nodes_1.size(); j++)
//	            	{
//	            		String q = nodes_1.get(j).getText();
//		            //	System.out.println(q);
//		            	System.out.println(getNum(q));
//	            	}	
//	            }
	         
	      } catch (DocumentException e) {
	         e.printStackTrace();
	      }
	   }
	public static int getNum(String text)
	{
		String trimmed = text.trim();
		int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		return words;
	}
	public static int get_Abstract_WC(Document document)
	{
		 List<Node> nodes = document.selectNodes("TEI/teiHeader/profileDesc/abstract" );
		 String l="";
		 for (Node node : nodes) 
         {
           // System.out.println("Data : " + node.selectSingleNode("p").getText());
            l = node.selectSingleNode("p").getText();
         }
		 return getNum(l);
	}
	public static void get_section_WC(Document document, int total, double[] t)
	{
		String[] s = {"introduction","(results)|(discussion)","conclusion(s)?"};
		double[] val = new double[3];
		List<Node> nodes = document.selectNodes("TEI/text/body/div");
		for(int i=0;i<3;i++)
		{
			int x=0;
			for(int j=0;j<nodes.size();j++)
			{
				String l = nodes.get(j).selectSingleNode("head").getText();
				String pattern = ".*\\b"+s[i]+"\\b.*";
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(l.toLowerCase().trim());
				if(m.find())
				{					
					List<Node> nodes_1 = nodes.get(j).selectNodes("p");
		        	for (int k=0; k<nodes_1.size(); k++)
		        	{
		        		String q = nodes_1.get(k).getText();
		            //	System.out.println(q);
		            	x += getNum(q);
		        	}
				}
			}
			val[i] = (x*(1.0))/total;
		}
		System.out.println("--------------------------------------------------");
		System.out.println("Introduction relative length: "+val[0]);
		System.out.println("--------------------------------------------------");
		System.out.println("Results relative length: "+val[1]);
		System.out.println("--------------------------------------------------");
		System.out.println("Conclusions relative length: "+val[2]);
		System.out.println("--------------------------------------------------");
		t[0] = val[0];
		t[1] = val[1];
		t[2] = val[2];
	}
	public static int get_Rel_length(Document document)
	{
		List<Node> nodes = document.selectNodes("TEI/text/body/div");
      //  System.out.println(nodes.size());
        int x=0;
        for (int i=0; i<nodes.size(); i++) 
        {
        	//System.out.println(nodes.get(i).selectSingleNode("head").getText());
        	List<Node> nodes_1 = nodes.get(i).selectNodes("p");
        	for (int j=0; j<nodes_1.size(); j++)
        	{
        		String q = nodes_1.get(j).getText();
            //	System.out.println(q);
            	x += getNum(q);
        	}	
        }
        return x;
	}
	public static boolean check_template(Document document)
	{
		String[] s = {"introduction","(results)|(discussion)","conclusion(s)?"};
		boolean[] b = {false, false, false};
		int k=0;
		int check=0;
		ArrayList<Double> ind = new ArrayList<>();
		ArrayList<Integer> freq = new ArrayList<>();
		List<Node> nodes = document.selectNodes("TEI/teiHeader/profileDesc/abstract");
		if(nodes.isEmpty())
		{
			return false;
		}
		for(int i=0;i<3;i++)
		{
			nodes = document.selectNodes("TEI/text/body/div");
			for(int j=0;j<nodes.size();j++)
			{
				String l = nodes.get(j).selectSingleNode("head").getText();
				String pattern = ".*\\b"+s[i]+"\\b.*";
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(l.toLowerCase().trim());
				//boolean contains = l.toLowerCase().trim().matches(".*\\b"+s[i]+"\\b.*");
				if(m.find())
				{
					b[i] = true;
					System.out.println(l);
					String arr[] = l.trim().split(" ", 2);
					String firstWord = arr[0];
					String str = firstWord.replaceAll("\\D+","");
					try
					{
						ind.add(Double.parseDouble(str));
						int count=0;
						for(k=0; k<firstWord.length();k++)
						{
							if(firstWord.charAt(k)=='.')
							{
								count++;
							}
						}
						freq.add(count);
					}
					catch (Exception e)
					{
						
					}
				}	
			}
		}
//		for(int i=0;i<ind.size(); i++)
//		{
//			System.out.print(ind.get(i)+" ");
//		}
//		for(int i=0;i<b.length; i++)
//		{
//			System.out.print(b[i]+" ");
//		}
		//System.out.println(check+" t");
		for(int i=0; i<3; i++)
		{
			if(b[i] == false)
			{
				return false;
			}
		}
		nodes = document.selectNodes("/TEI/text/back/div[@type='references']" );
		if(nodes.isEmpty())
		{
			return false;
		}
		boolean sorted = true;
		for (int i = 0; i < ind.size(); i++) {
		    if(freq.get(i) == 1)
		    {
		    	ind.set(i, ind.get(i)*100);
		    }
		    else if(freq.get(i) == 2)
		    {
		    	ind.set(i, ind.get(i)*10);
		    }
		}
		for (int i = 0; i < ind.size() - 1; i++) {
		    if (ind.get(i) > ind.get(i+1)) {
		    	//System.out.println("go"+i);
		        sorted = false;
		        break;
		    }
		}
		return sorted;
	}
	
//	public static int get_binary(double value, double threshold, String comp)
//	{
//		if(comp.equals(">="))
//		{
//			if(value >= threshold)
//			{
//				return 1;
//			}
//			else
//			{
//				return 0;
//			}
//		}
//		else if(comp.equals("<="))
//		{
//			if(value <= threshold)
//			{
//				return 0;
//			}
//			else
//			{
//				return 1;
//			}
//		}
//		return 0;
//	}
	
	public static int number_of_self_citations(Document doc)
    {
        List<Node> nodes = doc.selectNodes("/TEI/teiHeader/fileDesc/sourceDesc/biblStruct/analytic/author" );
        String authors_name[]=new String[nodes.size()];
        int k=0;
        for (Node node : nodes)
        {
            if(node.selectSingleNode("persName/forename[@type='first']")!=null)
            {
                authors_name[k]=node.selectSingleNode("persName/forename[@type='first']").getText()+" ";
            
            }
            if(node.selectSingleNode("persName/forename[@type='middle']")!=null)
            {
                authors_name[k]+=node.selectSingleNode("persName/forename[@type='middle']").getText()+" ";
           
            }
            if(node.selectSingleNode("persName/surname")!=null)
            {
                authors_name[k]+=node.selectSingleNode("persName/surname").getText();
            }
            k++;
        }
        int number_of_self_citations=0;
        int flag=1;
        System.out.println("The self cited authors");
        for(int i=0;i<all_references.size();++i)
        {
            for(int j=0;j<nodes.size();++j)
            {
                if(flag==1)
                {
                        for(int l=0;l<all_references.get(i).size();++l)
                     {
                             if(authors_name[j].equals(all_references.get(i).get(l)))
                         {
                             ++number_of_self_citations;
                             System.out.println(authors_name[j]);
                             flag=0;
                              break;

                         }
                             else 
                             {
                                 boolean a=false;
                                 int last_index1=authors_name[j].length()-1;
                                 int last_index2=all_references.get(i).get(l).length()-1;
                                 if(authors_name[j].charAt(0)==all_references.get(i).get(l).charAt(0))
                                    a=true;
                                 while(authors_name[j].charAt(last_index1)!=' ')
                                 {
                                     if(authors_name[j].charAt(last_index1)==all_references.get(i).get(l).charAt(last_index2))
                                     {
                                         --last_index1;
                                         --last_index2;
                                     }
                                     else
                                     {
                                         a=false;
                                         break;
                                     }
                                 }
                                
                               if(a)
                               {
                                 ++number_of_self_citations;
                                System.out.println(authors_name[j]);
                                flag=0;
                                break;
                               }
                             }
                     } 
                }
               else
                {
                    flag=1;
                    break;
                }
                
                
            }
        }
        
        return  number_of_self_citations ;
            
    }
	public static void total_references(Document document)
    {
         List<Node> nodes = document.selectNodes("/TEI/text/back/div[@type='references']/listBibl/biblStruct" );
     //    System.out.println(nodes.size());
         String code[] = new String[nodes.size()];
         int count = 0;
         for (Node node : nodes) {
       //     System.out.println("---------------------------- Reference " + (count+1) + " ----------------------");
            code[count] = "b"+Integer.toString(count);
       //     System.out.println(code[count]);
            if(node.selectSingleNode("analytic/title")!=null)
            {
         //      System.out.println("Title : " + node.selectSingleNode("analytic/title").getText());
            }
            else
            {
           //     System.out.println("Title : " + node.selectSingleNode("monogr/title").getText());
            }
              List<Node> authors = node.selectNodes("analytic/author");
         //     System.out.println("Authors:");
              String name;
              ArrayList<String> temp=new ArrayList<String>();
              for(Node auth : authors)
              {
                  name = "";
                  if(auth.selectSingleNode("persName/forename[@type='first']")!=null)
                      name = name + auth.selectSingleNode("persName/forename[@type='first']").getText() + " ";
                 if(auth.selectSingleNode("persName/forename[@type='middle']")!=null)
                    name = name + auth.selectSingleNode("persName/forename[@type='middle']").getText() + " ";
                  if(auth.selectSingleNode("persName/surname")!=null)
                      name = name + auth.selectSingleNode("persName/surname").getText();
           
           //       System.out.println(name);
                  temp.add(name);
                 
                 
              }
              all_references.add(temp);
              count++;
         }
         
    }
}

class All{
	public String file;
	public All(String File){
		file=File;
	}
	
	public void Mathematical_Formula(String s) {
		int i=1;
			try (BufferedReader br = new BufferedReader(new FileReader(s)))
			{

				String sCurrentLine;
				String line;
				
				int linecount=0;
				while ((sCurrentLine = br.readLine()) != null) {
					linecount++;
					
					int indexfound=sCurrentLine.indexOf("("+i+")"+"</formula>");
			        if (indexfound>-1)
			        	{
			        		//System.out.println("\n");
			        		//System.out.println("Formula number " + i + " was found at position::" +indexfound+ "::on line"+linecount);
			        		line=sCurrentLine;
			        		i++;	
			        		}
			       
				}
				System.out.println("Total No. of Mathematical Formulae :" + (i-1));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			if((i-1)>0){
				System.out.println("Mathematical Formulae : 1");
			}
			else
				System.out.println("Mathemmatical Formulae : 0");
			System.out.println("-------------------------------------------------------------");

		}
	
	public  void Uncited_Reference(String s) {
		int i=0;
		try (BufferedReader br = new BufferedReader(new FileReader(s)))
		{

			String sCurrentLine;
			String line;
			int linecount=0;
			while ((sCurrentLine = br.readLine()) != null) {
				linecount++;
				
				int indexfound=sCurrentLine.indexOf("xml:id="+"\""+"b"+ i +"\"");//xml:id="b27
		        if (indexfound>-1)
		        	{
		        		//System.out.println("\n");
		        		//System.out.println("Citations number " + i + " was found at refrence position::" +indexfound+ "::on line"+linecount);
		        		line=sCurrentLine;
		        		i++;	
		        		}
		       
			}
			 System.out.println("\n\n"+"Total number of references  :" + (i));
			 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		int flag=i;
		for(int j=0;j<i;j++){
		try (BufferedReader br = new BufferedReader(new FileReader(s)))
		{

			String sCurrentLine;
			String line;
			int linecount=0;
			int a=0;
			while ((sCurrentLine = br.readLine()) != null) {
				linecount++;
				int indexfound=sCurrentLine.indexOf("target=\"#b"+j+"\"");//xml:id="b27
		        if (indexfound>-1)
		        	{
		        		line=sCurrentLine;
		        		a++;	
		        		}
		       
			}
			// System.out.println("\n\n"+"Total number of occurences for b:" +j+" "+ a);
			if(a==0){
				System.out.println("Uncited Reference for b"+j);
				System.out.println("Uncited Reference : 0");
				flag=0;
				break;
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		}
		if(flag==i){
			System.out.println("Unvited reference : 1");
		}
		System.out.println("--------------------------------------------------------------");

	}

	public  void Table_Figures(String s) {
		Scanner scan=new Scanner(System.in);
		//System.out.println("Press 1 to see Table reference\nPress 2 to see Figure reference\nPress 3 to EXIT");
		int i=1;
		try (BufferedReader br = new BufferedReader(new FileReader(s)))
		{

			String sCurrentLine;
			String line;
			
			int linecount=0;
			while ((sCurrentLine = br.readLine()) != null) {
				linecount++;
				
				int indexfound=sCurrentLine.indexOf("Table "+i);
		        if (indexfound>-1)
		        	{
		        		System.out.println("\n");
		        	//	System.out.println("Table number " + i + " was found at position::" +indexfound+ "::on line"+linecount);
		        		line=sCurrentLine;
		        		i++;	
		        		}
		       
			}
			 System.out.println("\n\n"+"Total number of tables :" + (i-1));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		int count = 1;
		for (int j=1;j<i;j++){
		try (BufferedReader br = new BufferedReader(new FileReader(s)))
		{

			String sCurrentLine;
			String line;
			int a=0;
			int linecount=0;
			while ((sCurrentLine = br.readLine()) != null) {
				linecount++;
				
				int indexfound=sCurrentLine.indexOf("Table "+j);
		        if (indexfound>-1)
		        	{
		        		line=sCurrentLine;
		        		a++;	
		        		}
		       
			}
			 System.out.println("\n\n"+"Total number of occurence of table "+j+" is :" + (a-1));
			 count++;
			 if(a<2){
				 System.out.println("Table Reference : 0");
				 break;
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		if(count==i){
			System.out.println("Table Reference : 1");
		}
		System.out.println("-------------------------------------------------------------");

			 i=1;
			try (BufferedReader br = new BufferedReader(new FileReader(s)))
			{

				String sCurrentLine;
				String line;
				
				int linecount=0;
				while ((sCurrentLine = br.readLine()) != null) {
					linecount++;
					
					int indexfound=sCurrentLine.indexOf("Figure "+i);
			        if (indexfound>-1)
			        	{
			        		System.out.println("\n");
			        	//	System.out.println("Figure number " + i + " was found at position::" +indexfound+ "::on line"+linecount);
			        		line=sCurrentLine;
			        		i++;	
			        		}
			       
				}
				 System.out.println("\n\n"+"Total number of Figures :" + (i-1));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			 count=1;
			for (int j=1;j<i;j++){
			try (BufferedReader br = new BufferedReader(new FileReader(s)))
			{

				String sCurrentLine;
				String line;
				int a=0;
				int linecount=0;
				while ((sCurrentLine = br.readLine()) != null) {
					linecount++;
					
					int indexfound=sCurrentLine.indexOf("Figure "+j);
			        if (indexfound>-1)
			        	{
			        		line=sCurrentLine;
			        		a++;	
			        		}
			       
				}
				 System.out.println("\n\n"+"Total number of occurence of Figure "+j+" is :" + (a-1));
				 count++;
				 if(a<2){
					 System.out.println("Figure Reference : 0");
					 break;
				 }
			} catch (IOException e) {
				e.printStackTrace();
			} 
			}
			if(count==i){
				System.out.println("Figure Reference : 1");
			}
			System.out.println("------------------------------------------------------------------");
	}

}