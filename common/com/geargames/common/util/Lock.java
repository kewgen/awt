package com.geargames.common.util;

/**
 * User: mikhail v. kutuzov
 * Date: 21.09.12
 * Time: 14:15
 */
public abstract class Lock {
    /**
     * Остановить поток до тех пор пока ресурс не сможет быть им заблокирован.
     */
    public abstract void lock();

    /**
     * Разблокировать ресурс и разрешить оповестить остальные потоки об этом.
     */
    public abstract void release();
}
