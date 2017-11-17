//import aser.ufo.trace.Bytes;
//import aser.ufo.trace.LoadingTask;
//import aser.ufo.trace.FileInfo;
//import aser.ufo.trace.TLEventSeq;
//import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
//import it.unimi.dsi.fastutil.longs.Long2ObjectRBTreeMap;
//import trace.AbstractNode;
//
//import java.io.*;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.util.Comparator;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * Created by Bowen Cai on 10/14/16.
// * feedback2bowen@outlook.com
// */
//public class Test {
//
//  public static long getLong48b(BufferedInputStream iter) throws IOException {
//    byte b0 = (byte) iter.read();
//    byte b1 = (byte) iter.read();
//    byte b2 = (byte) iter.read();
//    byte b3 = (byte) iter.read();
//    byte b4 = (byte) iter.read();
//    byte b5 = (byte) iter.read();
//    return Bytes.longs._Ladd((byte) 0x00, (byte) 0x00, b5, b4, b3, b2, b1, b0);
//  }
////  18 = -64 taskId
////  19 = 77
////      20 = -128
////      21 = 116
////      22 = 0
////      23 = 0
//  static void t3() {
//    long l = Bytes.longs._Ladd(
//        (byte)0,
//        (byte)0,
//        (byte)125,
//        (byte)8,
//        (byte)0,
//        (byte)0,
//        (byte)-17,
//        (byte)-64
//    );
//    System.out.println(l);
//    l = Bytes.longs._Ladd(
//        (byte)0,
//        (byte)0,
//        (byte)0,
//        (byte)0,
//        (byte)0,
//        (byte)66,
//        (byte)2,
//        (byte)80
//    );
//    System.out.println(l);
//    System.out.println("================");
//    l = Bytes.longs._Ladd(
//        (byte)0,
//        (byte)0,
//        (byte)0,
//        (byte)0,
//        (byte)0,
//        (byte)0,
//        (byte)223,
//        (byte)192
//    );
//    System.out.println(l);
//    System.out.println(Bytes.ints._Ladd(
//        (byte)0,
//        (byte)68,
//        (byte)5,
//        (byte)73)
//    );
//  }
//
//  static void t1() {
//    //    0 0 -8 -48 77 1  -> 21876984
//    ByteBuffer bf = ByteBuffer.allocate(16);
//    bf.order(ByteOrder.LITTLE_ENDIAN);
//    bf.put((byte)0);
//    bf.put((byte)0);
//    int b1 = 248;
////    b1 <<<= 1;
//    int b2 = 208;
//    bf.put((byte)b1);
//    bf.put((byte)b2);
//
//    bf.put((byte)77);
//    bf.put((byte)1);
//
//    bf.put((byte)0);
//    bf.put((byte)0);
//    long d = bf.getLong(0);
//    System.out.println(d);
////    System.out.println(Long.toHexString(d));
//  }
//
//  static void t4() throws Exception {
//    DataInputStream din = new DataInputStream(new FileInputStream("rv-engine/sample_trace/fp/0"));
//    int typeid = din.readByte();
//    short parTid = din.readShort();
//    typeid = din.readByte();
//    byte[] barr8 = new byte[8];
//    din.read(barr8, 0, 6);
//    long addr = Bytes.longs._Ladd(barr8, 0);
//    din.read(barr8, 0, 6);
//    long pc = Bytes.longs._Ladd(barr8, 0);
//    ;
//
//    System.out.println(pc);
//  }
//
//  static void t2() throws IOException {
////    byte[] bs = java.nio.file.Files.readAllBytes(new File("rv-engine/sample_trace/fp/0").toPath());
//    byte[] bs = java.nio.file.Files.readAllBytes(new File("rv-engine/sample_trace/fp/1").toPath());
//    for (int i = 0; i < bs.length; i++) {
//      int v = bs[i] & 0xFF;
//      System.out.println(i + " = " + v);
//    }
////    byte[] bs = java.nio.file.Files.readAllBytes(new File("rv-engine/sample_trace/test.t").toPath());
////    21872888
////  1954565568
////  3226304628
////  1299497088
//    ByteBuffer bf = ByteBuffer.allocate(16);
//    bf.order(ByteOrder.LITTLE_ENDIAN);
//
//    bf.put((byte)0);
//    bf.put((byte)0);
//    bf.put((byte)-8);
//    bf.put((byte)-48);
//    bf.put((byte)77);
//    bf.put((byte)1);
//    bf.put((byte)0);
//    bf.put((byte)0);
//
////
////    bf.put((byte)65);
////    bf.put((byte)0);
//
//    System.out.println(bf.getLong(0));
//  }
//  static void t5() throws Exception {
//    File f = new File("/home/xkommando/workspace/ufo/benchmarks/mupdf-1.9a-source/build/debug/ufo_thread_local_traces/0");
//    TLEventSeq seq = new LoadingTask(new FileInfo(f,f.length(), (short) 0), 999999, new AtomicInteger(0)).call();
//    for (AbstractNode n : seq.events) {
//      System.out.println(n);
//    }
//  }
//
//  static void t6() {
//    int a = 17;
//    int b = 24;
//    long val = Bytes.longs.add(a, b);
//    int aa = Bytes.longs.part1(val);
//    int bb = Bytes.longs.part2(val);
//  }
//
//  static void t7() {
//    Long2LongLinkedOpenHashMap map = new Long2LongLinkedOpenHashMap();
//    map.put(1l, 11l);
//    map.put(2l, 22l);
//    map.put(3l, 33l);
//    map.put(4l, 44l);
//    for (long p : map.values()) {
//      System.out.println(p);
//    }
//
//    for (long p : map.keySet()) {
//      System.out.println(p);
//    }
//  }
//
//  static void t8() {
//    Long2ObjectRBTreeMap tree = new Long2ObjectRBTreeMap(new Comparator<Long>() {
//      @Override
//      public int compare(Long x, Long y) {
//        return (y < x) ? -1 : ((x.longValue() == y.longValue()) ? 0 : 1);
//      }
//    });
//    tree.put(1, "1");
//    tree.put(9, "9");
//    tree.put(4, "4");
//    tree.put(11, "11");
//    tree.put(99, "99");
//    tree.put(-5, "-5");
//
//    System.out.println(tree.size());
//  }
//
//  public static void main(String[] args) throws Exception {
////    t2();
////    t3();
////    t4();
////    t5();
////    t6();
////    t7();
//    t8();
//  }
//
//
//}
//
//
//
//
//
//
//
//
//
