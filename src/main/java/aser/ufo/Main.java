package aser.ufo;

import config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Bowen Cai on 10/9/16.
 * feedback2bowen@outlook.com
 */
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {

    Configuration config = new Configuration(args);
    extendConfig(config);

//    Session s = new Session(config);
    Session s = new Session2(config);
//    Session s = new Session3(config);
//    Session s = new ServerSession(config);
    s.init();
    s.start();
  }

  private static void extendConfig(Configuration config) throws IOException {
    Properties properties = new Properties();
    File cfg = new File("config.properties");
    if (!cfg.isFile())
      return;
    FileInputStream fin = new FileInputStream(cfg);
    properties.load(fin);
    config.traceDir = properties.getProperty("trace_dir");
//    config.binaryImage = properties.getProperty("binary_image");
    config.appname = properties.getProperty("app_name");

    LOG.info("app_name {}; trace_dir {}.", config.appname, config.traceDir);

    String val = properties.getProperty("solver_time");
    if (val != null && val.length() > 0) {
      config.solver_timeout = Long.parseLong(val);
      LOG.info("solver_timeout {}", config.solver_timeout);
    }
    val = properties.getProperty("solver_mem");
    if (val != null && val.length() > 0) {
      config.solver_memory = Long.parseLong(val);
      LOG.info("solver_mem {}", config.solver_memory);
    }
    val = properties.getProperty("window_size");
    if (val != null && val.length() > 0) {
      config.window_size = Long.parseLong(val);
      LOG.info("window_size {}", config.window_size);
    }

    fin.close();
  }


}
