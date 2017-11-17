//package aser.ufo.trace;
//
//import aser.ufo.misc.FileInfo;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.xerial.snappy.Snappy;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by xkommando on 11/9/16.
// * feedback2bowen@outlook.com
// */
//public class UnCompressorReader implements ByteReader {
//
//  private static Logger LOG = LoggerFactory.getLogger(UnCompressorReader.class);
//
//  private FileInputStream fin;
//  private byte[] buf;
//  private long lastFileOffset;
//  private int bufOffset;
//  private boolean readEOF = false;
//
//  private void fillIn(int nextBufOS) throws IOException {
//    int rawLen = getInt_Move(fin);
//    if (rawLen == -1) {
//      readEOF = true;
//      return;
//    }
//
//    byte[] rawBytes = new byte[rawLen];
//    int brd = fin.read(rawBytes);
//    if (brd < 0 || brd != rawLen)
//      throw new RuntimeException(
//          "Could not read enough data," +
//              " required " + rawLen + "  actual " + brd);
//
//    LOG.debug("read next chuck from {}, new data {}", lastFileOffset, rawBytes);
//
//    buf = Snappy.uncompress(rawBytes);
//    bufOffset = nextBufOS;
//    if (bufOffset >= buf.length)
//      throw new RuntimeException(
//          "bufferOffset exceeded unzipped buffer length," +
//              " buffer length " + buf.length + "   offset (from file info) " + bufOffset);
//
//    LOG.debug("uncompressed {}, buf offset is {}", buf.length, bufOffset);
//  }
//
//  public void init(FileInfo fileInfo) throws IOException {
//    fin = new FileInputStream(fileInfo.file);
//    long s = fin.skip(fileInfo.fileOffset);
//    if (s != fileInfo.fileOffset)
//      throw new RuntimeException("wrong position, need to skip " + fileInfo.fileOffset + "  actual " + s);
//    lastFileOffset = fileInfo.fileOffset;
//
//    fillIn(fileInfo.bufferOffset);
//  }
//
//  public int read() throws IOException {
//    if (readEOF)
//      return -1;
//    if (bufOffset < buf.length) {
//      int v = buf[bufOffset] & 0xff;
//      bufOffset++;
//      return v;
//    } else { // cEnd of unzipped
//      fillIn(0);
//      lastFileOffset = fin.getChannel().position();
//      return read();
//    }
//  }
//
//  public void finish(FileInfo fileInfo) throws IOException {
//    fileInfo.fileOffset = lastFileOffset;
//    fileInfo.bufferOffset = bufOffset;
//    fin.close();
//    LOG.debug("new file offset {}, new buffer offset {}", lastFileOffset, bufOffset);
//  }
//
//  private static int getInt_Move(InputStream breader) throws IOException {
//    byte b1;
//    byte b2;
//    byte b3;
//    byte b4;
//    int i = breader.read();
//    if (i == -1)
//      return -1;
//    b1 = (byte)i;
//
//    i = breader.read();
//    if (i == -1)
//      return -1;
//    b2 = (byte)i;
//
//    i = breader.read();
//    if (i == -1)
//      return -1;
//    b3 = (byte)i;
//
//    i = breader.read();
//    if (i == -1)
//      return -1;
//    b4 = (byte)i;
//    return Bytes.ints._Ladd(b4, b3, b2, b1);
//  }
//}
