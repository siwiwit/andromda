package org.andromda.schema2xmi;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Converts a database schema to an XMI document.
 * 
 * @author Chad Brandon
 */
public class Schema2XMI
{
    private static Options options;

    /**
     * The command to display help
     */
    private static final String HELP = "h";

    /**
     * The command line argument to specify the input model file.
     */
    private static final String INPUT_MODEL = "i";

    /**
     * The command line argument to specify the JDBC driver class
     */
    private static final String DRIVER = "d";

    /**
     * The command line argument to specify the schema user.
     */
    private static final String USER = "u";

    /**
     * The command line argument to specify the schema user password.
     */
    private static final String PASSWORD = "p";

    /**
     * The command line argument to specify the connection URL.
     */
    private static final String CONNECTION_URL = "c";

    /**
     * The command line argument to specify the transformed output file.
     */
    private static final String OUTPUT_NAME = "o";

    /**
     * The command line argument specifying the URI to the type mappings file.
     */
    private static final String MAPPINGS = "m";

    /**
     * The command line argument specifying the package to which the model
     * element will be generated.
     */
    private static final String PACKAGE = "P";

    /**
     * The command line argument specifying the tables names to match on
     */
    private static final String TABLE_PATTERN = "t";

    /**
     * The command line argument specifying whether or not to include tagged
     * values.
     */
    private static final String INCLUDE_TAGGED_VALUES = "v";

    /**
     * Configure the CLI options.
     */
    static
    {
        try
        {
            AndroMDALogger.configure();
            // turn off validation because of the incorrect parsers 
            // in the JDK
            XmlObjectFactory.setDefaultValidating(false);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }

        options = new Options();

        Option option = new Option(HELP, false, "Display help information");
        option.setLongOpt("help");
        options.addOption(option);

        option = new Option(
            INPUT_MODEL,
            true,
            "Input model file (to which model elements will be added)");
        option.setLongOpt("input");
        options.addOption(option);

        option = new Option(DRIVER, true, "JDBC driver class");
        option.setLongOpt("driver");
        options.addOption(option);

        option = new Option(CONNECTION_URL, true, "JDBC connection URL");
        option.setLongOpt("connectionUrl");
        options.addOption(option);

        option = new Option(USER, true, "Schema user name");
        option.setLongOpt("user");
        options.addOption(option);

        option = new Option(PASSWORD, true, "Schema user password");
        option.setLongOpt("password");
        options.addOption(option);

        option = new Option(
            MAPPINGS,
            true,
            "The type mappings URI (i.e. file:${basedir}/DataypeMappings.xml)");
        option.setLongOpt("mappings");
        options.addOption(option);

        option = new Option(
            TABLE_PATTERN,
            true,
            "The table name pattern of tables to process (regular expression)");
        option.setLongOpt("tablePattern");
        options.addOption(option);

        option = new Option(PACKAGE, true, "The package to output classifiers");
        option.setLongOpt("package");
        options.addOption(option);

        option = new Option(
            INCLUDE_TAGGED_VALUES,
            true,
            "Whether or not to include persistence tagged values, default is true");
        option.setLongOpt("taggedValues");
        options.addOption(option);

        option = new Option(
            OUTPUT_NAME,
            true,
            "Set output name to which the result of the transformation will be written");
        option.setLongOpt("output");
        options.addOption(option);
    }

    /**
     * Display usage information based upon current command-line option
     * configuration.
     */
    public static void displayHelp()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
            Schema2XMI.class.getName() + " [options] ...]]",
            "\nOptions:",
            options,
            "\n");

    }

    /**
     * Parse a string-array of command-line arguments.
     * <p>
     * This will parse the arguments against the configured odm2flatfile
     * command-line options, and return a <code>CommandLine</code> object from
     * which we can retrieve the command like options.
     * </p>
     * 
     * @see <a href="http://jakarta.apache.org/commons/cli/">CLI </a>
     * @param args The command-line arguments to parse.
     * @return The <code>CommandLine</code> result.
     * @throws ParseException If an error occurs while parsing the command-line
     *         options.
     */
    public CommandLine parseCommands(String[] args) throws ParseException
    {
        CommandLineParser parser = new PosixParser();
        return parser.parse(options, args);
    }

    public static void main(String args[])
    {
        Schema2XMI schema2Xmi = new Schema2XMI();
        try
        {
            CommandLine commandLine = schema2Xmi.parseCommands(args);
            if (commandLine.hasOption(HELP)
                || !(commandLine.hasOption(INPUT_MODEL)
                &&   commandLine.hasOption(OUTPUT_NAME)
                &&   commandLine.hasOption(DRIVER)
                &&   commandLine.hasOption(CONNECTION_URL)
                &&   commandLine.hasOption(USER)
                &&   commandLine.hasOption(PASSWORD)))
            {
                Schema2XMI.displayHelp();
            }
            else
            {
                String inputModel = commandLine.getOptionValue(INPUT_MODEL);
                SchemaTransformer transformer = new SchemaTransformer(
                    commandLine.getOptionValue(DRIVER),
                    commandLine.getOptionValue(CONNECTION_URL),
                    commandLine.getOptionValue(USER),
                    commandLine.getOptionValue(PASSWORD));

                // set the extra options
                transformer.setTypeMappings(commandLine
                    .getOptionValue(MAPPINGS));
                transformer.setPackageName(commandLine.getOptionValue(PACKAGE));
                transformer.setTableNamePattern(commandLine
                    .getOptionValue(TABLE_PATTERN));
                transformer.setIncludeTaggedValues(
                    Boolean.valueOf(
                        commandLine.getOptionValue(INCLUDE_TAGGED_VALUES)).booleanValue());

                String outputLocation = commandLine.getOptionValue(OUTPUT_NAME);
                transformer.transform(inputModel, outputLocation);
            }
        }
        catch (Throwable th)
        {
            th = getRootCause(th);
            th.printStackTrace();
        }
    }

    private static Throwable getRootCause(Throwable th)
    {
        Throwable cause = th;
        if (cause.getCause() != null)
        {
            cause = cause.getCause();
            cause = getRootCause(cause);
        }
        return cause;
    }
}