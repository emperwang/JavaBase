package com.wk.entity.fcaps;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
public class AmCollectorSourceVo extends AmCollectorSource {
    private Date fmHeartBeatTime;

    @Override
    public String toString() {
        return "AmCollectorSourceVo{" +
                "fmHeartBeatTime=" + fmHeartBeatTime +
                '}';
    }
}
