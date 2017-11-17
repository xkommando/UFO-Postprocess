//import aser.ufo.misc.CModule;
//import aser.ufo.misc.CModuleList;
//import aser.ufo.trace.FileInfo;
//import aser.ufo.trace.LoadingTask;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * Created by cbw on 11/10/16.
// */
//public class LoadingTest {
//
//  public static CModuleList loadCModuleInfo(File f) {
//    BufferedReader reader = null;
//    CModuleList symbolizeTool = new CModuleList();
//
//    try {
//      reader = new BufferedReader(new FileReader(f));
//      String line;
//      while (null != (line = reader.readLine())) {
//        String[] info = line.split(" ");
//        System.out.println(info.length);
//        if (info.length != 4) {
//          throw new IllegalArgumentException("module info format error " + f);
//        }
//        long base = Long.parseLong(info[1].trim(), 16);
//        long begin = Long.parseLong(info[2].trim(), 16);
//        long end = Long.parseLong(info[3].trim(), 16);
//        CModuleSection m = new CModuleSection(info[0].trim(), base, begin, end);
//        symbolizeTool.add(m);
//      }
//      reader.close();
//      return symbolizeTool;
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    } finally {
//      if (reader != null) {
//        try {
//          reader.close();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//  }
//
//  public static void main(String[] args) throws Throwable {
//    String path = "/home/cbw/workspace/benchmarks/concurrency-bugs-master/pbzip2-0.9.4/" +
//        "pbzip2-0.9.4/ufo_traces_32023/_module_info.txt";
//    loadCModuleInfo(new File(path));
//  }
//
//  static void t1() throws Throwable {
//
//    Thread.sleep(5000);
//    System.out.println("Start");
//    File t1 = new File("sample_trace/fluid57/1");
//    FileInfo fileInfo = new FileInfo(t1, t1.length(), (short) 1);
//    LoadingTask task = new LoadingTask(fileInfo, 2000 * 10000, new AtomicInteger(0));
//    task.call();
//    System.out.println("Finished");
//    Thread.sleep(20000);
//    System.out.println("Exit");
//  }
//}
