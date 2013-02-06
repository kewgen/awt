package com.geargames.common.network;

import com.geargames.common.Port;
import com.geargames.common.util.ArrayByte;

import java.io.*;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 12.03.12
 * Time: 19:36
 */
public abstract class SendHTTP {

    public Result send(String path, String postData, int answerSize) {
        //запрос по указанному урлу
        //postData - содержимое POST запроса, например d=name&core=2
        //answerSize - ожидаемый размер ответа, -1 - ответ неизвестен
        //не забывай убить массив array.free() !

        try {
            //log("Send4 to:" + path + "{" + answerSize + "}; data:" + postData);
            URL url = new URL(path);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (Port.IS_ANDROID) {//протестить на новом проекте
                //if (answerSize != -1) urlConnection.setFixedLengthStreamingMode(answerSize);
                //if (answerSize == -1) urlConnection.setChunkedStreamingMode(1024);//java.io.FileNotFoundException: http://pfp2.ru/srv/
            }

            if (postData != null) {
                urlConnection.setDoOutput(true);

                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(postData.getBytes());
                os.close();
            }

            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            try {
                int result = urlConnection.getResponseCode();
                //log(" local ip:" + InetAddress.getLocalHost() + ", res:" + result);

                return readStream(is, answerSize);

            } catch (SocketException e) {
                log(e.toString());
                return Result.create(Result.SOCKET_EXCEPTION);
            } catch (FileNotFoundException e) {//java.io.FileNotFoundException ошибка скрипта сервера
                log(e.toString());
                return Result.create(Result.FILE_NOT_FOUND_EXCEPTION);
            } catch (UnknownHostException e) {
                log(e.toString());
                return Result.create(Result.UNKNOWN_HOST_EXCEPTION);
            } catch (SocketTimeoutException e) {
                log(e.toString());
                return Result.create(Result.SOCKET_TIMEOUT_EXCEPTION);
            } catch (IOException e) {
                log("Send error to:" + path + "{" + answerSize + "}");
                return Result.create(Result.IOEXCEPTION);
            } catch (Exception e) {
                log("Send error to:" + path + "{" + answerSize + "}");
                logEx(e);
                return Result.create(Result.RESPONSE_EXCEPTION);
            } finally {
                if (is != null)
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                        log(e.toString() + " is.close");
                    } catch (Exception e) {
                        log(e.toString() + " is.close error");
                    }
                try {
                    if (urlConnection != null) urlConnection.disconnect();
                } catch (Exception e) {
                    log(e.toString() + " urlConnection.disconnect");
                }
            }

        } catch (MalformedURLException e) {
            log(e.toString());
            return Result.create(Result.MALFORMED_URL_EXCEPTION);
        } catch (IOException e) {//java.io.IOException: Server returned HTTP response code: 500 ошибка сервера
            log(e.toString());
            return Result.create(Result.IOEXCEPTION);
        } catch (Exception e) {//java.lang.NullPointerException at org.apache.harmony.security.provider.cert.X509CertImpl.verify(X509CertImpl.java:543)
            log(e.toString());
            logEx(e);
            return Result.create(Result.SEND_EXCEPTION);
        }
    }

    protected Result readStream(InputStream is, int answerSize) throws IOException {

        ArrayByte arrayByte = new ArrayByte(answerSize);

        int pos = 0;
        int r;
        int noticeIntervalTemp = 0;
        while ((r = is.read()) != -1) {
            if (pos == answerSize) {//пришедший пакет больше запрашиваемого
                return Result.create(Result.DATA_INCORRECT_SIZE);
            }
            arrayByte.set(pos, (byte) r);
            pos++;
            if (pos >= noticeIntervalTemp) {//если задан интервал слушания
                httpCounter.nextKBytes();
                noticeIntervalTemp += noticeInterval;
            }
        }
        if (pos != answerSize) {
            return Result.create(null, Result.DATA_INCORRECT_SIZE, pos);
        }

        return Result.create(arrayByte, Result.DATA_OK);

    }

    public void setHost(com.geargames.common.String host) {
        this.host = host.toString();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void setHttpCounter(HTTPCounter httpCounter, int noticeInterval) {
        this.httpCounter = httpCounter;
        this.noticeInterval = noticeInterval;
    }

    @Override
    public String toString() {
        return "SendHTTP{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public abstract void log(java.lang.String msg);

    public abstract void logEx(Exception exception);

    protected String host;
    private int port = 80;
    private int timeOut;//время жизни соединения

    protected HTTPCounter httpCounter;//слушатель уведомлений о загрузке байт
    protected int noticeInterval;//интервал отправки уведомлений о загрузке, в байтах

}

/*


*/