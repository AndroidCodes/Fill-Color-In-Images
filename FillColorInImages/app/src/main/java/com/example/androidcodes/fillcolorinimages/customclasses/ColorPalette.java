/*
 * Copyright (c) 2013 Byron Sanchez (hackbytes.com)
 * www.chompix.com
 *
 * This file is part of "Coloring Book for Android."
 *
 * "Coloring Book for Android" is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 2 of the
 * license.
 *
 * "Coloring Book for Android" is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Coloring Book for Android."  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.example.androidcodes.fillcolorinimages.customclasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.androidcodes.fillcolorinimages.R;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Creates a color palette from which users can select a color with which to
 * draw.
 */

public class ColorPalette implements OnClickListener {

    // A boolean to store the last selected color for ColorPalette.java's
    // highlights.
    public static String sLastTag = "";
    public float buttonSize = 0;
    // Stores the colors associated with the Palette.
    private HashMap<String, Integer> mColors;
    // Stores a parallel set of ImageButtons for each color.
    // Tags must be the same here.
    private HashMap<String, ImageButton> mIbColors = new HashMap<String, ImageButton>();
    private Context mContext;
    private int mStrokeSize;

    private ColorGFX colorGFX;

    private HashMap<String, ColorPalette> hmPalette;

    // The default color to highlight.
    private int defaultColor;

    /**
     * Constructor that sets the necessary properties for Palette objects.
     */
    public ColorPalette(Context context, HashMap<String, Integer> colors, String deviceType,
                        ColorGFX colorGFX, HashMap<String, ColorPalette> hmPalette) {

        this.mContext = context;

        this.mColors = colors;

        this.defaultColor = Color.rgb(247, 228, 197);

        this.colorGFX= colorGFX;

        this.hmPalette = hmPalette;

        // Calculate stroke palette stroke size based on the device. Do this in
        // the constructor so this calculation is only run once.
        if (deviceType.equals("Tablet")) {

            if (deviceType.equals("Small")) {

                mStrokeSize = 8;

            } else if (deviceType.equals("Normal")) {

                mStrokeSize = 10;

            } else if (deviceType.equals("Large")) {

                mStrokeSize = 12;

            }else if (deviceType.equals("ExtraLarge")) {

                mStrokeSize = 14;

            }
        } else {

            mStrokeSize = 5;

        }
    }

    /**
     * Constructor that sets the necessary properties for Palette objects, ad
     * has savedData.
     */
    public ColorPalette(Context context, HashMap<String, Integer> colors, boolean isSavedData,
                        int selectedColor, String deviceType, ColorGFX colorGFX,
                        HashMap<String, ColorPalette> hmPalette) {

        this.mContext = context;

        this.mColors = colors;

        if (isSavedData) {

            this.defaultColor = selectedColor;

        }

        this.colorGFX= colorGFX;

        this.hmPalette = hmPalette;

        // Calculate stroke palette stroke size based on the device. Do this in
        // the constructor so this calculation is only run once.
        if (deviceType.equals("Tablet")) {

            if (deviceType.equals("Small")) {

                mStrokeSize = 8;

            } else if (deviceType.equals("Normal")) {

                mStrokeSize = 10;

            } else if (deviceType.equals("Large")) {

                mStrokeSize = 12;

            }else if (deviceType.equals("ExtraLarge")) {

                mStrokeSize = 14;

            }
        } else {

            mStrokeSize = 5;

        }
    }

    /**
     * Calculates button size.
     */
    public void calculateButtonSize() {
        // Get the instance of the context making the call to this Palette
        // Class.
        //FillColorActivity activity = (FillColorActivity) mContext;

        // Get the screen width, subtract the image3 width and divide the
        // remaining space by 4, 1 for each of the four color palettes.
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        float availableWidth = dm.widthPixels - colorGFX.imageWidth;
        float availableHeight = dm.heightPixels;

        float resultSize;

        float resultWidth = availableWidth / 4;
        float resultHeight = availableHeight / 8;

        // Circle size is dependent on screen size. For fluid layouts, we
        // must determine the maximum amount of circular space to take up
        // without overlaying the canvas. So the minimum of the available
        // space for the width and height is calculated and used.
        if (resultHeight < resultWidth) {
            resultSize = resultHeight;
        } else {
            resultSize = resultWidth;
        }

        buttonSize = resultSize;
    }

    /**
     * Creates the ImageButtons for the palette. ColorGFX and the canvas MUST BE
     * CREATED BEFORE THIS CAN BE CALLED.
     */
    public void createButtons() {
        // Loop through the colors hashmap for this palette
        for (String key : mColors.keySet()) {

            // Create the image3 button for each color.
            ImageButton newImageButton = new ImageButton(mContext);

            // Set the color of the ImageButton to be the one that the Colors
            // HashMap has defined.
            newImageButton.setBackgroundResource(R.drawable.color_button);

            // Divide by 4, one for each palette.
            double paletteWidth = Math.floor(buttonSize);
            // Divide by 7, one for each palette element (5), the directional
            // buttons (1) and the advanced buttons (1).
            double paletteHeight = Math.floor(buttonSize);

            // Give the ImageButton some parameters.
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) paletteWidth, (int) paletteHeight);
            newImageButton.setLayoutParams(params);

            // Set a tag so we can identify this view if an event is fired.
            newImageButton.setTag(key);

            Drawable bgShape = newImageButton.getBackground();
            ((GradientDrawable) bgShape).setShape(GradientDrawable.OVAL);
            ((GradientDrawable) bgShape).setColor(mColors.get(key));

            // Calculate the stroke size based on the device being used.

            // If the key is the default color, highlight it, because this is
            // the current inital default selected color.
            if (mColors.get(key) == defaultColor) {

                if (key.equals("12_black")) {
                    ((GradientDrawable) bgShape).setStroke(mStrokeSize, 0xFFFFFFFF);
                } else {
                    ((GradientDrawable) bgShape).setStroke(mStrokeSize, 0xFF000000);
                }
                ColorPalette.sLastTag = key;
            } else {
                ((GradientDrawable) bgShape).setStroke(mStrokeSize, mColors.get(key));
            }

            bgShape.setBounds((int) (paletteWidth / 10), (int) (paletteHeight / 10),
                    (int) paletteWidth, (int) paletteHeight);

            // Set the context's onClick() listener to each button.
            newImageButton.setOnClickListener(this);

            // Add the new image3 button to the list of palette image3 buttons.
            // Make sure to tag appropriately with the corresponding color hm
            // tag.
            mIbColors.put(key, newImageButton);
        }
    }

    /**
     * Adds buttons to the specified view.
     */
    public void addToView(LinearLayout view) {

        Object[] buttonArray = (mColors.keySet().toArray());
        Arrays.sort(buttonArray);

        // Loop through the image3 buttons hashmap for this palette
        for (Object key : buttonArray) {

            view.addView(mIbColors.get(key.toString()));
        }
    }

    /**
     * Implements onClick().
     */
    @Override
    public void onClick(View view) {
        // The only registered onClick listeners for this scope are color
        // buttons.

        // Identify the button that has been clicked by the tag that was set
        // when the ImageButton was created.
        String tag = (String) view.getTag();

        // Determine which palette the click came from...

        // Retrieve the corresponding color information.
        Integer color = mColors.get(tag);

        // Get the instance of the context making the call to this Palette
        // Class.
        //FillColorActivity activity = (FillColorActivity) mContext;

        // From that information set the new selectedColor.
        // We use the context to get the activity instance implementing this
        // class.
        colorGFX.paint.setColor(color);
        colorGFX.selectedColor = color;

        // If a color was previously selected, redraw it to the default stroke.
        if (ColorPalette.sLastTag != "") {
            // Iterate through each of the available color palettes.
            for (String key : hmPalette.keySet()) {
                // If the current color palette contains the last color,
                // unhighlight it.
                if (hmPalette.get(key).mIbColors.containsKey(ColorPalette.sLastTag)) {
                    // Get the last selected color.
                    Drawable lastView = hmPalette.get(key).mIbColors.get(
                            ColorPalette.sLastTag)
                            .getBackground();
                    // Remove the highlight.
                    ((GradientDrawable) lastView).setStroke(mStrokeSize, mColors.get(ColorPalette.
                            sLastTag));
                    // Update the view.
                    hmPalette.get(key).mIbColors.get(ColorPalette.sLastTag).invalidate();

                    // End the loop.
                    break;

                }
            }
        }

        // Update the stroke color to highlight the new color.
        Drawable bgShape = view.getBackground();

        if (tag.equals("12_black")) {

            ((GradientDrawable) bgShape).setStroke(mStrokeSize, 0xFFFFFFFF);

        } else {

            ((GradientDrawable) bgShape).setStroke(mStrokeSize, 0xFF000000);

        }

//        ((GradientDrawable) bgShape).setStroke(mStrokeSize, 0xFF000000);

        // Set the tag in an external location where it will persist for all
        // color palettes.
        ColorPalette.sLastTag = tag;

        // Update the view.
        view.invalidate();
    }
}
