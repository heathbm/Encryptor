# Encryptor

A GUI text encryptor created with JavaFX using FXML and Java SE 8.

V1 Lightweight: A cyclic pattern is applied with a shift cipher to each character individually. The pattern is the size of the password.

V2 Advanced: A cyclic pattern is applied with a shift cipher to each character individually. The pattern is the size of the password times 3.

V3 AES: Plain text is enciphered with the AES algorithm in ECB mode at 128bit encryption.

---------------------------------------------------------------------

Purpose: 

1. Enter or load text from a plain text file (.txt) or a rich text file (.rtf). Or simply enter text in the upper text area.
2. Enter a password in the password field.
3. Press 'ENCRYPT' once to convert the integer charset values of the text in the text area.
4. Repeat step 2 and 3 to apply multiple layers of encryption. Or simply just step 3.
5. Decrypt the text with the appropritate passwords in the reverse order.

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

Disclaimer: This project is intended for educational purposes.

Demonstration:

Plain text:

![Alt text](/ScreenShots/plain.png?raw=true "plaintext")

Enciphered once:

![Alt text](/ScreenShots/encrypted2.png?raw=true "encrypted once")

Enciphered 5 times:

![Alt text](/ScreenShots/encrypted1.png?raw=true "encrypted five times")
