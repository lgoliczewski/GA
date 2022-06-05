
import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Solution implements Serializable {

    public Visualization v;
    public String frameTitle = "";

    public Instance instance;
    public int size;
    public int totalDistance;
    public ArrayList<Point2D.Double> pointList; // na wypadek euklidesowego i wizualizacji

    public ArrayList<Integer> order;
    public int[][] edge_weight_matrix;

    public void randomOrder(){
        Collections.shuffle(order);
    }

    public void setFields(Instance instance){
        order = new ArrayList<>();
        this.instance = instance;
        this.edge_weight_matrix = instance.edge_weight_matrix;
        this.size = instance.getDimension();
        this.pointList = instance.getNode_coord_list();
        int i = 0;
        while(i<size){
            order.add(i+1);
            i++;
        }
    }

    public Solution() {}

    public Solution copy() {
        Solution s = new Solution();
        s.instance = this.instance;
        s.frameTitle = this.frameTitle;
        s.size = this.size;
        s.pointList = this.pointList;
        s.order = new ArrayList<>();

        for(int i = 0; i < this.order.size(); i++) {
            s.order.add(this.order.get(i));
        }

        s.edge_weight_matrix = this.edge_weight_matrix;

        return s;
    }

    public void printOrder(){
        int i = 0;
        while(i<size){
            System.out.print(order.get(i) + " ");
            i++;
        }
        System.out.println(" ");
    }

    public void printPoints(){
        int i = 0;
        while(i<size){
            System.out.print(pointList.get(i) + " ");
            i++;
        }
        System.out.println(" ");
    }

    public int totalDistance(){
        int sum = 0;
        int i = 0;
        while(i<size){
            if(i == size - 1){
                sum = sum + edge_weight_matrix[order.get(i)-1][order.get(0)-1];
            }
            else {
                sum = sum + edge_weight_matrix[order.get(i) - 1][order.get(i + 1) - 1];
            }

            i++;
        }
        return sum;
    }

    public int totalDistanceAfterInvert(Instance instance, Solution holder, int holderDistance, int bestCandDistance, int i, int j) {
        int newDistance = bestCandDistance;

        int swapDistances[] = new int[4];

        if (instance.getType().equals(Instance.type_enum.TSP) && !(i == 1 && j == holder.size)) {

            swapDistances[0] = instance.distance(holder, i-1, i);
            swapDistances[1] = instance.distance(holder, j, j+1);
            swapDistances[2] = instance.distance(holder, i-1, j);
            swapDistances[3] = instance.distance(holder, i, j+1);

            newDistance = holderDistance - swapDistances[0] - swapDistances[1] + swapDistances[2] + swapDistances[3];

        } else if (instance.getType().equals(Instance.type_enum.ATSP)){
            newDistance = this.totalDistance();
        }

        return newDistance;
    }

    public int totalDistanceAfterSwap(Instance instance, Solution holder, int holderDistance, int i, int j) {
        int newDistance = holderDistance;
        int subtractDist[] = new int[4];
        int addDist[] = new int[4];

        if (j-i == 1) {
            subtractDist[0] = instance.distance(holder, i-1, i);
            subtractDist[1] = instance.distance(holder, i, j);
            subtractDist[2] = instance.distance(holder, j, j+1);

            addDist[0] = instance.distance(holder, i-1, j);
            addDist[1] = instance.distance(holder, j, i);
            addDist[2] = instance.distance(holder, i, j+1);
        } else if (j-i == holder.size - 1) {
            subtractDist[0] = instance.distance(holder, j, i);
            subtractDist[1] = instance.distance(holder, i, i+1);
            subtractDist[2] = instance.distance(holder, j-1, j);

            addDist[0] = instance.distance(holder, i, j);
            addDist[1] = instance.distance(holder, j, i+1);
            addDist[2] = instance.distance(holder, j-1, i);
        } else {
            subtractDist[0] = instance.distance(holder, i-1, i);
            subtractDist[1] = instance.distance(holder, i, i+1);
            subtractDist[2] = instance.distance(holder, j-1, j);
            subtractDist[3] = instance.distance(holder, j, j+1);

            addDist[0] = instance.distance(holder, i-1, j);
            addDist[1] = instance.distance(holder, j, i+1);
            addDist[2] = instance.distance(holder, j-1, i);
            addDist[3] = instance.distance(holder, i, j+1);
        }

        for (int k = 0; k < 4; k++) {
            newDistance -= subtractDist[k];
            newDistance += addDist[k];
            if (k == 2 && (j-i == 1 || j-i == holder.size - 1)) break;
        }
        //System.out.print(newDistance + " ");

        return newDistance;
    }

    public int totalDistanceAfterInsert(Instance instance, Solution holder, int holderDistance, int bestCandDistance, int i, int j) {

        if(i == j || j-i == 1 || i-j == holder.size - 1) return bestCandDistance;

        int newDistance = holderDistance;
        int subtractDist[] = new int[3];
        int addDist[] = new int[3];

        subtractDist[0] = instance.distance(holder, i-1, i);
        subtractDist[1] = instance.distance(holder, i, i+1);
        subtractDist[2] = instance.distance(holder, j-1, j);

        addDist[0] = instance.distance(holder, i-1, i+1);
        addDist[1] = instance.distance(holder, i, j);
        addDist[2] = instance.distance(holder, j-1, i);

        for (int k = 0; k < 3; k++) {
            newDistance -= subtractDist[k];
            newDistance += addDist[k];
        }

        return newDistance;
    }

    public void printMatrix(){
        int i = 0;
        int j = 0;
        while(i<size){
            j=0;
            while(j<size){
                System.out.print(edge_weight_matrix[i][j] + " ");
                j++;
            }
            i++;
            System.out.println(" ");
        }
    }

    public void visualize(){

        JFrame frame = new JFrame(frameTitle);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(850,850);
        frame.setVisible(true);
        v = new Visualization(this);
        frame.add(v);

    }

    public void updateDistance(){
        totalDistance = this.totalDistance();
    }

    public int getTotalDistance(){
        return totalDistance;
    }

}
