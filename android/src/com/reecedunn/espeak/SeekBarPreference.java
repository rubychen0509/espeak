/*
 * Copyright (C) 2013 Reece H. Dunn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reecedunn.espeak;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{
    private SeekBar mSeekBar;
    private TextView mValueText;

    private int mProgress = 0;
    private int mDefaultValue = 0;
    private int mMin = 0;
    private int mMax = 100;
    private String mFormatter = "%s";

    public void setProgress(int progress) {
        mProgress = progress;
        String text = Integer.toString(mProgress);
        callChangeListener(text);
    }

    public int getProgress() {
        return mProgress;
    }

    public void setDefaultValue(int defaultValue) {
        mDefaultValue = defaultValue;
    }

    public int getDefaultValue() {
        return mDefaultValue;
    }

    public void setMin(int min) {
        mMin =  min;
    }

    public int getMin() {
        return mMin;
    }

    public void setMax(int max) {
        mMax =  max;
    }

    public int getMax() {
        return mMax;
    }

    public void setFormatter(String formatter) {
        mFormatter = formatter;
    }

    public String getFormatter() {
        return mFormatter;
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setDialogLayoutResource(R.layout.seekbar_preference);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    public SeekBarPreference(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SeekBarPreference(Context context)
    {
        this(context, null);
    }

    @Override
    protected View onCreateDialogView() {
        View root = super.onCreateDialogView();
        mSeekBar = (SeekBar)root.findViewById(R.id.seekBar);
        mValueText = (TextView)root.findViewById(R.id.valueText);

        Button reset = (Button)root.findViewById(R.id.resetToDefault);
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                mSeekBar.setProgress(getDefaultValue() - mMin);
            }
        });
        return root;
    }

    @Override
    protected void onBindDialogView(View view) {
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setMax(mMax - mMin);
        mSeekBar.setProgress(mProgress - mMin);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mProgress = mSeekBar.getProgress() + mMin;
                String text = Integer.toString(mProgress);
                callChangeListener(text);
                if (shouldCommit()) {
                    SharedPreferences.Editor editor = getEditor();
                    editor.putString(getKey(), text);
                    editor.commit();
                }
                break;
        }
        super.onClick(dialog, which);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        String text = String.format(getFormatter(), Integer.toString(progress + mMin));
        mValueText.setText(text);
        mSeekBar.setContentDescription(text);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
    }
}