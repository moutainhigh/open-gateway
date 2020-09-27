package org.open.gateway.portal.constants;

import java.time.format.DateTimeFormatter;

/**
 * Created by miko on 9/25/20.
 *
 * @author MIKO
 */
public interface DateTimeFormatters {

    DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

}
