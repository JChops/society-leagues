import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.*

appender("FILE", RollingFileAppender) {
 def rootDir = new File("/data/logs");
  
  if (!rootDir.exists()) {
     rootDir = new File("build/logs");
  }

  file = rootDir.getAbsolutePath() + "/society-league.log";
  encoder(PatternLayoutEncoder) {
    pattern = "%-5p %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %m%n"
  }
  filter(ThresholdFilter) { level = TRACE  }
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = rootDir.getAbsolutePath()+ "/society-league.log.%d.gz"
  }
}

appender("CONSOLE", ch.qos.logback.core.ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p  [%t] %c{1}:%M:%L - %m%n"
  }
  filter(ThresholdFilter) {
  	level = TRACE  }
}

logger("com.society", DEBUG)
logger("org.springframework.security",INFO)
logger("org.springframework.aop",INFO)
logger("org.springframework.cglib",INFO)
logger("org.springframework.web",INFO)
logger("org.springframework.boot.actuate.trace",INFO)
logger("log4jdbc.log4j2",DEBUG)
root(INFO, ["FILE", "CONSOLE"])
