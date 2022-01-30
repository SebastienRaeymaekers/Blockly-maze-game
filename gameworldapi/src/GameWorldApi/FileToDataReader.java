package GameWorldApi;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;

public class FileToDataReader {

    /**
     * Loads the selected file from the chosen directory.
     * @return a HashMap representation of the loaded data.
     */
    public static HashMap<String, String> loadGameData(){
        File file = SelectFile();
        if (file == null) return null;
        return FileToDataReader.StringToData(FileToDataReader.FileToString(file));
    }

    public static HashMap<String, String> loadGameData(File file){
        return FileToDataReader.StringToData(FileToDataReader.FileToString(file));
    }

    public static File SelectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile;
        }
        return null;
    }

    public static String FileToString(File file)
            throws IllegalArgumentException {
        try {
            StringBuilder text = new StringBuilder();
            String line;

            BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
            }

            return text.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> StringToData(String text)
            throws IllegalArgumentException {
        HashMap<String, String> data = new HashMap<>();
        String[] list, line;

        text = text.trim();
        text = text.replace(" ", "");
        text = text.replace("\t", "");
        list = text.split(";");
        for (String string : list) {
            line = string.split(":");
            if (line.length != 2) {
                throw new IllegalArgumentException("invalid file");
            }
            data.put(GetAlias(line[0].toLowerCase()), line[1]);
        }

        return data;
    }

    private static String GetAlias(String text) {
        switch (text.toLowerCase()) {
            case "xpos":
            case "x-pos":
                return "x";
            case "ypos":
            case "y-pos":
                return "y";
            case "h":
                return "height";
            case "w":
                return "width";
            case "dir":
            case "d":
                return "direction";
            case "world":
            case "g":
                return "grid";
            default:
                return text.toLowerCase();
        }
    }

}
