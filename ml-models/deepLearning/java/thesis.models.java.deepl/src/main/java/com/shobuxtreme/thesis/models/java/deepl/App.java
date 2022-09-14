package com.shobuxtreme.thesis.models.java.deepl;

import com.opencsv.CSVWriter;
import org.apache.commons.cli.*;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

public class App {

    public static void main(String []args) throws Exception {

        Options options = new Options();
        Option trainOption = Option.builder()
                .longOpt("train")
                .build();

        Option validateOption = Option.builder()
                .longOpt("validate")
                .build();

        Option predictOption = Option.builder()
                .longOpt("predict")
                .build();

        options.addOption(trainOption);
        options.addOption(validateOption);
        options.addOption(predictOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);

        if(cmd.hasOption("train")){
            App.train();
        } else if (cmd.hasOption("validate")){
            App.validate();
        } else if(cmd.hasOption("predict")){
            App.predict();
        }else{
            System.out.println("No command specified");
        }

    }

    private static void train() throws Exception {
        Instant startTime = Instant.now();
        //1. Get Dataset
        DataSet data = App.readCSV("train.csv");


        //2. Create Model
        String simpleMlp = new ClassPathResource("latest.model.h5").getFile().getPath();
        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);

        //3. Train
        model.fit(data);

        //4. Save Model
        model.save(new File("latest.model.joblib"));

        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis()/1000;
        System.out.println("Training finished in "+duration+" s");
    }

    private static void validate() throws Exception {
        Instant startTime = Instant.now();
        //1. Get Dataset
        DataSet data = App.readCSV("validate.csv");

        //2. Load Model & Produce output
        MultiLayerNetwork model = MultiLayerNetwork.load(new File("./latest.model.joblib"), true);
        INDArray output = model.output(data.getFeatures());

        INDArray yValidate = data.getLabels();
        //3. Calculate Accuracy
        double matches = 0;
        for( int i=0; i< output.rows();++i ){
            double res = Math.round(output.getRow(i).getDouble(0));
            if(res == yValidate.getDouble(i)){
                ++matches;
            }
        }
        double accuracy = matches / output.rows();

        //4. Convert & Save CSV
        saveCSV(output,"validate.results.csv");

        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis()/1000;
        System.out.println("Accuracy: "+accuracy);
        System.out.println("Validating finished in "+duration+" s");
    }

    @SuppressWarnings("DuplicatedCode")
    private static void predict() throws Exception {
        Instant startTime = Instant.now();

        //1. Get Dataset
        DataSet data = App.readCSV("predict.csv");

        //2. Load Model & Produce output
        MultiLayerNetwork model = MultiLayerNetwork.load(new File("./latest.model.joblib"), true);
        INDArray output = model.output(data.getFeatures());

        //4. Convert & Save CSV
        saveCSV(output,"predict.results.csv");

        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis()/1000;
        System.out.println("Predicting finished in "+duration+" s");
    }

    private static DataSet readCSV(String fileName) throws Exception{
        // Skip first line with features meta-data
        try (RecordReader recordReader = new CSVRecordReader(1, ',')) {
            // Assumes header & line indices
            String filePath = "./"+fileName;
            File csvFile = new File(filePath);

            // Get dimensions
            // Remove index
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(("noindex."+fileName),false));

            int lines = 0;
            String line;
            while ( (line = reader.readLine()) != null) {
                String [] temp = line.split(",",2);
                writer.write(temp[1]);
                writer.newLine();
                lines++;
            }
            reader.close();
            writer.close();

            String processedFilePath = "./noindex."+fileName;
            File processedCsvFile = new File(processedFilePath);

            recordReader.initialize(new FileSplit(processedCsvFile));
            DataSetIterator iterator = new RecordReaderDataSetIterator(
                    recordReader, lines - 1 , 8, 1);

            return iterator.next();

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static void saveCSV(INDArray output, String fileName) throws IOException {

        FileWriter outputFile = new FileWriter(fileName);
        CSVWriter writer = new CSVWriter(outputFile, ',',CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        String[] header = { "", "0"};
        writer.writeNext(header);
        for( int i=0; i< output.rows();++i ){
            double res = Math.round(output.getRow(i).getDouble(0));
            String[] data = { Integer.toString(i), Integer.toString((int) res)};
            writer.writeNext(data);
        }
        writer.close();
    }

}
