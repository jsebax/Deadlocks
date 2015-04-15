/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlockDetection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author htrefftz
 */
public class CyclesInGraphs {
    
    Map<String, Integer> map;
    
    /**
     * Adjacency matrix. A true in position [row][col] means that there is 
     * a link in the graph from node "row" to node "col".
     */    
    private boolean [][] matrix;
    
    private String graphviz = "digraph G { ";
    private String log = "";
    
    private ArrayList<Integer> arrayList;   
    private ArrayList<Integer> arrayList2;

    public CyclesInGraphs() {
        this.map = new HashMap<>();
        this.arrayList = new ArrayList<>();
        this.arrayList2 = new ArrayList<>();
    }
    
    public String getGraphviz() {
        return this.graphviz;
    }
    
    public String getLog() {
        return this.log;
    }
    
    /**
     * Receives an arrayList with the number of the nodes visited so far.
     * If a number appears twice, it means that there is a cycle in the graph.
     * In this case, the nodes forming the cycle are listed.
     * @return true if a cycle is detected, false otherwise
     */
    public boolean detectCycle() {
        arrayList2.clear();
        if(arrayList.size() <= 1) {
            return false;
        }
        for(int i = 0; i < arrayList.size(); i++) {
            for(int j = i + 1; j < arrayList.size(); j++) {
                int x = arrayList.get(i);
                int y = arrayList.get(j);
                if(x == y) {                    
                    //System.out.println("------------------------------>");
                    //System.out.println("Cycle Detected: ");
                    log += "------------------------------>\n";
                    log += "Cycle Detected: \n";
                    for(int k = i; k < j; k++) {
                        log += getStringMap(arrayList.get(k)) + " -> ";
                        //System.out.print(getStringMap(arrayList.get(k)) + " -> ");
                    }
                    log += getStringMap(arrayList.get(j)) + "\n";
                    //System.out.println(getStringMap(arrayList.get(j)));
                    return true;
                }
                if(matrix[x][y]) {
                    arrayList2.add(x);
                    arrayList2.add(y);
                }
            }
        }
        log += setArrayList() + "\n";
        //System.out.println(setArrayList());
        return false;
    }
    
    public String setArrayList() {
        for(int i = 1; i < arrayList2.size() - 1; i++) {
            arrayList2.remove(i);
        }
        String r = "";
        for(int j = 0; j < arrayList2.size() - 1; j++) {
            r += getStringMap(arrayList2.get(j)) + " -> ";
        }
        r += getStringMap(arrayList2.get(arrayList2.size() - 1));
        return r;
    }
    
    /**
     * Visits each node in the graph
     */
    public void traverseGraph() {
        for(int row = 0; row < matrix.length; row++) {
            arrayList = new ArrayList<>();
            log += "Visiting row: " + row + "\n";
            //System.out.println("Visiting row: " + row);
            visitNode(row);
        }
    }
    
    /**
     * Visits one node in the graph.
     * Then it visits all nodes to which there are links from this node,
     * in a recursive manner.
     * An arrayList of visited nodes is kept, in order to detect cycles.
     * @param i Number of node to be visited
     */
    public void visitNode(int i) {
        arrayList.add(i);
        boolean cycle = detectCycle();
        if(cycle == true) {
            log += "There is a Deadlock\n";
            log += "------------------------------>\n";
            //System.out.println("There is a Deadlock");
            //System.out.println("------------------------------>");
            arrayList.remove(arrayList.size() - 1);
            return;
        }
        for(int col = 0; col < matrix[i].length; col++) {
           if(matrix[i][col]) {
               visitNode(col);
           }
        }
        arrayList.remove(arrayList.size() - 1);
    }
    
    public void sortMap() {
        ArrayList<String> strArray = new ArrayList<>();
        Iterator it = map.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next().toString();
            strArray.add(key);
        }
        Collections.sort(strArray);
        map.clear();
        for(int i = 0; i < strArray.size(); i++) {
            map.put(strArray.get(i), i);
        }
    }
    
    public void readGraph() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("file.txt"));
        int nodes = sc.nextInt(); // Num of nodes 
        matrix = new boolean[nodes][nodes];
        fillMatrix();
        for(int i = 0; i < nodes; i++) {
            String node = sc.next();
            map.put(node, 0);
        }
        sortMap();
        int arcs = sc.nextInt(); // Num of arcs
        for(int j = 0; j < arcs; j++) {   
            String start = sc.next();
            String msg = sc.next(); 
            String end = sc.next();
            int s = map.get(start);
            int e = map.get(end);
            switch (msg) {
                case "holds":
                    matrix[e][s] = true;
                    graphviz += end + " [shape=box];" + "\n";
                    graphviz += start + " [shape=circle];" + "\n";
                    graphviz += end + " -> " + start + ";\n";
                    break;
                case "wants":
                    graphviz += start + " [shape=circle];" + "\n";
                    graphviz += end + " [shape=box];" + "\n";
                    graphviz += start + " -> " + end + ";\n";
                    matrix[s][e] = true;
                    break;
            }
        }
        graphviz += "}";
    }
    
    public String getStringMap(int index) {
        Iterator it = map.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next().toString();
            if(index == map.get(key)) {
                return key;
            }
        }
        return null;
    }
    
    public void fillMatrix() {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                matrix[i][j] = false;
            }
        }
    }
    
    public void printGraph() {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                if(matrix[i][j] == true) {
                    String s1 = getStringMap(i);
                    String s2 = getStringMap(j);
                    log += s1 + " -> " + s2 + "\n";
                    //System.out.println(s1 + " -> " + s2);
                }
            }
        }
    }    
}
