public class PasswordEntry {
    private String name;
    private String password;
    private String category;
    private String login;
    private String website;

    // Конструктор для создания объекта PasswordEntry с заданными значениями
    public PasswordEntry(String name, String password, String category, String login, String website) {
        this.name = name;
        this.password = password;
        this.category = category;
        this.login = login;
        this.website = website;
    }

    // Метод для получения имени
    public String getName() {
        return name;
    }

    // Метод для установки пароля
    public void setPassword(String password) {
        this.password = password;
    }

    // Метод для установки категории
    public void setCategory(String category) {
        this.category = category;
    }

    // Метод для установки логина
    public void setLogin(String login) {
        this.login = login;
    }

    // Метод для установки веб-сайта
    public void setWebsite(String website) {
        this.website = website;
    }

    // Метод для получения категории
    public String getCategory() {
        return category;
    }

    // Переопределение метода toString для удобного отображения информации о пароле
    @Override
    public String toString() {
        return name + "\t" + password + "\t" + category + "\t" + login + "\t" + website;
    }

    // Метод для получения пароля
    public String getPassword() {
        return this.password;
    }

    // Метод для получения логина
    public String getLogin() {
        return this.login;
    }

    // Метод для получения веб-сайта
    public String getWebsite() {
        return website;
    }
}
