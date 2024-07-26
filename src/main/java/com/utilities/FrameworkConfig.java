package com.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static com.configuration.BaseTest.printMsgOnConsole;


public final class FrameworkConfig {

	/**
	 * The filepath to the configuration file.
	 */
	private static String filePath;

	/**
	 * The Configuration file data.
	 */
	private static Properties configuration;

	/**
	 * There should be no instance of this class.
	 */
	private FrameworkConfig() { }

	/**
	 * Initialize the Framework Configuration loading the configuration
	 * from the file at configFilePath.
	 * @param configFilePath - The file path to the configuration file.
	 */
	public static void init(final String configFilePath) {
		filePath = configFilePath;
		printMsgOnConsole("File Path : ==== " + filePath);
		loadFromFile();
	}

	/**
	 * Load the configuration file.
	 */
	private static void loadFromFile() {
		configuration = new Properties();
		try {
			configuration.load(
					Files.newInputStream(
							Paths.get(filePath)));
		} catch (IOException e) {
			printMsgOnConsole("Failed to load configuration file from path :"
                    + "\n"
					+ filePath
					+ "\n"
					+ e.getMessage());
		}
	}

	/**
	 * Retrieve a value from the Configuration File using the 'key'.
	 * @param key - The key to use to locate the value in the
	 *               configuration file.
	 * @return A {@link String } containing the value stored at the key
	 * in the configuration file.
	 */
	public static String get(final String key) {
		return configuration.getProperty(key);
	}

}
