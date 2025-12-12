package com.finpro.frontend.pools;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T> {
    private List<T> available = new ArrayList<>();//pool yg ada
    private List<T> inUse = new ArrayList<>(); //pool digunakan
    //abstract method
    protected abstract T createObject();
    protected abstract void resetObject(T object);
    public T obtain(){
        T object;
        if(available.isEmpty()){
            object = createObject();
        }else{
            object = available.remove(0);
        }
        inUse.add(object);
        return object;
    }
    public void release(T object){
        if(inUse.remove(object)){
            resetObject(object);
            available.add(object);
        }
    }
    public void releaseAll(){
        for (T object : inUse){
            resetObject(object);
            available.add(object);
        }
        inUse.clear();
    }
    public int getActiveCount(){
        return inUse.size();
    }
    public List<T> getInUse() {
        return new ArrayList<>(inUse);
    }
}
