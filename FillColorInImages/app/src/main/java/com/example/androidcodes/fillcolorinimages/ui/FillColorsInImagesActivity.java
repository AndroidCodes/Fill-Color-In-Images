package com.example.androidcodes.fillcolorinimages.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.androidcodes.fillcolorinimages.R;
import com.example.androidcodes.fillcolorinimages.customclasses.ColorGFX;
import com.example.androidcodes.fillcolorinimages.customclasses.ColorGfxData;
import com.example.androidcodes.fillcolorinimages.customclasses.ColorPalette;
import com.example.androidcodes.fillcolorinimages.svgparser.SVG;
import com.example.androidcodes.fillcolorinimages.svgparser.SVGParseException;
import com.example.androidcodes.fillcolorinimages.svgparser.SVGParser;

import java.util.HashMap;

/**
 * Created by peacock on 15/3/17.
 */

public class FillColorsInImagesActivity extends AppCompatActivity implements View.OnClickListener {

    int height = 0, width = 0;
    private Activity activity;
    // Tablet vs. phone boolean. Defaults to phone.
    private boolean sIsTablet = false, sIsSmall = false, sIsNormal = false, sIsLarge = false,
            sIsExtraLarge = false, isDirectionRight = true, isSavedState = false;// The saved state data;
    private ProgressBar pbFloodFill;
    // The render engine.
    private ColorGFX colorGFX;
    // Define a container for the palettes
    private HashMap<String, ColorPalette> hmPalette = new HashMap<>();

    private ImageView img_preview;

    private TextView tv_save, tv_back;
    //InterstitialAd mInterstitialAd;

    private ColorGfxData savedData = null;
    // Define image3 properties.
    private int sCurrentImageId = 0;

    private int mMinImageId, mMaxImageId;
    // Define views.
    private FrameLayout fl_fill_color_image;

    private LinearLayout ll_color_palette;

    private ImageButton ib_previous, ib_next, ib_share;

    private ToggleButton tb_brush_and_paint, tb_erase_color;

    private MaskFilter mBlur;

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);

        return bmOverlay;

    }

    /*public void saveShare(String folder, String toast) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + folder);
        myDir.mkdirs();

        String fileName = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String strDate = sdf.format(c.getTime());
        switch (sCurrentImageId) {
            case 0:

                fileName = "Colored_Fish_" + strDate + ".jpg";

                break;

            case 1:

                fileName = "Colored_Butterfly_" + strDate + ".jpg";
                break;

            case 2:

                fileName = "Colored_Snail_" + strDate + ".jpg";
                break;

            case 3:

                fileName = "Colored_Bird_" + strDate + ".jpg";
                break;

            case 4:

                fileName = "Colored_Micky_" + strDate + ".jpg";
                break;

            default:
                break;
        }

        File file = new File(myDir, fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);

            Bitmap temp1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            temp1.eraseColor(Color.WHITE);

            Bitmap temp = overlay(temp1, colorGFX.bitmap);

            String mResourcePaint = "_" + String.valueOf(sCurrentImageId + 1)
                    + "_color_the_picture";

            // Get the resource id based on the image3's file name.
            int resId = getResources().getIdentifier(mResourcePaint, "raw",
                    getPackageName());

            Bitmap picture = overlay(temp,
                    svgToBitmap(getResources(), resId, height, width));
            picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            if (toast.equals("yes"))
                Toast.makeText(getApplicationContext(),
                        "Image Saved Successfully..!!", 3).show();
            else {
                Intent sharingIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                sharingIntent.setType("image3*//*");

                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM,
                        Uri.fromFile(file));
                Log.d("", file + "");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(
                "SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID").build();

        mInterstitialAd.loadAd(adRequest);

    }*/

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity to full screen mode.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.
                FLAG_FULLSCREEN);

        final Object colorGfxData = getLastNonConfigurationInstance();

        if (colorGfxData != null) {

            isSavedState = true;

            savedData = ((ColorGfxData) colorGfxData);

            sCurrentImageId = savedData.currentImageId;

        } else {

            isSavedState = false;

        }

        /*mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId("ca-app-pub-8135904395081184/1732645558");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();*/

        // Add the default content.
        setContentView(R.layout.fill_colors_in_images_activity);

        activity = FillColorsInImagesActivity.this;

        img_preview = (ImageView) findViewById(R.id.img_preview);

        tv_save = (TextView) findViewById(R.id.tv_save);
        /*tv_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                View inf = LayoutInflater.from(activity).inflate(
                        R.layout.dialog, null);

                TextView tv_dialog_ok = (TextView) inf.findViewById(R.id.tv_ok);
                tv_dialog_ok.setText("Yes");

                TextView tv_cancel = (TextView) inf
                        .findViewById(R.id.tv_cancel);
                tv_cancel.setText("No");

                TextView tv_thank_you = (TextView) inf
                        .findViewById(R.id.tv_thank_you);
                tv_thank_you.setVisibility(View.VISIBLE);
                tv_thank_you.setText("Pre School");

                TextView tv_error_string = (TextView) inf
                        .findViewById(R.id.tv_error_string);
                tv_error_string
                        .setText("Are you sure? Want to save the Final Picture?");

                final Dialog dialog = new Dialog(activity);
                dialog.setCancelable(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(inf);
                dialog.getWindow().setBackgroundDrawableResource(
                        R.drawable.rounded);
                dialog.show();

                tv_dialog_ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        saveShare("/Pre School", "yes");
                    }
                });

                tv_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
            }

        });*/

        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
                /*if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }*/
            }
        });

        // Determine whether or not the current device is a tablet.
        sIsTablet = getResources().getBoolean(R.bool.isTablet);

        sIsSmall = getResources().getBoolean(R.bool.isSmall);

        sIsNormal = getResources().getBoolean(R.bool.isNormal);

        sIsLarge = getResources().getBoolean(R.bool.isLarge);

        sIsExtraLarge = getResources().getBoolean(R.bool.isExtraLarge);
        //
        // final Object colorGfxData = getLastNonConfigurationInstance();
        //
        // if (colorGfxData != null) {
        //
        // isSavedState = true;
        // savedData = ((ColorGfxData) colorGfxData);
        // sCurrentImageId = savedData.currentImageId;
        // }
        // else {
        // isSavedState = false;
        // }

        Bundle extras = getIntent().getExtras();

        mMinImageId = 0;

        mMaxImageId = 3;

        ib_previous = (ImageButton) findViewById(R.id.ib_previous);

        ll_color_palette = (LinearLayout) findViewById(R.id.ll_color_palette);
        // mLlColorPaletteLeft2 = (LinearLayout)
        // findViewById(R.id.llColorPaletteLeft2);

        tb_brush_and_paint = (ToggleButton) findViewById(R.id.tb_brush_and_paint);
        tb_brush_and_paint.setChecked(true);

        ib_next = (ImageButton) findViewById(R.id.ib_next);

        ib_share = (ImageButton) findViewById(R.id.ib_share);
        ib_share.setOnClickListener(this);

        tb_erase_color = (ToggleButton) findViewById(R.id.tb_erase_color);

        fl_fill_color_image = (FrameLayout) findViewById(R.id.fl_fill_color_image);

        fl_fill_color_image.setDrawingCacheEnabled(true);
        fl_fill_color_image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                // TODO Auto-generated method stub
                height = fl_fill_color_image.getHeight();

                width = fl_fill_color_image.getWidth();

                init(colorGfxData);

                fl_fill_color_image.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });
    }

    public void init(Object colorGfxData) {

        // Create the progressbar view
        pbFloodFill = new ProgressBar(activity);
        // Give the progressbar some parameters.
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.
                WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;

        pbFloodFill.setLayoutParams(params);
        pbFloodFill.setIndeterminate(true);
        pbFloodFill.setVisibility(View.GONE);

        // Request focus to the FrameLayout containing the paint view.
        fl_fill_color_image.requestFocus();

        // Load the canvas.
        loadColorCanvas();

        // Load the color palettes.
        loadColorPalettes();

        // Create the palette buttons.
        loadPaletteButtons();

        // Load brushes
        loadBrushes();

        // Set any event listeners.
        ib_previous.setOnClickListener(this);

        ib_next.setOnClickListener(this);

        tb_brush_and_paint.setOnClickListener(this);

        tb_erase_color.setOnClickListener(this);

        if (colorGfxData != null) {

            if (savedData != null) {
                // Restore the previous data.
                colorGFX.selectedColor = savedData.selectedColor;
                colorGFX.isFillModeEnabled = savedData.isFillModeEnabled;
                colorGFX.isEraseModeEnabled = savedData.isEraseModeEnabled;
                colorGFX.paint = savedData.paint;
                // Set the disabled image3 resources for the brush and fill
                // buttons.
                if (savedData.isEraseModeEnabled) {

                    tb_brush_and_paint.setBackgroundResource(R.drawable.bucket_button_disabled);

                } else {

                    tb_brush_and_paint.setBackgroundResource(R.drawable.bucket_button);

                }
            }
        }
    }

    /*
     * Implements onRetainNonConfigurationInstance(). Saves current Gfx data for
     * resumes.
     */
    /*@Override
    public Object onRetainNonConfigurationInstance() {

        ColorGfxData colorGfxData = new ColorGfxData();

        colorGfxData.selectedColor = colorGFX.selectedColor;
        colorGfxData.isFillModeEnabled = colorGFX.isFillModeEnabled;
        colorGfxData.isEraseModeEnabled = colorGFX.isEraseModeEnabled;
        colorGfxData.bitmap = colorGFX.bitmap;
        colorGfxData.paint = colorGFX.paint;
        colorGfxData.currentImageId = sCurrentImageId;

        return colorGfxData;

    }*/

    /**
     * Loads the brush and it's stylings.
     */
    public void loadBrushes() {

        colorGFX.paint = new Paint();
        colorGFX.paint.setAntiAlias(true);
        colorGFX.paint.setDither(true);
        colorGFX.paint.setColor(colorGFX.selectedColor);
        colorGFX.paint.setStyle(Paint.Style.STROKE);
        colorGFX.paint.setStrokeJoin(Paint.Join.ROUND);
        colorGFX.paint.setStrokeCap(Paint.Cap.ROUND);
        colorGFX.paint.setStrokeWidth(12);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);

        colorGFX.paint.setMaskFilter(mBlur);

    }

    /**
     * Sets up the color palettes.
     */
    private void loadColorPalettes() {

		/*
         * Pallete 1
		 */

        // Create a tag and a HashMap of colors to assign to Palette1
        String tag = "Palette1";

        HashMap<String, Integer> colors = new HashMap<String, Integer>();
        colors.put("1_darkgreen", Color.rgb(0, 100, 0));
        colors.put("2_indigo", Color.rgb(255, 208, 70));
        colors.put("3_blue", Color.rgb(99, 184, 255));
        colors.put("4_green", Color.rgb(0, 255, 0));
        colors.put("5_orange", Color.rgb(255, 20, 147));
        colors.put("6_yellow", Color.rgb(255, 255, 0));
        colors.put("7_red", Color.rgb(255, 0, 0));
        colors.put("8_white", Color.rgb(255, 255, 255));
        colors.put("9_lightBlue", Color.rgb(0, 150, 214));
        colors.put("10_pink", Color.rgb(247, 228, 197));
        colors.put("11_brown", Color.rgb(139, 69, 19));
        colors.put("12_black", Color.rgb(0, 0, 0));
        // Create a new palette based on activity information.
        ColorPalette Palette1;

        if (isSavedState) {

            Palette1 = new ColorPalette(activity, colors, isSavedState, savedData.selectedColor,
                    "Large", colorGFX, hmPalette);
            //Palette1 = new ColorPalette(activity, colors, isSavedState, savedData.selectedColor);

        } else {

            Palette1 = new ColorPalette(activity, colors, "Large", colorGFX, hmPalette);
            //Palette1 = new ColorPalette(activity, colors);

        }

        // Add the palette to the HashMap.
        hmPalette.put(tag, Palette1);

        // /*
        // * Palette 2
        // */
        //
        // // Create a tag and a HashMap of colors to assign to Palette1
        // tag = "Palette2";
        //
        // HashMap<String, Integer> colors2 = new HashMap<String, Integer>();
        // colors2.put("1_red", Color.rgb(255,0, 0));
        // colors2.put("2_white", Color.rgb(255,255,255));
        // colors2.put("3_lightBlue", Color.rgb(0, 150, 214));
        // colors2.put("4_pink", Color.rgb(247,228,197));
        //
        // colors2.put("5_brown", Color.rgb(139,69,19));
        // colors2.put("6_black", Color.rgb(0, 0, 0));
        // // Create a new palette based on activity information.
        // ColorPalette Palette2;
        //
        // if (isSavedState) {
        // Palette2 = new ColorPalette(activity, colors2, isSavedState,
        // savedData.selectedColor);
        // } else {
        // Palette2 = new ColorPalette(activity, colors2);
        // }
        //
        // // Add the palette to the HashMap.
        // hmPalette.put(tag, Palette2);
    }

    /**
     * Creates Image Buttons for each color defined in each palette.
     */
    private void loadPaletteButtons() {
        // Iterate through the palettes
        for (String key : hmPalette.keySet()) {
            // Load the button size.bac
            hmPalette.get(key).calculateButtonSize();
            // Get the palette object and create ImageButtons for the color set
            // that corresponds to that palette object.
            hmPalette.get(key).createButtons();
            // Add the palettes to a view.
        }

        // Set the left Palette buttons on the screen.
        hmPalette.get("Palette1").addToView(ll_color_palette);

        // Set the left Palette buttons on the screen.
        // hmPalette.get("Palette2").addToView(mLlColorPaletteLeft2);

        // Set the right Palette buttons on the screen.
        // hmPalette.get("Palette3").addToView(mLlColorPaletteRight);
        //
        // // Set the right Palette buttons on the screen.
        // hmPalette.get("Palette4").addToView(mLlColorPaletteRight2);
    }

    public Bitmap svgToBitmap(Resources res, int resource, int height, int width) {

        try {
            // size = (int)(size*res.getDisplayMetrics().density);
            SVG svg = SVGParser.getSVGFromResource(res, resource);

            PictureDrawable pictureDrawable = svg.createPictureDrawable();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawPicture(pictureDrawable.getPicture(), new Rect(0, 0, width, height));

            // svg.renderToCanvas(canvas);
            return bitmap;

        } catch (SVGParseException e) {

            e.printStackTrace();

        }

        return null;

    }

    /**
     * Sets up the coloring canvas. Loads the bitmap and draws it to the screen
     * on the canvas.
     */
    private void loadColorCanvas() {

        // Load the first image3 in the currently selected coloring book.
        loadImage();

        // Add a tag so we can reference it later. Usually ids gen'd in a loop.
        colorGFX.setTag(0);

        // Give the ImageButton some parameters.
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                colorGFX.imageWidth, colorGFX.imageHeight);
        params.gravity = Gravity.CENTER;
        colorGFX.setLayoutParams(params);

        // Add the image3 view to the layout.
        fl_fill_color_image.addView(colorGFX);
        fl_fill_color_image.addView(pbFloodFill);
    }

    /**
     * Sets the image3 to display on the canvas.
     */
    private void loadImage() {

        if (sCurrentImageId < mMinImageId) {

            sCurrentImageId = mMaxImageId;

        }

        if (sCurrentImageId > mMaxImageId) {

            sCurrentImageId = mMinImageId;

        }

        // TODO
        String mResourcePaint = "image"+ String.valueOf(sCurrentImageId + 1);

        // Get the resource id based on the image3's file name.
        int resId = getResources().getIdentifier(mResourcePaint, "raw",
                getPackageName());

        Bitmap picture = svgToBitmap(getResources(), resId, height, width);

        String mResourcePaintSmall = "img" + String.valueOf(sCurrentImageId + 1);

        int resIdSmall = getResources().getIdentifier(mResourcePaintSmall,
                "drawable", getPackageName());

        Bitmap pictureSmall = BitmapFactory.decodeResource(getResources(),
                resIdSmall);
        img_preview.setImageBitmap(pictureSmall);

        if (colorGFX == null) {

            if (isSavedState && savedData != null) {

                colorGFX = new ColorGFX(activity, picture.getWidth(),
                        picture.getHeight(), isSavedState, savedData.bitmap, pbFloodFill);
                // colorGFX.isFillModeEnabled = true;
            } else {

                colorGFX = new ColorGFX(activity, picture.getWidth(), picture.getHeight(),
                        pbFloodFill);
                colorGFX.isFillModeEnabled = true;

            }
            colorGFX.resume();
        } else {
            // Clear the previous image3 and colors from the canvas.
            if (colorGFX.pathCanvas != null) {

                colorGFX.clear();
            }
        }

        // Clear the bitmaps from the screen.
        colorGFX.isNextImage = true;

        // Set the canvas's bitmap image3 so it can be drawn on canvas's run
        // method.
        colorGFX.pictureBitmapBuffer = picture;

        // Set the canvas's paint map.
        colorGFX.paintBitmapName = mResourcePaint;

    }

    /**
     * Resizes the image3 if it is too big for the screen. activity should almost
     * never really be needed if the proper images are supplied to the drawable
     * folders. However, in practice activity may not be the case and therefore,
     * activity is used as a protection against these bad cases.
     */
    // private Bitmap decodeImage(int resId) {
    //
    // // Get the screen width and height.
    // DisplayMetrics dm = new DisplayMetrics();
    // getWindowManager().getDefaultDisplay().getMetrics(dm);
    //
    //
    // float screenWidth = dm.widthPixels;
    // float screenHeight = dm.heightPixels;
    //
    // BitmapFactory.Options options = new BitmapFactory.Options();
    // options.inJustDecodeBounds = true;
    // BitmapFactory.decodeResource(getResources(),
    // resId, options);
    //
    // int inSampleSize = 1;
    // int imageWidth = options.outWidth;
    // int imageHeight = options.outHeight;
    //
    // // If the scale fails, we will need to use more memory to perform
    // // scaling for the layout to work on all size screens.
    // boolean scaleFailed = false;
    // Bitmap scaledBitmap = null;
    // float resizeRatioHeight = 1;
    //
    // // activity IS DESIGNED FOR FITTING ON THE SCREEN WITH NO SCROLLBAR
    //
    // // Scale down if the image3 width exceeds the screen width.
    // if (imageWidth > screenWidth || imageHeight > screenHeight) {
    //
    // // If we need to resize the image3 because the width or height is too
    // // big, get the resize ratios for width and height.
    // resizeRatioHeight = (float) imageHeight / (float) screenHeight;
    //
    // // Get the smaller ratio.
    // inSampleSize = (int) resizeRatioHeight;
    //
    // if (inSampleSize <= 1) {
    // scaleFailed = true;
    // }
    // }
    //
    // // Decode Bitmap with inSampleSize set
    // options.inSampleSize = inSampleSize;
    // options.inJustDecodeBounds = false;
    //
    // Bitmap picture = BitmapFactory.decodeResource(getResources(), resId,
    // options);
    //
    // // If the scale failed, that means a scale was needed but didn't happen.
    // // We need to create a scaled copy of the image3 by allocating more
    // // memory.
    // if (scaleFailed) {
    // int newWidth = (int) (picture.getWidth() / resizeRatioHeight);
    // int newHeight = (int) (picture.getHeight() / resizeRatioHeight);
    //
    // scaledBitmap = Bitmap.createScaledBitmap(picture, newWidth, newHeight,
    // true);
    //
    // // Recycle the picture bitmap.
    // picture.recycle();
    // }
    // else {
    // // No scaling was needed in the first place!
    // scaledBitmap = picture;
    // }
    //
    // return scaledBitmap;
    // }

    /**
     * Implements onBackPressed().
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        /*if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }*/
        // Ensure that the music will continue to play when the user returns to
        // another activity.
    }

    /**
     * Implements onPause().
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Pause the thread.
        if (colorGFX != null) {
            colorGFX.pause();
        }
    }

    /**
     * Implements onResume().
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (colorGFX != null) {

            ColorGfxData colorGfxData = new ColorGfxData();

            colorGfxData.selectedColor = colorGFX.selectedColor;
            colorGfxData.isFillModeEnabled = colorGFX.isFillModeEnabled;
            colorGfxData.isEraseModeEnabled = colorGFX.isEraseModeEnabled;
            colorGfxData.bitmap = colorGFX.bitmap;
            colorGfxData.paint = colorGFX.paint;
            colorGfxData.currentImageId = sCurrentImageId;

            colorGFX.resume();

        }
    }

    /**
     * Implements onClick().
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ib_previous:
                // Switch to the previous image3.
                sCurrentImageId--;
                // Set the direction boolean for table row skipping if an id
                // doesn't
                // exist.
                isDirectionRight = false;

                // If the user goes below the minimum possible image3, we cycle
                // around back
                // to the max.
                if (sCurrentImageId < mMinImageId) {

                    sCurrentImageId = mMaxImageId;

                }

                // If a fill op is happening, kill it.
                if (colorGFX.mThread != null && colorGFX.mThread.isAlive()) {

                    colorGFX.isThreadBroken = true;

                }

                // Set the savedState flag to false.
                isSavedState = false;
                // Load the image3.
                loadImage();

                break;

            case R.id.ib_next:

                // Switch to the next image3.
                sCurrentImageId++;
                // Set the direction boolean for table row skipping if an id
                // doesn't
                // exist.
                isDirectionRight = true;

                // If the user goes above the maximum possible image3, we cycle
                // around back
                // to the min.
                if (sCurrentImageId > mMaxImageId) {
                    sCurrentImageId = mMinImageId;
                }

                // If a fill op is happening, kill it.
                if (colorGFX.mThread != null && colorGFX.mThread.isAlive()) {
                    colorGFX.isThreadBroken = true;
                }

                // Set the savedState flag to false.
                isSavedState = false;

                // Load the image3.
                loadImage();

                break;

            case R.id.ib_share:

                //saveShare("/Pre School/Shared", "no");

                break;

            case R.id.tb_brush_and_paint:

                // Check to see if erase mode is enabled.
                if (tb_erase_color.isChecked()) {
                    // If it is, simply set activity button as the enabled button.

                    // Prevent toggle.
                    tb_brush_and_paint.setChecked(!tb_brush_and_paint.isChecked());
                    colorGFX.isFillModeEnabled = tb_brush_and_paint.isChecked();

                    // Disable erase mode
                    colorGFX.paint.setXfermode(null);
                    // Set the blur mode on again for path drawing.
                    colorGFX.paint.setMaskFilter(mBlur);
                    // Set the isEraseModeEnabled boolean
                    colorGFX.isEraseModeEnabled = false;

                    // Turn the eraser button off.
                    tb_erase_color.setChecked(false);

                    // Replace the drawable with the color versions.
                    tb_brush_and_paint.setBackgroundResource(R.drawable.bucket_button);

                } else {

                    colorGFX.isFillModeEnabled = tb_brush_and_paint.isChecked();

                }

                break;

            case R.id.tb_erase_color:

                boolean isEraseModeEnabled = tb_erase_color.isChecked();

                if (isEraseModeEnabled) {

                    // Set the disabled image3 resources for the brush and fill
                    // buttons.
                    tb_brush_and_paint.setBackgroundResource(R.drawable.bucket_button_disabled);

                    // Set the current brush mode to erase.
                    colorGFX.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    // Take the blur mode off for the eraser.
                    colorGFX.paint.setMaskFilter(null);
                    // Set the colorGFX isEraseModeEnabled Boolean
                    colorGFX.isEraseModeEnabled = true;

                } else {
                    // Set the enabled image3 resources for the brush and fill
                    // buttons.
                    tb_brush_and_paint.setBackgroundResource(R.drawable.bucket_button);

                    colorGFX.paint.setXfermode(null);
                    // Set the blur mode on again for path drawing.
                    colorGFX.paint.setMaskFilter(mBlur);
                    // Set the isEraseModeEnabled boolean
                    colorGFX.isEraseModeEnabled = false;

                }

                break;

        }
    }
}
