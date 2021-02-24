package com.wk.analyseFile;

import java.util.Date;

/**
 * @author: ekiawna
 * @Date: 2021/2/23 16:12
 * @Description
 */
public interface FileAnalyse {
    public void analyseLastBatch() throws Exception;
    public void analyseHistoryHour() throws Exception;
    public void generateTask(Date time) throws Exception;
    public Date getLastExecuteTime(String cron, Date startTime) throws Exception;
    public Date getLastExecuteTime(Date lTime,int period) throws Exception;
}
