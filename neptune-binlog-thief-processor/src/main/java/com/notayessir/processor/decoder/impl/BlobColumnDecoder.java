package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.BlobColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_BLOB)
public class BlobColumnDecoder extends AbsColumnDecoder<BlobColumn> {

    @Override
    public BlobColumn decode(ByteBuf in, ColumnDef columnDef) {
        int packLength = Byte.toUnsignedInt(columnDef.getMetadata()[0]);
        int blobLen = ByteUtil.readIntAndRelease(in.readBytes(packLength));
        byte[] val = ByteUtil.readBytesAndRelease(in.readBytes(blobLen));
        BlobColumn blobColumn = new BlobColumn();
        blobColumn.setVal(val);
        return blobColumn;
    }



}
