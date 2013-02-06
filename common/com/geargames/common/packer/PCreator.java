package com.geargames.common.packer;

/**
 * User: mikhail v. kutuzov
 * Объекты хранящиеся в PManager состоят не только из конкретных, но и абстрактных классов.
 * Из конкретного проекта необходимо предоставить реализацию этого класса.
 */
public abstract class PCreator {
    public abstract PUnit createUnit(int pid, int size);
}
