package io.github.yuazer.zpokebackup.Utils;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PokeUtils {
    //将宝可梦存为NBT文件
    public static void setPokemonInFile_NBT(Pokemon pokemon, File file) throws IOException {
        NBTTagCompound nbt = new NBTTagCompound();
        pokemon.writeToNBT(nbt);
        CompressedStreamTools.func_74795_b(nbt, file);
    }

    //从文件中的NBT获取宝可梦
    public static Pokemon getPokemonInFile_NBT(File file) throws IOException {
        Pokemon pokemon = Pixelmon.pokemonFactory.create(CompressedStreamTools.func_74797_a(file));
        return pokemon;
    }

    public static List<Pokemon> getTeam(PlayerPartyStorage pps) {
        List<Pokemon> team = new ArrayList();
        Pokemon[] var2 = pps.getAll();
        for (Pokemon pokemon : var2) {
            if (pokemon != null) {
                team.add(pokemon);
            }
        }
        return team;
    }

    public static boolean hasFilesInFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            return files != null && files.length > 0;
        } else {
            return false;
        }
    }

    public static void cleanupFolder(File folder) {
        if (!folder.isDirectory()) {
            return;
        }
        if (hasFilesInFolder(folder)) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        cleanupFolder(file); // 递归清理子文件夹
                    } else {
                        file.delete();
                    }
                }
            }
        }
    }

    public static void savePlayerPCData(Player player) throws IOException {
        //保存PC
        File dir = new File("plugins/ZPokeBackUp/pc/" + player.getName());
        //如果文件夹不存在，就自动创建它
        dir.mkdirs();
        if (hasFilesInFolder(dir)) {
            cleanupFolder(dir);
        }
        PCStorage pcStorage = Pixelmon.storageManager.getPCForPlayer(player.getUniqueId());
        for (PCBox box : pcStorage.getBoxes()) {
            for (Pokemon pokemon : box.getAll()) {
                if (pokemon != null) {
                    StoragePosition pos = box.getPosition(pokemon);
                    //创建一个File对象，表示要保存宝可梦的文件
                    File file = new File(dir, pos.box + "_" + pos.order + ".zps");
                    setPokemonInFile_NBT(pokemon, file);
                }
            }
        }
    }

    public static void savePlayerPokeData(Player player) throws IOException {
        File dir = new File("plugins/ZPokeBackUp/poke/" + player.getName());
        dir.mkdirs();
        if (hasFilesInFolder(dir)) {
            cleanupFolder(dir);
        }
        PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player.getUniqueId());
        for (Pokemon pokemon : getTeam(pps)) {
            File file = new File(dir, pokemon.getUUID() + ".zps");
            setPokemonInFile_NBT(pokemon, file);
        }
    }

    public static void loadPCData(Player player) throws IOException {
        PCStorage pcStorage = Pixelmon.storageManager.getPCForPlayer(player.getUniqueId());
        //删除玩家PC宝可梦
        for (PCBox box : pcStorage.getBoxes()) {
            for (Pokemon pokemon : box.getAll()) {
                if (pokemon != null) {
                    StoragePosition pos = box.getPosition(pokemon);
                    box.set(pos, null);
                }
            }
        }
        File dir = new File("plugins/ZPokeBackUp/pc/" + player.getName());
        if (dir.listFiles() != null) {
            //获取PC宝可梦并存储
            for (File file : dir.listFiles()) {
                Pokemon pokemon = getPokemonInFile_NBT(file);
                String name = file.getName().replace(".zps", "");
                int box = Integer.parseInt(name.split("_")[0]);
                int order = Integer.parseInt(name.split("_")[1]);
                pcStorage.set(box, order, pokemon);
            }
        }
    }

    public static void loadPokeData(Player player) throws IOException {
        PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player.getUniqueId());
        //清空背包宝可梦
        for (int i = 0; i <= 5; i++) {
            if (pps.get(i) != null) {
                pps.set(i, null);
            }
        }
        File dir = new File("plugins/ZPokeBackUp/poke/" + player.getName());
        if (dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                pps.add(getPokemonInFile_NBT(file));
            }
        }
    }
}
