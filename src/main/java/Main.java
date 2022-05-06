
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Tests t = new Tests();
        //t.startSolutionTest();
        t.moveTypeTest();
        //Instance instance = new Instance();
        //instance.generateRandomInstanceEUC_2D(100,1000); //generowanie 10 losowych punktow o wspolrzednych z przedzialu [0,40]


        /*Parser parser = new Parser();
        File file = new File("data/ftv47.atsp");
        parser.setParameters(file,instance);

        Solution solution1, solution2, solution3, solution4;
        solution1 = instance.getSolution();
        //parser.parseSolution(file, solution);

        AlgorithmHolder alg = new AlgorithmHolder();

        long start, end;
        solution1 = alg.ExNearestNeighbor(instance);
        //solution1.randomOrder();
        solution2 = solution1.copy();
        solution3 = solution1.copy();
        solution4 = solution1.copy();

        System.out.println("start distance: " + solution1.totalDistance() + "\n");

        //solution1.printOrder();
        start = System.currentTimeMillis();
        solution1 = alg.NewTabuSearchAlgorithm(instance, solution1,50,100);
        end = System.currentTimeMillis();
        System.out.println("New tabu");
        System.out.println("total distance: " + solution1.totalDistance());
        System.out.println("time : " + (end - start) + "ms\n");

        //solution2.printOrder();
        start = System.currentTimeMillis();
        solution2 = alg.NewTabuSearchAlgorithm(instance, solution2, 50, 100, "invert");
        end = System.currentTimeMillis();
        System.out.println("New Tabu - move: invert");
        System.out.println("total distance: " + solution2.totalDistance());
        System.out.println("time : " + (end - start) + "ms\n");

        //solution3.printOrder();
        start = System.currentTimeMillis();
        solution3 = alg.NewTabuSearchAlgorithm(instance, solution3, 50, 100, "swap");
        end = System.currentTimeMillis();
        System.out.println("New Tabu - move: swap");
        System.out.println("total distance: " + solution3.totalDistance());
        System.out.println("time : " + (end - start) + "ms\n");
        solution3.printOrder();

        start = System.currentTimeMillis();
        solution4 = alg.NewTabuSearchAlgorithm(instance, solution4, 50, 100, "insert");
        end = System.currentTimeMillis();
        System.out.println("New Tabu - move: insert");
        System.out.println("total distance: " + solution4.totalDistance());
        System.out.println("time : " + (end - start) + "ms\n");
        solution4.printOrder();

         */

        //solution.visualize();

        //instance.setName("instance1");
        //ToFileWriter writer = new ToFileWriter();
        //writer.saveToFile(solution);

        //Tests t = new Tests();
        //t.runTests();

    }
}
