import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class LvlCompile {
	public static void main(String[] args) throws IOException, ParserException {
		FileInputStream ifile = new FileInputStream(args[0]);
		FileOutputStream ofile = new FileOutputStream(args[1]);
		
		Scanner scan = new Scanner(ifile);  
		scan.useDelimiter("\\Z");  
		String content = scan.next(); 
		ifile.close();
		Parser.parse(content).generate(new DataOutputStream(ofile));
		ofile.close();
	}
}