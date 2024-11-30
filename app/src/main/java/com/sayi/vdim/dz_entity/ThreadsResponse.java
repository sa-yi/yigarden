package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ThreadsResponse extends BaseResponse{

    @SerializedName("Variables")
    private Variables variables;

    @Override
    public String toString() {
        return super.toString() + variables;
    }

    @Override
    public Variables getVariables(){
        return variables;
    }

    public static class Variables extends BaseVariables{

        public List<ThreadData.Variables> getData() {
            return data;
        }

        @SerializedName("data")
        private List<ThreadData.Variables> data;

        @Override
        public String toString() {
            return super.toString()+
                    ", data=" + data ;
        }

    }
}
