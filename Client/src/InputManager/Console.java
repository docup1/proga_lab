package InputManager;

import java.util.Scanner;

/**
 * Интерфейс Console предоставляет методы для работы с консолью: ввода команд и вывода результата.
 */
public interface Console {
    /**
     * Выводит объект в консоль без перевода строки.
     * @param obj объект для вывода
     */
    void print(Object obj);

    /**
     * Выводит объект в консоль с переводом строки.
     * @param obj объект для вывода
     */
    void println(Object obj);

    /**
     * Считывает строку из консоли.
     * @return считанная строка
     */
    String readln();

    /**
     * Проверяет, доступна ли строка для чтения.
     * @return true, если доступна; false в противном случае
     */
    boolean isCanReadln();

    /**
     * Выводит сообщение об ошибке.
     * @param obj объект ошибки
     */
    void printError(Object obj);

    /**
     * Выводит два объекта в виде таблицы.
     * @param obj1 первый объект
     * @param obj2 второй объект
     */
    void printTable(Object obj1, Object obj2);

    /**
     * Выводит приглашение ввода на консоль.
     */
    void prompt();

    /**
     * Получает текущее приглашение ввода.
     * @return текущее приглашение ввода
     */
    String getPrompt();

    /**
     * Выбирает сканер файла для чтения.
     * @param scanner сканер файла
     */
    void selectFileScanner(Scanner scanner);

    /**
     * Выбирает сканер консоли для чтения.
     */
    void selectConsoleScanner();
    void ps1();
    void ps2();
    String getPS1();
    String getPS2();
    String nextLine();
}
