package com.scarletomato.nofly.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.scarletomato.nofly.DimNoFly;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class EditDimensionsCommand implements ICommand {
	private static final Logger LOG = LogManager.getLogger(DimNoFly.class);
	
	private static final String NAME = "nofly";
	private List<String> aliases;
	private DimNoFly context;

	public EditDimensionsCommand(DimNoFly context) {
		this.context = context;
		this.aliases = new ArrayList<String>();
		this.aliases.add(NAME);
		this.aliases.add("nf");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
	    return NAME + " [list|add|sub] <dimensionId>";
	}

	@Override
	public List getCommandAliases() {
	    return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] args) {
		String response;
		try {
			if("add".equalsIgnoreCase(args[0])) {
				response = context.addDimension(Integer.valueOf(args[1]));
			} else if("sub".equalsIgnoreCase(args[0])) {
				response = context.removeDimension(Integer.valueOf(args[1]));
			} else {
				response = "Flight is banned in dimensions " + context.listDimensions();
			}
		} catch(Exception e) {
			LOG.debug("Failed command ", e);
			response = "Use: " + getCommandUsage(icommandsender);
		}
		icommandsender.addChatMessage(new ChatComponentText(response));
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		if(icommandsender instanceof EntityPlayer) {
			return FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(((EntityPlayer) icommandsender).getGameProfile());
		} else {
			//Console??
			return true;
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int index) {
		return false;
	}

}
