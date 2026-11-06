package gr.uoi.ooad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gr.uoi.ooad.parser.ast.ASTParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.jupiter.api.Test;

public class LoggingConfigurationTest {

    @Test
    void projectLoggingConfigurationIsLoadedTest() {
        Configuration configuration = LoggerContext.getContext(false).getConfiguration();

        boolean containsStdout = configuration.getRootLogger().getAppenders().containsKey("stdout");
        boolean isInfoEnabled = LogManager.getLogger(ASTParser.class).isInfoEnabled();

        assertEquals("OOAD", configuration.getName());
        assertTrue(containsStdout, "The root logger has no stdout appender");
        assertTrue(isInfoEnabled, "INFO messages of the parser are not logged");
    }
}
