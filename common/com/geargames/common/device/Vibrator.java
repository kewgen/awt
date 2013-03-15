package com.geargames.common.device;

// import android.os.Vibrator;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public interface Vibrator {

    /**
     * Вернет true, если вибрация поддерживается устройством.
     */
    boolean hasVibrator();

    /**
     * Запустить вибрацию на milliseconds миллисекунд.
     * @param milliseconds
     */
    void vibrate(int milliseconds);

    /**
     * Запустить вибрацию по шаблону pattern заданное число repeat раз.
     * @param pattern
     * @param repeat
     */
    void vibrate(int[] pattern, int repeat);

    /**
     * Остановить вибрацию.
     */
    void cancel();

}