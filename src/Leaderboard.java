import java.io.*;
import java.util.*;

public class Leaderboard {
    private static final String FILE_NAME = "src/leaderboard.txt";

    // Update das Leaderboard mit einem neuen Score
    public static void updateLeaderboard(String name, String date, int score) {
        List<String> leaderboard = new ArrayList<>();

        // Datei lesen, falls sie existiert
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                leaderboard.add(line);
            }
        } catch (IOException e) {
            System.out.println("leaderboard.txt not found.");
        }

        // Neuen Score hinzufügen
        leaderboard.add(name + " - " + score + " - " + date);

        // Sortiere die Scores absteigend
        leaderboard.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split(" - ")[1]);
            int scoreB = Integer.parseInt(b.split(" - ")[1]);
            return Integer.compare(scoreB, scoreA);
        });


        // Schreibe die Scores zurück in die Datei
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String entry : leaderboard) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lese das Leaderboard und gebe es als String zurück
    public static String readLeaderboard() {
        StringBuilder leaderboard = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                leaderboard.append(line).append("\n");
            }
        } catch (IOException e) {
            leaderboard.append("Keine Daten verfügbar.");
        }
        return leaderboard.toString();
    }
}
