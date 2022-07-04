package net.miloszp22.technomod.mixin;

import net.miloszp22.technomod.main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    // Defining Entity
    private LivingEntity thisEntity = (LivingEntity)(Object)this;
    // Defining Target
    private Entity target;
    @Inject(method = "baseTick",at = @At("TAIL"))
    public void baseTick(CallbackInfo ci) {
        float explosionradius = 3;
        // Checking if Entity is a pig and target isn't null.
        if (this.getClass().equals(PigEntity.class) && target != null){


                if (!target.isAlive()){
                    // Respawn Fix
                    target = null;
                }
                else if (thisEntity.getDistance(target) > explosionradius && thisEntity.getEntityWorld() == target.getEntityWorld() && !thisEntity.isInvulnerable()){
                    // Entity go boom boom
                    thisEntity.world.createExplosion(thisEntity, net.miloszp22.technomod.main.TECHNOBLADED, null,target.getPosition().getX(),target.getPosition().getY(),target.getPosition().getZ(),explosionradius,false, Explosion.Mode.BREAK);
                }
        }
    }
    @Inject(method = "damageEntity",at = @At("HEAD"),cancellable = true)
    public void onEntityDamage(DamageSource source, float damage, CallbackInfo ci) {
        if (this.getClass().equals(PigEntity.class)) {
            String s = thisEntity.getName().getString();
            if (s != null && ("Techno".equals(s) || "Technoblade".equals(s) || "Alex".equals(s)) && thisEntity.getHealth() - damage <= 0) {
                // Playing Totem Sound
                thisEntity.playSound(SoundEvents.ITEM_TOTEM_USE, 0.15F, 1.0F);
                thisEntity.setHealth(1.0F);
                thisEntity.clearActivePotions();
                thisEntity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                thisEntity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                thisEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                thisEntity.world.setEntityState(thisEntity, (byte) 35);
                // Gets target
                target = source.getTrueSource();
                ci.cancel();
            }
        }
    }
}
