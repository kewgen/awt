package com.geargames.common.resource;

import com.geargames.common.util.ArrayByte;
import com.geargames.common.util.HashMap;
import com.geargames.common.util.Recorder;

/**
 * User: mkutuzov
 * Date: 12.03.13
 * Класс сокрывает в себе способ которым ресурсы попадают в руки программисту.
 * ресурсы получаются через запрос по:
 * 0. ResourceDescription
 * 1. типу ресурса и имени ресурса
 * Конкретные реализации класса перегружают методы загруки и освобождения конкретных видов ресурсов на конкретных платформах.
 */
public abstract class ResourceManager {
    private HashMap resources;

    protected ResourceManager() {
        resources = new HashMap();
    }

    public Resource getResource(ResourceDescription description) throws Exception {
        HashMap typeResource = (HashMap) resources.get(description.getType());
        if (typeResource == null) {
            typeResource = new HashMap();
        }
        Resource resource = (Resource) typeResource.get(description.getName());
        if (resource == null) {
            ArrayByte content = Recorder.load(description.getType() + description.getName());
            if (content != null) {
                resource = convert(content);
            } else {
                resource = load(description);
            }
            resource.setDescription(description);
        }
        return resource;
    }

    public void unLoadResource(ResourceDescription description) {
        HashMap typeResource = (HashMap) resources.get(description.getType());
        if (typeResource != null) {
            Resource resource = (Resource) typeResource.get(description.getName());
            if (resource != null) {
                if (!resource.isLocked()) {
                    release(description);
                }
            }
        }
    }

    public Resource getResourceByName(String type, String name) throws Exception {
        HashMap typeResource = (HashMap) resources.get(type);
        if (typeResource == null) {
            typeResource = new HashMap();
        }
        Resource resource = (Resource) typeResource.get(name);
        if (resource == null) {
            ResourceDescription description = new ResourceDescription();
            description.setType(type);
            description.setName(name);
            description.setPriority(ResourcePriority.NORMAL);

            ArrayByte content = Recorder.load(description.getType() + description.getName());
            if(content != null){
                resource = convert(content);
            }else{
                resource = load(description);
            }
            resource.setDescription(description);
        }
        return resource;
    }

    public void unLoadResource(String type, String name) {
        HashMap typeResource = (HashMap) resources.get(type);
        if (typeResource != null) {
            Resource resource = (Resource) typeResource.get(name);
            if (resource != null) {
                if (!resource.isLocked()) {
                    release(resource.getDescription());
                }
            }
        }
    }

    protected abstract Resource convert(ArrayByte content);

    /**
     * Загрузка ресурса из внешнего хранилища.
     *
     * @param description
     * @return возвращает экземпляр ресурса
     */
    protected abstract Resource load(ResourceDescription description);

    /**
     * Освобождение памяти из под ресурса.
     *
     * @param description
     */
    protected abstract void release(ResourceDescription description);

}
