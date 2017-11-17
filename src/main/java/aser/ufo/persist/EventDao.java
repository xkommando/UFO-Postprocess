package aser.ufo.persist;

import aser.ufo.misc.RawUaf;
import trace.AbstractNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbw on 11/11/16.
 */
public class EventDao {

  public void init(String appname) {
    String dbName = appname.replace(File.separatorChar, '_');
  }

  public int saveEvents(int sessionID, ArrayList<AbstractNode> allSharedNodes) {
    return 0;
  }

  public int saveSchedules_1st(int sessionID, List<RawUaf> uafLs) {
    return 0;
  }

  public int saveSchedules(int sessionID, List<RawUaf> uafLs) {
    return 0;
  }

}
