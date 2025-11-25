package net.ict_campus.Wordle.security.configurations; // Or a new package/class

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//you only need to run this by clicking on the green play button
public class PasswordEncoderUtil {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin"; // <--- CHANGE THIS to your desired password
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password for '" + rawPassword + "': " + encodedPassword);
    }
}
