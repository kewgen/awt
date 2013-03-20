package com.geargames.platform.resource;

import com.geargames.common.resource.Resource;
import com.geargames.common.resource.ResourceDescription;
import com.geargames.common.resource.ResourceManager;
import com.geargames.common.util.ArrayByte;
import com.geargames.common.util.Recorder;
import com.geargames.platform.packer.Image;

/**
 * User: mkutuzov
 * Date: 19.03.13
 */
public class BitMapImageManager extends ResourceManager {



    @Override
    protected Resource load(ResourceDescription description)  {
        try{


        } catch (Exception e){

        }

        return null;
    }

    @Override
    protected void release(ResourceDescription description) {

    }

    @Override
    protected Resource convert(ArrayByte content) {
        try{
            return Image.createImage(content.getArray(), 0, content.getArray().length);
        }catch (Exception e){
            return Image.createImage(0,0);
        }
    }
}
