package com.trackme.trackmeapplication.httpConnection;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Connection thread is the main thread for the connection between client and server.
 *
 * @author Mattia Tibaldi
 */
public class ConnectionThread extends Thread {

    private URL url;
    private HttpMethod httpAction;
    private HttpEntity entity;

    private HttpStatus statusReturned;
    private String stringReturned;

    private SSLContext sllContext;
    private HostnameVerifier hostnameVerifier;

    private LockInterface lock;

    /**
     * Constructor
     *
     * @param sslContext ssl context with ssl config
     * @param hostnameVerifier object to verify the server address.
     * @param lockInterface interface for handling the synchronization
     */
    ConnectionThread(SSLContext sslContext, HostnameVerifier hostnameVerifier, LockInterface lockInterface) {
        this.sllContext = sslContext;
        this.hostnameVerifier = hostnameVerifier;
        this.lock = lockInterface;
    }

    /**
     * Setter method.
     *
     * @param url to set.
     */
    void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter method.
     *
     * @param httpAction action to set.
     */
    void setHttpAction(HttpMethod httpAction) {
        this.httpAction = httpAction;
    }

    /**
     * Setter method
     *
     * @param entity to set.
     */
    void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        InputStream in = null;
        HttpsURLConnection urlConnection = null;
        OutputStream out = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(hostnameVerifier);
            urlConnection.setSSLSocketFactory(sllContext.getSocketFactory());
            urlConnection.setDoInput(true);

            if (entity.getHeaders().getAuthorization() != null)
                urlConnection.setRequestProperty("Authorization",entity.getHeaders().getAuthorization());

            switch (httpAction){
                case GET:
                    in = new BufferedInputStream(urlConnection.getInputStream());
                    onPostExecute(readStream(in), HttpStatus.valueOf(urlConnection.getResponseCode()));
                    break;
                case POST:
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    out = new BufferedOutputStream(urlConnection.getOutputStream());
                    writeStream(out);

                    Log.d("STATUS:", String.valueOf(urlConnection.getResponseCode()));

                    switch (urlConnection.getResponseCode()) {
                        case 200:
                            in = new BufferedInputStream(urlConnection.getInputStream());
                            onPostExecute(readStream(in), HttpStatus.valueOf(urlConnection.getResponseCode()));
                            break;
                        case 201:
                            in = new BufferedInputStream(urlConnection.getInputStream());
                            onPostExecute(readStream(in), HttpStatus.valueOf(urlConnection.getResponseCode()));
                            break;
                        default: onPostExecute("", HttpStatus.valueOf(urlConnection.getResponseCode()));
                    }
                    break;
                default: throw new IllegalStateException();
            }

        } catch (FileNotFoundException e) {
            onPostExecute("", HttpStatus.NOT_FOUND);
        }
        catch (IOException e) {
            e.printStackTrace();
            onPostExecute("", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    /**
     * Execute on the finishing of the run method. It set the result of the connection
     *
     * @param body response body
     * @param status response status code
     */
    private void onPostExecute(String body, HttpStatus status) {
        synchronized (lock.getLock()) {
            this.stringReturned = body;
            this.statusReturned = status;
            lock.getLock().notifyAll();
        }
    }

    /**
     * Util for writing on the out stream
     *
     * @param out output stream
     * @throws IOException error event
     */
    private void writeStream(OutputStream out) throws IOException {
        if (entity.getBody() != null) {
            out.write(entity.getBody().toString().getBytes());
        }
        else
            out.write(entity.toString().getBytes());
        out.flush();
    }

    /**
     * Util for reading from input stream
     *
     * @param in input stream
     * @return a string with the content of the input stream
     * @throws IOException error event
     */
    private String readStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder message = new StringBuilder();

        for (String line; (line = r.readLine()) != null; ) {
            message.append(line).append('\n');
        }

        return message.toString();
    }

    /**
     * Getter method
     *
     * @return the response body
     */
    public String getResponse() {
        return stringReturned;
    }

    /**
     * Getter method
     *
     * @return the response status.
     */
    public HttpStatus getStatusReturned() {
        return statusReturned;
    }

}
