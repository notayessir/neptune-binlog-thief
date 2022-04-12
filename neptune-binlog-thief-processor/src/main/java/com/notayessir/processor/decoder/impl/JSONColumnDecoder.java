package com.notayessir.processor.decoder.impl;


import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.JSONColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_JSON)
public class JSONColumnDecoder extends AbsColumnDecoder<JSONColumn> {


    @Override
    public JSONColumn decode(ByteBuf in, ColumnDef columnDef) {
        // should be long ?
        int len = ByteUtil.readIntAndRelease(in.readBytes(4));
        byte[] val = ByteUtil.readBytesAndRelease(in.readBytes(len));

        // use json library to deserialize bytes
        JSONColumn jsonColumn = new JSONColumn();
        jsonColumn.setVal(val);
        return jsonColumn;
    }
}
