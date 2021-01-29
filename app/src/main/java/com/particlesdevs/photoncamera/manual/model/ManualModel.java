package com.particlesdevs.photoncamera.manual.model;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.util.Range;

import com.particlesdevs.photoncamera.R;
import com.particlesdevs.photoncamera.manual.ManualParamModel;
import com.particlesdevs.photoncamera.ui.camera.views.manualmode.ManualMode;
import com.particlesdevs.photoncamera.ui.camera.views.manualmode.knobview.KnobInfo;
import com.particlesdevs.photoncamera.ui.camera.views.manualmode.knobview.KnobItemInfo;
import com.particlesdevs.photoncamera.ui.camera.views.manualmode.knobview.KnobView;
import com.particlesdevs.photoncamera.ui.camera.views.manualmode.knobview.KnobViewChangedListener;
import com.particlesdevs.photoncamera.ui.camera.views.manualmode.knobview.ShadowTextDrawable;

import java.util.ArrayList;
import java.util.List;

public abstract class ManualModel<T extends Comparable<? super T>> implements KnobViewChangedListener, IModel {
    protected final ManualParamModel manualParamModel;
    private final List<KnobItemInfo> knobInfoList;
    private final ValueChangedEvent valueChangedEvent;
    protected Range<T> range;
    protected KnobInfo knobInfo;
    protected KnobItemInfo currentInfo, autoModel;
    protected Context context;

    public ManualModel(Context context, Range<T> range, ManualParamModel manualParamModel, ValueChangedEvent valueChangedEvent) {
        this.context = context;
        this.range = range;
        this.valueChangedEvent = valueChangedEvent;
        this.manualParamModel = manualParamModel;
        knobInfoList = new ArrayList<>();
        fillKnobInfoList();
    }

    public void setAutoTxt() {
        fireValueChangedEvent(autoModel.text);
    }

    private void fireValueChangedEvent(final String txt) {
        if (valueChangedEvent != null)
            valueChangedEvent.onValueChanged(txt);
    }

    protected KnobItemInfo getNewAutoItem(double defaultVal, String defaultText) {
        ShadowTextDrawable autoDrawable = new ShadowTextDrawable();
        String auto_string = context.getString(R.string.manual_mode_auto);
        if (defaultText != null)
            auto_string = defaultText;
        autoDrawable.setText(auto_string);
        autoDrawable.setTextAppearance(context, R.style.ManualModeKnobText);
        ShadowTextDrawable autoDrawableSelected = new ShadowTextDrawable();
        autoDrawableSelected.setText(auto_string);
        autoDrawableSelected.setTextAppearance(context, R.style.ManualModeKnobTextSelected);
        StateListDrawable autoStateDrawable = new StateListDrawable();
        autoStateDrawable.addState(new int[]{-android.R.attr.state_selected}, autoDrawable);
        autoStateDrawable.addState(new int[]{android.R.attr.state_selected}, autoDrawableSelected);
        autoModel = new KnobItemInfo(autoStateDrawable, auto_string, 0, defaultVal);
        return autoModel;
    }

//    public KnobItemInfo getItemInfo(String text, double val, int tick) {
//        ShadowTextDrawable autoDrawable = new ShadowTextDrawable();
//        autoDrawable.setText(text);
//        autoDrawable.setTextAppearance(context, R.style.ManualModeKnobText);
//        ShadowTextDrawable autoDrawableSelected = new ShadowTextDrawable();
//        autoDrawableSelected.setText(text);
//        autoDrawableSelected.setTextAppearance(context, R.style.ManualModeKnobTextSelected);
//        StateListDrawable autoStateDrawable = new StateListDrawable();
//        autoStateDrawable.addState(new int[]{-android.R.attr.state_selected}, autoDrawable);
//        autoStateDrawable.addState(new int[]{android.R.attr.state_selected}, autoDrawableSelected);
//        return new KnobItemInfo(autoStateDrawable, text, tick, val);
//    }

    protected abstract void fillKnobInfoList();

    @Override
    public List<KnobItemInfo> getKnobInfoList() {
        return knobInfoList;
    }

    @Override
    public KnobItemInfo getCurrentInfo() {
        return currentInfo;
    }

    @Override
    public KnobInfo getKnobInfo() {
        return knobInfo;
    }

    @Override
    public void onSelectedKnobItemChanged(KnobView knobView, KnobItemInfo knobItemInfo, final KnobItemInfo knobItemInfo2) {
        Log.d(ManualMode.class.getSimpleName(), "onSelectedKnobItemChanged");
        if (knobItemInfo == knobItemInfo2)
            return;
        onSelectedKnobItemChanged(knobItemInfo2);
        if (knobItemInfo != null) {
            knobItemInfo.drawable.setState(new int[]{-android.R.attr.state_selected});
        }
        knobItemInfo2.drawable.setState(new int[]{android.R.attr.state_selected});
        fireValueChangedEvent(knobItemInfo2.text);
    }

    public void resetModel() {
        onSelectedKnobItemChanged(null, null, autoModel);
    }

    public abstract void onSelectedKnobItemChanged(KnobItemInfo knobItemInfo2);

    public interface ValueChangedEvent {
        void onValueChanged(String value);
    }
}
