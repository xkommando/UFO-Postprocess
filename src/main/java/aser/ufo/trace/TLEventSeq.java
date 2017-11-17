package aser.ufo.trace;

import aser.ufo.trace.TLHeader;
import aser.ufo.trace.TLStat;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import trace.AbstractNode;

import java.util.ArrayList;

/**
 * Created by Bowen Cai on 10/20/16.
 * feedback2bowen@outlook.com
 */
public class TLEventSeq {

  public final short tid;
  public final ShortOpenHashSet newTids = new ShortOpenHashSet(40);
//  public final ShortOpenHashSet endedTids = new ShortOpenHashSet(60);
  public TLHeader header;
  public ArrayList<AbstractNode> events;
  public final TLStat stat = new TLStat();

  public TLEventSeq(short tid) {
    this.tid = tid;
  }
}
