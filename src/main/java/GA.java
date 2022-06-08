import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GA {

    ArrayList<Solution> population;
    AlgorithmHolder algorithmHolder;
    Random random;
    Instance instance;
    int numberOfPeople;

    GA(Instance instance, int numberOfPeople) {
        algorithmHolder = new AlgorithmHolder();
        this.instance = instance;
        random = new Random();
        this.numberOfPeople = numberOfPeople;
    }

    public ArrayList<Solution> generate(double KRandomProbability, long maxTime) throws IOException {

        long start = System.currentTimeMillis();

        population = new ArrayList<>();
        Solution solution = instance.getSolution();

        for (int i = 0; i < numberOfPeople; i++) {
            Solution newSolution = solution.copy();
            newSolution.randomOrder();
            population.add(newSolution);
            newSolution.updateDistance();
        }

        int i = 0;
        ArrayList<Solution> newPopulation = new ArrayList<>();
        for (Solution s : population) {
            long finish = System.currentTimeMillis();
            if(i<KRandomProbability*numberOfPeople && (finish-start) < maxTime){
                s =  algorithmHolder.NewAccelTwoOptAlgorithm(instance,s);
            }
            s.updateDistance();
            newPopulation.add(s);
            i++;
        }

        //System.out.println(" ");
        return newPopulation;
    }

    public ArrayList<Solution> selection(String selectionType) {
        if (Objects.equals(selectionType, "roulette")) {
            population = rouletteSelection();
        } else if (Objects.equals(selectionType, "rouletterank")) {
            population = rouletteRankSelection();
        }
        else if (Objects.equals(selectionType, "tournament")) {
            population = tournamentSelection(0.5);
        }
        return population;
    }

    public ArrayList<Solution> rouletteSelection() {
        //System.out.print("Przed selekcją: ");
        population.sort(Comparator.comparingInt(Solution::getTotalDistance));
        //printPopulation(population);

        long sum = 0;
        for (Solution s : population) {
            sum += s.totalDistance();
        }

        ArrayList<Solution> newPopulation = new ArrayList<>();
        int eliteN = (int)Math.floor(Math.pow(numberOfPeople, 1.0/3.0));
        //int eliteN = 0;
        for (int i = 0; i < eliteN; i++) {
            newPopulation.add(population.get(i));
            sum -= population.get(i).totalDistance();
        }

        double divs[] = new double[numberOfPeople - eliteN];
        double temp = 0;
        for (int i = 0; i < numberOfPeople - eliteN; i++) {
            divs[i] = temp;
            //System.out.print(divs[i] + " ");
            temp += 1.0/(double)(numberOfPeople - 1) * (1.0 - (double)population.get(i + eliteN).totalDistance()/sum);
        }
        //System.out.println(" ");
        for (int i = eliteN; i < numberOfPeople/2; i++) {
            Solution chosen = new Solution();
            do {
                double x = random.nextDouble();
                for (int j = 0; j < numberOfPeople - eliteN; j++) {
                    if (x < divs[j]) {
                        chosen = population.get(j-1 + eliteN);
                        break;
                    } else if (j == numberOfPeople - eliteN - 1) {
                        chosen = population.get(j + eliteN);
                        break;
                    }
                }
            } while (newPopulation.contains(chosen));
            newPopulation.add(chosen);
        }

        return newPopulation;
    }

    public ArrayList<Solution> rouletteRankSelection() {
        //System.out.print("Przed selekcją: ");
        population.sort(Comparator.comparingInt(Solution::getTotalDistance));
        //printPopulation(population);

        long sum = (long) (1 + numberOfPeople) * numberOfPeople / 2 + 0L * numberOfPeople;

        ArrayList<Solution> newPopulation = new ArrayList<>();
        //int eliteN = (int)Math.floor(Math.pow(numberOfPeople, 1.0/3.0));
        int eliteN = 0;
        for (int i = 0; i < eliteN; i++) {
            newPopulation.add(population.get(i));
            sum -= (numberOfPeople - i);
            sum -= 0;
        }

        double[] divs = new double[numberOfPeople - eliteN];
        double temp = 0;
        for (int i = 0; i < numberOfPeople - eliteN; i++) {
            divs[i] = temp;
            System.out.print(divs[i] + " ");
            temp += (numberOfPeople - eliteN - i + 0)/(double)sum;
        }
        System.out.println(" ");
        for (int i = eliteN; i < numberOfPeople*4/10; i++) {
            Solution chosen = new Solution();
            do {
                double x = random.nextDouble();
                for (int j = 0; j < numberOfPeople - eliteN; j++) {
                    if (x < divs[j]) {
                        chosen = population.get(j-1 + eliteN);
                        break;
                    } else if (j == numberOfPeople - eliteN - 1) {
                        chosen = population.get(j + eliteN);
                        break;
                    }
                }
            } while (newPopulation.contains(chosen));
            newPopulation.add(chosen);
        }

        while (newPopulation.size() < numberOfPeople/2) {
            int idx = random.nextInt(numberOfPeople);
            while (newPopulation.contains(population.get(idx))) {
                idx = random.nextInt(numberOfPeople);
            }
            newPopulation.add(population.get(idx));
        }

        return newPopulation;
    }

    public ArrayList<Solution> tournamentSelection(double prob) {

        ArrayList<Solution> newPopulation = new ArrayList<>();
        int eliteN = 1;
        for (int i = 0; i < eliteN; i++) {
            newPopulation.add(population.get(i));
        }

        ArrayList<Solution> noElitePopulation = new ArrayList<>(population);
        for (int i = eliteN; i < numberOfPeople; i++) {
            noElitePopulation.add(population.get(i));
        }

        Collections.shuffle(noElitePopulation);
        int tournamentSize = 2;
        ArrayList<Solution> tournamentPopulation = new ArrayList<>();
        int winners = numberOfPeople/10;
        int n = 0;
        for (int i = 0; i < numberOfPeople - eliteN; i++) {
            tournamentPopulation.add(noElitePopulation.get(i));
            if (i > 0 && (i + 1) % tournamentSize == 0) {
                tournamentPopulation.sort(Comparator.comparingInt(Solution::totalDistance));
                double currProb = prob;
                for (int j = 0; j < tournamentSize; j++) {
                    if (random.nextDouble() < currProb) {
                        newPopulation.add(tournamentPopulation.get(j));
                        n++;
                    }
                    currProb *= 1-prob;
                }
                tournamentPopulation = new ArrayList<>();
                if(n == winners) break;
                if (newPopulation.size() == numberOfPeople/2)
                    break;
            }
        }
        while (newPopulation.size() < numberOfPeople/2) {
            int idx = random.nextInt(numberOfPeople - eliteN);
            while (newPopulation.contains(noElitePopulation.get(idx))) {
                idx = random.nextInt(numberOfPeople - eliteN);
            }
            newPopulation.add(noElitePopulation.get(idx));
        }

        return newPopulation;
    }

    public void printPopulation(ArrayList<Solution> population){
        for(Solution s : population){
            System.out.print(s.getTotalDistance() + " ");
        }
        System.out.println(" ");
    }

    public Solution OX(Solution firstParent, Solution secondParent, double substringLength){
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

    public Solution OBX(Solution firstParent, Solution secondParent, double substringLength){

        ArrayList<Integer> order = new ArrayList<>();
        ArrayList<Integer> usedCities = new ArrayList<>();
        int k = 0;
        while(k<instance.getDimension()) {
            order.add(null);
            k++;
        }
        k = 0;
        while(k<substringLength*instance.getDimension()) {
            int pos = random.nextInt(instance.getDimension());
            if (!contains(usedCities, firstParent.order.get(pos))) {
                usedCities.add(firstParent.order.get(pos));
                order.set(pos,firstParent.order.get(pos));
                k++;
            }
        }
        //System.out.print("used = ");
        //printList(usedCities);
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
        //System.out.print("s = ");
        //solution.printOrder();
        return solution;
    }

    public Solution PMX(Solution firstParent, Solution secondParent, double substringLength){

        Solution newChild = firstParent.copy();
        int first = random.nextInt(instance.getDimension() - (int)(substringLength*instance.getDimension())) + 1;
        //int last = first + 1 + random.nextInt(instance.getDimension() - first);
        int last = first + (int)(substringLength*instance.getDimension());
        //System.out.println("First = " + first + ", Last = " + last);
        for (int i = first; i <= last; i++) {
            int m = 1;
            while (!firstParent.order.get(m-1).equals(secondParent.order.get(i-1))) {
                m++;
            }

            newChild = algorithmHolder.swap(newChild, i, m);
        }

        return newChild;
    }

    public ArrayList<Solution> crossover(double substringLength, String type) {
        ArrayList<Solution> newPopulation = new ArrayList<>(population);
        while (newPopulation.size() != numberOfPeople) {
            //System.out.println("");
            int idx1 = random.nextInt(population.size());
            int idx2 = idx1;
            while (idx2 == idx1) {
                idx2 = random.nextInt(population.size());
            }
            Solution parent1 = population.get(idx1);
            Solution parent2 = population.get(idx2);
            Solution childSolution = null;
            if(Objects.equals(type, "OBX")) {
                childSolution = OBX(parent2, parent1, substringLength);
            }
            else if(Objects.equals(type,"OX")){
                childSolution = OX(parent2, parent1, substringLength);
            }
            else if(Objects.equals(type,"PMX")){
                childSolution = PMX(parent2, parent1, substringLength);
            }
            //Solution childSolution = PMX(parent1,parent2);
            newPopulation.add(childSolution);
            if (newPopulation.size() == numberOfPeople) break;
            if(Objects.equals(type, "OBX")) {
                childSolution = OBX(parent2, parent1, substringLength);
            }
            else if(Objects.equals(type,"OX")){
                childSolution = OX(parent2, parent1, substringLength);
            }
            else if(Objects.equals(type,"PMX")){
                childSolution = PMX(parent2, parent1, substringLength);
            }
            //childSolution = PMX(parent2,parent1);
            newPopulation.add(childSolution);
        }

        return newPopulation;
    }



    public void printList(ArrayList<Integer> list){
        int i = 0;
        while(i<list.size()){
            System.out.print(list.get(i) + "       ");
            i++;
        }
        System.out.println(" ");
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

    public boolean contains(ArrayList<Integer> list, int value){
        for(Integer i : list){
            if(i == value){
                return true;
            }
        }
        return false;
    }

    public Solution invertMutation(Solution solution){
        int i = 1 + random.nextInt(instance.getDimension());
        int j = i;
        while (j == i || Math.abs(j-i)> instance.getDimension()/2) {
            j = 1 + random.nextInt(instance.getDimension());
        }
        solution = algorithmHolder.invert(solution, i, j);
        return solution;
    }

    public ArrayList<Solution> mutationInvert(ArrayList<Solution> population, double probabilityOfMutation, double twoOPTPpb) throws IOException {
        for(Solution s : population){
            double value = random.nextDouble();
            if(value<probabilityOfMutation){
                s = invertMutation(s);
            }
            value = random.nextDouble();
            if(value<twoOPTPpb){
                s = algorithmHolder.TwoOptAlgorithm(instance,s);
            }
        }
        return population;
    }

    public ArrayList<Solution> mutationInsert(ArrayList<Solution> population, double probabilityOfMutation) {
        for(Solution s : population){
            double value = random.nextDouble();
            if(value<probabilityOfMutation) {
                int i = random.nextInt(instance.getDimension() + 1);
                int j = random.nextInt(instance.getDimension() + 1);
                while (i == j || j-i == 1 || i-j == instance.getDimension() - 1) {
                    j = random.nextInt(instance.getDimension() + 1);
                }
                s = algorithmHolder.insert(s, i, j);
            }
        }
        return population;
    }

    public Solution RSM(Solution s){
        int k = 2 + random.nextInt(instance.getDimension()-1);
        s = algorithmHolder.invert(s,k,instance.getDimension());
        s = algorithmHolder.invert(s,1,k-1);
        return s;
    }

    public ArrayList<Solution> mutationRSM(ArrayList<Solution> population, double probabilityOfMutation){
        for(Solution s : population){
            double value = random.nextDouble();
            if(value<probabilityOfMutation){
                RSM(s);
            }
        }
        return population;
    }

    public ArrayList<Solution> mutation(String mutationType, double ppb, double ppb2OPT) throws IOException {
        if(Objects.equals(mutationType, "invert")){
            population = mutationInvert(population, ppb, ppb2OPT);
        }
        else if(Objects.equals(mutationType, "insert")){
            population = mutationInsert(population, ppb);
        }
        else if(Objects.equals(mutationType, "RSM")){
            population = mutationRSM(population, ppb);
        }
        return population;
    }
    public void geneticAlgorithm(File file, long maxTime, String crossType, String selectionType, String mutationType, double substringLength, double genPpb, double mutationPpb, double mutation2OPTPpb) throws IOException, InterruptedException {

        FileWriter outputFile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputFile);

        Solution ExNN = algorithmHolder.ExNearestNeighbor(instance);
        Solution best2OPTfromExNN = algorithmHolder.NewAccelTwoOptAlgorithm(instance, ExNN);
        int best2OPTfromExNNdist = best2OPTfromExNN.totalDistance();

        long start = System.currentTimeMillis();
        int i = 0;
        population = generate(genPpb,15000);
        Solution best2OPT = getBest();
        int bestDistance2OPT = best2OPT.totalDistance();
        Solution best = best2OPT.copy();
        int bestDistance = bestDistance2OPT;
        long finish = System.currentTimeMillis();
        long extraTime = System.currentTimeMillis();
        while(finish-start<maxTime) {

            for (Solution s : population)
                s.updateDistance();

            Solution currBest = getBest();
            int currBestDistance = currBest.totalDistance();
            if (currBestDistance < bestDistance) {
                best = currBest;
                bestDistance = currBestDistance;
            }
            i++;

            population = selection(selectionType);
            population = crossover(substringLength, crossType);
            population = mutation(mutationType, mutationPpb, mutation2OPTPpb);
            finish = System.currentTimeMillis();
            if(finish-extraTime>=100){
                String[] dataToWrite = {String.valueOf(finish-start), String.valueOf(bestDistance)};
                writer.writeNext(dataToWrite);
                extraTime = System.currentTimeMillis();
            }
        }
        writer.close();
    }

    public Solution getBest(){
        Solution best = new Solution();
        int bestDistance = Integer.MAX_VALUE;
        for (Solution s : population) {
            if (s.getTotalDistance() < bestDistance) {
                best = s.copy();
                bestDistance = s.getTotalDistance();
            }
        }
        return best;
    }

}
