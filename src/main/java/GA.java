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
        //Thread thread = new Thread();

        for(int i = 0; i<numberOfPeople; i++){
            Solution newSolution = solution.copy();
            newSolution.randomOrder();
            double result = random.nextDouble();
            if(result<KRandomProbability){
                System.out.println("Przed = " + newSolution.totalDistance());
                Solution newSolutionFor2OPT = algorithmHolder.NewAccelTwoOptAlgorithm(instance,newSolution);
                System.out.println("Po = " + newSolutionFor2OPT.totalDistance());
                //thread.sleep(1000);
                population.add(newSolutionFor2OPT);
            }
            else{
                population.add(newSolution);
            }
            population.get(i).updateDistance();
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

        //System.out.print("Po selekcji: ");
        //printPopulation(newPopulation);
        //System.out.println(" ");

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

        //System.out.print("Po selekcji: ");
        //printPopulation(newPopulation);
        //System.out.println(" ");

        return newPopulation;
    }

    public ArrayList<Solution> tournamentSelection(double prob) {
        //System.out.print("Przed selekcją: ");
        population.sort(Comparator.comparingInt(Solution::totalDistance));
        //printPopulation(population);

        ArrayList<Solution> newPopulation = new ArrayList<>();
        //int eliteN = (int)Math.floor(Math.pow(numberOfPeople, 1.0/5.0));
        int eliteN = 2;
        for (int i = 0; i < eliteN; i++) {
            newPopulation.add(population.get(i));
        }

        ArrayList<Solution> noElitePopulation = new ArrayList<>(population);
        for (int i = eliteN; i < numberOfPeople; i++) {
            noElitePopulation.add(population.get(i));
        }

        Collections.shuffle(noElitePopulation);
        int tournamentSize = 3;
        ArrayList<Solution> tournamentPopulation = new ArrayList<>();
        //Solution winner = new Solution();
        //int winnerDistance = Integer.MAX_VALUE;
        int winners = (int)Math.floor(Math.pow(numberOfPeople, 1.0/6.0));
        int n = 0;
        for (int i = 0; i < numberOfPeople - eliteN; i++) {
            tournamentPopulation.add(noElitePopulation.get(i));
            //int distance = population.get(i).totalDistance();
            //if (distance < winnerDistance) {
                //winnerDistance = distance;
                //winner = population.get(i).copy();
            //}
            //if (i > 0 && (i + 1) % tournamentSize == 0) {
                //newPopulation.add(winner);
                //winnerDistance = Integer.MAX_VALUE;
            //}
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

        System.out.print("Po selekcji: ");
        printPopulation(newPopulation);
        System.out.println(" ");

        return newPopulation;
    }

    public ArrayList<Solution> chooseBest2(ArrayList<Solution> list, ArrayList<Solution> newList){
        int i = 0;
        for(Solution s : list){
            newList.add(s);
            i++;
            if(i==3){
                return newList;
            }
        }
        return list;
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
        System.out.print("s = ");
        solution.printOrder();
        return solution;
    }

    public Solution OBX(Solution firstParent, Solution secondParent, double substringLength){

        double rand = random.nextDouble();
        ArrayList<Integer> order = new ArrayList<>();
        ArrayList<Integer> usedCities = new ArrayList<>();
        System.out.print("fp = ");
        firstParent.printOrder();
        System.out.print("sp = ");
        secondParent.printOrder();
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
        System.out.print("used = ");
        printList(usedCities);
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
        System.out.print("s = ");
        solution.printOrder();
        return solution;
    }

    public Solution PMX(Solution firstParent, Solution secondParent){

        Solution newChild = firstParent.copy();
        int first = random.nextInt(instance.getDimension() - 1) + 1;
        int last = first + 1 + random.nextInt(instance.getDimension() - first);
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
            Solution childSolution = OX(parentOne,parentTwo,substringLength);
            population.add(childSolution);
            i = i + 2;
        }
        return population;
    }

    public ArrayList<Solution> crossover1(double substringLength) {
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
            Solution childSolution = OX(parent1,parent2,substringLength);
            //Solution childSolution = PMX(parent1,parent2);
            newPopulation.add(childSolution);
            if (newPopulation.size() == numberOfPeople) break;
            childSolution = OX(parent2,parent1,substringLength);
            //childSolution = PMX(parent2,parent1);
            newPopulation.add(childSolution);
        }

        return newPopulation;
    }

    public ArrayList<Solution> crossover2(ArrayList<Solution> population, double parentCount) throws InterruptedException {
        Collections.shuffle(population);
        int i = 0;
        int j;
        int size = population.size();
        while(i<parentCount*size){
            Solution parentOne = population.get(i);
            j=i;
            Solution parentTwo = population.get(j);
            while(sameOrder(parentOne, parentTwo)){
                j++;
                parentTwo = population.get(j);
            }
            Solution childSolution1 = cycleCrossover(parentOne,parentTwo);
            Solution childSolution2 = cycleCrossover(parentTwo,parentOne);
            population.add(childSolution1);
            population.add(childSolution2);
            i = i + 2;
        }
        return population;
    }



    public Solution cycleCrossover(Solution parentOne, Solution parentTwo) throws InterruptedException {
        int i = 0;
        int currHolder = 0;
        int currHolderPrev = 0;
        boolean odd = true;
        int[] childSolution = new int[parentOne.order.size()];
        while(i<childSolution.length){
            childSolution[i] = -1;
            i++;
        }
        printArray(childSolution);
        ArrayList<Integer> visitedCities = new ArrayList<>();
        Thread thread = new Thread();
        while(true){
            System.out.print("p1 = ");
            parentOne.printOrder();
            System.out.print("p2 = ");
            parentTwo.printOrder();
            System.out.print("pc = ");
            printArray(childSolution);
            System.out.print("vc = ");
            printList(visitedCities);
            System.out.println("currHolder = " + currHolder + " p1h = " + parentOne.order.get(currHolder) + " p2h = " + parentTwo.order.get(currHolder));
            System.out.println(" ");
            thread.sleep(30);
            if(nonZero(childSolution) == childSolution.length){
                Solution s = parentOne.copy();
                s.order = arrayToList(childSolution);
                return s;
            }
            int pom;
            if(odd) {
                childSolution[currHolder] = parentOne.order.get(currHolder);
                pom = parentOne.order.get(currHolder);
            }
            else{
                childSolution[currHolder] = parentTwo.order.get(currHolder);
                pom = parentTwo.order.get(currHolder);
            }
            if (visitedCities.contains(pom)){
                currHolder = nextWithMinusOne(childSolution);
                //System.out.println("nextwith-1 = " + nextWithMinusOne(childSolution));
                if(odd){
                    odd = false;
                }
                else{
                    odd = true;
                }
            }
            else{
                if(odd) {
                    visitedCities.add(parentOne.order.get(currHolder));
                }
                else{
                    visitedCities.add(parentTwo.order.get(currHolder));
                }
                currHolderPrev = currHolder;
                currHolder = findPosition(parentTwo.order.get(currHolder),parentOne.order);

            }

        }
    }

    public void printList(ArrayList<Integer> list){
        int i = 0;
        while(i<list.size()){
            System.out.print(list.get(i) + "       ");
            i++;
        }
        System.out.println(" ");
    }

    public void printArray(int[] array){
        int i = 0;
        while(i<array.length){
            System.out.print(array[i] + "       ");
            i++;
        }
        System.out.println(" ");
    }

    public int nonZero(int[] array){
        int i = 0;
        int c = 0;
        while(i<array.length){
            if(array[i]!=-1){
                c++;
            }
            i++;
        }
        return c;
    }
    public int findPosition(int value, ArrayList<Integer> arrayList){
        int i = 0;
        while(i<arrayList.size()){
            if(arrayList.get(i) == value){
                return i;
            }
            i++;
        }
        return -2;
    }

    public ArrayList<Integer> arrayToList(int[] array){
        ArrayList<Integer> arrayList = new ArrayList<>();
        int i = 0;
        while(i<array.length){
            arrayList.add(array[i]);
            i++;
        }
        return arrayList;
    }

    public int nextWithMinusOne(int[] childSolution){
        int i = 0;
        while(i<childSolution.length){
            if(childSolution[i]==-1){
                return i;
            }
            i++;
        }
        return i;
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

    public ArrayList<Solution> mutationInvert(ArrayList<Solution> population, double probabilityOfMutation, double twoOPTPpb, int numberOfInverts) throws IOException {
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

    public Solution TWORS(Solution s){
        int k = 1 + random.nextInt();
        s = algorithmHolder.invert(s,k,instance.getDimension());
        return s;
    }

    public ArrayList<Solution> TWORSmutation(ArrayList<Solution> population, double probabilityOfMutation){
        for(Solution s : population){
            double value = random.nextDouble();
            if(value<probabilityOfMutation){
                TWORS(s);
            }
        }
        return population;
    }



    public void geneticAlgorithm(int numberOfIterations) throws IOException, InterruptedException {

        Solution ExNN = algorithmHolder.ExNearestNeighbor(instance);
        Solution best2OPTfromExNN = algorithmHolder.NewAccelTwoOptAlgorithm(instance, ExNN);
        int best2OPTfromExNNdist = best2OPTfromExNN.totalDistance();

        int i = 0;

        population = generate(0.1);
        Solution best2OPT = getBest();
        int bestDistance2OPT = best2OPT.totalDistance();
        Solution best = best2OPT.copy();
        int bestDistance = bestDistance2OPT;
        //Thread thread = new Thread();
        while(i < numberOfIterations) {
            for (Solution s : population)
                s.updateDistance();

            Solution currBest = getBest();
            int currBestDistance = currBest.totalDistance();
            if (currBestDistance < bestDistance) {
                best = currBest;
                bestDistance = currBestDistance;
            }
            //System.out.println("Best: " + bestDistance);
            i++;

            //System.out.println("i = " + i);
            population = rouletteSelection();
            population = crossover1(0.1);
            population = mutationInvert(population, 0.002,0,1);
            addOneToAge(population);
            /*for (Solution s : population) {
                System.out.print(s.getAge() + " ");
            }
            System.out.println();*/
        }

        System.out.println("Best: " + bestDistance);
        System.out.println("Best 2OPT = " + bestDistance2OPT);
        System.out.println("Best 2OPTfromExNN = " + best2OPTfromExNNdist);
    }

    public void addOneToAge(ArrayList<Solution> population){
        for(Solution s : population){
            s.age+=1;
        }
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
