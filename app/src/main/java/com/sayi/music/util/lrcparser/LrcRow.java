package com.sayi.music.util.lrcparser;

import java.util.regex.*;
import com.sayi.music.*;

public class LrcRow{
	public static LrcRow nullLrcRow=new LrcRow("00:00","");
	
	public float time=-1;
	public String timeString="";
	public String lrc="";
	public LrcRow(String time,String lrc){
		this.time=unformatTime(time);
		this.timeString=time;
		this.lrc=lrc;
	}
	public static String formatTime(float msec){
		int minute=((int)msec)/1000/60;
		int second=((int)msec)/1000%60;
		String minuteString;
		String secondString;
		if(minute<10){
			minuteString="0"+minute;
		}else{
			minuteString=""+minute;
		}
		if(second<10){
			secondString="0"+second;
		}else{
			secondString=""+second;
		}
		return minuteString+":"+secondString;
	}

	
	public static float unformatTime(String time){
		String[] timeParts=time.split(":");
		int minutes=Integer.parseInt(timeParts[0]);
		float seconds=Float.parseFloat(timeParts[1]);
		return minutes*60+seconds;
	}

	@Override
	public String toString(){
		
		return time+lrc;
	}
	
}
