package com.scarletomato.nofly.event;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.EventLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.scarletomato.nofly.DimNoFly;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class PlayerListener {
	private static final Logger LOG = LogManager.getLogger(DimNoFly.class);
	DimNoFly context;

	public PlayerListener(DimNoFly context) {
		this.context = context;
	}

	@SubscribeEvent
	public void onFly(PlayerTickEvent playerTick){
		EntityPlayer player = playerTick.player;
		PlayerCapabilities cap = player.capabilities;
		if(cap.isFlying && context.isBanned(player.dimension) && !cap.isCreativeMode){
			LOG.info("dropping " + player.getDisplayName() + " out of the sky");
			cap.isFlying = false;
			player.sendPlayerAbilities();
		}
	}

	public void register() {
    	//FML Events: these become very important in 1.7.2, as this is where TickEvents and KeyInputEvents are posted, with TickHandler and KeyHandler no longer existing.
		FMLCommonHandler.instance().bus().register(this);
	}
}
