package ru.track;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/**
 * Задание 1: Реализовать два метода
 *
 * Формат файла: текстовый, на каждой его строке есть (или/или)
 * - целое число (int)
 * - текстовая строка
 * - пустая строка (пробелы)
 *
 * Числа складываем, строки соединяем через пробел, пустые строки пропускаем
 *
 *
 * Пример файла - words.txt в корне проекта
 *
 * ******************************************************************************************
 *  Пожалуйста, не меняйте сигнатуры методов! (название, аргументы, возвращаемое значение)
 *
 *  Можно дописывать новый код - вспомогательные методы, конструкторы, поля
 *
 * ******************************************************************************************
 *
 */
public class CountWords {

    String skipWord;

    public CountWords(String skipWord) {
        this.skipWord = skipWord;
    }

    /**
     * Метод на вход принимает объект File, изначально сумма = 0
     * Нужно пройти по всем строкам файла, и если в строке стоит целое число,
     * то надо добавить это число к сумме
     * @param file - файл с данными
     * @return - целое число - сумма всех чисел из файла
     */
    public long countNumbers(File file) throws Exception {
        long result = 0;
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (line == null) {
                break;
            }
            try {
                result += Integer.parseInt(line);
            } catch (NumberFormatException exception) {
                //
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        CountWords cw = new CountWords("");
        File file = new File("/Users/yunysovilya/TechnoTrack/1sem/track18-spring/L2-objects/words.txt");
        System.out.println(cw.concatWords(file));
        System.out.println(cw.countNumbers(file));
    }


    /**
     * Метод на вход принимает объект File, изначально результат= ""
     * Нужно пройти по всем строкам файла, и если в строка не пустая и не число
     * то надо присоединить ее к результату через пробел
     * @param file - файл с данными
     * @return - результирующая строка
     */
    public String concatWords(File file) throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        long num = 0;
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (line == null) {
                break;
            }
            try {
                num += Integer.parseInt(line);
            } catch (NumberFormatException exception) {
                if (!(line.equals(this.skipWord))) {
                    stringBuilder.append(line + ' ');
                }
            }
        }
        return stringBuilder.toString();
    }

}

