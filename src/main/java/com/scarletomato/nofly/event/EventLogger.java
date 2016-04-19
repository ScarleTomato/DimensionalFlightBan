package com.scarletomato.nofly.event;

import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.scarletomato.nofly.DimNoFly;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventLogger {
	
	private static final Logger LOG = LogManager.getLogger(DimNoFly.class);

	private List<Class> eventClasses = new LinkedList<Class>();

	@SubscribeEvent
	public void onEvent(Event event) {
		Class<?> eventClass = event.getClass();
		if(!eventClasses.contains(eventClass)){
			LOG.info("Event: " + event.getClass());
			eventClasses.add(eventClass);
		}
	}

	public void register() {
    	//Most events get posted to this bus
    	MinecraftForge.EVENT_BUS.register(this);
    	//Most world generation events happen here, such as Populate, Decorate, etc., with the strange exception that Pre and Post events are on the regular EVENT_BUS
    	MinecraftForge.TERRAIN_GEN_BUS.register(this);
    	//Ore generation, obviously
    	MinecraftForge.ORE_GEN_BUS.register(this);
    	//FML Events: these become very important in 1.7.2, as this is where TickEvents and KeyInputEvents are posted, with TickHandler and KeyHandler no longer existing.
    	FMLCommonHandler.instance().bus().register(this);
	}

}
