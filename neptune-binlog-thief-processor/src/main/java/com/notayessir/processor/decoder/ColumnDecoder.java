package com.notayessir.processor.decoder;
import com.notayessir.common.column.ColumnDef;
import com.notayessir.processor.decoder.column.Column;
import io.netty.buffer.ByteBuf;

public interface ColumnDecoder<C extends Column<?>> {

    C decode(ByteBuf in, ColumnDef columnDef);

}
