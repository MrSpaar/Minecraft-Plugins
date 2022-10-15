package ci.offlist;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class OfflistHandler {
    private static final Gson gson = new Gson();
    private static final File file = new File("./whitelist.json");
    private static final Type type = new TypeToken<List<HashMap<String, String>>>() {}.getType();

    private static final List<HashMap<String, String>> cache = readWhitelist();

    public static int getIndex(String name) {
        for (int i=0; i<cache.size(); i++)
            if (cache.get(i).get("name").equals(name))
                return i;
        return -1;
    }

    public static List<String> getNicknames() {
        return cache.stream().map(e -> e.get("name")).toList();
    }

    public static void addPlayer(String nickname, CommandSender sender) {
        cache.add(new HashMap<>() {{
            put("name", nickname);
            put("uuid", UUID.nameUUIDFromBytes(("OfflinePlayer:"+nickname).getBytes(StandardCharsets.UTF_8)).toString());
        }});

        updateWhitelist(sender);
    }

    public static boolean removePlayer(String name, CommandSender sender) {
        int index = getIndex(name);

        if (index < 0)
            return false;

        cache.remove(index);
        updateWhitelist(sender);
        return true;
    }

    private static List<HashMap<String, String>> readWhitelist() {
        try {
            FileReader reader = new FileReader(file);
            List<HashMap<String, String>> content = gson.fromJson(reader, type);
            reader.close();
            return content;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading the file");
        }

        return Collections.emptyList();
    }

    private static void updateWhitelist(CommandSender sender) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(cache, type));
            writer.flush();
            writer.close();
            sender.getServer().dispatchCommand(sender, "whitelist reload");
        } catch (IOException e) {
            System.out.println("Error while editing the whitelist");
        }
    }
}
