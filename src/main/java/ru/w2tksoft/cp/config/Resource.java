package ru.w2tksoft.cp.config;

import java.io.*;

/**
 * <p>Класс для взаимодействия с ресурсами.</p>
 * <p>Для игры есть 3 вида ресурсов: настройки, данные и информационные файлы.
 * Соответственно есть 3 папки для их расположений: папка настроек, папка данных и главная папка.</p>
 * <p><b>Информационные файлы</b> представляют собой простые текстовые файлы (readme, history, license, это всё).
 * Они всегда имеют расширение .txt и доступны только для чтения.</p>
 * <p><b>Данные</b> имеют расширение .dat и доступны только для чтения.
 * Однако они могут находиться в двух разных расположениях: в папке данных в классах и  обычной папке данных.
 * Папка данных в классах приоритетнее.</p>
 * <p><b>Настройки имеют расширение ".set" и доступны как для чтения, так и для записи.</p>
 */

public class Resource {
    private File mainFile;
    private File dataFile;
    private File settingsFile;
    private ClassLoader classLoader;
    private String jarDataPath;
    static private final String dataFolderName = "DATA";
    static private final String settingsFolderName = "SETTINGS";

    /**
     * @param mainPath главная папка (если null, то рабочая папка, обязана существовать)
     * @param dataPath папка данных (если null, то папка DATA относительно главной)
     * @param settingsPath папка настроен (если null, то папка SETTINGS относительно главной)
     * @param classLoader загрузчик классов, для загрузки данных из классов
     * @param jarDataPath папка данных в классах относительно корня (если null, то папка DATA)
     * @throws IOException бросает в случае, если папки установлены неверно
     */
    public Resource(String mainPath, String dataPath, String settingsPath, ClassLoader classLoader, String jarDataPath) throws IOException{
        if (mainPath == null)
            mainFile = new File(".");
        else
            mainFile = new File(mainPath);
        if (!mainFile.isDirectory())
            throw new IOException("Main directory " + mainFile.getPath() + " is not a directory");

        if (dataPath == null)
            dataFile = new File(mainFile, dataFolderName);
        else
            dataFile = new File(dataPath);

        if (settingsPath == null)
            settingsFile = new File(mainFile, settingsFolderName);
        else
            settingsFile = new File(settingsPath);

        if (!settingsFile.isDirectory() && settingsFile.exists())
            throw new IOException("Settings directory " + settingsFile.getPath() + " is not a directory");

        this.classLoader = classLoader;

        if (jarDataPath == null)
            this.jarDataPath = "/" + dataFolderName + "/";
        else
            this.jarDataPath = jarDataPath;
    }

    /**
     * Загружает данные
     * @param type тип данных (подпапка в папке данных)
     * @param name имя данных (название файла без расширения)
     * @return поток ввода для данных
     * @throws IOException если данные загрузить не удалось
     */
    public InputStream getInputDataResource(String type, String name) throws IOException {
        StringBuilder path = new StringBuilder(30);
        path.append(jarDataPath);
        path.append(type);
        path.append("/");
        path.append(name);
        path.append(".dat");
        if (classLoader.getResourceAsStream(path.toString()) != null)
            return classLoader.getResourceAsStream(path.toString());
        File typeFile = new File(dataFile, type);
        File nameFile = new File(typeFile, name + ",dat");
        return new FileInputStream(nameFile);
    }

    /**
     * Загружает настройки для чтения
     * @param type тип настроек (подпапка в папке настроек)
     * @param name имя настроек (название файла без расширения)
     * @return поток ввода для настроек
     * @throws IOException если настройки загрузить не удалось
     */
    public InputStream getInputSettingsResource(String type, String name) throws IOException {
        File typeFile = new File(settingsFile, type);
        File nameFile = new File(typeFile, name + ".set");
        return new FileInputStream(nameFile);
    }

    /**
     * Загружает настройки для редактирования
     * @param type тип настроек (подпапка в папке настроек)
     * @param name имя настроек (название файла без расширения)
     * @return поток вывода для настроек
     * @throws IOException если настройки загрузить не удалось
     */
    public OutputStream getOutputSettingsResource(String type, String name) throws IOException {
        File typeFile = new File(settingsFile, type);
        File nameFile = new File(typeFile, name +".set");
        return new FileOutputStream(nameFile);
    }

    /**
     * Загружает информационные файл
     * @param name имя информационногофайла (название файла без расширения)
     * @return читатель для этого файла
     * @throws IOException если файл загрузить не удалось
     */
    public Reader getInputTextResource(String name) throws IOException {
        File file = new File(mainFile, name + ".txt");
        return new FileReader(file);
    }

    /*public static byte[] inputStreamToBytesArray(InputStream is) throws IOException {
        final int MAX_BYTES = 1024 * 1024 * 32 * 32;
        byte[] bytes = new byte[MAX_BYTES];
        int count = is.read(bytes, 0, MAX_BYTES);
        if (is.read() != -1)
            throw new IOException("InputStream too large");
        return bytes.
    }*/
}
