package com.tbaumeist.harvesting;

import com.tbaumeist.common.Node;

public class Edge implements Comparable<Edge> {
    private Node nodeA;
    private Node nodeB;
    
    public Edge(Node a, Node b){
        // nodeA is always the larger
        if( a.compareTo(b) > 0){
            nodeA = a;
            nodeB = b;
        } else {
            nodeA = b;
            nodeB = a;
        }
    }
    
    public Node getNodeA(){
        return this.nodeA;
    }
    
    public Node getNodeB(){
        return this.nodeB;
    }
    
    public int compareTo(Edge o) {
        if(o == null) 
            return 1;
        if(nodeA.equals(o.nodeA))
            return nodeB.compareTo(o.nodeB);
        return nodeA.compareTo(o.nodeA);
    }
    
    public String toString(){
        return nodeA + "--" + nodeB;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;

        if (!(obj instanceof Edge))
            return false;
        Edge e = (Edge) obj;
        return e.nodeA.equals(nodeA) && e.nodeB.equals(nodeB);
    }

    @Override
    public int hashCode() {
        return nodeA.hashCode() + nodeB.hashCode();
    }
}
