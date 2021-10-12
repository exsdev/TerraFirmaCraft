package net.dries007.tfc.client.render;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.util.Helpers;

public class TFCBoatRenderer extends BoatRenderer
{
    public static ModelLayerLocation boatName(String name)
    {
        return ClientHelpers.modelIdentifier("boat/" + name);
    }

    private final Pair<ResourceLocation, BoatModel> location;

    public TFCBoatRenderer(EntityRendererProvider.Context context, String name)
    {
        super(context);
        this.location = Pair.of(Helpers.identifier("textures/entity/boat/" + name + ".png"), new BoatModel(context.bakeLayer(boatName(name))));
    }

    @Override
    public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat boat)
    {
        return location;
    }
}
