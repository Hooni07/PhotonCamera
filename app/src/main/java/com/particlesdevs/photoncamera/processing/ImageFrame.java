package com.particlesdevs.photoncamera.processing;

import android.media.Image;

import com.particlesdevs.photoncamera.processing.parameters.IsoExpoSelector;

import java.nio.ByteBuffer;

public class ImageFrame {
    public ByteBuffer buffer;
    public Image image;
    public float luckyParameter;
    public int number;
    public IsoExpoSelector.ExpoPair pair;

    public ImageFrame(ByteBuffer in) {
        buffer = in;
    }
}
