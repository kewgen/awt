package com.geargames.common.resource;

/**
 * User: abarakov
 * Date: 07.03.13
 */
// ResourceDescription, Locable
public abstract class Resource {

    private byte priority;
    private int lockCount;

    public Resource() {
        priority = ResourcePriority.NORMAL;
        lockCount = 0;
    }

    /**
     * Вернуть приоритет ресурса. Чем выше значение, тем менее вероятно, что неиспользуемый ресурс будет выгружен.
     * @return одно из значений ResourcePriority
     */
    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public final boolean isLocked() {
        return lockCount > 0;
    }

    public final void lock() {
        lockCount++;
        if (lockCount == 1) {
            load();
        }
    }

    public final boolean unlock() {
        lockCount--;
        if (lockCount == 0) {
            release();
            return true;
        } else {
            return false;
        }
    }

    protected abstract void load();

    protected abstract void release();

    public abstract boolean isLoaded();

}