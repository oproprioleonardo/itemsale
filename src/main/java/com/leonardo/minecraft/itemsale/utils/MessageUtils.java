package com.leonardo.minecraft.itemsale.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

@UtilityClass
public class MessageUtils {

    private static final String version;
    private static final Class<?> chatSerializerClass;
    private static final Class<?> chatComponentClass;
    private static final Class<?> packetActionbarClass;
    private static final Constructor<?> actionBarConstructor;
    private static final Method actionBarMethod;
    private static final Class<?> packetTitleClass;
    private static final Constructor<?> titleConstructor;
    private static final Object enumTitleObject;
    private static final Object enumSubtitleObject;


    static {
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        chatSerializerClass = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0];
        chatComponentClass = getNMSClass("IChatBaseComponent");
        packetActionbarClass = getNMSClass("PacketPlayOutChat");
        packetTitleClass = getNMSClass("PacketPlayOutTitle");
        try {
            actionBarConstructor = packetActionbarClass.getDeclaredConstructor(chatComponentClass, byte.class);
            actionBarMethod = chatSerializerClass.getMethod("a", String.class);
            enumTitleObject = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            enumSubtitleObject = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            titleConstructor = packetTitleClass.getDeclaredConstructor(packetTitleClass.getDeclaredClasses()[0], chatComponentClass, int.class, int.class, int.class);
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private static void sendPacket(Player player, Object packet) {
        try {
            final Object handle = player.getClass().getMethod("getHandle").invoke(player);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadein, int stay, int fadeout) {
        title = ChatColor.translateAlternateColorCodes('&', title);
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        try {

            final Object chatTitle = chatSerializerClass.getMethod("a", String.class).invoke(chatSerializerClass, "{\"text\": \"" + title + "\"}");
            final Object packetTitle = titleConstructor.newInstance(enumTitleObject, chatTitle, fadein, stay, fadeout);
            sendPacket(player, packetTitle);

            final Object chatSubtitle = chatSerializerClass.getMethod("a", String.class).invoke(chatSerializerClass, "{\"text\": \"" + subtitle + "\"}");
            final Object packetSubtitle = titleConstructor.newInstance(enumSubtitleObject, chatSubtitle, fadein, stay, fadeout);
            sendPacket(player, packetSubtitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendActionBar(Player player, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        try {
            final Object actionbar = actionBarMethod.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}");
            final Object packet = actionBarConstructor.newInstance(actionbar, (byte) 2);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
