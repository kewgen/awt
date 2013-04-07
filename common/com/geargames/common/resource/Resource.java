package com.geargames.common.resource;

/**
 * User: abarakov, m.v. kutuzov
 * Date: 07.03.13
 */
public abstract class Resource {

    private ResourceDescription description;

    /**
     * Вернуть описание ресурса.
     * @return
     */
    public ResourceDescription getDescription() {
        return description;
    }

    public void setDescription(ResourceDescription description) {
        this.description = description;
    }

    /**
     * Защищён ли ресурс от очистки через ResourceManager.
     * @return true если защищён.
     */
    public abstract boolean isLocked();
}