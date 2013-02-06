package com.geargames.common.network;

import com.geargames.common.util.ArrayByte;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 13.09.12
 * Time: 16:33
 * Описание результата сетевого запроса
 */
public class Result {

    public static final int DATA_OK = 0;
    public static final int DATA_INCORRECT_SIZE = 1;
    public static final int SOCKET_EXCEPTION = 10;
    public static final int MALFORMED_URL_EXCEPTION = 11;
    public static final int FILE_NOT_FOUND_EXCEPTION = 12;//сервер есть, но не возвращает результат
    public static final int UNKNOWN_HOST_EXCEPTION = 13;//неверный адрес сервера, скорее всего
    public static final int IOEXCEPTION = 14;
    public static final int STREAM_CLOSE_EXCEPTION = 15;
    public static final int SOCKET_TIMEOUT_EXCEPTION = 16;
    public static final int SOCKET_URLCONNECT_EXCEPTION = 17;
    public static final int SEND_EXCEPTION = 18;
    public static final int DATA_LEN_ERROR = 19;
    public static final int RESPONSE_EXCEPTION = 20;
    public static final int INCORRECT_ANSWER = 21;

    private Result(ArrayByte data, int errorID) {
        this.data = data;
        this.errorID = errorID;
    }

    public static Result create(ArrayByte data, int errorID, int param) {
        Result result = new Result(data, errorID);
        result.param = param;
        return result;
    }

    public static Result create(ArrayByte data, int errorID) {
        return new Result(data, errorID);
    }

    public static Result create(int errorID) {
        return new Result(null, errorID);
    }

    public ArrayByte getData() {
        return data;
    }

    public int getErrorID() {
        return errorID;
    }

    public int getParam() {
        return param;
    }

    private final ArrayByte data;
    private final int errorID;
    private int param;

    public void free() {
        if (data != null) data.free();
    }

}
