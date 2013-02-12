package com.geargames.common.network;

import com.geargames.common.util.ArrayByte;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 12.03.12
 * Time: 19:36
 */
public abstract class SendHTTP {

    public Result send(String url, int answerSize) {
        //запрос файла указанного размера
        //не забывай убить массив array.free() !
        InputStream is = null;
        try {

            //log("Send to:" + url + "{" + answerSize + "}; local ip:" + InetAddress.getLocalHost());
            URLConnection conn = new URL(url).openConnection();

            is = conn.getInputStream();
            int result = getResponseCode(conn);
            log("Send to:" + url + "{" + answerSize + "}; local ip:" + InetAddress.getLocalHost() + ", res:" + result);
            ArrayByte arrayByte = new ArrayByte(answerSize);

            int pos = 0;
            int r;
            int noticeIntervalTemp = 0;
            while ((r = is.read()) != -1) {
                if (pos == answerSize) {//пришедший пакет больше запрашиваемого
                    return Result.create(Result.DATA_INCORRECT_SIZE);
                }
                arrayByte.set(pos, (byte) r);
                //log("" + pos + "," + r + " ");
                pos++;
                if (pos >= noticeIntervalTemp) {//если задан интервал слушания
                    httpCounter.nextKBytes();
                    noticeIntervalTemp += noticeInterval;
                }
            }
            if (pos != answerSize) {
                return Result.create(null, Result.DATA_INCORRECT_SIZE, pos);
            }
            is.close();

            return Result.create(arrayByte, Result.DATA_OK);
        } catch (SocketException e) {
            log(e.toString());
            return Result.create(Result.SOCKET_EXCEPTION);
        } catch (MalformedURLException e) {
            log(e.toString());
            return Result.create(Result.MALFORMED_URL_EXCEPTION);
        } catch (FileNotFoundException e) {
            log(e.toString());
            return Result.create(Result.FILE_NOT_FOUND_EXCEPTION);
        } catch (UnknownHostException e) {
            log(e.toString());
            return Result.create(Result.UNKNOWN_HOST_EXCEPTION);
        } catch (SocketTimeoutException e) {
            log(e.toString());
            return Result.create(Result.SOCKET_TIMEOUT_EXCEPTION);
        } catch (IOException e) {
            log("Send error to:" + url + "{" + answerSize + "}");
            //logEx(e);
            return Result.create(Result.IOEXCEPTION);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    logEx(e);
                    Result.create(Result.STREAM_CLOSE_EXCEPTION);
                }
        }

    }

    public ArrayByte send(String param, ArrayByte data) {
        //отправка байтового массива и возврат на него ответа
        //не забывай убить массив array.free() !
        try {

            log(toString());
            URLConnection conn = new URL(host).openConnection();
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(param.getBytes());//например имя переменной d=
            os.write(data.getArray());
            os.close();

            InputStream is = conn.getInputStream();
            int result = getResponseCode(conn);
            //log("Send to:" + host + ", local ip:" + InetAddress.getLocalHost() + ", res:" + result);
            int len = ((is.read() & 0xff) << 24) | ((is.read() & 0xff) << 16) | ((is.read() & 0xff) << 8) | (is.read() & 0xff);
            if (len < 0) {
                is.close();
                return null;
            }
            if (len > 1000000) {//на случай ошибки длины пакета
//                String log = "data:";
//                int pos = 0;
//                int r;
//                while ((r = is.read()) != -1) {
//                    log += (r + ",");
//                    pos++;
//                    if (pos > 300) break;
//                }
//                log(log);
//                logEx(new Exception("Illegal answer size from:" + host + "; len:" + len + ", res:" + result));
                is.close();
                return null;
            }
            ArrayByte res = new ArrayByte(len);

            int pos = 0;
            int r;
            while ((r = is.read()) != -1) {
                res.set(pos, (byte) r);
                pos++;
            }
            is.close();

            return res;
        } catch (SocketException e) {
            log(e.toString());
        } catch (MalformedURLException e) {
            log(e.toString());
        } catch (FileNotFoundException e) {//сервер есть, но не возвращает результат
            log(e.toString());
        } catch (UnknownHostException e) {//неверный адрес сервера, скорее всего
            log(e.toString() + ". Host:" + host);
        } catch (SocketTimeoutException e) {
            log(e.toString());
        } catch (IOException e) {
            //log("Send error " + data.toString());
            log(e.toString());
        }
        return null;
    }

    protected int responseCode = -1;
    protected String responseMessage = null;

    public int getResponseCode(URLConnection connection) throws IOException {
        /*
        * We're got the response code already
        */
        if (responseCode != -1) {
            return responseCode;
        }

        /*
        * Ensure that we have connected to the server. Record
        * exception as we need to re-throw it if there isn't
        * a status line.
        */
        Exception exc = null;
        try {
            connection.getInputStream();
        } catch (Exception e) {
            exc = e;
        }

        /*
        * If we can't a status-line then re-throw any exception
        * that getInputStream threw.
        */
        String statusLine = connection.getHeaderField(0);
        if (statusLine == null) {
            if (exc != null) {
                if (exc instanceof RuntimeException)
                    throw (RuntimeException) exc;
                else
                    throw (IOException) exc;
            }
            return -1;
        }
        /*
        * Examine the status-line - should be formatted as per
        * section 6.1 of RFC 2616 :-
        *
        * Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase
        *
        * If status line can't be parsed return -1.
        */
        if (statusLine.startsWith("HTTP/1.")) {
            int codePos = statusLine.indexOf(' ');
            if (codePos > 0) {

                int phrasePos = statusLine.indexOf(' ', codePos + 1);
                if (phrasePos > 0 && phrasePos < statusLine.length()) {
                    responseMessage = statusLine.substring(phrasePos + 1);
                }

                // deviation from RFC 2616 - don't reject status line
                // if SP Reason-Phrase is not included.
                if (phrasePos < 0)
                    phrasePos = statusLine.length();

                try {
                    responseCode = Integer.parseInt(statusLine.substring(codePos + 1, phrasePos));
                    return responseCode;
                } catch (NumberFormatException e) {
                }
            }
        }
        return -1;
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

    private String host;
    private int port = 80;
    private int timeOut;//время жизни соединения

    private HTTPCounter httpCounter;//слушатель уведомлений о загрузке байт
    private int noticeInterval;//интервал отправки уведомлений о загрузке, в байтах

}

/*


*/