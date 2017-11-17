package trace;

/**
 * Created by Bowen Cai on 10/5/16.
 */
public class RangeWriteNode extends MemAccNode {

  public static NodeType TYPE = NodeType.RANGE_W;

  public final int length;

  public RangeWriteNode(short tid, long p, long add, int len, long i) {
    super(tid, add, p, i);
    length = len;
  }


  public String toString() {
    return gid + " #" + tid + "   pc:0x" + Long.toHexString(pc)  + " Range W  addr:" + addr + " length:" + length;
  }

  public long getAddr() {
    return addr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    RangeWriteNode that = (RangeWriteNode) o;

    if (addr != that.addr) return false;
    return length == that.length;

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int) (addr ^ (addr >>> 32));
    result = 31 * result + length;
    return result;
  }
}
