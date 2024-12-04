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

    public List<ThreadData.Variables> getData() {
        return variables.data;
    }

    public static class Variables extends BaseVariables{

        @SerializedName("data")
        private List<ThreadData.Variables> data;

        @Override
        public String toString() {
            return super.toString()+
                    ", data=" + data ;
        }
    }
}
