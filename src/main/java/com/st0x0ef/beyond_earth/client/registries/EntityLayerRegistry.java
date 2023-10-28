package com.st0x0ef.beyond_earth.client.registries;

import com.st0x0ef.beyond_earth.client.renderers.entities.glacianram.GlacianRamModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.st0x0ef.beyond_earth.BeyondEarth;
import com.st0x0ef.beyond_earth.client.renderers.armors.JetSuitModel;
import com.st0x0ef.beyond_earth.client.renderers.armors.SpaceSuitModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.alien.AlienModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.alienzombie.AlienZombieModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.flag.FlagHeadModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.globe.GlobeModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.lander.LanderModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.martianraptor.MartianRaptorModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.mogler.MoglerModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.pygro.PygroModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.rockettier1.RocketTier1Model;
import com.st0x0ef.beyond_earth.client.renderers.entities.rockettier2.RocketTier2Model;
import com.st0x0ef.beyond_earth.client.renderers.entities.rockettier3.RocketTier3Model;
import com.st0x0ef.beyond_earth.client.renderers.entities.rockettier4.RocketTier4Model;
import com.st0x0ef.beyond_earth.client.renderers.entities.rover.RoverModel;
import com.st0x0ef.beyond_earth.client.renderers.entities.starcrawler.StarCrawlerModel;

@Mod.EventBusSubscriber(modid = BeyondEarth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityLayerRegistry {

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        /** MOBS */
        event.registerLayerDefinition(AlienModel.LAYER_LOCATION, AlienModel::createBodyLayer);
        event.registerLayerDefinition(AlienZombieModel.LAYER_LOCATION, AlienZombieModel::createBodyLayer);
        event.registerLayerDefinition(StarCrawlerModel.LAYER_LOCATION, StarCrawlerModel::createBodyLayer);
        event.registerLayerDefinition(PygroModel.LAYER_LOCATION, PygroModel::createBodyLayer);
        event.registerLayerDefinition(MoglerModel.LAYER_LOCATION, MoglerModel::createBodyLayer);
        event.registerLayerDefinition(MartianRaptorModel.LAYER_LOCATION, MartianRaptorModel::createBodyLayer);

        /** VEHICLES */
        event.registerLayerDefinition(RocketTier1Model.LAYER_LOCATION, RocketTier1Model::createBodyLayer);
        event.registerLayerDefinition(RocketTier2Model.LAYER_LOCATION, RocketTier2Model::createBodyLayer);
        event.registerLayerDefinition(RocketTier3Model.LAYER_LOCATION, RocketTier3Model::createBodyLayer);
        event.registerLayerDefinition(RocketTier4Model.LAYER_LOCATION, RocketTier4Model::createBodyLayer);
        event.registerLayerDefinition(LanderModel.LAYER_LOCATION, LanderModel::createBodyLayer);
        event.registerLayerDefinition(RoverModel.LAYER_LOCATION, RoverModel::createBodyLayer);

        /** ARMORS **/
        event.registerLayerDefinition(SpaceSuitModel.SpaceSuitP1.LAYER_LOCATION, SpaceSuitModel.SpaceSuitP1::createBodyLayer);
        event.registerLayerDefinition(SpaceSuitModel.SpaceSuitP2.LAYER_LOCATION, SpaceSuitModel.SpaceSuitP2::createBodyLayer);
        event.registerLayerDefinition(JetSuitModel.JetSuitP1.LAYER_LOCATION, JetSuitModel.JetSuitP1::createBodyLayer);
        event.registerLayerDefinition(JetSuitModel.JetSuitP2.LAYER_LOCATION, JetSuitModel.JetSuitP2::createBodyLayer);

        /** BLOCK ENTITIES */
        event.registerLayerDefinition(FlagHeadModel.LAYER_LOCATION, FlagHeadModel::createHumanoidHeadLayer);
        event.registerLayerDefinition(GlobeModel.LAYER_LOCATION, GlobeModel::createLayer);
    }
}
