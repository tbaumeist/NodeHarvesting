package com.tbaumeist.harvesting;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import com.tbaumeist.common.Node;
import com.tbaumeist.common.Topology;
import com.tbaumeist.common.dataFileReaders.TopologyFileReaderManager;
import com.tbaumeist.common.dataFileWriters.TopologyFileWriterDOT;
import com.tbaumeist.common.logging.LoggingManager;

public class NodeHarvester implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(NodeHarvester.class
            .getName());

    private final Arguments args;
    private RandomGenerator rand = null;

    public NodeHarvester(Arguments arguments) throws Exception {
        this.args = arguments;
        LoggingManager.addLogFile(args.logFileLocation, args.logLevel);

        LOGGER.info("Random seed used " + args.seed);
        // configure random number generator
        this.rand = new MersenneTwister(args.seed);

        LOGGER.info("ARGUMENTS:\n" + args.toString());
    }

    public void run() {
        try {
            Topology top = this.loadTopology(args.inputFile);
            
            if(top == null)
                throw new Exception("Error reading the topology file.");

            LOGGER.info("Loaded the graph from " + args.inputFile
                    + ", Number of nodes: " + top.getAllNodes().size());

            Set<Node> corruptNodes = this.placeCorruptNodes(top,
                    args.percentCorrupt /* , args.corruptPlacement */);

            LOGGER.info("Number of corrupt nodes: " + corruptNodes.size());

            // always using 2 look-ahead ATM
            AdversaryView view = this.createAdversaryTopologyView(corruptNodes);

            Topology adverTop = this.createAdversaryTopology(view);

            // Write out the results
            w("Actual Node Count: \t" + top.getAllNodes().size());
            w("Adversary Node Count: \t" + adverTop.getAllNodes().size());
            w("Difference Node Count: \t"
                    + (adverTop.getAllNodes().size() / (double) top
                            .getAllNodes().size()) * 100 + "%");
            w("");
            w("Actual Edge Count: \t" + top.getEdgeCount());
            w("Adversary Edge Count: \t" + adverTop.getEdgeCount());
            w("Difference Edge Count: \t"
                    + (adverTop.getEdgeCount() / (double) top.getEdgeCount())
                    * 100 + "%");
            w("");

            StringBuilder b = new StringBuilder();
            for (Node n : corruptNodes)
                b.append(" ").append(n);
            w("Corrupt Nodes:" + b.toString());
            w("");
            w("Adversaries Topology:");
            new TopologyFileWriterDOT().writeDot(adverTop, args.outputFile);

        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    public Topology loadTopology(String graphFile) throws Exception {
        TopologyFileReaderManager reader = new TopologyFileReaderManager();

        if (graphFile == null || graphFile.isEmpty()) {
            // load from stdin
            StringBuilder b = new StringBuilder();
            Scanner input = new Scanner(System.in);
            while (input.hasNextLine())
                b.append(input.nextLine()).append("\n");
            input.close();
            return reader.readFromString(b.toString());
        } else {
            // load from file
            return reader.readFromFile(graphFile);
        }
    }

    private void w(String s) {
        this.args.outputFile.println(s);
        LOGGER.info(s);
    }

    private Set<Node> placeCorruptNodes(Topology top, double corruptPercent) {

        // not ignoring placement because it is always random ATM
        int numCorruptNodes = (int) Math.floor(top.getAllNodes().size()
                * corruptPercent);
        Set<Node> corrupt = new HashSet<Node>();
        while (corrupt.size() < numCorruptNodes) {
            Node n = top.getAllNodes().get(
                    this.rand.nextInt(top.getAllNodes().size()));
            corrupt.add(n);
        }

        return corrupt;
    }

    private AdversaryView createAdversaryTopologyView(Set<Node> corruptNodes) {

        AdversaryView view = new AdversaryView();
        for (Node n : corruptNodes) {

            // first direct peers
            for (Node p : n.getDirectNeighbors()) {
                // direct peers (one hop away)
                view.addDirectEdge(n, p);

                // indirect peers (two hops away)
                for (Node p2 : p.getDirectNeighbors()) {
                    view.addIndirectEdge(p, p2);
                }
            }
        }

        // clean remove duplicates from view
        view.purge();

        return view;
    }

    private Topology createAdversaryTopology(AdversaryView view) {
        Topology top = new Topology();

        this.edgesToTopology(top, view.getDirectEdges());
        this.edgesToTopology(top, view.getIndirectEdges());

        return top;
    }

    private void edgesToTopology(Topology top, Set<Edge> edges) {
        for (Edge e : edges) {
            Node a = e.getNodeA().clone();
            Node b = e.getNodeB().clone();
            top.addNode(a);
            top.addNode(b);
            a = top.findNode(a.getLocation(), a.getID());
            b = top.findNode(b.getLocation(), b.getID());
            a.addNeighbor(b);
            b.addNeighbor(a);
        }
    }

}
