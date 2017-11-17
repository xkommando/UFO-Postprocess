package trace;

/**
 * Created by Bowen Cai on 10/5/16.
 */
public class RangeReadNode extends MemAccNode {

  public static NodeType TYPE = NodeType.RANGE_R;

  public final int length;

  public RangeReadNode(short tid, long p, long a, int len, long i) {
    super(tid, a, p, i);
    length = len;
  }

  public String toString() {
    return gid + " #" + tid + "   pc:0x" + Long.toHexString(pc)  + " Range R  addr:" + addr + " length:" + length;
  }
  public long getAddr() {
    return addr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    RangeReadNode that = (RangeReadNode) o;

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
