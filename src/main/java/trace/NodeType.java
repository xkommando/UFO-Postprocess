package trace;

/**
 * Created by cbw on 12/12/16.
 */
public enum  NodeType {
    INIT,
    READ, WRITE, LOCK, UNLOCK, WAIT,
    NOTIFY, START, JOIN, BRANCH, BB,
    PROPERTY, ALLOC, DEALLOC, RANGE_W, RANGE_R,
    BEGIN, END, FUNC_IN, FUNC_OUT
}
