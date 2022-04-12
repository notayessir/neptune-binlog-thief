package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.NewDecimalColumn;
import com.notayessir.processor.decoder.column.NewDecimalDigit;
import com.notayessir.processor.util.NewDecimalDigitUtil;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * https://dev.mysql.com/doc/refman/5.7/en/precision-math-decimal-characteristics.html
 * https://github.com/twitter-forks/mysql/blob/master/strings/decimal.c decimal2bin
 */
@Decoder(ColumnType.MYSQL_TYPE_NEWDECIMAL)
public class NewDecimalColumnDecoder extends AbsColumnDecoder<NewDecimalColumn> {



    @Override
    public NewDecimalColumn decode(ByteBuf in, ColumnDef columnDef) {
        int integerLen = columnDef.getMetadata()[0] - columnDef.getMetadata()[1];
        NewDecimalDigit integerStruct = NewDecimalDigitUtil.parseStruct(integerLen);
        int digitsLen = integerStruct.getOccupiedLen();
        int fractionalLen = columnDef.getMetadata()[1];

        NewDecimalDigit fractionalStruct = null;
        if (fractionalLen != 0){
            fractionalStruct = NewDecimalDigitUtil.parseStruct(fractionalLen);
            digitsLen += fractionalStruct.getOccupiedLen();
        }

        byte [] digits = ByteUtil.readBytesAndRelease(in.readBytes(digitsLen));

        // negative or positive
        boolean positive = NewDecimalDigitUtil.isPositive(digits[0]);
        if (!positive){
            NewDecimalDigitUtil.invertBytes(digits);
        }

        // then invert the highest bit
        NewDecimalDigitUtil.invertHighestBit(digits);


        // read integer part
        byte [] integerDigits = new byte[integerStruct.getOccupiedLen()];
        System.arraycopy(digits, 0, integerDigits, 0, integerStruct.getOccupiedLen());

        String integerPartStr = NewDecimalDigitUtil.parseIntegerDigits(integerDigits, integerStruct.getByteNumbers());
        StringBuilder val = new StringBuilder(integerPartStr);

        // try to read fractional part
        String fractionalPartStr = "";
        if (Objects.nonNull(fractionalStruct)){
            byte [] fractionalDigits = new byte[fractionalStruct.getOccupiedLen()];
            System.arraycopy(digits, integerStruct.getOccupiedLen(), fractionalDigits, 0, fractionalStruct.getOccupiedLen());
            fractionalPartStr = NewDecimalDigitUtil.parseFractionalDigits(fractionalDigits, fractionalStruct.getByteNumbers(), fractionalLen);
            // concat integer and fractional
            val.append(".").append(fractionalPartStr);
        }
        if (!positive){
            val.insert(0, "-");
        }
        NewDecimalColumn newDecimalColumn = new NewDecimalColumn();
        newDecimalColumn.setVal(val.toString());
        return newDecimalColumn;
    }



}
