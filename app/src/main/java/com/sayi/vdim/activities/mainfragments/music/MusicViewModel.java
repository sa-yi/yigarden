package com.sayi.vdim.activities.mainfragments.music;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.vdim.sayi_music_entity.Music;
import com.sayi.vdim.sayi_music_entity.MusicFully;
import com.sayi.vdim.sayi_music_entity.SyClient;
import com.sayi.vdim.sayi_music_entity.SyService;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicViewModel extends ViewModel {
    static String TAG="MusicViewModel";
    private final SyService syService=SyClient.getRetrofitInstance().create(SyService.class);
    public MutableLiveData<ArrayList<Music>> getSongList() {
        return songList;
    }

    private MutableLiveData<ArrayList<Music>> songList;

    public MusicViewModel(){
        songList=new MutableLiveData<>();
    }

    public void fetchSongList(){
        syService.getSongList().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ArrayList<Music>> call, Response<ArrayList<Music>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Music> musicArrayList = response.body();
                    songList.postValue(musicArrayList);
                    Log.d(TAG,"success");
                }else {
                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("RuntimeError",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Music>> call, Throwable throwable) {
                Log.e(TAG,"error"+throwable.getMessage());
            }
        });
    }
    public void fetchSongInfo(int id,OnFetchCompletedListener listener){
        syService.getInfo(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MusicFully> call, Response<MusicFully> response) {
                if(response.isSuccessful()){
                    MusicFully music=response.body();
                    listener.onFinished(music);
                }
            }

            @Override
            public void onFailure(Call<MusicFully> call, Throwable throwable) {

            }
        });
    }
    public interface OnFetchCompletedListener{
        public void onFinished(MusicFully music);
    }
}