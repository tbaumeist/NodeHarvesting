package com.tbaumeist.harvesting;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.tbaumeist.harvesting.Enums.CORRUPT_NODE_PLACEMENT;


public class Arguments {
    private static final Logger LOGGER = Logger.getLogger(Arguments.class
            .getName());

    public String logFileLocation = "";
    public PrintStream outputFile = null;
    public String inputFile = "";

    public Level logLevel = Level.INFO;

    public int seed = 0;
    
    public double percentCorrupt = 0;
    
    public CORRUPT_NODE_PLACEMENT corruptPlacement = CORRUPT_NODE_PLACEMENT.RANDOM;


    private static Option OPT_RANDOM_SEED = new Option(
            "s",
            "seed",
            true,
            "The seed for the random number generator (integer). [default] random seed value.");
    private static Option OPT_HELP = new Option("h", "help", false,
            "Print program help manual.");
    private static Option OPT_LOG_FILE = new Option("f", "log-file", true,
            "File name to save log file to. [default] no log file is generated.");
    private static Option OPT_PERCENT_CORRUPT = new Option("p", "percent-corrupt",
            true, "Percentage of nodes to make corrupt. (e.g. 0.3 = 30%)");
    private static Option OPT_CORRUPT_PLACE = new Option("c", "corrupt-placement", true,
            "Overwrote in generateOptions method");
    private static Option OPT_RESULTS_OUTPUT = new Option("o", "output", true,
            "File to save output results to. [default] results written to stdout.");
    private static Option OPT_GRAPH_INPUT = new Option("i", "input-graph", true,
            "File to read DOT graph from. [default] DOT graph is read from stdin.");

    protected Arguments() {
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Field f : getClass().getFields()) {
            b.append(f.getName());
            b.append(" : ");
            try {
                Object o = f.get(this);
                b.append(o == null? "NULL": o);
            } catch (Exception e) {
                LOGGER.severe(e.getMessage());
            }
            b.append("\n");
        }

        return b.toString();
    }

    public static Arguments Parse(String[] args) throws Exception {
        Arguments arguments = new Arguments();

        final Options options = generateOptions();
        final CommandLineParser parser = new GnuParser();
        final CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(OPT_HELP.getLongOpt())) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(
                    "java -jar NodeHarvesting-0.0.1-jar-with-dependencies.jar",
                    options);
            return null;
        }

        if (!cmd.hasOption(OPT_PERCENT_CORRUPT.getLongOpt()))
            throw new Exception("-" + OPT_PERCENT_CORRUPT.getOpt()
                    + " option is required!");

        if (cmd.hasOption(OPT_LOG_FILE.getLongOpt()))
            arguments.logFileLocation = cmd.getOptionValue(OPT_LOG_FILE
                    .getLongOpt());

        arguments.seed = Integer.parseInt(cmd.getOptionValue(
                OPT_RANDOM_SEED.getLongOpt(),
                Integer.toString((int) System.currentTimeMillis())));

        arguments.percentCorrupt = Double.parseDouble(cmd
                .getOptionValue(OPT_PERCENT_CORRUPT.getLongOpt()));
        
        if(arguments.percentCorrupt < 0 || arguments.percentCorrupt > 1)
            throw new Exception("The argument " + OPT_PERCENT_CORRUPT.getOpt() + " must be between 0 and 1.");

        if (cmd.hasOption(OPT_GRAPH_INPUT.getLongOpt()))
            arguments.inputFile = cmd.getOptionValue(
                    OPT_GRAPH_INPUT.getLongOpt());

        if (cmd.hasOption(OPT_CORRUPT_PLACE.getLongOpt()))
            arguments.corruptPlacement = CORRUPT_NODE_PLACEMENT.valueOf(cmd.getOptionValue(
                    OPT_CORRUPT_PLACE.getLongOpt()).toUpperCase());

        if (cmd.hasOption(OPT_RESULTS_OUTPUT.getLongOpt()))
            arguments.outputFile = new PrintStream(new File(cmd.getOptionValue(OPT_RESULTS_OUTPUT
                    .getLongOpt())));
        else
            arguments.outputFile = System.out;

        return arguments;
    }

    private static Options generateOptions() {
        Options options = new Options();
        StringBuilder descBuilder = null;

        descBuilder = new StringBuilder(
                "Placement of corrupt nodes. [default] "
                        + CORRUPT_NODE_PLACEMENT.RANDOM.name() + " [options]");
        for (CORRUPT_NODE_PLACEMENT type : CORRUPT_NODE_PLACEMENT.values()) {
            descBuilder.append(" ").append(type);
        }
        OPT_CORRUPT_PLACE.setDescription(descBuilder.toString());

        options.addOption(OPT_HELP);
        options.addOption(OPT_LOG_FILE);
        options.addOption(OPT_RANDOM_SEED);
        options.addOption(OPT_PERCENT_CORRUPT);
        options.addOption(OPT_CORRUPT_PLACE);
        options.addOption(OPT_RESULTS_OUTPUT);
        options.addOption(OPT_GRAPH_INPUT);

        return options;
    }

}
