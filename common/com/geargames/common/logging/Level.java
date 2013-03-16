package com.geargames.common.logging;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public class Level {

    public static final byte DEBUG     = 0; // Отладочное сообщение. Может использоваться для глубокой отладки
                                            // приложения или его отдельных компонентов.
    // CONFIG ~ INFO
    public static final byte CONFIG    = 1; // Информационное сообщение. Также используется для отладки приложения, а
                                            // также для предоставления конфигурационной информации (наименование модели
                                            // устройства, разрешение экрана, наличие расширений OpenGL и т.д.)
    // INFO ~ NOTICE
    public static final byte INFO      = 2; // Сообщение - уведомление. Информирует о серьезных событиях в ходе
                                            // программы (завершена инициализация подсистем, начата загрузка уровня,
                                            // игрок убит и т.д.).
    public static final byte WARNING   = 3; // Сообщение - предупреждение. Может использоваться в том случае, если
                                            // обнаружено некое несоответствие ожидаемому поведению программы.
    public static final byte ERROR     = 4; // Сообщение об ошибке. Текущая функция не может быть выполнена правильно,
                                            // хотя общий процесс игры не прерывается (восстановимая ошибка).
    public static final byte CRITICAL  = 5; // Сообщение о критической ошибке. Какие-то компоненты приложения могут
                                            // перестать функционировать (звуковая подсистема).
    public static final byte ALERT     = 6; // Сообщение о невосстановимой ошибке. Приложение детектировало некорректную
                                            // ситуацию, которая, c большой долей вероятности, может привести к
                                            // фатальной остановке программы.
    // EMERGENCY
    public static final byte FATAL     = 7; // Фатальное сообщение об ошибке. Как правило, используется в обработчиках
                                            // Unhandled exceptions и в других случаях, когда приложение оказывается
                                            // "за гранью" фатальной остановки.

//  public static final byte EXCEPTION = 8;

    public static final byte ALL       = 0;
    public static final byte MINIMUM   = 0;
    public static final byte MAXIMUM   = 7;

    private static final String[] levelNames = {
            "Debug",
            "Config",
            "Info",
            "Warning",
            "Error",
            "Critical",
            "Alert",
            "Fatal",
    };

    // Это число будет использоваться для выравнивания столбца с уровнем лог-сообщения.
    public static final byte MAXIMUM_LENGTH_LEVEL_NAMES = 8;

    public static String getLevelName(byte level) {
        if (level < MINIMUM || level > MAXIMUM) {
            throw new IllegalArgumentException();
        }
        return levelNames[level];
    }

}