import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class Tests {

    public void runTests() throws IOException {

        File file = new File("data/u574.tsp");
        Parser parser = new Parser();
        AlgorithmHolder a = new AlgorithmHolder();
        Instance instance = new Instance();
        parser.setParameters(file, instance);
        Solution s = new Solution();
        s.setFields(instance);

        long maxTime = 10000;
        //long[][] data2opt = TwoOptTest(instance, maxTime);
        long[][] dataAcc2opt = AccelTwoOptTest(instance, maxTime);
        long[][] dataNN = ExNearestNeighborTest(instance, maxTime);
        long[][] dataKrand = KRandomTest(instance, maxTime);
        //s = a.ExNearestNeighbor(instance);
        //System.out.println(s.totalDistance());
        //s.setFields(instance);
        //s = a.TwoOptAlgorithm(instance, s);
        //System.out.println(s.totalDistance());

        //long start;
        //long end;
        /*long sum = 0;
        int iter = 1;
        while(iter<2) {
            s.randomOrder();
            start = System.currentTimeMillis();
            s = a.AccelTwoOptAlgorithm(instance, s);
            end = System.currentTimeMillis();
            sum = sum + (end-start);
            iter++;
        }
        System.out.println(sum);
        sum = 0;
        iter = 1;
        while(iter<2){
            Solution s2 = a.ExNearestNeighbor(instance);
            start = System.currentTimeMillis();
            s2 = a.AccelTwoOptAlgorithm(instance, s2);
            end = System.currentTimeMillis();
            sum = sum + (end-start);
            iter++;
        }
        System.out.println(sum);*/
        /*s.randomOrder();
        start = System.currentTimeMillis();
        s = a.AccelTwoOptAlgorithm(instance, s);
        end = System.currentTimeMillis();
        System.out.println(end-start);
        Solution s2 = a.ExNearestNeighbor(instance);
        start = System.currentTimeMillis();
        s2 = a.AccelTwoOptAlgorithm(instance, s2);
        end = System.currentTimeMillis();
        System.out.println(end-start);*/


    }

    public long[][] KRandomTest(Instance instance, long maxTime) {

        AlgorithmHolder ah = new AlgorithmHolder();
        long timeCount = maxTime;
        long[][] data = ah.KRandomAlgorithmTest(instance, maxTime, timeCount);
        int i = 0;
        while(i<maxTime/timeCount){
            System.out.println(data[0][i]);
            i++;
        }
        return data;
    }

    public long[][] ExNearestNeighborTest(Instance instance, long maxTime) {

        AlgorithmHolder ah = new AlgorithmHolder();
        long timeCount = maxTime;
        long[][] data = ah.ExNearestNeighborTest(instance, maxTime, timeCount);
        int i = 0;
        while(i<maxTime/timeCount){
            System.out.println(data[0][i]);
            i++;
        }
        return data;
    }

    public long[][] TwoOptTest(Instance instance, long maxTime) {

        AlgorithmHolder ah = new AlgorithmHolder();
        long timeCount = maxTime;
        long[][] data = ah.TwoOptAlgorithmTest(instance, maxTime, timeCount);
        int i = 0;
        while(i<maxTime/timeCount){
            System.out.println(data[0][i]);
            i++;
        }
        return data;
    }

    public long[][] AccelTwoOptTest(Instance instance, long maxTime) {
        AlgorithmHolder ah = new AlgorithmHolder();
        long timeCount = maxTime;
        long[][] data = ah.AccelTwoOptAlgorithmTest(instance, maxTime, timeCount);
        int i = 0;
        while(i<maxTime/timeCount){
            System.out.println(data[0][i]);
            i++;
        }
        return data;
    }
    public int[][] KRandWith2OPT(File file) throws IOException {
        Parser parser = new Parser();
        Instance instance = new Instance();
        parser.setParameters(file,instance);
        AlgorithmHolder ah = new AlgorithmHolder();
        Solution s = null;
        int k = 1000;
        int maxLimit = 1000000;
        int[][] results = new int[3][maxLimit/k];
        while(k<maxLimit){
            s = ah.KRandomAlgorithm(instance,k);
            results[1][(k/1000)-1] = s.totalDistance();
            //System.out.print(results[1][(k / 1000) - 1] + " ");
            s = ah.TwoOptAlgorithm(instance,s);
            results[2][(k/1000)-1] = s.totalDistance();
            //System.out.println(results[2][(k / 1000) - 1]);
            k=k+1000;
        }
        k =1000;
        while(k<maxLimit){
            System.out.println(results[1][(k / 1000) - 1] + " ");
            k=k+1000;
        }
        System.out.println("-------------------------------");
        k = 1000;
        while(k<maxLimit){
            System.out.println(results[2][(k / 1000) - 1]);
            k=k+1000;
        }

        return results;
    }

    public int[][] NeighborWith2OPT(File file) throws IOException {
        Parser parser = new Parser();
        Instance instance = new Instance();
        parser.setParameters(file,instance);
        AlgorithmHolder ah = new AlgorithmHolder();
        Solution s = null;
        int k = 10;
        int maxLimit = instance.getDimension();
        int[][] results = new int[3][maxLimit/k];
        while(k<maxLimit){
            s = ah.ExNearestNeighborTest2(instance,k);
            results[1][(k/10)-1] = s.totalDistance();
            System.out.println(results[1][(k/10)-1] + " ");
            s = ah.TwoOptAlgorithm(instance,s);
            results[2][(k/10)-1] = s.totalDistance();
            k+=10;
        }
        k = 10;
        while(k<maxLimit){
            System.out.println(results[1][(k/10)-1] + " ");
            k+=10;
        }
        System.out.println("-------------------------------");
        k = 10;
        while(k<maxLimit){
            System.out.println(results[2][(k/10)-1]);
            k+=10;
        }

        return results;
    }

    public void LengthOfTabuListTestForInstance(String inputFile, String outFile, int maxIter) throws IOException {

        Instance instance = new Instance();
        Parser parser = new Parser();
        File file = new File(inputFile);
        parser.setParameters(file,instance);
        Solution solution = instance.getSolution();
        Solution solution2;
        AlgorithmHolder ah = new AlgorithmHolder();
        FileWriter outputFile = new FileWriter(outFile);
        CSVWriter writer = new CSVWriter(outputFile);
        int listSize = 1;
        while(listSize<maxIter){
            solution2 = solution.copy();
            solution2 = ah.NewTabuSearchAlgorithm(instance, solution2,listSize,maxIter);
            System.out.println("size = " + listSize + ", " + solution2.totalDistance());
            String[] dataToWrite = {String.valueOf(listSize), String.valueOf(solution2.totalDistance())};
            writer.writeNext(dataToWrite);
            listSize = listSize + 1;
        }
        writer.close();
    }

    public void LengthOfTabuListTest() throws IOException {

        // ch130

        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L30.csv",30);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L60.csv",60);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L90.csv",90);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L120.csv",120);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L150.csv",150);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L180.csv",180);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L210.csv",210);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L240.csv",240);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L270.csv",270);
        LengthOfTabuListTestForInstance("data/ch130.tsp","lengthTest/ch130L300.csv",300);

        // tsp225

        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L30.csv",30);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L60.csv",60);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L90.csv",90);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L120.csv",120);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L150.csv",150);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L180.csv",180);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L210.csv",210);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L240.csv",240);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L270.csv",270);
        LengthOfTabuListTestForInstance("data/tsp225.tsp","lengthTest/tsp225L300.csv",300);

        // lin105

        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L30.csv",30);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L60.csv",60);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L90.csv",90);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L120.csv",120);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L150.csv",150);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L180.csv",180);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L210.csv",210);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L240.csv",240);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L270.csv",270);
        LengthOfTabuListTestForInstance("data/lin105.tsp","lengthTest/lin105L300.csv",300);
        
        // att48

        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L30.csv",30);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L60.csv",60);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L90.csv",90);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L120.csv",120);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L150.csv",150);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L180.csv",180);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L210.csv",210);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L240.csv",240);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L270.csv",270);
        LengthOfTabuListTestForInstance("data/ftv47.atsp","lengthTest/ftv47L300.csv",300);

    }

    public void startSolutionForInstanceTestRandom() throws IOException {

        int size = 10;
        FileWriter outputFile1 = new FileWriter("diffStartSolutions/TSfromDifferentStartSolutions.csv");
        CSVWriter writer = new CSVWriter(outputFile1);
        String[] header1 = {"size" ,"rand", "krand(k=100)", "2opt", "nn", "ms = 50, miter = 100"};
        writer.writeNext(header1);

        FileWriter outputFileKRandom = new FileWriter("diffStartSolutions/KRandom.csv");
        CSVWriter writerKRandom = new CSVWriter(outputFileKRandom);

        FileWriter outputFile2OPT = new FileWriter("diffStartSolutions/2OPT.csv");
        CSVWriter writer2OPT = new CSVWriter(outputFile2OPT);

        FileWriter outputFileNN = new FileWriter("diffStartSolutions/NN.csv");
        CSVWriter writerNN = new CSVWriter(outputFileNN);
        while(size<=300){
            AlgorithmHolder ah = new AlgorithmHolder();
            Instance instance = new Instance();
            instance.generateRandomInstanceEUC_2D(size,5*size);
            Solution holder = instance.getSolution();
            Solution solutionRandom = holder.copy();
            Solution solutionKRandom = ah.KRandomAlgorithm(instance,100);
            Solution solution2OPT = ah.AccelTwoOptAlgorithm(instance,holder);
            Solution solutionNN = ah.ExNearestNeighbor(instance);
            
            int solutionKRandomHolder = solutionKRandom.totalDistance();
            int solution2OPTHolder = solution2OPT.totalDistance();
            int solutionNNHolder = solutionNN.totalDistance();
            
            System.out.println("zwykly = " + solutionRandom.totalDistance() + ", Krand = " + solutionKRandom.totalDistance() +
                    ", 2OPT = " + solution2OPT.totalDistance() + ", NN = " + solutionNN.totalDistance());
            
            solutionRandom = ah.NewTabuSearchAlgorithm(instance, solutionRandom,50,100);
            solutionKRandom = ah.NewTabuSearchAlgorithm(instance, solutionKRandom,50,100);
            solution2OPT = ah.NewTabuSearchAlgorithm(instance, solution2OPT,50,100);
            solutionNN = ah.NewTabuSearchAlgorithm(instance, solutionNN,50,100);
            
            String[] dataAll = {String.valueOf(size), String.valueOf(solutionRandom.totalDistance()),
                    String.valueOf(solutionKRandom.totalDistance()), String.valueOf(solution2OPT.totalDistance()),
                    String.valueOf(solutionNN.totalDistance())};
            writer.writeNext(dataAll);
            
            String[] dataKRandom = {String.valueOf(size), String.valueOf(solutionKRandomHolder), String.valueOf(solutionKRandom.totalDistance())};
            writerKRandom.writeNext(dataKRandom);

            String[] data2OPT = {String.valueOf(size), String.valueOf(solution2OPTHolder), String.valueOf(solution2OPT.totalDistance())};
            writer2OPT.writeNext(data2OPT);

            String[] dataNN = {String.valueOf(size), String.valueOf(solutionNNHolder), String.valueOf(solutionNN.totalDistance())};
            writerNN.writeNext(dataNN);
            
            System.out.println("zwykly = " + solutionRandom.totalDistance() + ", Krand = " + solutionKRandom.totalDistance() +
                    ", 2OPT = " + solution2OPT.totalDistance() + ", NN = " + solutionNN.totalDistance());
            System.out.println(" - - - - - - - - - - - - - - - - - - - - - -");
            size = size + 10;

        }
        
        writer.close();
        writerKRandom.close();
        writer2OPT.close();
        writerNN.close();



    }

    public void startSolutionTest() throws IOException {
            startSolutionForInstanceTestRandom();
    }

}
