package com.common.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

/**
 * 工具类
 *
 * @author wangzy
 */
public class Tool {


    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static String getDisplayMetricsText(Context cx) {

        StringBuffer sbf = new StringBuffer();
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        sbf.append("\nThe absolute width:" + String.valueOf(screenWidth) + " pixels\n");
        sbf.append("The absolute heightin:" + String.valueOf(screenHeight)
                + " pixels\n");
        sbf.append("The logical density of the display.:" + String.valueOf(density)
                + "\n");
        sbf.append("X dimension :" + String.valueOf(xdpi) + "pixels per inch\n");
        sbf.append("Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n");
        return sbf.toString();
    }

    public static void sendShareFile(Context context, File file) {
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("application/octet-stream");
//        String[] emailReciver = new String[] {  };
//
//        String emailTitle = "微信聊天记录";
//        String emailContent = "内容";
//        email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
//        email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);
//        email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
        // 附件
        email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        // 调用系统的邮件系统
        context.startActivity(Intent.createChooser(email, "Send with"));

    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * getListView realChild View
     *
     * @param listView
     * @param wantedPosition
     * @return
     */
    public static View getChildViwFromList(ListView listView, int wantedPosition) {

        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0

        int wantedChild = wantedPosition - firstPosition;
        if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
            return null;
        }
        View wantedView = listView.getChildAt(wantedChild);

        return wantedView;
    }


    /**
     * 读取RAW
     *
     * @param activity
     * @param id
     * @return
     */
    public static String readStringFromStream(Activity activity, int id) {
        try {
            InputStream is = activity.getResources().openRawResource(id);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = -1;
            byte[] buffer = new byte[512];
            while ((i = is.read(buffer)) != -1) {
                bo.write(buffer, 0, i);
            }
            bo.flush();
            byte[] bytes = bo.toByteArray();
            String ret = new String(bytes);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("wzy", "err:" + e.getLocalizedMessage());
            return "";
        }

    }

    public static float getFontHeight(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return Math.abs(fm.descent - fm.ascent);
    }

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return Math.abs(fm.leading - fm.ascent);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] getFileContent(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedInputStream bfi = new BufferedInputStream(new FileInputStream(file));
                ByteArrayOutputStream bro = new ByteArrayOutputStream();
                byte[] buffer = new byte[512];
                int ret = -1;
                while ((ret = bfi.read(buffer)) != -1) {
                    bro.write(buffer, 0, ret);
                }
                bro.flush();
                return bro.toByteArray();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String getFileType(String fileUri) {
        File file = new File(fileUri);
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return fileType;
    }

    public static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }

    public static long getCacheSize(Context context) {
        File cacheDir = context.getCacheDir();
        return cacheDir.getTotalSpace() - cacheDir.getUsableSpace();
    }

    public static void clearCahce(Context context) {
        File cacheDir = context.getCacheDir();
        searchDirs(cacheDir);
    }

    private static void searchDirs(File fdir) {
        File[] lsfirs = fdir.listFiles();
        for (File f : lsfirs) {
            if (f.isFile()) {
                f.delete();
                LogUtil.i("wzy", "delte:" + f.getAbsolutePath());
            } else {
                searchDirs(f);
            }
        }
    }

    public static void startActivityForResult(Activity activity, Class claz, int requestCode) {
        Intent intent = new Intent(activity, claz);
        activity.startActivityForResult(intent, requestCode);
    }

    // 选择图片与裁剪＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
    // 裁剪意图，用于相册
    public static void startPhotoZoom(Activity activity, Uri uri, File outputFile, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    // 裁剪意图，用于拍照
    public static void startCropImg(Activity activity, Bitmap data, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void chosePicFromAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void chosePicFromCamera(Activity activity, int requestCode) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 选择图片与裁剪＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝end=====

    public static void showMessageDialog(String message, Activity ativity) {

        Builder builder = new Builder(ativity);
        builder.setMessage(message);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    public static byte[] int2bytearray(int i) {

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        buf.write(i);
        byte[] b = buf.toByteArray();
        return b;

        // byte[] result = new byte[4];
        // result[3] = (byte) ((i >> 24) & 0xFF);
        // result[2] = (byte) ((i >> 16) & 0xFF);
        // result[1] = (byte) ((i >> 8) & 0xFF);
        // result[0] = (byte) (i & 0xFF);
        // return result;
    }

    public static void testCrash() {
        int x = 0;
        int y = 1 / x;
    }

    /**
     * 验证是身份证
     *
     * @param idCard
     * @return
     */
    public static boolean checkIdCard(String idCard) {
        IdcardValidator idca = new IdcardValidator();
        return idca.isValidatedAllIdcard(idCard);
    }

    /**
     * 判断sd卡是否可用
     *
     * @return
     */
    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 直接跳转到另一个activity
     *
     * @param fromContext
     * @param toContext
     */
    public static void startActivity(Context fromContext, Class claz) {
        Intent i = new Intent();
        i.setClass(fromContext, claz);
        fromContext.startActivity(i);
    }


    public static void startActivity(Context fromContext, Class claz, Intent i) {
        i.setClass(fromContext, claz);
        fromContext.startActivity(i);
    }

    /**
     * 安装apk
     *
     * @param dir
     */
    public static void installApk(Context context, String dir) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(dir)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 读取apk安装包的信息
     *
     * @param apkDir
     * @return{packageName,version
     */
    public static String[] getAppApkInfo(Context context, String apkDir) {

        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkDir, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String packageName = appInfo.packageName; // 得到安装包名称
            String version = info.versionName; // 得到版本信息
            String infos[] = {packageName, version};
            return infos;
        }
        return null;
    }

    /**
     * 文件拷贝
     *
     * @param finsrc
     * @param dst
     * @return
     */
    public static boolean fileCopy(InputStream finsrc, File dst) {
        try {
            if (null == dst) {
                return false;
            } else {
                dst.delete();
                dst.createNewFile();
                FileOutputStream fout = new FileOutputStream(dst);
                byte[] buffer = new byte[512];
                int readsize = 0;
                while ((readsize = finsrc.read(buffer)) != -1) {
                    fout.write(buffer, 0, readsize);
                }
                // fout.flush();
                finsrc.close();
                fout.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(Constant.TAG, "copy file error:" + e.getMessage());
            return false;
        }
    }

    public static float getScreenDensity(Context context) {

        return context.getResources().getDisplayMetrics().density;
    }

    // dip转像素
    public int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;
    }

    // 像素转dip
    public float PixelsToDip(Context context, int Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float dips = Pixels / SCALE;

        return dips;

    }

    /**
     * 获取渠道号
     *
     * @return
     */
    public static String getChannel(Context context) {
        String chnnel = "";
        ArrayList<String> lines = AssertTool.readLinesFromAssertsFiles(context, "channel.txt");
        chnnel = lines.get(0).trim();
        return chnnel;
    }

    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        String version = "-1";
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 启动一个新的Activity
     *
     * @param ctx
     * @param clasz
     */
    public static void startOtherActivity(Activity ctx, Class clasz) {
        Intent intent = new Intent();
        intent.setClass(ctx, clasz);
        ctx.startActivity(intent);
        // ctx.overridePendingTransition(R.anim.activity_anim_in,
        // R.anim.activity_anim_out);
    }

    /**
     * 获取屏幕高度宽度
     *
     * @param ctx
     * @return
     */
    public static Point getDisplayMetrics(Context ctx) {
        DisplayMetrics metrcis = ctx.getResources().getDisplayMetrics();
        Point metricsPoint = new Point();
        metricsPoint.x = metrcis.widthPixels;
        metricsPoint.y = metrcis.heightPixels;
        return metricsPoint;
    }

    /**
     * 参数必须为：getDisplayMetrics返回值
     *
     * @param point
     * @return
     */
    public static Point getRandomPoint(Point point) {
        Random rand = new Random();
        int rx = rand.nextInt(point.x);
        int ry = rand.nextInt(point.y);
        return new Point(rx, ry);
    }

    /**
     * 根据设定后的宽度和高度设置按钮的出事随机值
     *
     * @param width
     * @param height
     * @return
     */
    public static Point getDimensionsByDimens(int width, int height) {
        Random rand = new Random();
        int rx = rand.nextInt(width);
        int ry = rand.nextInt(height);
        return new Point(rx, ry);
    }

    /**
     * 创建一个dlgbuilder
     *
     * @param context
     * @param view
     * @return
     */
    public static Builder createADialig(Context context, View view) {
        Builder alertDialog = new Builder(context);
        alertDialog.setView(view);
        return alertDialog;
    }

    /**
     * 发送广播
     *
     * @param context
     * @param intent
     */
    public static void sendBoardCast(Context context, Intent intent) {
        context.sendBroadcast(intent);
    }

    /**
     * 请求url
     *
     * @param context
     * @param url
     */
    public static void startUrl(Context mContext, String url) {

        // DownloadManager.Request down=new DownloadManager.Request
        // (Uri.parse(url));
        // DownloadManager manager =
        // (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        // //设置允许使用的网络类型，这里是移动网络和wifi都可以
        // down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        // //禁止发出通知，既后台下载
        // down.setShowRunningNotification(true);
        // //不显示下载界面
        // down.setVisibleInDownloadsUi(false);
        // //设置下载后文件存放的位置
        // down.setDestinationInExternalFilesDir(mContext, null,
        // "skyworth.apk");
        // //将下载请求放入队列
        // manager.enqueue(down);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        mContext.startActivity(i);
    }

    /**
     * 获取状态栏和标题栏的高度和宽度
     *
     * @param context
     * @return
     */
    public static int getAppRect(Activity context) {
        /**
         * decorView是window中的最顶层view，可以从window中获取到decorView，
         * 然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏
         */
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;// 状态栏

        /**
         * getWindow().findViewById(Window.ID_ANDROID_CONTENT)
         * 这个方法获取到的view就是程序不包括标题栏的部分，然后就可以知道标题栏的高度了。
         */
        int contentTop = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;// 标题栏
        return titleBarHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int geteAppUnVisibleHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;// 状态栏

        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;// 标题栏
        return statusBarHeight + titleBarHeight;
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void mkdir(String dir) {
        File file = new File(dir);
        file.mkdir();
    }

    /**
     * 把asset下面的资源拷贝到sdcard下面
     *
     * @param context
     * @param destFile
     * @throws IOException
     */
    public static void copyAssetFile2Sdcard(Context context, String fname, String destFile) throws IOException {
        AssetManager asm = context.getAssets();
        File df = new File(destFile);
        if (df.exists() == false) {
            df.mkdir();
        } else {
            InputStream os = asm.open(fname);
            byte buf[] = new byte[256];
            int b = -1;
            FileOutputStream fout = new FileOutputStream(destFile + fname);
            while ((b = os.read(buf)) != -1) {
                fout.write(buf, 0, buf.length);
            }
            fout.flush();
            os.close();
            fout.close();
        }

    }

    public static void ToastShow(Activity activity, int id) {
        if (!activity.isFinishing()) {
            Toast.makeText(activity, activity.getResources().getString(id), Toast.LENGTH_SHORT).show();
        }
    }

    public static void ToastShow(Activity activity, String message) {
        if (!activity.isFinishing()) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * m 获取wifi型号强度
     *
     * @return
     */
    public static void showWifiStrenth(Activity context) {
        // 2.得到的值是一个0到-100的区间值，是一个int型数据，
        // 其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，
        // 有可能连接不上或者掉线
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int rssi = wifiInfo.getRssi();
        if (rssi < 0 && rssi >= -50) {
            // Tool.ToastShow(context, "亲，你的wifi信号不错！");
        } else {
            Tool.ToastShow(context, "亲，你的wifi信号较差,使用起来不够流畅！");
        }
    }

    /**
     * 获取imei,如果获取不到，则生成一个15位号码
     *
     * @return
     */
    public static String getImei(final Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei;
        try {
            imei = tm.getDeviceId();// imei
        } catch (Exception e) {
            imei = null;
        }

        if (StringUtils.isEmpty(imei) || "0".equals(imei)) {
            // 如果imei号为空或0，取mac地址作为imei号传递
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            imei = info.getMacAddress();

            // 如果mac地址为空或0，则通过uuid生成的imei号来传递
            if (StringUtils.isEmpty(imei) || "0".equals(imei)) {

                imei = SharePersistent.getPerference(context, Constant.STRING_IMEI);

                if (StringUtils.isEmpty(imei)) {
                    imei = UUIDGenerator.getUUID(15);
                    if (StringUtils.isEmpty(imei)) {
                        return "0";
                    }
                    SharePersistent.savePerference(context, Constant.STRING_IMEI, imei);
                }
            }
        }

        return imei;
    }

    /**
     * 比较版本号，确定是否升级
     *
     * @param locationVersion
     * @param newVersion
     * @return
     */
    public static boolean compareAppVersionName(String locationVersion, String newVersion) {

        try {
            String[] locationVersions = locationVersion.split("\\.");
            String[] newVersions = newVersion.split("\\.");
            StringBuffer sbfOld = new StringBuffer();
            StringBuffer sbfNew = new StringBuffer();
            for (int i = 0, isize = locationVersions.length; i < isize; i++) {
                int old = Integer.parseInt(locationVersions[i]);
                int now = Integer.parseInt(newVersions[i]);
                sbfOld.append(old);
                sbfNew.append(now);
            }
            int old = Integer.parseInt(sbfOld.toString());
            int now = Integer.parseInt(sbfNew.toString());
            if (now > old) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拨号
     *
     * @param number
     * @param context
     */
    private static void dial(String number, Context context) {
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Object iTelephony;
            iTelephony = (Object) getITelephonyMethod.invoke(tManager, (Object[]) null);
            Method dial = iTelephony.getClass().getDeclaredMethod("dial", String.class);
            dial.invoke(iTelephony, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打电话
     *
     * @param number
     * @param context
     */
    private static void call(String number, Context context) {
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Object iTelephony;
            iTelephony = (Object) getITelephonyMethod.invoke(tManager, (Object[]) null);
            Method dial = iTelephony.getClass().getDeclaredMethod("call", String.class);
            dial.invoke(iTelephony, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程休眠
     *
     * @param times
     */
    public static void delay(long times) {
        try {
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRightLocation(String lat, String lng) {
        try {
            float lt = Math.abs(Float.parseFloat(lat));
            float lg = Math.abs(Float.parseFloat(lng));
            if ((lg > 0 && lg <= 180) && (lt > 0 && lt <= 90)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
