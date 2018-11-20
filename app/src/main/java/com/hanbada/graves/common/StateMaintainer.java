package com.hanbada.graves.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class StateMaintainer {

    /**
     *
     * auth : leegh
     */
    protected  final String TAG = getClass().getSimpleName();

    private final String mStateMaintenerTag;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMngFragment mStateMaintainerFrag;
    private boolean mIsRecreating;

    public StateMaintainer(FragmentManager fragmentManager, String stateMaintenerTag){
        mFragmentManager = new WeakReference<>(fragmentManager);
        mStateMaintenerTag = stateMaintenerTag;
    }

    public boolean firstTimein(){
        try {
            mStateMaintainerFrag = (StateMngFragment) mFragmentManager.get().findFragmentByTag(mStateMaintenerTag);

            if(mStateMaintainerFrag == null){
                Log.d(TAG,"create fragment " + mStateMaintainerFrag );
                mStateMaintainerFrag = new StateMngFragment();
                mFragmentManager.get().beginTransaction().add(mStateMaintainerFrag, mStateMaintenerTag).commit();
                mIsRecreating = false;
                return true;
            }else{
                Log.d(TAG, "fragment exist " + mStateMaintenerTag);
                mIsRecreating = true;
                return false;
            }
        }catch (NullPointerException e){
            Log.w(TAG,"Error firstTimeIn()");
            e.printStackTrace();
            return false;
        }
    }

    public boolean wasRecreated(){
        return mIsRecreating;
    }

    public void put(String key, Object obj){
        mStateMaintainerFrag.put(key, obj);
    }

    public void put(Object obj){
        put(obj.getClass().getName(), obj);
    }

    public <T> T get(String key){
        return mStateMaintainerFrag.get(key);
    }

    public boolean hasKey(String key){
        return mStateMaintainerFrag.get(key) != null;
    }

    public static class StateMngFragment extends Fragment {
        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void put(String key, Object obj){
            mData.put(key, obj);
        }
        public void put(Object object){
            put(object.getClass().getName(), object);
        }

        public<T> T get(String key){
            return (T)mData.get(key);
        }
    }
}
