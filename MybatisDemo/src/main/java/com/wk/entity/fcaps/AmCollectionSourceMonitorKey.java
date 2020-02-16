package com.wk.entity.fcaps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class AmCollectionSourceMonitorKey {
    private String sourceId;

    private String infoType;

    public AmCollectionSourceMonitorKey(String sourceId, String infoType) {
        this.sourceId = sourceId;
        this.infoType = infoType;
    }
}