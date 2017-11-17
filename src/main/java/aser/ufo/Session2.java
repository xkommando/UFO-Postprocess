package aser.ufo;

import aser.ufo.misc.Pair;
import aser.ufo.misc.RawUaFCpx;
import aser.ufo.misc.RawUaf;
import aser.ufo.trace.AllocaPair;
import aser.ufo.trace.Indexer;
import config.Configuration;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import trace.DeallocNode;
import trace.MemAccNode;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by cbw on 12/14/16.
 */
public class Session2 extends Session {

  protected static final boolean LOAD_ONLY = false;

  public Session2(Configuration c) throws Exception {
    super(c);
  }

  int __c1 = 0;
  int __c2 = 0;
  int __c3 = 0;

  int loadedEventCount = 0;

  @Override
  public void start() {
    sessionID = 0;
    long _t1 = System.currentTimeMillis();
    uafID = 0;

    while (traceLoader.hasNext()) {
      sessionID++;
      Indexer indexer = new Indexer();
      traceLoader.populateIndexer(indexer);

      loadedEventCount += indexer.metaInfo.rawNodeCount;

      HashMap<MemAccNode, HashSet<AllocaPair>> candidateUafLs = indexer.getMachtedAcc();

      __c3 += candidateUafLs.size();
//      5485

      if (candidateUafLs.isEmpty())
        continue;
      System.out.println("candidateUafLs.size()  " + candidateUafLs.size() + "    " + __c3);

//      if (System.currentTimeMillis() > 1) {
//        System.out.println(" continue;");
//        continue;
//      }

      __c1 += indexer.metaInfo.sharedAddrs.size();
      for (HashSet<AllocaPair> sp : candidateUafLs.values()) {
        if (sp.size() > 1)
          __c2++;
      }

      if (LOAD_ONLY)
        continue;

      prepareConstraints(indexer);
      
      solver.setCurrentIndexer(indexer);

      LOG.info("candidateUafLs: {}", candidateUafLs.size());
      ct_candidataUaF.add(candidateUafLs.size());

      writerD.append("#" + sessionID + " Session")
          .append("   nodes number: " + (indexer.getGid2node() == null ? 0: indexer.getGid2node().size()))
          .append("   candidateUafLs: " + candidateUafLs.size()).append('\n');

      Iterator<Map.Entry<MemAccNode, HashSet<AllocaPair>>> iter = candidateUafLs.entrySet().iterator();
      while (iter.hasNext()) {
        List<RawUaf> ls = solveUafConstr(iter, UFO.PAR_LEVEL);
        if (ls != null && ls.size() > 0)
          outputUafLs(ls, indexer);
      }

      if (solver.ct_constr.size() > 0) {
        ct_vals.push(solver.ct_vals);
        Pair<Integer, Long> max_total = _Max_total(solver.ct_constr);
        ct_constr.push(max_total.value.intValue());
        if (max_total.value > Integer.MAX_VALUE)
          throw new RuntimeException("Overflow long -> int " + max_total.value);
        ct_max_constr.push(max_total.key);
//        int avg = _Max_total.value / solver.ct_constr.size();
//        ct_constr_max_avg.add(new Pair<Integer, Integer>(_Max_total.key, avg));
      }
      solver.reset(); // release constraints for this round
      writerD.append("\r\n");
    } // while

    System.out.println(__c1 + "   " + __c2 + "   " + __c3);
    System.out.println(config.window_size +  "  Time:  " + ((System.currentTimeMillis() - _t1) / 1000));
    _PrintStat();
    exe.shutdown();
    try {
      writerD.close();
      exe.awaitTermination(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      LOG.error(" error finishing ", e);
    }
    exe.shutdownNow();
  }

  private HashSet<Pair<Long, Long>> knownUAF = new HashSet<Pair<Long, Long>>(250);

  public HashMap<Integer, Integer> ct_uaf = new HashMap<Integer, Integer>();
  public IntArrayList ct_vals = new IntArrayList(1000);
  public IntArrayList ct_constr = new IntArrayList(1000);
  public IntArrayList ct_max_constr = new IntArrayList(1000);
  public IntArrayList ct_candidataUaF = new IntArrayList(1000);
//  public ArrayList<Pair<Integer, Integer>> ct_constr_max_avg = new ArrayList<Pair<Integer, Integer>>(100);


  public static Pair<Integer, Long> _Max_total(IntArrayList ct_constr) {
    long c = 0;
    int max = 0;
    for (int i : ct_constr) {
      c += i;
      if (i > max)
        max = i;
    }
    return new Pair<Integer, Long>(max, c);
  }
  public static int _Avg(IntArrayList ct_constr) {
    if (ct_constr == null || ct_constr.size() == 0)
      return -1;
    long c = 0;
    for (int i : ct_constr)
      c += i;

    return (int) (c /  ct_constr.size());
  }

  public void _PrintStat() {
    int max_max = _Max_total(ct_max_constr).key;
    Pair<Integer, Long> mc_constr = _Max_total(ct_constr);

    System.out.println("\r\n=================================\r\n");
    System.out.printf(
        "Window:%d   nodes: %d \r\n|session %d | avg vals %d | constr max %d  avg %d | total constr %d | total candidate UaF %d \r\n",
        traceLoader.getWindowSize(),
        loadedEventCount,
        sessionID,
        _Avg(ct_vals),
        max_max,
        _Avg(ct_constr),
        mc_constr.value,
        _Max_total(ct_candidataUaF).value
        );

    System.out.println("Solved UAF: " + ct_uaf);
  }

  @Override
  public void outputUafLs(List<RawUaf> uafLs, Indexer indexer) {
    LOG.info("Use-After-Free bugs: {}", uafLs.size());
    for (RawUaf uaf : uafLs) {
      long dePC;
      if (uaf instanceof RawUaFCpx) {
        RawUaFCpx cu = (RawUaFCpx) uaf;
        dePC = cu.pairs.hashCode();
      } else {
         dePC = uaf.deallocNode.pc;
      }
      Pair<Long, Long> pair = new Pair<Long, Long>(dePC, uaf.accNode.pc);
      if (knownUAF.contains(pair)) {
        System.out.println("Skip known access violation  ");
        continue;
      }
      knownUAF.add(pair);
      uafID++;

      writerD.append("#" + uafID + "  UAF").append("\r\n");
      if (uaf instanceof RawUaFCpx) {
        writerD.append("\r\n\r\n!!!!!!!!! Real UaF   " + ((RawUaFCpx)uaf).pairs.size() + "\r\n");
        System.out.println("\r\n\r\n!!!!!!!!! Real UaF   " + ((RawUaFCpx)uaf).pairs.size() + "\r\n");
      }

      writerD.append("\r\n------- access call stack  \r\n");
      writeCallStack(indexer, uaf.accNode);

      int sz = 1;
      if (uaf instanceof RawUaFCpx) {
        RawUaFCpx cu = (RawUaFCpx) uaf;
        sz = cu.pairs.size();
        for (AllocaPair ap : cu.pairs) {
          if (ap.deallocNode != null) {
          writerD.append("\r\n------- dealloc call stack  \r\n");
          writeCallStack(indexer, ap.deallocNode);
          }
        }
      } else {
        writerD.append("\r\n------- dealloc call stack  \r\n");
        writeCallStack(indexer, uaf.deallocNode);
      }

      if (ct_uaf.containsKey(sz)) {
        ct_uaf.put(sz, ct_uaf.get(sz) + 1);
      } else ct_uaf.put(sz, 1);
    }
  }

  protected void _stat(HashMap<MemAccNode, HashSet<AllocaPair>> candidateUafLs) {
    HashMap<Integer, Integer> ct = new HashMap<Integer, Integer>();
    int cm = 0;
    for (HashSet<AllocaPair> p : candidateUafLs.values()) {
      int sz = p.size();
      if (sz > 1)
        cm++;
      if (ct.containsKey(sz))
        ct.put(sz, ct.get(sz) + 1);
      else ct.put(sz, 1);
    }
    System.out.println(((float)cm / candidateUafLs.size()) + "    " + ct);
  }

  public List<RawUaf> solveUafConstr(Iterator<Map.Entry<MemAccNode, HashSet<AllocaPair>>>  iter, int limit) {

    CompletionService<RawUaf> cexe = new ExecutorCompletionService<RawUaf>(exe);
    int task = 0;
    while (iter.hasNext() && limit > 0) {
      limit--;
      Map.Entry<MemAccNode, HashSet<AllocaPair>> e = iter.next();
      final MemAccNode acc = e.getKey();
      final HashSet<AllocaPair> pairs = e.getValue();
      cexe.submit(new Callable<RawUaf>() {
        @Override
        public RawUaf call() throws Exception {
          IntArrayList bugSchedule;
          if (pairs.size() == 1) {
            AllocaPair p = pairs.iterator().next();
            bugSchedule = solver.searchUafSchedule(!LOAD_ONLY, new Pair<DeallocNode, MemAccNode>(p.deallocNode, acc));
            if (bugSchedule != null)
              return new RawUaf(acc, p.deallocNode, bugSchedule);
            else return null;
          } else {
            if (solver.mustUaF(!LOAD_ONLY, acc, pairs))
              return new RawUaFCpx(acc, pairs);
            else return null;
          }
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
