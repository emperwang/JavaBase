package com.wk;

import javax.transaction.xa.Xid;

public class MyXid implements Xid {
    public int formatId;
    public byte gtrib[];
    public byte bqual[];

    public MyXid(int formatId, byte gtrib[], byte bqual[]){
        this.formatId = formatId;
        this.gtrib = gtrib;
        this.bqual = bqual;
    }

    @Override
    public int getFormatId() {
        return formatId;
    }

    @Override
    public byte[] getGlobalTransactionId() {
        return gtrib;
    }

    @Override
    public byte[] getBranchQualifier() {
        return bqual;
    }
}
