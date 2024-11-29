package com.sayi.vdim.utils;

import android.os.*;
import java.util.*;

public class Ticker{
	private Handler mHandler;

	//private Ticker.OnTickListener mOnTickListener;
	
	private ArrayList<Ticker.OnTickListener> onTickListeners=new ArrayList<>();

	private Thread mThread;

	private long mPeriod=1000;

	private boolean mEnabled=true;

	private boolean isRun=false;

	private long mLast;

	private long mOffset;


	public Ticker(){
		init();
	}

	private void init(){
		mHandler=new Handler()
		{
			public void handleMessage(Message msg){
				/*if(mOnTickListener!=null)
					mOnTickListener.onTick();*/
				if(onTickListeners.size()!=0){
					for(OnTickListener tl:onTickListeners){
						tl.onTick();
					}
				}
			}
		};
		mThread= new Thread(() -> {
			isRun=true;
			while(isRun){
				long now=System.currentTimeMillis();
				if(!mEnabled)
					mLast=now-mOffset;
				if(now-mLast>=mPeriod){
					mLast=now;
					mHandler.sendEmptyMessage(0);
				}

				try{
					Thread.sleep(1);
				}
				catch(InterruptedException e){
				}
			}
		});
	}

	public void setPeriod(long period){
		mLast=System.currentTimeMillis();
		mPeriod=period;
	}

	public long getPeriod(){
		return mPeriod;
	}


	public void setInterval(long period){
		mLast=System.currentTimeMillis();
		mPeriod=period;
	}

	public long getInterval(){
		return mPeriod;
	}

	public void setEnabled(boolean enabled){
		mEnabled=enabled;
		if(!enabled)
			mOffset=System.currentTimeMillis()-mLast;
	}

	public boolean getEnabled(){
		return mEnabled;
	}

	/*public void setOnTickListener(OnTickListener ltr){
		mOnTickListener=ltr;
	}*/
	
	public void addOnTickListener(OnTickListener onTickListener){
		onTickListeners.add(onTickListener);
	}
	

	public void start(){
		mThread.start();
	}
	public void resume(){
		isRun=true;
	}
	public void stop(){
		isRun=false;
	}

	public boolean isRun(){
		return isRun;
	}


	public interface OnTickListener{
		public void onTick();
	}
}
