package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.Timestamp2Column;
import com.notayessir.processor.util.FSPUtil;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Date;

@Decoder(ColumnType.MYSQL_TYPE_TIMESTAMP2)
public class Timestamp2ColumnDecoder extends AbsColumnDecoder<Timestamp2Column> {



    @Override
    public Timestamp2Column decode(ByteBuf in, ColumnDef columnDef) {
        byte[] bytes = ByteUtil.readBytesAndRelease(in.readBytes(4));

        byte fractionLen = columnDef.getMetadata()[0];
        if (fractionLen != 0){
            byte[] fractionBytes = ByteUtil.readBytesAndRelease(in.readBytes(FSPUtil.getLen(fractionLen)));
        }
        ArrayUtils.reverse(bytes);
        Date val = new Date(ByteUtil.readLong(bytes) * 1000);
        Timestamp2Column timestamp2Column = new Timestamp2Column();
        timestamp2Column.setVal(val);
        return timestamp2Column;
    }

}
