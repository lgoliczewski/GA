import java.io.IOException;
import java.util.*;

public class GA {

    ArrayList<Solution> population;
    AlgorithmHolder algorithmHolder;
    Random random;
    Instance instance;
    int numberOfPeople;

    GA(Instance instance, int numberOfPeople){
        algorithmHolder = new AlgorithmHolder();
        this.instance = instance;
        random = new Random();
        this.numberOfPeople = numberOfPeople;
    }

    public ArrayList<Solution> generate(double KRandomProbability) throws IOException, InterruptedException {

        population = new ArrayList<>();
        Solution solution = instance.getSolution();
        Random random = new Random();
        Thread thread = new Thread();

        for(int i = 0; i<numberOfPeople; i++){
            Solution newSolution = solution.copy();
            newSolution.randomOrder();
            double result = random.nextDouble();
            if(result<KRandomProbability){
                System.out.println("Przed = " + newSolution.totalDistance());
                Solution newSolutionFor2OPT = algorithmHolder.NewAccelTwoOptAlgorithm(instance,newSolution);
                System.out.println("Po = " + newSolutionFor2OPT.totalDistance());
                thread.sleep(1000);
                population.add(newSolutionFor2OPT);
            }
            else{
                population.add(newSolution);
            }
        }

        System.out.print("Po generowaniu: ");
        printPopulation(population);
        System.out.println(" ");
        return population;
    }

    public ArrayList<Solution> evaluateAndKill2(){

        System.out.print("Przed sortem ");
        printPopulation(population);
        System.out.println(" ");
        Collections.shuffle(population);
        population.sort(Comparator.comparingInt(Solution::getAge));
        ArrayList<Solution> newPopulation = new ArrayList<>();

        System.out.print("Przed killem: ");
        printPopulation(population);
        System.out.println(" ");
        int i = 1;
        for(Solution s : population){
            newPopulation.add(s);
            if(i==numberOfPeople){
                System.out.print("Po killu ");
                printPopulation(population);
                return newPopulation;
            }
            i++;
        }

        return null;
    }

    public ArrayList<Solution> evaluateAndKill(){

        System.out.println(" ");
        population.sort(Comparator.comparingInt(Solution::totalDistance));
        ArrayList<Solution> newPopulation = new ArrayList<>();
        newPopulation =  chooseBest2(population, newPopulation);
        System.out.print("Przed killem: ");
        printPopulation(population);
        System.out.println(" ");
        int i = numberOfPeople;
        while(newPopulation.size()<numberOfPeople){
            double ppb = random.nextDouble();
            //System.out.println("population size = " + population.size() + "npsize = " + newPopulation.size());
            int number = random.nextInt(population.size()-1);
            Solution holder = population.get(number);
            newPopulation.add(holder);
            population.remove(holder);

        }
        /*for(Solution s : population){
            newPopulation.add(s);
            double ppb = random.nextDouble();
            if(ppb<0.01) {
                newPopulation.add(s);
                population.remove(s);
            }
            if(listSize == numberOfPeople){
                System.out.print("Po killu ");
                printPopulation(population);
                return newPopulation;
            }
        }*/

        return newPopulation;
    }

    public ArrayList<Solution> chooseBest2(ArrayList<Solution> list, ArrayList<Solution> newList){
        int i = 0;
        for(Solution s : list){
            newList.add(s);
            i++;
            if(i==170){
                return newList;
            }
        }
        return null;
    }

    public void printPopulation(ArrayList<Solution> population){
        for(Solution s : population){
            System.out.print(s.totalDistance() + " ");
        }
        System.out.println(" ");
    }

    public Solution OBX(Solution firstParent, Solution secondParent, double substringLength){

        double rand = random.nextDouble();
        double value = (1-substringLength)*rand;
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
        Solution solution = instance.getSolution();
        solution.order = order;
        return solution;
    }

    public ArrayList<Solution> crossover(ArrayList<Solution> population, double parentCount, double substringLength){
        Collections.shuffle(population);
        int i = 0;
        int j = 1;
        int size = population.size();
        while(i<parentCount*size){
            Solution parentOne = population.get(i);
            j=i;
            Solution parentTwo = population.get(j);
            while(sameOrder(parentOne, parentTwo)){
                j++;
                parentTwo = population.get(j);
            }
            Solution childSolution = OBX(parentOne,parentTwo,substringLength);
            population.add(childSolution);
            i = i + 2;
        }
        return population;
    }

    public boolean sameOrder(Solution s1, Solution s2){
        int k = 0;
        for(Integer i : s1.order){
            if(!Objects.equals(i, s2.order.get(k))){
                return false;
            }
            k++;
        }
        return true;
    }

    public boolean containsOrder(ArrayList<Solution> population, Solution solution){
        for(Solution s : population){
            int i = 0;
            if (s.order.get(i)!=solution.order.get(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(ArrayList<Integer> list, int value){
        for(Integer i : list){
            if(i == value){
                return true;
            }
        }
        return false;
    }

    public Solution invertMutation(Solution solution, int numberOfInverts){
        for(int k = 0; k<numberOfInverts; k++) {
            int i = 1 + random.nextInt(instance.getDimension());
            int j = i;
            while (j == i || Math.abs(j-i)> instance.getDimension()/2) {
                j = 1 + random.nextInt(instance.getDimension());
            }
            solution = algorithmHolder.invert(solution, i, j);
        }
        return solution;
    }

    public ArrayList<Solution> mutation(ArrayList<Solution> population, double probabilityOfMutation, double twoOPTPpb, int numberOfInverts) throws IOException {
        for(Solution s : population){
            double value = random.nextDouble();
            if(value<probabilityOfMutation){
                s = invertMutation(s,numberOfInverts);
            }
            value = random.nextDouble();
            if(value<twoOPTPpb){
                s = algorithmHolder.TwoOptAlgorithm(instance,s);
            }
        }
        return population;
    }

    public void geneticAlgorithm(int numberOfIterations) throws IOException, InterruptedException {
        int i = 0;
        population = generate(0.1);
        Thread thread = new Thread();
        while(true) {
            population = evaluateAndKill();
            population = crossover(population, 0.5, 0.1);
            population = mutation(population, 0.07,0,1);
            addOneToAge(population);
            i++;
        }
    }

    public void addOneToAge(ArrayList<Solution> population){
        for(Solution s : population){
            s.age+=1;
        }
    }

    public void getBest(){

    }

}
