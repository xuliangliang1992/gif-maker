package com.highlands.common.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.highlands.common.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import timber.log.Timber;

/**
 * @author xll
 * @date 2018/1/1
 */
public class FileUtil {

    /**
     * 图片保存路径
     */
    public static String IMAGE_SD_PATH = Environment.getExternalStorageState() + File.separator + "com.xll.gif" + File.separator + "Photo/";
    public static String VIDEO_SD_PATH = Environment.getExternalStorageState() + File.separator + "com.xll.gif" + File.separator + "video/";
    public static String GIF_SD_PATH = Environment.getExternalStorageState() + File.separator + "com.xll.gif" + File.separator + "gif/";
    /**
     * apk保存路径
     */
    private static String APK_SD_PATH = Environment.getExternalStorageState() + File.separator + BaseApplication.APP_PACKAGE + File.separator + "Apk_Sp/";
    /**
     * 附件保存路径
     */
    public static String FILE_SD_PATH = Environment.getExternalStorageState() + File.separator + BaseApplication.APP_PACKAGE + File.separator + "Contract/";
    /**
     * 缓存地址
     */
    public static String CACHE_SD_PATH = Environment.getExternalStorageState() + File.separator + BaseApplication.APP_PACKAGE + File.separator + "Cache/";
    /**
     * 文件类型0:图片 1:apk
     */
    public static final int FILE_TYPE_IMAGE = 0;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final int FILE_TYPE_GIF = 3;
    public static final int FILE_TYPE_APK = 1;

    /**
     * @return boolean
     * @Title: sdCardExist
     * @Description: 检测手机是否存在sd card
     */
    public static boolean sdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * @param folderPath
     * @return boolean
     * @Title: createFolder
     * @Description: 新建文件夹
     */
    public static boolean createFolder(String folderPath) {
        if (!sdCardExist()) {
            return false;
        }
        if (!TextUtils.isEmpty(folderPath)) {
            File dir = new File(folderPath);
            if (!dir.exists()) {
                return dir.mkdirs();
            }
        }
        return false;
    }

    /**
     * @param folderPath
     * @return boolean
     * @Title: createFolder
     * @Description: 新建文件夹
     */
    public static String createDir(String folderPath) {
        if (!sdCardExist()) {
            return "";
        }
        if (!TextUtils.isEmpty(folderPath)) {
            File dir = new File(folderPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getPath();
        }
        return "";
    }

    public static File createFile(Context context,String fileName) {
        Timber.tag("aaa->").e("is: " + context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        File filePath = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/gif/");
        if (!filePath.exists()) {
            boolean is = filePath.mkdirs();
            Timber.tag("aaa->").e("is: " + is);
        }
        File file = new File(filePath,fileName);
        return file;
    }

    public static File createFile(String fileName, int fileType) {
        if (!sdCardExist()) {
            return null;
        }
        String filePath = "";
        if (fileType == FILE_TYPE_IMAGE) {
            filePath = IMAGE_SD_PATH + fileName;
        } else if (fileType == FILE_TYPE_VIDEO) {
            filePath = VIDEO_SD_PATH + fileName;
        } else if (fileType == FILE_TYPE_GIF) {
            filePath = GIF_SD_PATH + fileName;
        } else if (fileType == FILE_TYPE_APK) {
            filePath = APK_SD_PATH + fileName;
        }
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File createFile(String fileName) {
        if (!sdCardExist()) {
            return null;
        }
        File file = new File(fileName);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

  /*  public static boolean saveApkOnSdCard(ResponseBody responseBody, String fileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        try {
            is = responseBody.byteStream();

            File cacheFile = FileUtils.createFile(fileName, FileUtils.FILE_TYPE_APK);
            if (cacheFile != null) {
                fos = new FileOutputStream(cacheFile);
                bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean saveFileOnSdCard(ResponseBody responseBody, String fileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        try {
            is = responseBody.byteStream();

            File cacheFile = FileUtils.createFile(fileName);
            if (cacheFile != null) {
                fos = new FileOutputStream(cacheFile);
                bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/

    /**
     * 删除此路径名表示的文件或目录。 </p>
     * <p>
     * 如果此路径名表示一个目录，则会先删除目录下的内容再将目录删除，所以该操作不是原子性的。</p>
     * <p>
     * 如果目录中还有目录，则会引发递归动作。</p>
     *
     * @param filePath 要删除文件或目录的路径。
     * @return 当且仅当成功删除文件或目录时，返回 true；否则返回 false。
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return deleteFile(file);
    }

    /**
     * 删除此路径名表示的文件或目录。 </p>
     * <p>
     * 如果此路径名表示一个目录，则会先删除目录下的内容再将目录删除，所以该操作不是原子性的。</p>
     * <p>
     * 如果目录中还有目录，则会引发递归动作。</p>
     * <p>
     * 要删除文件或目录的File。
     *
     * @return 当且仅当成功删除文件或目录时，返回 true；否则返回 false。
     */
    public static boolean deleteFile(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return false;
        }
        for (File deleteFile : files) {
            if (deleteFile.isDirectory()) {
                // 如果是文件夹，则递归删除下面的文件后再删除该文件夹
                if (!deleteFile(deleteFile)) {
                    // 如果失败则返回
                    return false;
                }
            } else {
                if (!deleteFile.delete()) {
                    // 如果失败则返回
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static String getAbsolutePath(final File file) {
        return file != null ? file.getAbsolutePath() : null;
    }

    public static boolean deleteFile2(String filePath) {
        if (StringUtil.isStringNull(filePath)) {
            return false;
        }
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 将文件转为String
     *
     * @param file
     * @return
     */
    public static String file2String(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            bos.close();
            in.close();
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断文件是否存在
     */
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
