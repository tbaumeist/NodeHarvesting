package com.tbaumeist.harvesting;

import java.util.logging.Logger;

import com.tbaumeist.common.logging.LoggingManager;

public class Main {
    
    private static final Logger LOGGER = Logger.getLogger(Main.class
            .getName());

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Setup the logger
            LoggingManager.initialize();
            
            LOGGER.info("Starting node harvesting analyzer");

            // Read command line arguments
            Arguments arguments = Arguments.Parse(args);
            if(arguments == null)
                return;
            
            // Run the analysis
            new NodeHarvester(arguments).run();
            
            arguments.outputFile.flush();
            
            LOGGER.info("Exiting node harvesting analyzer");
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }

    }

}
