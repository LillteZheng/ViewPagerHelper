package com.zhengsr.viewpagerhelper.rx;

import android.content.Context;
import android.view.View;

import java.io.File;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by zhengshaorui
 * Time on 2018/8/14
 */

public class RxNetClient {
    private static final String TAG = "RestClient";
    private Builder mBuilder;

    public RxNetClient(Builder builder) {
        this.mBuilder = builder;
    }

    public Observable<String> get() {
        return reqeust(HttpMethod.GET);

    }
    public RxNetClient uploadFile(){

        return this;
    }

    private Observable<String> reqeust(HttpMethod method) {
        RxNetService service = RxNetCreate.getService();
        Observable<String> observable = null;
        switch (method) {
            case GET:
                observable = service.get(mBuilder.getUrl(), mBuilder.getParams());
                break;
            case UPLOAD :

                break;
            default:
                break;

        }
        return observable;

    }

    public static class Builder {
        String url;
        RequestBody body;
        Context context;
        View dialogview;
        String downloadDir;
        String extension;
        String name;
        File file;
        WeakHashMap<String, Object> params = new WeakHashMap<>();


        public Builder setParams(WeakHashMap<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder setParams(String key,Object value){
            this.params.put(key,value);
            return this;
        }



        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setBody(RequestBody body) {
            this.body = body;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setDialogview(View dialogview) {
            this.dialogview = dialogview;
            return this;
        }

        public Builder setDownloadDir(String downloadDir) {
            this.downloadDir = downloadDir;
            return this;
        }

        public Builder setExtension(String extension) {
            this.extension = extension;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        public RxNetClient builder() {
            return new RxNetClient(this);
        }

        public String getUrl() {
            return url;
        }

        public RequestBody getBody() {
            return body;
        }

        public Context getContext() {
            return context;
        }

        public View getDialogview() {
            return dialogview;
        }

        public String getDownloadDir() {
            return downloadDir;
        }

        public String getExtension() {
            return extension;
        }

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }

        public WeakHashMap<String, Object> getParams() {
            return params;
        }
    }
}
