import aser.ufo.trace.Bytes;
import aser.ufo.trace.TLHeader;
import org.xerial.snappy.Snappy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Created by xkommando on 11/8/16.
 * feedback2bowen@outlook.com
 */
public class TestSnappy {

  public static void main(String[] args) throws Throwable {
    String tracePath = "sample_trace/test/0";
    byte[] bs = Files.readAllBytes(new File(tracePath).toPath());
    FileInputStream fin = new FileInputStream(tracePath);
    int h = fin.read();
    TLHeader header = getHeader(h, fin);
    int foff = (int) fin.getChannel().position();
    System.out.println(header);
    int blockLen = getInt(fin);
    foff += 4;
    fin.close();
    byte[] comp = Arrays.copyOfRange(bs, foff, foff + blockLen);
//    int len = SnappyDecompressor.decompress(comp).getLength();
//    int len = Snappy.uncompress(comp).length;
    System.out.println(comp.length);
    Files.write(new File("out.sp").toPath(), comp);
    System.out.println(Snappy.uncompress(comp).length);
  }

  private static TLHeader getHeader(final int typeIdx,
                                    InputStream breader) throws IOException {
    if (typeIdx != 13)
      throw new RuntimeException("Could not read header");
//        const u64 version = UFO_VERSION;
//        const TidType tid;
//        const u64 timestamp;
//        const u32 length;
    long version = getLong64b(breader);
    short tidParent = getShort(breader);
    long time = getLong64b(breader);
    int len = getInt(breader);
//    LOG.info(">>> UFO header version:{}  tid:{}  time:{}  len:{}", version , tidParent, new Date(time), len);
    return new TLHeader(tidParent, version, time, len);
  }

  public static short getShort(InputStream breader) throws IOException {
    byte b1 = (byte) breader.read();
    byte b2 = (byte) breader.read();
    return Bytes.shorts.add(b2, b1);
  }

  public static int getInt(InputStream breader) throws IOException {

    byte b1 = (byte) breader.read();
    byte b2 = (byte) breader.read();
    byte b3 = (byte) breader.read();
    byte b4 = (byte) breader.read();
    return Bytes.ints._Ladd(b4, b3, b2, b1);
//    ints.add(getShort(breader), getShort(breader))
  }


  public static long getLong48b(InputStream breader) throws IOException {
    byte b0 = (byte) breader.read();
    byte b1 = (byte) breader.read();
    byte b2 = (byte) breader.read();
    byte b3 = (byte) breader.read();
    byte b4 = (byte) breader.read();
    byte b5 = (byte) breader.read();
    return Bytes.longs._Ladd((byte) 0x00, (byte) 0x00, b5, b4, b3, b2, b1, b0);
  }

  public static long getLong64b(InputStream breader) throws IOException {
    byte b0 = (byte) breader.read();
    byte b1 = (byte) breader.read();
    byte b2 = (byte) breader.read();
    byte b3 = (byte) breader.read();
    byte b4 = (byte) breader.read();
    byte b5 = (byte) breader.read();
    byte b6 = (byte) breader.read();
    byte b7 = (byte) breader.read();
    return Bytes.longs._Ladd(b7, b6, b5, b4, b3, b2, b1, b0);
  }
}
