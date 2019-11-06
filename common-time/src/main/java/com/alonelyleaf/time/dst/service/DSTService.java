package com.alonelyleaf.time.dst.service;

import com.alonelyleaf.time.dst.entity.WinTimeZone;
import com.alonelyleaf.time.dst.util.DSTUtil;
import com.alonelyleaf.time.dst.vo.DSTConfigVO;
import com.alonelyleaf.time.dst.vo.DSTRule;

/**
 * @author bijl
 * @date 2019/11/5
 */
public class DSTService {

    /**
     * 检查指定时间是否是该时区的夏令时，如果是，则返回偏移时长
     *
     * @param zoneId 时区id
     * @param checkTime 指定的时间，如
     * @return
     */
    public Long checkDST(String zoneId, String checkTime) {

        WinTimeZone winTimeZone = getZone(zoneId);

        DSTRule dstRule = DSTUtil.getDSTRule(winTimeZone, checkTime);

        Long dayLightDelta = 0L;
        if (dstRule != null) {
            dayLightDelta = dstRule.getDayLightDelta();
        }

        return dayLightDelta;
    }

    public WinTimeZone getZone(String zoneId) {

        WinTimeZone zone = new WinTimeZone();

        return zone;
    }
}
