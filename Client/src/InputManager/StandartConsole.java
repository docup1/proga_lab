package InputManager;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс StandartConsole предоставляет реализацию интерфейса Console для ввода команд и вывода результатов в стандартную консоль.
 */
public class StandartConsole implements Console {
    private static final String PROMPT = "$ ";
    private static Scanner fileScanner = null;
    private static final Scanner defScanner = new Scanner(System.in);
    private static final String PS1 = "$ ";
    private static final String PS2 = "> ";

    /**
     * Выводит объект в консоль без перевода строки.
     *
     * @param obj Объект для вывода.
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит объект в консоль с добавлением перевода строки.
     *
     * @param obj Объект для вывода.
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Выводит сообщение об ошибке в консоль.
     *
     * @param obj Объект с сообщением об ошибке.
     */
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    /**
     * Считывает строку из консоли.
     *
     * @return Строка из консоли.
     * @throws NoSuchElementException   если нет следующей строки
     * @throws IllegalStateException    если консоль не готова к чтению
     */
    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).nextLine();
    }

    /**
     * Проверяет, доступно ли чтение из консоли.
     *
     * @return true, если доступно чтение из консоли, в противном случае - false.
     * @throws IllegalStateException    если консоль не готова к чтению
     */
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).hasNextLine();
    }

    /**
     * Выводит два элемента в виде таблицы.
     *
     * @param elementLeft  Левый элемент таблицы.
     * @param elementRight Правый элемент таблицы.
     */
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }

    /**
     * Выводит приглашение к вводу команды в консоль.
     */
    public void prompt() {
        print(PROMPT);
    }

    /**
     * Возвращает приглашение к вводу команды.
     *
     * @return Строка с приглашением к вводу команды.
     */
    public String getPrompt() {
        return PROMPT;
    }

    /**
     * Устанавливает сканнер для чтения из файла.
     *
     * @param scanner Сканнер для чтения из файла.
     */
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    /**
     * Устанавливает сканнер для чтения из консоли.
     */
    public void selectConsoleScanner() {
        fileScanner = null;
    }
    /**
     * Выводит PS1 текущей консоли
     */
    public void ps1() {
        print(PS1);
    }

    /**
     * Выводит PS2 текущей консоли
     */
    public void ps2() {
        print(PS2);
    }

    /**
     * @return PS1 текущей консоли
     */
    public String getPS1() {
        return PS1;
    }

    /**
     * @return PS2 текущей консоли
     */
    public String getPS2() {
        return PS1;
    }

    /**
     * Считывает строку из консоли.
     *
     * @return Строка из консоли.
     * @throws NoSuchElementException   если нет следующей строки
     * @throws IllegalStateException    если консоль не готова к чтению
     */
    public String nextLine() throws NoSuchElementException, IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).nextLine();
    }
}
