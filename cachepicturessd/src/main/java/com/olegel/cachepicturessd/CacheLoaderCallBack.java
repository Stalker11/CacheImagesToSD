package com.olegel.cachepicturessd;

/**
 * Created by Oleg on 11.09.2017.
 */

public interface CacheLoaderCallBack {
    void onSuccess();
    void onError(String error);
}
