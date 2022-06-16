package me.unleqitq.customtickspeed;

import me.unleqitq.commandframework.CommandManager;
import me.unleqitq.commandframework.building.argument.FloatArgument;
import me.unleqitq.commandframework.building.command.FrameworkCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class CustomTickSpeed extends JavaPlugin {
	
	public static CommandManager commandManager;
	static String fieldName = "nextTickTime";
	static float targetTPS = 20;
	static Field nextTickTimeField;
	static Object minecraftServerObject;
	
	@Override
	public void onEnable() {
		commandManager = new CommandManager(this);
		try {
			NMSHandler.init();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			Class<?> minecraftServerClass = Class.forName("net.minecraft.server.MinecraftServer");
			Method getServerMethod = minecraftServerClass.getDeclaredMethod("getServer");
			minecraftServerObject = getServerMethod.invoke(null);
			
			nextTickTimeField = minecraftServerClass.getDeclaredField(
					NMSHandler.getClassData("net.minecraft.server.MinecraftServer").getFieldData(fieldName)
							.obfuscatedName());
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException |
		         NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
		FrameworkCommand.Builder<CommandSender> topBuilder = FrameworkCommand.commandBuilder("tickspeed");
		commandManager.register(
				topBuilder.subCommand("set").argument(FloatArgument.of("value").withMin(0.1f).withMax(1000))
						.handler(c -> {
							targetTPS = c.get("value");
							return true;
						}));
		
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (targetTPS == 20)
				return;
			int addition = (int) (1000 / targetTPS - 50);
			if (addition == 0)
				return;
			nextTickTimeField.setAccessible(true);
			try {
				long time = nextTickTimeField.getLong(minecraftServerObject);
				time += addition;
				nextTickTimeField.setLong(minecraftServerObject, time);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}, 0, 1);
		/*for (Player player : Bukkit.getOnlinePlayers()) {
			Class<? extends Player> playerClass = player.getClass();
			try {
				Method getHandleMethod = playerClass.getDeclaredMethod("getHandle");
				Object entityPlayer = getHandleMethod.invoke(player);
				Class<?> entityPlayerClass = entityPlayer.getClass();
				for (Method method : entityPlayerClass.getDeclaredMethods()) {
					player.sendMessage(method.toGenericString());
				}
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}*/
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
	
}
