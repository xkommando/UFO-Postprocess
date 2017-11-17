package trace;

/**
 * Created by Bowen Cai on 10/10/16.
 * feedback2bowen@outlook.com
 */
public class DeallocNode extends AbstractNode {

  public static NodeType TYPE = NodeType.DEALLOC;

  public final long addr;
  public        int length;
  public final long pc;
  public final long idx;

  public DeallocNode(short tid, long p, long a, long i) {
    super(tid);
    addr = a;
    pc = p;
    length = -1;
    idx = i;
  }

  public String toString() {
    return gid + "#" + tid  + "   pc:0x" + Long.toHexString(pc)  + "   Dealloc addr:" +  addr + "  fsize:" + length;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    DeallocNode that = (DeallocNode) o;

    if (addr != that.addr) return false;
    if (pc != that.pc) return false;
    return idx == that.idx;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int) (addr ^ (addr >>> 32));
    result = 31 * result + (int) (pc ^ (pc >>> 32));
    result = 31 * result + (int) (idx ^ (idx >>> 32));
    return result;
  }
}
