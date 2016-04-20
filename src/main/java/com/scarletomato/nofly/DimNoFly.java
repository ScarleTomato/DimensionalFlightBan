package com.scarletomato.nofly;

import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.scarletomato.nofly.command.EditDimensionsCommand;
import com.scarletomato.nofly.event.PlayerListener;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(acceptableRemoteVersions="*", modid = DimNoFly.MODID, version = DimNoFly.VERSION)
public class DimNoFly
{
    protected static final Logger LOG = LogManager.getLogger(DimNoFly.class);
    public static final String MODID = "dimnofly";
    public static final String VERSION = "1.0";
    
    List<Integer> bannedDimensions;
    Configuration config;
    Property bdProp;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        // you will be able to find the config file in .minecraft/config/ and it will be named Dummy.cfg
        // here our Configuration has been instantiated, and saved under the name "config"
        config = new Configuration(event.getSuggestedConfigurationFile());

        // loading the configuration from its file
        config.load();

        bdProp = config.get(Configuration.CATEGORY_GENERAL, "bannedDimensions", new int[]{-1});
        initDimensionsList(bdProp.getIntList());

        // saving the configuration to its file
        config.save();
}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	new PlayerListener(this).register();
    }

	@EventHandler
	public void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand(new EditDimensionsCommand(this));
	}
    
    private void initDimensionsList(int[] savedDimensions){
    	bannedDimensions = new LinkedList<Integer>();
    	for(int dim : savedDimensions) {
    		bannedDimensions.add(dim);
    	}
    }

	public String addDimension(Integer dimId) {
		if(bannedDimensions.contains(dimId)) {
			return "Flight is already banned in dimension " + dimId;
		} else {
			bannedDimensions.add(dimId);
			saveDimList();
			return "Flight is now banned in dimension " + dimId;
		}
	}

	public String removeDimension(Integer dimId) {
		if(bannedDimensions.contains(dimId)) {
			bannedDimensions.remove(dimId);
			saveDimList();
			return "Flight is now allowed in dimension " + dimId;
		} else {
			return "Flight is already allowed in dimension " + dimId;
		}
	}
	
	private void saveDimList() {
		bdProp.set(ArrayUtils.toPrimitive(bannedDimensions.toArray(new Integer[bannedDimensions.size()])));
		config.save();
	}

	public String listDimensions() {
		return bannedDimensions.toString();
	}

	public boolean isBanned(int dimension) {
		return bannedDimensions.contains(dimension);
	}
}
