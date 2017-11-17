package aser.ufo;

import aser.ufo.misc.Pair;
import aser.ufo.misc.RawUaf;
import aser.ufo.trace.AllocaPair;
import aser.ufo.trace.HeapMonitor;
import aser.ufo.trace.Indexer2;
import config.Configuration;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import trace.DeallocNode;
import trace.MemAccNode;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by cbw on 5/17/17.
 */
public class Session3 extends Session2 {

  public Session3(Configuration c) throws Exception {
    super(c);
  }

  @Override
  public void start() {
    sessionID = 0;
    uafID = 0;

    long count_realUaf = 0;
    while (traceLoader.hasNext()) {
      sessionID++;
      Indexer2 indexer = new Indexer2();
      traceLoader.populateIndexer(indexer);
      List<Pair<HeapMonitor.AllocPair, MemAccNode> > possibleUaf = indexer.getPossibleUafList();
      if (possibleUaf.isEmpty()) {
//        System.out.print("#" + sessionID + " empty   ");
        continue;
      }
      System.out.println();

      __c3 += possibleUaf.size();
      System.out.println("possibleUaf.size()  " + possibleUaf.size() + "    " + __c3);

      __c1 += indexer.metaInfo.sharedAddrs.size();

      if (LOAD_ONLY)
        continue;

      prepareConstraints(indexer);

      writerD.append("#" + sessionID + " Session")
          .append("   possibleUaf: " + possibleUaf.size()).append('\n');

      List<RawUaf> rawUafs = solveUafLs(possibleUaf);
      if (rawUafs.isEmpty()) {
        writerD.append("no real UaF\r\n");
        continue;
      }
      count_realUaf += rawUafs.size();

      outputUafLs(rawUafs, indexer);

      solver.reset(); // release constraints for this round
      writerD.append("\r\n");
    } // while

    System.out.println("\r\nDone.\r\npossible UaF:" + __c3 + "   real Uaf:" + count_realUaf);
    exe.shutdown();
    try {
      writerD.close();
      exe.awaitTermination(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      LOG.error(" error finishing ", e);
    }
    exe.shutdownNow();
  }

  public List<RawUaf> solveUafLs(List<Pair<HeapMonitor.AllocPair, MemAccNode> > uafLs) {

    CompletionService<RawUaf> cexe = new ExecutorCompletionService<RawUaf>(exe);
    int task = 0;
    for (Pair<HeapMonitor.AllocPair, MemAccNode> pair : uafLs) {
      final MemAccNode acc = pair.value;
      final HeapMonitor.AllocPair ap = pair.key;
      cexe.submit(new Callable<RawUaf>() {
        @Override
        public RawUaf call() throws Exception {
          IntArrayList bugSchedule;
          bugSchedule = solver.searchUafSchedule(!LOAD_ONLY, new Pair<DeallocNode, MemAccNode>(ap.deallocEvent, acc));
          if (bugSchedule != null)
            return new RawUaf(acc, ap.deallocEvent, bugSchedule);
          else return null;
        }
      });
      task++;
    }

    ArrayList<RawUaf> ls = new ArrayList<RawUaf>(task);
    try {
      while (task-- > 0) {
        Future<RawUaf> f = cexe.take(); //blocks if none available
        RawUaf uaf = f.get();
        if (uaf != null)
          ls.add(uaf);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return ls;
  }

}
