package com.notayessir.processor.filter;

import com.notayessir.processor.configure.AppConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 数据库、表过滤器
 */
@Component
public class IgnoreFilter {

//    private static final String IGNORE_DATABASE = "neptune";

    public boolean filter(AppConfiguration appConfiguration, String dbName, String tbName) {
//        if (StringUtils.equalsIgnoreCase(dbName, IGNORE_DATABASE)){
//            return true;
//        }
        String ignoredTable = appConfiguration.getIgnoredTable();
        if (StringUtils.isBlank(ignoredTable)){
            return false;
        }
        String [] databases = StringUtils.split(ignoredTable, ";");
        for (String item : databases) {
            String [] dbNameAndTbName = StringUtils.split(item.trim(), ":");
            if (!StringUtils.equalsIgnoreCase(dbNameAndTbName[0].trim(), dbName)){
                continue;
            }

            // the whole database will be ignored
            if (StringUtils.equalsIgnoreCase(dbNameAndTbName[1].trim(), "*")){
                return true;
            }

            // the table will be ignored
            if (StringUtils.containsIgnoreCase(dbNameAndTbName[1].trim(), tbName)){
                return true;
            }

        }
        return false;
    }


}
