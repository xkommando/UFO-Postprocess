package trace;

/**
 * Created by Bowen Cai on 10/10/16.
 * feedback2bowen@outlook.com
 */
public class AllocNode extends AbstractNode {

  public static NodeType TYPE = NodeType.ALLOC;

  public final long addr;
  public final int length;
  public final long pc;
  public final long idx;
  public AllocNode(short tid, long pc_, long a, int len, long i) {
    super(tid);
    addr = a;
    length = len;
    pc = pc_;
    idx = i;
  }


  public String toString() {
    return gid + " #" + tid  + "   pc:0x" + Long.toHexString(pc) + " Alloc  addr:" +  addr + "  fsize:" + length;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    AllocNode allocNode = (AllocNode) o;

    if (addr != allocNode.addr) return false;
    if (length != allocNode.length) return false;
    if (pc != allocNode.pc) return false;
    return idx == allocNode.idx;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int) (addr ^ (addr >>> 32));
    result = 31 * result + length;
    result = 31 * result + (int) (pc ^ (pc >>> 32));
    result = 31 * result + (int) (idx ^ (idx >>> 32));
    return result;
  }
}
