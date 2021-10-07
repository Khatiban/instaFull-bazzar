package com.instagram.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

import com.adapters.ImageModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadFileFromService {
    int NotifId;
    Context f98c;
    String extension;
    String fileName;
    FileDownloadFromServiceListener mListener;
    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    /* renamed from: com.instagram.data.DownloadFileFromService.1 */
    class C14331 extends AsyncTask<Void, Integer[], Boolean> {
        float lastProgress;
        private final /* synthetic */ ArrayList val$downloadableImages;

        C14331(ArrayList arrayList) {
            this.val$downloadableImages = arrayList;
            this.lastProgress = 0.0f;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... params) {
            one_play_preferences = f98c.getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
            one_play_editor = one_play_preferences.edit();
            String name_pic = one_play_preferences.getString("IMAGE_POST", "InstaFull/Pictures");
            String name_video = one_play_preferences.getString("VIDEO_POST", "InstaFull/Video");
            File fDirPic = new File(Environment.getExternalStorageDirectory().toString() + "/" + name_pic);
            if (!fDirPic.exists()) {
                fDirPic.mkdirs();
            }
            File fDirVid = new File(Environment.getExternalStorageDirectory().toString() + "/" + name_video);
            if (!fDirVid.exists()) {
                fDirVid.mkdirs();
            }
            int currentProgress = 0;
            int totalItems = this.val$downloadableImages.size();
            for (int i = 0; i < totalItems; i++) {
                int currentItem = i;
                ImageModel model = (ImageModel) this.val$downloadableImages.get(i);
                URL url = null;
                try {
                    url = new URL(model.filePath);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                URLConnection conection = null;
                try {
                    conection = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    conection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int lenghtOfFile = conection.getContentLength();
                InputStream input = null;
                try {
                    input = new BufferedInputStream(url.openStream(), AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File fTargetDir = fDirPic;
                if (model.type == 0) {
                    fTargetDir = fDirPic;
                    model.fileName = model.fileName.substring(0, model.fileName.indexOf(".jpg") + 4);
                } else {
                    fTargetDir = fDirVid;
                }
                StringBuilder stringBuilder = new StringBuilder();
                File out = new File(stringBuilder.append(fTargetDir).append("/").append(fileName).append(".").append(DownloadFileFromService.this.extension).toString());
                if (out.exists()) {
                    Object[] objArr = new Integer[1][];
                    Integer[] numArr = new Integer[3];
                    numArr[0] = Integer.valueOf(currentItem + 1);
                    numArr[1] = Integer.valueOf(totalItems);
                    numArr[2] = Integer.valueOf(currentItem % 2 == 0 ? 100 : 0);
                    objArr[0] = numArr;
                    publishProgress((Integer[][]) objArr);
                } else {
                    OutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(out);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    byte[] data = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                    long total = 0;
                    Object[] objArr2 = new Integer[1][];
                    objArr2[0] = new Integer[]{Integer.valueOf(currentItem + 1), Integer.valueOf(totalItems), Integer.valueOf(currentProgress)};
                    publishProgress((Integer[][]) objArr2);
                    while (true) {
                        int count = 0;
                        try {
                            count = input.read(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (count == -1) {
                            break;
                        }
                        total += (long) count;
                        currentProgress = (int) ((100 * total) / ((long) lenghtOfFile));
                        if (((float) currentProgress) - this.lastProgress > 0.0f) {
                            objArr2 = new Integer[1][];
                            objArr2[0] = new Integer[]{Integer.valueOf(currentItem + 1), Integer.valueOf(totalItems), Integer.valueOf(currentProgress)};
                            publishProgress((Integer[][]) objArr2);
                            this.lastProgress = (float) currentProgress;
                        }
                        try {
                            fileOutputStream.write(data, 0, count);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (model.type == 0) {
                        Utils.scanMedia(DownloadFileFromService.this.f98c, out, "image/*");
                    } else {
                        try {
                            Utils.scanMedia(DownloadFileFromService.this.f98c, out, "video/*");
                        } catch (Exception e) {
                            return Boolean.valueOf(false);
                        }
                    }
                    try {
                        fileOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer[]... values) {
            DownloadFileFromService.this.mListener.onProgressUpdate(DownloadFileFromService.this.NotifId, values[0][2].intValue());
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Boolean result) {
            try {
                if (result.booleanValue()) {
                    DownloadFileFromService.this.mListener.onFileDownloadComplete(DownloadFileFromService.this.NotifId);
                } else {
                    DownloadFileFromService.this.mListener.onConnectionError(DownloadFileFromService.this.NotifId);
                }
            } catch (Exception e) {
            }
            super.onPostExecute(result);
        }
    }

    public interface FileDownloadFromServiceListener {
        void onConnectionError(int i);

        void onFileDownloadComplete(int i);

        void onProgressUpdate(int i, int i2);
    }

    public DownloadFileFromService(Context c, String fileName, String extension, FileDownloadFromServiceListener mListener, int notifId) {
        this.mListener = mListener;
        this.f98c = c;
        this.NotifId = notifId;
        this.fileName = fileName;
        this.extension = extension;
    }

    public void doDownloadFile(ArrayList<ImageModel> downloadableImages) {
        new C14331(downloadableImages).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}
