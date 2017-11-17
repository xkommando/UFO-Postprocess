/*******************************************************************************
 * Copyright (c) 2013 University of Illinois
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package trace;

public class LockNode extends ISyncNode {

	public static NodeType TYPE = NodeType.LOCK;
	//	private long did;// this is the ID of the trace from the same thread the
						// rwnode depends on
	public final long lockID;
	public final long pc;
	public final long idx;

	public LockNode(short tid, long lockID, long pc, long idx) {
		super(tid);
		this.lockID = lockID;
		this.pc = pc;
		this.idx = idx;
	}

	public long getAddr() {
		return lockID;
	}

	public String toString() {
		return gid + " #" + tid + " pc:0x" + Long.toHexString(pc) + " lock " + lockID;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		LockNode lockNode = (LockNode) o;

		if (lockID != lockNode.lockID) return false;
		if (pc != lockNode.pc) return false;
		return idx == lockNode.idx;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (int) (lockID ^ (lockID >>> 32));
		result = 31 * result + (int) (pc ^ (pc >>> 32));
		result = 31 * result + (int) (idx ^ (idx >>> 32));
		return result;
	}
}
