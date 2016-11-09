# Encryptor

A GUI charset encryptor created with JavaFX using FXML and Java SE 8.

---------------------------------------------------------------------

Purpose: 

1. Enter or load text from a plain text file (.txt) or a rich text file (.rtf).
2. Enter a password in the password field.
3. Press 'ENCRYPT' once to convert the integer charset values of the text in the text area.
4. Repeat step 2 and 3 to apply multiple layers of encryption.

Notes:

- You will be notified when you have applied the maximum level of encryption.
- Certain layers of encryption are CPU intensive. Due to each character's integer value being modified individually.
- The program can only save files in plain text format (.txt).
- If you decrypt encrypted text with the wrong password and overwrite the file, the content will be lost. Use 'RELOAD FILE' to reattempt decryption.

Button Functions:

- 'ENCRYPT': Requires a password in the password field and text in the text area. Then encrypts text.
- 'DECRYPT': Requires the appropriate password and encrypted text in the text area. Then decrypts the text.
- 'CLEAR': Resets the program and clears all variables.
- 'RELOAD FILE': restores the file to its initial state, in case of wrong decryption password.

OS specific issues:

- OSX: No known issues.
- Linux: No known issues.
- Windows: Windows notepad will not acknowledge carriage returns of files saved with the program. Instead, use wordpad.
