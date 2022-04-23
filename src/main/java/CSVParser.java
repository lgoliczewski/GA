import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;

public class CSVParser {

    public void getFile(File file) throws IOException {

        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.println(cell);
                }
                System.out.println();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void writeTest(File file) throws IOException {
        FileWriter outputFile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputFile);
        String[] data0 = {"raz", "dwa", "trzy"};
        String[] data1 = {"cztery", "piec"};
        String[] data2 = {"1", "5", "8", "13"};
        writer.writeNext(data0);
        writer.writeNext(data1);
        writer.writeNext(data2);
        writer.close();
    }


}