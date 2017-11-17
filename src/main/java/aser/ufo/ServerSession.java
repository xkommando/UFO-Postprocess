//package aser.ufo;
//
//import aser.ufo.persist.EventDao;
//import aser.ufo.misc.Pair;
//import aser.ufo.misc.RawUaf;
//import aser.ufo.trace.Indexer;
//import config.Configuration;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import trace.DeallocNode;
//import trace.MemAccNode;
//
//import java.util.List;
//import java.util.concurrent.Callable;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
///**
// *
// * work on the server,
// * Process trace, solve constraint.
// * Does not map events to source.
// * Instead, persist current session to file.
// *
// * The mapping and presentation is done by client machine.
// *
// *
// * Created by Bowen Cai on 11/11/16.
// * feedback2bowen@outlook.com
// */
//public class ServerSession extends Session {
//
//  private static final Logger LOG = LoggerFactory.getLogger(ServerSession.class);
//
//  private final EventDao dao;
//
//  public ServerSession(Configuration c) throws Exception {
//    super(c);
//    dao = new EventDao();
//  }
//
//  public void init() {
//    super.init();
//    dao.init(config.appname);
//
//    windowSize = (int) config.window_size;
//    if (windowSize < 10) {
//      windowSize = (int) (UFO.MAX_MEM_SIZE / UFO.AVG_EVENT / traceLoader.fileInfoMap.size()
//          // 50% mem for events
//          / 2
//          // 47.1%, only 47.1 events will stay after indexing
//          / 0.471
//      );
//    }
//  }
//
//  public void start() {
//    int id = 0;
//    try {
//
//      while (traceLoader.hasNext()) {
//
//        final int curID = id;
//        id++;
//
//        final List<Pair<DeallocNode, MemAccNode>> causalConstrLs;
//        { // indexer life time
//          final Indexer indexer = traceLoader.populateIndexer(getWindowSize());
//
//          int sac = indexer.metaInfo.sharedAddrs.size();
//          if (sac < 1)
//            continue;
//          LOG.info("Shared address count: {} ", sac);
//          int cac = indexer.getTSAcc2Dealloc().size();
//          LOG.info("Conflicting access count: {}", cac);
//          if (cac < 1)
//            continue;
//
//          // IO intensive
//          Future<Integer> fioNodes = exe.submit(new Callable<Integer>() {
//            public Integer call() throws Exception {
//              return dao.saveEvents(curID, indexer.getAllNodeSeq());
//            }
//          });
//
//          // CPU intensive
//          solver.declareVariables(indexer.getAllNodeSeq());
//          solver.buildIntraThrConstr(indexer.getTSTid2sqeNodes());
//          solver.buildSyncConstr(indexer);
//
//          // need reach engine ready
//          causalConstrLs = findCandidateUafLs(indexer);
//          LOG.info("causalConstr count: {}", causalConstrLs.size());
//
//          int nsz = fioNodes.get();
//          LOG.info("All nodes (shared acc) saved to DB: {}", nsz);
//          System.gc();
//          // GC the indexer and all nodes, leave nothing but the causalConstrLs and solver
//          // yield memory for z3
//        }
//
//        // invoke many z3 to search for schedules
//        // uafLs1st is immutable
//        final List<RawUaf> uafLs1st = solveUafConstr(causalConstrLs);
//
//        Future<Integer> fioUafR = exe.submit(new Callable<Integer>() {
//          public Integer call() throws Exception {
//            return dao.saveSchedules_1st(curID, uafLs1st);
//          }
//        });
//
//        List<RawUaf> uafLs2st = trimFP(uafLs1st, causalConstrLs);
//        int savedScd = dao.saveSchedules(curID, uafLs2st);
//        LOG.info("Buggy schedules saved to DB: {}", savedScd);
//
//        int nsz = fioUafR.get();
//        LOG.info("First round RAW schedules saved to DB: {}", nsz);
//
//        LOG.info("Use-After-Free bugs: {}", uafLs1st.size());
//
//        // release constraints for this round
//        // GC all object
//        solver.reset();
//        System.gc();
//      } // while
//    } catch (Exception e) {
//      LOG.error("DB exception, stop session.", e);
//    }
//
//    exe.shutdown();
//    try {
//      exe.awaitTermination(10, TimeUnit.SECONDS);
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
//    exe.shutdownNow();
//  }
//
//
//}
