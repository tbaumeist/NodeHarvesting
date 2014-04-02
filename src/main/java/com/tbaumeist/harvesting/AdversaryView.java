package com.tbaumeist.harvesting;

import java.util.HashSet;
import java.util.Set;

import com.tbaumeist.common.Node;

public class AdversaryView {
    private Set<Edge> directRelationships = new HashSet<Edge>();
    private Set<Edge> indirectRelationships = new HashSet<Edge>();
    
    public void addDirectEdge(Node a, Node b){
        directRelationships.add(new Edge(a, b));
    }
    
    public void addIndirectEdge(Node a, Node b){
        indirectRelationships.add(new Edge(a, b));
    }
    
    public void purge(){
        // direct relationships trump indirect
        indirectRelationships.removeAll(directRelationships);
    }
    
    public Set<Edge> getDirectEdges(){
        return this.directRelationships;
    }
    
    public Set<Edge> getIndirectEdges(){
        return this.indirectRelationships;
    }
}
