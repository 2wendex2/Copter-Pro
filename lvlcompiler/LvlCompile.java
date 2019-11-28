import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

/*
	Данный модуль производит компиляцию уровней
	В 1 аргумент командной строки передаём путь к файлу, который хотим скомпилировать
	Во 2 куда хотим сохранить
	Для автоматизации этого процесса есть compile, но он автоматически сохраняет откомпилированные лвлы в игру
	И может похерить всё
	Поэтому лвлы также коммитятся в гитхаб
	
	Модуль использует класс Parser, у которого есть статический метод parse, который возвращает дерево разбора
	Само дерево разбора (LevelGenerator) имеет метод generate, записывающий разобранную последовательность в DataOutputStream
	
	Информация о парсере в Parser.java
	Информацие о деревьях разбора в LevelGenerator.java
*/

public class LvlCompile {
	public static void main(String[] args) throws IOException, ParserException {
		FileInputStream ifile = new FileInputStream(args[0]);
		FileOutputStream ofile = new FileOutputStream(args[1]);
		
		//Считываем весь исходный файл уровня в content
		Scanner scan = new Scanner(ifile);  
		scan.useDelimiter("\\Z");  
		String content = scan.next(); 
		ifile.close();
		
		//Парсим и записываем в файл
		Parser.parse(content).generate(new DataOutputStream(ofile));
		ofile.close();
	}
}