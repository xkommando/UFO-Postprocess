package trace;

/**
 * Created by Bowen Cai on 10/8/16.
 */
public class TBeginNode extends ISyncNode {

  public static NodeType TYPE = NodeType.BEGIN;

  public final short tidParent;
  public final int eTime;

  public TBeginNode(short thisTid, short titPa, int t) {
    super(thisTid);
    tidParent = titPa;
    eTime = t;
  }

  public long getAddr() {
    return tidParent;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    TBeginNode TBeginNode = (TBeginNode) o;

    if (tidParent != TBeginNode.tidParent) return false;
    return eTime == TBeginNode.eTime;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int) tidParent;
    result = 31 * result + eTime;
    return result;
  }
}
