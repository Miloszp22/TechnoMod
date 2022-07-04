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

    private LivingEntity thisEntity = (LivingEntity)(Object)this;
    private Entity target;
    @Inject(method = "baseTick",at = @At("HEAD"))
    public void baseTick(CallbackInfo ci) {
        float explosiondistance = 3;
        if (this.getClass().equals(PigEntity.class) && target != null){


                if (!target.isAlive()){
                    target = null;

                }
                else if (target instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity)target;
                    if (!player.isInvulnerableTo(main.TECHNOBLADED) && (thisEntity.getDistance(target) > explosiondistance && thisEntity.getEntityWorld() == target.getEntityWorld()))
                        thisEntity.world.createExplosion(thisEntity, net.miloszp22.technomod.main.TECHNOBLADED, null,target.getPosition().getX(),target.getPosition().getY(),target.getPosition().getZ(),explosiondistance,false, Explosion.Mode.BREAK);
                }
                else if (thisEntity.getDistance(target) > explosiondistance && thisEntity.getEntityWorld() == target.getEntityWorld()){
                    thisEntity.world.createExplosion(thisEntity, net.miloszp22.technomod.main.TECHNOBLADED, null,target.getPosition().getX(),target.getPosition().getY(),target.getPosition().getZ(),explosiondistance,false, Explosion.Mode.BREAK);
                }
        }
    }
    @Inject(method = "damageEntity",at = @At("HEAD"),cancellable = true)
    public void hurt(DamageSource source, float damage, CallbackInfo ci) {
        if (this.getClass().equals(PigEntity.class)) {
            String s = thisEntity.getName().getString();
            System.out.println("damage: " + String.valueOf(damage));
            if (s != null && ("Techno".equals(s) || "Technoblade".equals(s) || "Alex".equals(s)) && thisEntity.getHealth() - damage <= 0) {
                thisEntity.playSound(SoundEvents.ITEM_TOTEM_USE, 0.15F, 1.0F);
                thisEntity.setHealth(1.0F);
                thisEntity.clearActivePotions();
                thisEntity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                thisEntity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                thisEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                thisEntity.world.setEntityState(thisEntity, (byte) 35);
                target = source.getTrueSource();
                ci.cancel();
            }
        }
    }
}
