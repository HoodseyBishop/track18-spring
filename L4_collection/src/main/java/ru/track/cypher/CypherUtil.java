package ru.track.cypher;

import java.util.*;

import org.jetbrains.annotations.NotNull;

/**
 * Вспомогательные методы шифрования/дешифрования
 */
public class CypherUtil {

    public static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Генерирует таблицу подстановки - то есть каждой буква алфавита ставится в соответствие другая буква
     * Не должно быть пересечений (a -> x, b -> x). Маппинг уникальный
     *
     * @return таблицу подстановки шифра
     */
    @NotNull
    public static Map<Character, Character> generateCypher() {
        Map<Character, Character> cypher = new HashMap<>();
        char[] key = SYMBOLS.toCharArray();
        List<Character> value = new ArrayList<>();
        for (char c : key) value.add(c);
        Collections.shuffle(value);
        for (int i = 0; i < key.length; i++) cypher.put(key[i], value.get(i));
        return cypher;
    }
}
