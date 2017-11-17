package aser.ufo.misc;

import aser.ufo.misc.Addr2line;
import aser.ufo.persist.EventDao;

/**
 * Created by cbw on 11/12/16.
 */
public class Addr2LineDB extends Addr2line {

  public final EventDao dao;

  public Addr2LineDB(CModuleList moduleList, EventDao dao) {
    super(moduleList);
    this.dao = dao;
  }
}
