package com.tbaumeist.harvesting;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.tbaumeist.common.Topology;
import com.tbaumeist.common.testing.TestHelper;


public class TestNodeHarvester{

    public TestNodeHarvester() {
    }
    
    @Test
    public void loadGraphFile() throws Exception {
        
        // Load from file
        String[] strArgs1 = new String[]{"-p", "0.2", "-i", TestHelper.getResourcePath("10_3_S1001_random.dot")};
        Arguments args1 = Arguments.Parse(strArgs1);
        NodeHarvester harvester1 = new NodeHarvester(args1);
        Topology topFile = harvester1.loadTopology(args1.inputFile);
        
        // Load from STDIN
        InputStream originalInput = System.in;
        System.setIn(new ByteArrayInputStream(Files.readAllBytes(Paths.get(args1.inputFile))));
        
        String[] strArgs2 = new String[]{"-p", "0.2"};
        Arguments args2 = Arguments.Parse(strArgs2);
        NodeHarvester harvester2 = new NodeHarvester(args2);
        Topology topStr = harvester2.loadTopology(args2.inputFile);
       
       System.setIn(originalInput); // restore STDIN
       
       // Compare them
       String topFileStr = topFile.toString();
       String topStrStr = topStr.toString();
       assertTrue(topFileStr.equals(topStrStr));
    }
    
    @Test
    public void simpleGraph() throws Exception {
        
        // Load from file
        String[] strArgs = new String[]{"-p", "0.2", "-i", TestHelper.getResourcePath("10_3_S1001_random.dot")};
        Arguments args = Arguments.Parse(strArgs);
        NodeHarvester harvester = new NodeHarvester(args);
        harvester.run();

    }
    
}
