# java-password-manager-gui  
##  Java Password Manager (GUI)

A secure and user-friendly **Java Swing-based password manager** with file encryption using a custom Caesar Cipher.  
This project allows users to store, view, search, sort, and manage passwords with a master-password protected interface.

---

##  Key Features

- **Master password** authentication  
- **Encrypted file storage** using Caesar Cipher  
- **Categorized password storage** (Name, Category, Login, Website)  
- **Add / Delete / Save passwords** with ease  
- **Search by category**  
- **Sort passwords** by name or category  
- **Random password generator**  
- **Last decryption time** display  
-  **Modern Java Swing GUI**

---

##  Project Structure

```
PasswordManager-GUI/
├── Main.java              # GUI and main app logic
├── PasswordEntry.java     # Class for storing each password entry
├── PasswordManager.java   # File I/O and data encryption/decryption
├── CaesarCipher.java      # Encryption logic using Caesar cipher
└── *.txt                  # Encrypted file created when saving passwords
```


---

##  Technologies Used

- Java 8+  
- Swing (JFrame, JTable, Layouts)  
- Caesar Cipher (custom implementation)  
- Java IO and NIO for file handling  
- Java Time API (for timestamps)

---

##  Example Configuration

```
<encrypted_timestamp>
<encrypted_name>\t<encrypted_password>\t<encrypted_category>\t<encrypted_login>\t<encrypted_website>
```


---

##  How to Run

1. **Clone the repo**  
```bash
git clone https://github.com/Desstori15/java-password-manager-gui.git
cd java-password-manager-gui


2. **Compile**
```bash
javac Main.java CaesarCipher.java PasswordManager.java PasswordEntry.java
```

3. **Run**
```bash
java Main
```

java Main
 Example Actions in GUI

Enter master password
Choose or create a .txt file to store passwords
Add new password entry
Click Save to File to encrypt and persist data
Use Search or Sort to manage entries
Passwords stored are encrypted on save and decrypted on load


## What I Learned
Java Swing GUI architecture
Basic encryption using Caesar Cipher
File handling using java.nio and java.io
Designing secure and responsive UI
Managing data with JTable + TableModel


👨‍💻 Author
Vladislav Dobriyan
GitHub: @Desstori15
