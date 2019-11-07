package util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by thales on 04/04/17.
 */
public class Chronometer {
    private long tin = 0, tfn = 0,tim = 0, tfm = 0;
    private LocalDateTime datei, datef;
    private static final long SECOND_IN_NANOS = 1000000000;
    private static final long SECOND_IN_MILLIS = 1000;

    public void start(){
        datei = LocalDateTime.now();
        tin = System.nanoTime();
        tim = System.currentTimeMillis();
    }

    public void stop(){
        datef = LocalDateTime.now();
        tfn = System.nanoTime();
        tfm = System.currentTimeMillis();
    }

    public BigDecimal getNanoTime() {
        BigDecimal nanoTime = BigDecimal.valueOf(tfn - tin);
        BigDecimal secondsTime = BigDecimal.valueOf(nanoTime.doubleValue() / BigDecimal.valueOf(SECOND_IN_NANOS).doubleValue());
        return secondsTime;
    }

    public BigDecimal getMilliTime() {
        BigDecimal milliTime = BigDecimal.valueOf(tfm - tim);
        BigDecimal secondsTime = BigDecimal.valueOf(milliTime.doubleValue() / BigDecimal.valueOf(SECOND_IN_MILLIS).doubleValue());
        return secondsTime;
    }

    public LocalDateTime getDatei() {
        return datei;
    }

    public LocalDateTime getDatef() {
        return datef;
    }
}
