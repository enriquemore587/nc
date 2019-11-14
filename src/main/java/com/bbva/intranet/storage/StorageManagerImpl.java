package com.bbva.intranet.storage;

import com.bbva.intranet.storage.exceptions.StorageException;
import com.bbva.intranet.storage.utilities.StorageUtility;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class StorageManagerImpl {

    private static Logger LOG = LoggerFactory.getLogger(StorageManagerImpl.class);

    public void saveDevice(Object obj) throws StorageException {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            String path = String.format("/s/v1/url");
            HttpPost httpPost = new HttpPost(path);
            httpPost.addHeader("requestor", "requestor");
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            httpPost.setHeader("Accept-Encoding", "UTF-8");
            Gson gson = new Gson();
            String jsonToSend = gson.toJson(obj);
            LOG.info("JSON to Send" +  jsonToSend);
            StringEntity postingString = new StringEntity(jsonToSend, UTF_8.toString());
            postingString.setContentType("application/json; charset=UTF-8");
            postingString.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json;charset=UTF-8"));
            postingString.setChunked(true);
            httpPost.setEntity(postingString);
            HttpResponse response = httpClient.execute(httpPost);
            StorageUtility.checkResponse(response);
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            LOG.error("Uncontrolled error");
            throw new StorageUtility.InternalServerError(e.getMessage(), e.getCause().hashCode());
        }
    }

}
