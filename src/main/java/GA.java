import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GA {

    ArrayList<Solution> population;
    Instance instance;
    int numberOfPeople;

    GA(Instance instance, int numberOfPeople){
        this.instance = instance;
    }

    public ArrayList<Solution> generate(double KRandomProbability) throws IOException {

        population = new ArrayList<>();
        Solution solution = instance.getSolution();
        Random random = new Random();
        AlgorithmHolder algorithmHolder = new AlgorithmHolder();

        for(int i = 0; i<numberOfPeople; i++){
            Solution newSolution = solution.copy();
            newSolution.randomOrder();
            population.add(newSolution);
        }

        for(Solution s : population){
            double result = random.nextDouble();
            if(result<KRandomProbability){
                s = algorithmHolder.NewAccelTwoOptAlgorithm(instance,s);
            }
            s.updateDistance();
        }

        return population;
    }

    public ArrayList<Solution> evaluateAndKill(){

        population.sort(Comparator.comparingInt(Solution::getTotalDistance));
        ArrayList<Solution> newPopulation = new ArrayList<>();
        int i = 1;
        for(Solution s : population){
            newPopulation.add(s);
            if(i==numberOfPeople){
                return newPopulation;
            }
        }
        return null;
    }

    public Solution OBX(Solution firstParent, Solution secondParent, double substringLength){
        Random random = new Random();
        double rand = random.nextDouble();
        System.out.println(rand);
        double value = (1-substringLength)*rand;
        System.out.println(value);
        System.out.println(value*instance.getDimension());
        System.out.println((value+substringLength)*instance.getDimension());
        ArrayList<Integer> order = new ArrayList<>();
        ArrayList<Integer> usedCities = new ArrayList<>();
        int k = 0;
        for(Integer i : firstParent.order){
            if(k > value*instance.getDimension() && k < ((value+substringLength)*instance.getDimension())){
                order.add(i);
                usedCities.add(i);
            }
            else{
                order.add(null);
            }
            k++;
        }

        System.out.println("Wybrane maista");
        for(Integer i : usedCities){
            System.out.print(i + " ");
        }
        System.out.println(" ");

        System.out.println("Order");
        for(Integer i : order){
            if(i==null){
                System.out.print("null ");
            }
            else {
                System.out.print(i + " ");
            }
        }
        System.out.println(" ");

        k = 0;
        for(Integer i : order){
            if(i==null) {
                while(contains(usedCities,secondParent.order.get(k))){
                    k++;
                }
                order.set(order.indexOf(i),secondParent.order.get(k));
                k++;
            }
        }
        Solution solution = firstParent.copy();
        solution.order = order;
        return solution;
    }

    public boolean contains(ArrayList<Integer> list, int value){
        for(Integer i : list){
            if(i == value){
                return true;
            }
        }
        return false;

    }
}
