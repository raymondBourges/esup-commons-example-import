/**
 * 
 */
package org.esupportail.example.batch;

import java.util.Properties;

import org.esupportail.commons.services.application.VersionningService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author bourges
 *
 */
public class Launcher {

	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(Launcher.class);

	/**
	 * Print the syntax and exit.
	 */
	private static void syntax() {
		System.out.print(
				"syntax: " + Launcher.class.getSimpleName() + " <options>\n" +
				"where option can be:\n" +
				"- init-db\n" +
				"- upgrade-db\n");
	}

	/**
	 * Dispatch dependaing on the arguments.
	 * @param args
	 */
	protected static void dispatch(final String[] args) {
		switch (args.length) {
		case 0:
			syntax();
			break;
		case 1:
			if ("init-db".equals(args[0])) {
				initDb();
			} else if ("upgrade-db".equals(args[0])) {
				upgradeDb();
			} else {
				syntax();
			}
			break;
		default:
			syntax();
			break;
		}
	}

	/**
	 * get the versionning service by spring initialization 
	 * System environment variable generateDdl is set to true in order to force hibernate to generate DDL 
	 * @return versionning service
	 */
	private static VersionningService getVersionningService() {
		Properties properties = System.getProperties();
		properties.put("generateDdl", "true");
		System.setProperties(properties);
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("properties/applicationContext.xml");
		VersionningService versionningService =  (VersionningService) context.getBean("versionningService");
		return versionningService;
	}

	/**
	 * Upgrade the database
	 */
	private static void upgradeDb() {
		VersionningService versionningService = getVersionningService();
		versionningService.upgradeDatabase();
	}

	/**
	 * Initialize the database 
	 */
	private static void initDb() {
		VersionningService versionningService = getVersionningService();
		versionningService.initDatabase();
	}

	/**
	 * The main method, called by ant.
	 * @param args
	 */
	public static void main(final String[] args) {
		dispatch(args);
	}


}
