import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Tests {

    public void runTests() throws IOException {

        File file = new File("data/br17.tsp");
        Parser parser = new Parser();
        AlgorithmHolder a = new AlgorithmHolder();
        Instance instance = new Instance();
        parser.setParameters(file, instance);
        Solution s = new Solution();
        s.setFields(instance);

    }

    public void cycleTest() throws InterruptedException {

        Instance instance = new Instance();
        GA ga = new GA(instance, 100);
        Solution solution1 = new Solution();
        ArrayList<Integer> order1 = new ArrayList<>();
        order1.add(8);
        order1.add(4);
        order1.add(7);
        order1.add(3);
        order1.add(6);
        order1.add(2);
        order1.add(5);
        order1.add(1);
        order1.add(9);
        order1.add(0);
        solution1.order = order1;
        solution1.printOrder();

        Solution solution2 = new Solution();
        ArrayList<Integer> order2 = new ArrayList<>();
        order2.add(0);
        order2.add(1);
        order2.add(2);
        order2.add(3);
        order2.add(4);
        order2.add(5);
        order2.add(6);
        order2.add(7);
        order2.add(8);
        order2.add(9);
        solution2.order = order2;
        solution2.printOrder();

        Solution solution3 = ga.OBX(solution1, solution2, 0.5);
        solution3.printOrder();
    }

    public void print(ArrayList<Integer> list){
        int i = 0;
        while(i<list.size()){
            System.out.print(list.get(i) + " ");
            i++;
        }
        System.out.println(" ");
    }
}
