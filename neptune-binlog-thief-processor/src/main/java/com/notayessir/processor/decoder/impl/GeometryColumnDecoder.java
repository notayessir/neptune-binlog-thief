package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.GeometryColumn;
import io.netty.buffer.ByteBuf;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

@Decoder(ColumnType.MYSQL_TYPE_GEOMETRY)
public class GeometryColumnDecoder extends AbsColumnDecoder<GeometryColumn> {


    @Override
    public GeometryColumn decode(ByteBuf in, ColumnDef columnDef) {
        int packLength = Byte.toUnsignedInt(columnDef.getMetadata()[0]);
        int wkbLen = ByteUtil.readIntAndRelease(in.readBytes(packLength));
        // skip 4 bytes for integer SRID (0)
        in.skipBytes(4);
        wkbLen = wkbLen - 4;
        byte[] bytes = ByteUtil.readBytesAndRelease(in.readBytes(wkbLen));
        WKBReader reader = new WKBReader();
        String val = null;
        try {
            Geometry geometry = reader.read(bytes);
            val = geometry.toText();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GeometryColumn geometryColumn = new GeometryColumn();
        geometryColumn.setVal(val);
        return geometryColumn;
    }


}
