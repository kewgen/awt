package com.geargames.awt.cinematics;

/**
 * user: Mikhail V. Kutuzov
 * date: 10.12.11
 * time: 20:09
 * Вспомогательный класс чьи реализации должны помочь MovingObject решить: завершён ли его полёт?
 */
public abstract class FinishAdviser {
    public abstract boolean isFinished();

    public abstract void onTick();
}
