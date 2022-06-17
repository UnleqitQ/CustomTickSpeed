package me.unleqitq.customtickspeed;

import me.unleqitq.commandframework.CommandManager;
import me.unleqitq.commandframework.building.argument.FloatArgument;
import me.unleqitq.commandframework.building.argument.IntegerArgument;
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
	
	static float warpMultiplier = 1;
	static int warpTicksLeft = 0;
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
							c.getSender().sendMessage("Set TickSpeed to " + targetTPS);
							return true;
						}));
		commandManager.register(topBuilder.subCommand("get").handler(c -> {
			c.getSender().sendMessage("TickSpeed is set to " + targetTPS);
			return true;
		}));
		commandManager.register(topBuilder.subCommand("getcurrent").handler(c -> {
			c.getSender().sendMessage(
					"TickSpeed is currently targeting " + targetTPS * (warpTicksLeft > 0 ? warpMultiplier : 1));
			return true;
		}));
		
		commandManager.register(FrameworkCommand.commandBuilder("tickwarp").setDescription("Increase the TickSpeed for a set amount of ticks").permission("tickspeed")
				.argument(IntegerArgument.of("ticks"), "The amount of ticks you want to speed up")
				.argument(FloatArgument.optional("multiplier", 2).withMin(1.5f).withMax(100),
						"The ratio you want to speed the world up to").handler(c -> {
					warpMultiplier = c.get("multiplier");
					warpTicksLeft = c.get("ticks");
					c.getSender().sendMessage("Warping " + warpTicksLeft + " with " + warpMultiplier + "x Speed");
					return true;
				}));
		
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (warpTicksLeft > 0) {
				if (targetTPS * warpMultiplier == 20)
					return;
				int addition = (int) (1000 / (targetTPS * warpMultiplier) - 50);
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
				warpTicksLeft--;
			}
			else {
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
