public class CaesarCipher {

    // Метод для шифрования текста с использованием ключа
    public static String encrypt(String text, String key) {
        // Определение смещения на основе длины ключа
        int shift = key.length() % 26;
        StringBuilder result = new StringBuilder();

        // Перебор каждого символа в тексте
        for (char character : text.toCharArray()) {
            if (character != '\t') { // Игнорирование символа табуляции
                char shifted = (char) (character + shift); // Сдвиг символа
                result.append(shifted); // Добавление сдвинутого символа в результат
            } else {
                result.append(character); // Добавление символа табуляции
            }
        }

        return result.toString(); // Возврат зашифрованного текста
    }

    // Метод для расшифровки текста с использованием ключа
    public static String decrypt(String text, String key) {
        // Определение смещения на основе длины ключа
        int shift = key.length() % 26;
        StringBuilder result = new StringBuilder();

        // Перебор каждого символа в тексте
        for (char character : text.toCharArray()) {
            if (character != '\t') { // Игнорирование символа табуляции
                char shifted = (char) (character - shift); // Обратный сдвиг символа
                result.append(shifted); // Добавление сдвинутого символа в результат
            } else {
                result.append(character); // Добавление символа табуляции без изменений
            }
        }

        return result.toString(); // Возврат расшифрованного текста
    }
}


