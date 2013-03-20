package com.geargames.common.resource;

/**
 * User: mkutuzov
 * Date: 12.03.13
 */
public class ResourceDescription {

    private String type;
    private String name;

    private byte priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }
}
