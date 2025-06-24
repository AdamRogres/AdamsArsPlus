package com.adamsmods.adamsarsplus.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Supplier;

// CREDIT TO ARS TRINKETS

/*
 * Run the method passed to this every tickInterval number of ticks
 *
 * After timeToLive number of ticks have passed, unregister itself.
 *
 * */

public class SetInterval {

    int ticks = 0;
    Runnable method;
    int tickInterval = 0; //How many ticks have to pass before the method is called again
    int timeToLive = 0;
    Supplier<Boolean> end;

    public SetInterval(Runnable method, int tickInterval, int timeToLive, Supplier<Boolean> end){
        //function, tick rate, time to live
        this.method = method;
        this.tickInterval = tickInterval;
        this.timeToLive = timeToLive;
        this.end = end;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event){
        if(event.phase == TickEvent.Phase.START){

            if(ticks >= (timeToLive - tickInterval) || end.get()){
                MinecraftForge.EVENT_BUS.unregister(this);
            }

            if(ticks % tickInterval == 0){
                this.method.run();
            }
            ticks++;
        }
    }
}

/*
 * This class will auto unregister itself, so i could use it an a setInterval() that has expiration time.
 *
 * like, when the form is cast, call a function that will register a setInterval. The setInterval will receive
 * an expiration time, tick rate and a function to call as parameters
 *
 * */