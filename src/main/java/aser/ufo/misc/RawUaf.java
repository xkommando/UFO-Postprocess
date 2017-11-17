package aser.ufo.misc;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import trace.DeallocNode;
import trace.MemAccNode;

/**
 * before first normalization
 *
 * Created by Bowen Cai on 10/24/16.
 * feedback2bowen@outlook.com
 */
public class RawUaf {
  public final MemAccNode accNode;
  public final DeallocNode deallocNode;
  public final IntArrayList schedule;

  public RawUaf(MemAccNode accNode, DeallocNode deallocNode, IntArrayList schedule) {
    this.accNode = accNode;
    this.deallocNode = deallocNode;
    this.schedule = schedule;
  }
}
