package aser.ufo.trace;

import java.io.IOException;

/**
 * Created by xkommando on 11/8/16.
 * feedback2bowen@outlook.com
 */
public interface ByteReader {

  void init(FileInfo fileInfo) throws IOException;

  int read() throws IOException;
  void finish(FileInfo fileInfo) throws IOException;

}
