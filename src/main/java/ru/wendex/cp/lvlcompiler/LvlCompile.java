package ru.wendex.cp.lvlcompiler;


import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.logging.Level;

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

public class LvlCompile
{
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            throw new IllegalArgumentException("LVL COMPILER: arguments length must be >= 2");
        }
        System.out.println("copter pro level compiler v" + LvlCompilerInternal.getVersion());
        System.out.println("compile directory \"" + args[0] + "\" to \"" + args[1] + "\"");
        compileDirectory(FileSystems.getDefault().getPath(args[0]), FileSystems.getDefault().getPath(args[1]));
        System.out.println("directory compiled successfully");
    }

    public static void compile(Path sourcePath, Path targetPath) throws IOException, ParserException
    {
        InputStream ifile = Files.newInputStream(sourcePath);
        OutputStream ofile = Files.newOutputStream(targetPath);

        //Считываем весь исходный файл уровня в content
        Scanner scan = new Scanner(ifile);
        scan.useDelimiter("\\Z");
        String content = scan.next();
        ifile.close();

        //Парсим и записываем в файл
        Parser.parse(content).generate(new DataOutputStream(ofile));
        ofile.close();
    }

    public static void compileDirectory(Path sourceDirectory, Path targetDirectory)
    {
        try (DirectoryStream<Path> inputDirectoryStream = Files.newDirectoryStream(sourceDirectory))
        {
            Files.createDirectories(targetDirectory);
            for (Path sourcePath : inputDirectoryStream)
            {
                String fileName = sourcePath.getFileName().toString();
                if (fileName.endsWith(".txt"))
                {

                    try
                    {
                        compile(sourcePath, targetDirectory.resolve(fileName.substring(0,
                                fileName.lastIndexOf('.')) + ".dat"));
                    }
                    catch (ParserException | IOException exception)
                    {
                        LvlCompilerInternal.getLogger().log(Level.SEVERE,
                                MessageFormat.format("Error compile file {0}", sourcePath.toString(),
                                        exception));
                    }
                }
            }
        }
        catch (IOException exception)
        {
            LvlCompilerInternal.getLogger().log(Level.SEVERE,
                    MessageFormat.format("Error compile directory {0}", sourceDirectory.toString(),
                            exception));
        }
    }
}