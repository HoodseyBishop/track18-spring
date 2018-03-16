package ru.track.cypher;

import java.util.*;

import org.jetbrains.annotations.NotNull;

public class Decoder {

    // Расстояние между A-Z -> a-z
    public static final int SYMBOL_DIST = 32;

    private Map<Character, Character> cypher;

    /**
     * Конструктор строит гистограммы открытого домена и зашифрованного домена
     * Сортирует буквы в соответствие с их частотой и создает обратный шифр Map<Character, Character>
     *
     * @param domain - текст по кторому строим гистограмму языка
     */
    public Decoder(@NotNull String domain, @NotNull String encryptedDomain) {
        Map<Character, Integer> domainHist = createHist(domain);
        Map<Character, Integer> encryptedDomainHist = createHist(encryptedDomain);
        cypher = new LinkedHashMap<>();
        Iterator<Character> domainIterator = domainHist.keySet().iterator();
        Iterator<Character> encryptedDomainIterator = encryptedDomainHist.keySet().iterator();
        while (domainIterator.hasNext()) {
            cypher.put(encryptedDomainIterator.next(), domainIterator.next());
        }
    }

    public Map<Character, Character> getCypher() { return cypher; }

    /**
     * Применяет построенный шифр для расшифровки текста
     *
     * @param encoded зашифрованный текст
     * @return расшифровка
     */
    @NotNull
    public String decode(@NotNull String encoded) {
        StringBuilder decoded = new StringBuilder();
        char[] encodedChars = encoded.toCharArray();
        for (int i = 0; i < encoded.length(); i++) {
            char symbol = encodedChars[i];
            if (Character.isLetter(symbol)) {
                decoded.append(cypher.get(encodedChars[i]));
            } else {
                decoded.append(symbol);
            }
        }
        return decoded.toString();
    }

    /**
     * Считывает входной текст посимвольно, буквы сохраняет в мапу.
     * Большие буквы приводит к маленьким
     *
     *
     * @param text - входной текст
     * @return - мапа с частотой вхождения каждой буквы (Ключ - буква в нижнем регистре)
     * Мапа отсортирована по частоте. При итерировании на первой позиции наиболее частая буква
     */
    @NotNull
    Map<Character, Integer> createHist(@NotNull String text) {
        Map<Character, Integer> hist = new LinkedHashMap<>();
        char[] textChars = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            char symbol = textChars[i];
            if (Character.isLetter(symbol)) {
                symbol = Character.toLowerCase(symbol);
                if (hist.get(symbol) == null) {
                    hist.put(symbol, 1);
                } else {
                    int count = hist.get(symbol);
                    hist.put(symbol, count + 1);
                }
            }
        }
        List<Map.Entry<Character, Integer>> list = new LinkedList<>(hist.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        }.reversed());
        Map<Character, Integer> sortedHist = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> elem : list) {
            sortedHist.put(elem.getKey(), elem.getValue());
        }
        hist = sortedHist;
        return hist;
    }

}
