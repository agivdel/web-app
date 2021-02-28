package agivdel.webApp1311.utils;

import agivdel.webApp1311.password.PBKDF2;
import agivdel.webApp1311.password.Password;

public class PasswordManager {
    public static PasswordManagerBuilder builder() {
        return new PasswordManagerBuilder();
    }

    public static class PasswordManagerBuilder {
        private Password passwordImpl = null;

        public PasswordManagerBuilder() {
        }

        public PasswordManagerBuilder algorithm(String algorithm) {
            switch (algorithm) {
                case "PBKDF2":
                    this.passwordImpl = new PBKDF2();
                    break;
                case "Bcrypt":
//                    this.passwordImpl = new SomeBcryptClass();
                    break;
                case "Argon2":
//                    this.passwordImpl = new SomeArgon2Class();
                    break;
                default: this.passwordImpl = new PBKDF2();
            }
            return this;
        }

        public PasswordManagerBuilder adjust(String details) {
            passwordImpl.adjust(details);
            return this;
        }

        public Password build() {
            return passwordImpl;
        }
    }
}