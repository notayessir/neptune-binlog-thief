package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.MediumBlobColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_MEDIUM_BLOB)
public class MediumBlobColumnDecoder extends AbsColumnDecoder<MediumBlobColumn> {



    @Override
    public MediumBlobColumn decode(ByteBuf in, ColumnDef columnDef) {
        int len = ByteUtil.readEncodedInt(in);
        byte[] val = ByteUtil.readBytesAndRelease(in.readBytes(len));
        MediumBlobColumn mediumBlobColumn = new MediumBlobColumn();
        mediumBlobColumn.setVal(val);
        return mediumBlobColumn;
    }


}
