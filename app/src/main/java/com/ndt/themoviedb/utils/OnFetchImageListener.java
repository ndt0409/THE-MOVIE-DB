package com.ndt.themoviedb.utils;

import android.graphics.Bitmap;

public interface OnFetchImageListener {
    void onImageLoaded(Bitmap bitmap);

    void onImageError(Exception e);
}
