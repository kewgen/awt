package com.geargames.common;

/**
 * User: kewgen
 * Date: 19.10.12
 * Time: 14:02
 * модель управления игрой
 */
public abstract class Manager {

    abstract public void paint(Graphics g);

    public abstract void draw();

    abstract public void resizeScreenBuffer(int w, int h);

    abstract public void mainLoop();//Вызов основного игрового цикла. Он должен вызывать рендер нужных фреймов

    abstract public void loading();

    public abstract void loadSplash();



    public abstract void onResume();

    public abstract void onPause();

    abstract public void onStop();

    public abstract void onLowMemory();

    abstract public boolean onTouchEvent(int action, int x, int y);

    abstract public void keyPressed(int key);

    abstract public void backPressed();

    abstract public void menuPressed();


    abstract public int getFrameTotalCount(); //OpenGL общее число фреймов для кеширования

    public abstract void setOpenGLDrawCount(int drawCount);

    public abstract int getSizeExpectedResources();

}
