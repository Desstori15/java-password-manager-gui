import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PasswordManager {
    private static List<PasswordEntry> entries = new ArrayList<>(); // Список записей паролей
    private static LocalDateTime lastDecryptionTime; // Время последней расшифровки

    // Метод для получения списка записей паролей
    public static List<PasswordEntry> getEntries() {
        return entries;
    }

    // Метод для получения времени последней расшифровки
    public static LocalDateTime getLastDecryptionTime() {
        return lastDecryptionTime;
    }

    // Метод для создания нового файла
    public static void createNewFile(String fileName) {
        try {
            Files.createFile(Paths.get(fileName)); // Создание нового файла
            lastDecryptionTime = LocalDateTime.now(); // Установка времени последней расшифровки
            saveToFile(fileName, "default"); // Сохранение в файл с дефолтным мастер-паролем
        } catch (IOException e) {
            System.err.println("Error creating new file: " + e.getMessage());
        }
    }

    // Метод для чтения из файла
    public static void readFromFile(String fileName, String masterPassword) {
        entries.clear(); // Очистка списка перед загрузкой новых данных
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            // Расшифровка времени последней расшифровки
            lastDecryptionTime = LocalDateTime.parse(CaesarCipher.decrypt(line, masterPassword), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Чтение оставшихся строк
            while ((line = reader.readLine()) != null) {
                String[] parts = CaesarCipher.decrypt(line, masterPassword).split("\t");
                // Создание записи пароля из расшифрованных данных
                PasswordEntry entry = new PasswordEntry(parts[0], parts[1], parts[2], parts.length > 3 ? parts[3] : "", parts.length > 4 ? parts[4] : "");
                entries.add(entry); // Добавление записи в список
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    // Метод для сохранения в файл
    public static void saveToFile(String fileName, String masterPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Сохранение времени последней расшифровки
            writer.write(CaesarCipher.encrypt(lastDecryptionTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), masterPassword) + "\n");

            // Сохранение всех записей паролей
            for (PasswordEntry entry : entries) {
                writer.write(CaesarCipher.encrypt(entry.toString(), masterPassword) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    // Метод для добавления новой записи пароля
    public static void addEntry(PasswordEntry entry) {
        entries.add(entry);
    }

    // Метод для удаления записи пароля по индексу
    public static void deleteEntry(int index) {
        if (index >= 0 && index < entries.size()) {
            entries.remove(index);
        }
    }
}

