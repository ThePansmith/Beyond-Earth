package com.st0x0ef.beyond_earth.common.events;

import com.st0x0ef.beyond_earth.common.events.forge.*;
import com.st0x0ef.beyond_earth.common.util.*;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import com.st0x0ef.beyond_earth.BeyondEarth;
import com.st0x0ef.beyond_earth.common.data.PlanetData;
import com.st0x0ef.beyond_earth.common.data.PlanetData.PlanetDataHandler;
import com.st0x0ef.beyond_earth.common.data.PlanetData.StarEntry;
import com.st0x0ef.beyond_earth.common.entities.LanderEntity;
import com.st0x0ef.beyond_earth.common.registries.*;

@Mod.EventBusSubscriber(modid = BeyondEarth.MODID)
public class Events {

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;

            /** OPEN AUTOMATIC PLANET GUI */
            Methods.openPlanetGui(player);

            /** JET SUIT HOVER POSE */
            Methods.setJetSuitHoverPose(player);

            /** DISABLE KICK BY FLYING IF IN PLANET GUI */
            Methods.disableFlyAntiCheat(player, player.getPersistentData().getBoolean(BeyondEarth.MODID + ":planet_selection_menu_open"));
        }
    }


    @SubscribeEvent
    public static void livingEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Level level = livingEntity.level();

        /** DROP OFF HAND VEHICLE ITEM */
        Methods.dropOffHandVehicle(livingEntity);

        /** ENTITY OXYGEN SYSTEM */
        OxygenSystem.entityOxygen(livingEntity, level);

        /** VENUS RAIN SYSTEM */
        Methods.venusRain(livingEntity, LevelRegistry.VENUS);

        /** PLANET FIRE SYSTEM */
        Methods.planetFire(livingEntity, LevelRegistry.VENUS);
        Methods.planetFire(livingEntity, LevelRegistry.MERCURY);
    }

    @SubscribeEvent
    public static void itemEntityEndTick(ItemEntityTickAtEndEvent event) {
        ItemEntity itemEntity = event.getEntity();
        Level level = itemEntity.level();

        /** ITEM ENTITY GRAVITY SYSTEM */
        ItemGravity.setGravities(itemEntity, level);
    }

    @SubscribeEvent
    public static void entityTick(EntityTickEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();

        /** ARTIFICIAL GRAVITY FOR LIVING ENTITIES */
        if (entity instanceof LivingEntity living) {

            double artificialGravity = EntityGravity.getArtificalGravityModifier(level, entity.blockPosition());

            // You can text this by placing a barrel, then un-commenting the following code.
//            if (artificialGravity == 0 && level instanceof ServerLevel slevel) {
//                Stream<PoiRecord> points = slevel.getPoiManager().getInRange(p -> p.is(PoiTypes.FISHERMAN),
//                        entity.getOnPos(), 5, Occupancy.ANY);
//                points.forEach(r -> {
//                    BlockPos pos = r.getPos();
//                    GravitySource g = new GravitySource(pos, 0.5f, 10);
//                    EntityGravity.addGravitySource(slevel, g);
//                });
//            }

            Attribute attribute = ForgeMod.ENTITY_GRAVITY.get();
            artificialGravity *= attribute.getDefaultValue();
            AttributeInstance attributeInstance = living.getAttribute(attribute);
            AttributeModifier modifier = attributeInstance.getModifier(EntityGravity.ARTIFICIAL_GRAVITY_ID);
            if (modifier != null && modifier.getAmount() != artificialGravity) {
                attributeInstance.removeModifier(EntityGravity.ARTIFICIAL_GRAVITY_ID);
                modifier = null;
            }
            if (modifier == null) {
                modifier = new AttributeModifier(EntityGravity.ARTIFICIAL_GRAVITY_ID, "beyond_earth:artificial_grabity",
                        artificialGravity, Operation.ADDITION);
                attributeInstance.addTransientModifier(modifier);
            }
        }

        /** LANDER ORBIT TELEPORT SYSTEM */
        if (entity.getVehicle() instanceof LanderEntity) {
            Methods.entityFallWithLanderToPlanet(entity, level);
        }

        /** ORBIT TELEPORT SYSTEM */
        if (!(entity.getVehicle() instanceof LanderEntity)) {
            if ((entity instanceof LanderEntity) && entity.isVehicle()) {
                return;
            }

            Methods.entityFallToPlanet(entity, level);
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Level level = event.level;

            // Tick the rain for the level
            if (Planets.LEVELS_WITHOUT_RAIN.contains(level.dimension())) {
                level.thunderLevel = 0;
                level.rainLevel = 0;
            }

            // Tick the planet locations
            Planets.updatePlanetLocations(level);
        }
    }

    @SubscribeEvent
    public static void livingEntityAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();

        if (!Methods.isLivingInNetheriteSpaceSuit(entity) && !Methods.isLivingInJetSuit(entity)) {
            return;
        }

        entity.setRemainingFireTicks(0);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void projectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult().getType() != HitResult.Type.ENTITY) {
            return;
        }

        Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();

        if (Methods.isVehicle(entity)) {
            ((Event) event).setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        /** RESET PLANET GUI PARAMETERS */
        if (entity instanceof Player player && entity.getPersistentData().getBoolean("beyond_earth:planet_selection_menu_open")) {
            player.closeContainer();
            Methods.resetPlanetSelectionMenuNeededNbt(player);
            player.setNoGravity(false);
        }

        // TODO : READ THIS
        /** JET SUIT EXPLODE */
        /**if (Methods.isLivingInJetSuit(entity) && entity.isFallFlying()
                && (event.getSource() == DamageSource.FLY_INTO_WALL)) {
            if (!entity.level.isClientSide) {
                entity.level.explode(null, entity.getX(), entity.getY(), entity.getZ(), 10, true,
                        Level.ExplosionInteraction.TNT);
            }
        }*/
    }

    @SubscribeEvent
    public static void livingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Attribute attribute = ForgeMod.ENTITY_GRAVITY.get();
        double gravity = entity.getAttributeValue(attribute) / attribute.getDefaultValue();
        float scale = (float) (gravity - 1);
        scale *= 10 * scale;
        event.setDistance(event.getDistance() - scale);
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level level = event.getLevel();

        if (entity instanceof LivingEntity livingEntity) {
            /** ENTITY GRAVITY SYSTEM */
            EntityGravity.setGravities(livingEntity, level);
        }
    }

    @SubscribeEvent
    public static void fireworkRocketUse(FireworkRocketUseEvent event) {
        Player player = event.getEntity();

        /** CANCEL BOOST BY FLYING JET SUIT */
        if (Methods.isLivingInJetSuit(player) && player.isFallFlying()) {
            event.getCallbackInfoReturnable()
                    .setReturnValue(InteractionResultHolder.pass(player.getItemInHand(event.getInteractionHand())));
        }
    }

    @SubscribeEvent
    public static void tryStartFallFlying(TryStartFallFlyingEvent event) {
        Player player = event.getEntity();

        /** CANCEL FALL FLYING IF PRESS AGAIN SPACE BY JET SUIT */
        if (Methods.isLivingInJetSuit(player) && player.isFallFlying()) {
            if (!player.level().isClientSide) {
                event.getCallbackInfoReturnable().setReturnValue(false);
            } else {
                event.getCallbackInfoReturnable().setReturnValue(true);
            }
        }

        /** JET SUIT SONIC BOOM SOUND */
        if (Methods.isLivingInJetSuit(player) && !player.isFallFlying()) {
            if (player.isSprinting()) {
                player.level().playSound(null, player, SoundRegistry.SONIC_BOOM_SOUND.get(), SoundSource.NEUTRAL, 1.0F,
                        1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void livingSprinting(LivingSprintingEvent event) {
        LivingEntity entity = event.getEntity();

        /** JET SUIT SONIC BOOM SOUND */
        if (Methods.isLivingInJetSuit(entity) && event.getSprinting() && entity.isFallFlying()) {
            entity.level().playSound(null, entity, SoundRegistry.SONIC_BOOM_SOUND.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onDataSync(OnDatapackSyncEvent event) {
        PlanetData data = new PlanetData();
        Planets.getStarsList().forEach(s -> data.stars.add(new StarEntry(s)));
        PlanetDataHandler holder = new PlanetDataHandler();
		holder.data = data;
		// This can be null during the /reload command
		if (event.getPlayer() != null)
			NetworkRegistry.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> event.getPlayer()), holder);
    }

    @SubscribeEvent
    public static void onPlayerOpenContainer(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        AbstractContainerMenu container = event.getContainer();
        NonNullList<ItemStack> itemStacks = container.getItems();
        for (ItemStack stack : itemStacks) {
            if (stack.is(TagRegistry.RADIOACTIVE_ITEMS_TAG)) {
                if(!Methods.isLivingInJetSuit(player)) {
                    player.addEffect(new MobEffectInstance(MobEffectsRegistry.RADIATION.get(), 100));
                }
            }
        }
    }
}
