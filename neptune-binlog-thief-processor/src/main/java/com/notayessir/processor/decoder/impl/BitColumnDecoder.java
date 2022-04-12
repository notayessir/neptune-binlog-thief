package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.BitColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_BIT)
public class BitColumnDecoder extends AbsColumnDecoder<BitColumn> {


    @Override
    public BitColumn decode(ByteBuf in, ColumnDef columnDef) {
        int byteNumber = Byte.toUnsignedInt(columnDef.getMetadata()[1]);
        byte[] bytes = ByteUtil.readBytesAndRelease(in.readBytes(byteNumber));
        String val = ByteUtil.bytesToBinaryString(bytes);
        BitColumn bitColumn = new BitColumn();
        bitColumn.setVal(val);
        return bitColumn;
    }


}
