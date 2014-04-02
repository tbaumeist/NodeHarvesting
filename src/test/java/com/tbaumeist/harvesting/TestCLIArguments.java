package com.tbaumeist.harvesting;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tbaumeist.harvesting.Enums.CORRUPT_NODE_PLACEMENT;

public class TestCLIArguments {

    public TestCLIArguments() {
    }

    @Test
    public void correctOne() throws Exception {

        String[] strArgs = new String[] { "-p", "0.2", "-s", "1001" };
        Arguments args = Arguments.Parse(strArgs);

        assertTrue(true);
        assertTrue(args.percentCorrupt == 0.2);
        assertTrue(args.seed == 1001);
        assertTrue(args.logFileLocation.equals(""));
        assertTrue(args.inputFile.equals(""));
    }
    
    @Test
    public void correctTwo() throws Exception {

        String[] strArgs = new String[] { "-p", "0.2", "-s", "1001", "-o", "test.txt", "-f", "test.log", "-i", "graph.dot", "-c", "RandOm" };
        Arguments args = Arguments.Parse(strArgs);

        assertTrue(true);
        assertTrue(args.percentCorrupt == 0.2);
        assertTrue(args.seed == 1001);
        assertTrue(args.logFileLocation.equals("test.log"));
        assertTrue(args.inputFile.equals("graph.dot"));
        assertTrue(args.corruptPlacement == CORRUPT_NODE_PLACEMENT.RANDOM);
    }

    @Test
    public void missingPercentArgument() throws Exception {

        String[] strArgs = new String[] {};
        try {
            Arguments.Parse(strArgs);
        } catch (Exception ex) {
            assertTrue(true);
            return;
        }

        assertTrue(false);
    }
    
    @Test
    public void invalidPercentArgumentBelow() throws Exception {

        String[] strArgs = new String[] {"-p", "-0.1"};
        try {
            Arguments.Parse(strArgs);
        } catch (Exception ex) {
            assertTrue(true);
            return;
        }

        assertTrue(false);
    }
    
    @Test
    public void invalidPercentArgumentAbove() throws Exception {

        String[] strArgs = new String[] {"-p", "1.1"};
        try {
            Arguments.Parse(strArgs);
        } catch (Exception ex) {
            assertTrue(true);
            return;
        }

        assertTrue(false);
    }
    
    @Test
    public void help() throws Exception {
        
       String[] strArgs = new String[]{"-h"};
       Arguments args = Arguments.Parse(strArgs);
       
       assertTrue(true);
       assertTrue(args == null);
    }
}
