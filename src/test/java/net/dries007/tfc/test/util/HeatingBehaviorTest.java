/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.test.util;

import java.util.Collection;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.items.IItemHandler;

import net.dries007.tfc.AutoGameTest;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.TestAssertions;
import net.dries007.tfc.common.blockentities.PitKilnBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.capabilities.VesselLike;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.util.Fuel;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.calendar.CalendarTransaction;
import net.dries007.tfc.util.calendar.Calendars;

@GameTestHolder(TerraFirmaCraft.MOD_ID)
public class HeatingBehaviorTest
{
    @GameTestGenerator
    public Collection<TestFunction> generator()
    {
        return TestAssertions.unitTestGenerator();
    }

    @AutoGameTest
    public void checkTicksToCool10mBCopperInSmallVessel(GameTestHelper helper)
    {
        checkTicksSpentMoltenAfterPitKiln(helper, 404, false, false, Metal.Default.COPPER, new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_COPPER).get()));
    }

    @AutoGameTest
    public void checkTicksToCool100mBCopperInSmallVessel(GameTestHelper helper)
    {
        checkTicksSpentMoltenAfterPitKiln(helper, 576, false, false, Metal.Default.COPPER, new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.NORMAL).get(), 4));
    }

    @AutoGameTest
    public void checkTicksToCool560mBCopperInSmallVessel(GameTestHelper helper)
    {
        checkTicksSpentMoltenAfterPitKiln(helper, 1459, false, false, Metal.Default.COPPER, new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 16));
    }

    @AutoGameTest
    public void checkTicksToCool2240mBCopperInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 16);
        checkTicksSpentMoltenAfterPitKiln(helper, 4684, false, false, Metal.Default.COPPER, copper16, copper16, copper16, copper16);
    }

    @AutoGameTest
    public void checkPitKilnCanMelt16CopperInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 16);
        checkTicksSpentMoltenAfterPitKiln(helper, 560, true, true, Metal.Default.COPPER, copper16);
    }

    @AutoGameTest
    public void checkPitKilnCanMelt64CopperInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 16);
        checkTicksSpentMoltenAfterPitKiln(helper, 2082, false, true, Metal.Default.COPPER, copper16, copper16, copper16, copper16);
    }

    @AutoGameTest
    public void checkPitKilnCanMelt100mBCopperInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper100mB = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.NORMAL).get(), 4);
        checkTicksSpentMoltenAfterPitKiln(helper, 100, true, true, Metal.Default.COPPER, copper100mB);
    }

    @AutoGameTest
    public void checkPitKilnCanMelt100mBBronzeInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper90mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_COPPER).get(), 9);
        final ItemStack tin10mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.CASSITERITE).get(), 1);
        checkTicksSpentMoltenAfterPitKiln(helper, 100, true, true, Metal.Default.BRONZE, copper90mB, tin10mB);
    }

    @AutoGameTest
    public void checkPitKilnCanMelt100mBBismuthBronzeInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper50mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_COPPER).get(), 5);
        final ItemStack zinc30mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.SPHALERITE).get(), 3);
        final ItemStack bismuth20mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.BISMUTHINITE).get(), 2);
        checkTicksSpentMoltenAfterPitKiln(helper, 100, true, true, Metal.Default.BISMUTH_BRONZE, copper50mB, zinc30mB, bismuth20mB);
    }

    @AutoGameTest
    public void checkPitKilnCanMelt100mBBlackBronzeInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper60mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_COPPER).get(), 6);
        final ItemStack silver20mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_SILVER).get(), 2);
        final ItemStack gold20mB = new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_GOLD).get(), 2);
        checkTicksSpentMoltenAfterPitKiln(helper, 100, true, true, Metal.Default.BLACK_BRONZE, copper60mB, silver20mB, gold20mB);
    }

    @AutoGameTest
    public void checkPitKilnCanMeltBronzeInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 16);
        final ItemStack copper8 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 8);
        final ItemStack tin4 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.CASSITERITE).get(Ore.Grade.RICH).get(), 4);
        checkTicksSpentMoltenAfterPitKiln(helper, 1540, true, true, Metal.Default.BRONZE, copper16, copper16, copper8, tin4);
    }

    @AutoGameTest
    public void checkPitKilnCanMeltBismuthBronzeInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 16);
        final ItemStack copper10 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 10);
        final ItemStack bismuth10 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.BISMUTHINITE).get(Ore.Grade.RICH).get(), 10);
        final ItemStack zinc14 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.SPHALERITE).get(Ore.Grade.RICH).get(), 14);
        checkTicksSpentMoltenAfterPitKiln(helper, 1750, true, true, Metal.Default.BISMUTH_BRONZE, copper16, copper10, bismuth10, zinc14);
    }

    @AutoGameTest
    public void checkPitKilnCanMeltBlackBronzeInSmallVessel(GameTestHelper helper)
    {
        final ItemStack copper16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH).get(), 16);
        final ItemStack gold16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_GOLD).get(Ore.Grade.RICH).get(), 16);
        final ItemStack silver16 = new ItemStack(TFCItems.GRADED_ORES.get(Ore.NATIVE_SILVER).get(Ore.Grade.RICH).get(), 16);
        checkTicksSpentMoltenAfterPitKiln(helper, 2103, false, true, Metal.Default.BLACK_BRONZE, copper16, copper16, gold16, silver16);
    }

    @AutoGameTest
    public void checkPitKilnCanFirePottery(GameTestHelper helper)
    {
        final PitKilnBlockEntity pitKiln = pitKiln(helper);
        final IItemHandler pitKilnInventory = Helpers.getCapability(pitKiln, Capabilities.ITEM);

        TestAssertions.assertNotNull(pitKilnInventory);

        try (CalendarTransaction tr = Calendars.SERVER.transaction())
        {
            pitKilnInventory.insertItem(0, new ItemStack(TFCItems.UNFIRED_VESSEL.get()), false);

            tr.add(8000L);
            pitKiln.updateCache();
            pitKiln.cookContents();

            final ItemStack outputStack = pitKilnInventory.extractItem(0, 1, false);

            // Can't compare NBT, as the new vessel will have heat
            TestAssertions.assertEquals(TFCItems.VESSEL.get(), outputStack.getItem());
            TestAssertions.assertEquals(1, outputStack.getCount());

            final IHeat outputHeat = Helpers.getCapability(outputStack, HeatCapability.CAPABILITY);

            TestAssertions.assertNotNull(outputHeat);
            TestAssertions.assertEquals(1400.0f, outputHeat.getTemperature(), "Pit kiln did not reach expected temperature, got: " + outputHeat.getTemperature());
        }
    }

    @AutoGameTest
    public void checkCookingStickWithHeat(GameTestHelper helper)
    {
        TestAssertions.assertEquals(76, ticksRequiredToMelt(new ItemStack(Items.STICK)));
    }

    @AutoGameTest
    public void checkCookingStickBunchWithHeat(GameTestHelper helper)
    {
        TestAssertions.assertEquals(600, ticksRequiredToMelt(new ItemStack(TFCItems.STICK_BUNCH.get())));
    }

    @AutoGameTest
    public void checkCookingSandWithHeat(GameTestHelper helper)
    {
        TestAssertions.assertEquals(160, ticksRequiredToMelt(new ItemStack(TFCBlocks.SAND.get(SandBlockType.BLACK).get())));
    }

    @AutoGameTest
    public void checkCookingSandInVesselWithHeat(GameTestHelper helper)
    {
        TestAssertions.assertEquals(401, ticksRequiredToMeltVessel(new ItemStack(TFCBlocks.SAND.get(SandBlockType.BLACK).get())));
    }

    @AutoGameTest
    public void checkCooking4SandInVesselWithHeat(GameTestHelper helper)
    {
        TestAssertions.assertEquals(809, ticksRequiredToMeltVessel(new ItemStack(TFCBlocks.SAND.get(SandBlockType.BLACK).get(), 4)));
    }

    @AutoGameTest
    public void checkCooking4SandSpreadOutInVesselWithHeat(GameTestHelper helper)
    {
        final ItemStack sand = new ItemStack(TFCBlocks.SAND.get(SandBlockType.BLACK).get());
        TestAssertions.assertEquals(809, ticksRequiredToMeltVessel(sand, sand, sand, sand));
    }

    @AutoGameTest
    public void checkCooking8SandInVesselWithHeat(GameTestHelper helper)
    {
        TestAssertions.assertEquals(1353, ticksRequiredToMeltVessel(new ItemStack(TFCBlocks.SAND.get(SandBlockType.BLACK).get(), 8)));
    }

    @AutoGameTest
    public void checkCooking16SandInVesselWithHeat(GameTestHelper helper)
    {
        TestAssertions.assertEquals(2441, ticksRequiredToMeltVessel(new ItemStack(TFCBlocks.SAND.get(SandBlockType.BLACK).get(), 16)));
    }

    @AutoGameTest
    public void checkBronzeIngotTimeSpentWorkable(GameTestHelper helper)
    {
        TestAssertions.assertEquals(1086, ticksRequiredToBeNotWorkable(new ItemStack(TFCItems.METAL_ITEMS.get(Metal.Default.BRONZE).get(Metal.ItemType.INGOT).get())));
    }

    @AutoGameTest
    public void checkBronzeDoubleIngotTimeSpentWorkable(GameTestHelper helper)
    {
        TestAssertions.assertEquals(2172, ticksRequiredToBeNotWorkable(new ItemStack(TFCItems.METAL_ITEMS.get(Metal.Default.BRONZE).get(Metal.ItemType.DOUBLE_INGOT).get())));
    }

    @AutoGameTest
    public void checkIronIngotTimeSpentWorkable(GameTestHelper helper)
    {
        TestAssertions.assertEquals(1755, ticksRequiredToBeNotWorkable(new ItemStack(TFCItems.METAL_ITEMS.get(Metal.Default.WROUGHT_IRON).get(Metal.ItemType.INGOT).get())));
    }

    @AutoGameTest
    public void checkIronDoubleIngotTimeSpentWorkable(GameTestHelper helper)
    {
        TestAssertions.assertEquals(3509, ticksRequiredToBeNotWorkable(new ItemStack(TFCItems.METAL_ITEMS.get(Metal.Default.WROUGHT_IRON).get(Metal.ItemType.DOUBLE_INGOT).get())));
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithPoorOres(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.POOR, false, false, 43200, 24 * 15);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithNormalOres(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.NORMAL, false, false, 43200, 24 * 25);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithRichOres(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.RICH, false, false, 43200, 24 * 35);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithPoorOresCastingIntoIngotsFirst(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.POOR, true, false, 9614, 24 * 15);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithNormalOresCastingIntoIngotsFirst(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.NORMAL, true, false, 15014, 24 * 25);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithRichOresCastingIntoIngotsFirst(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.RICH, true, false, 18614, 24 * 35);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithPoorOresAndCrucibleCastingIntoIngotsFirst(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.POOR, true, true, 6905, 24 * 15);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithNormalOresAndCrucibleCastingIntoIngotsFirst(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.NORMAL, true, true, 12305, 24 * 25);
    }

    @AutoGameTest
    public String checkBloomeryFuelEfficiencyWithRichOresAndCrucibleCastingIntoIngotsFirst(GameTestHelper helper)
    {
        return checkBloomeryEfficiency(Ore.Grade.RICH, true, true, 15905, 24 * 35);
    }

    private String checkBloomeryEfficiency(Ore.Grade oreGrade, boolean castIntoIngotsFirst, boolean useCrucible, int expectedFuelTicks, int expectedOreOutput)
    {
        final Fuel charcoal = Fuel.get(new ItemStack(Items.CHARCOAL));
        final ItemStack ore = new ItemStack(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(oreGrade).get());
        final HeatingRecipe recipe = HeatingRecipe.getRecipe(ore);
        final int itemCount = 24; // Full sized bloomery

        TestAssertions.assertNotNull(charcoal, "Missing fuel for " + charcoal);
        TestAssertions.assertNotNull(recipe, "Missing heat recipe for " + ore);

        final int oreOutput = itemCount * recipe.assembleFluid(new ItemStackInventory(ore)).getAmount();
        final int concurrentMelting = useCrucible ? 5 + 9 : 5;

        int fuelTicks;
        if (castIntoIngotsFirst)
        {
            // Condense the input into 100 mB pieces, but using up additional fuel ticks in the process
            fuelTicks = (oreOutput / 100) * charcoal.getDuration() + itemCount * ticksRequiredToMelt(ore) / concurrentMelting;
        }
        else
        {
            fuelTicks = itemCount * charcoal.getDuration();
        }

        TestAssertions.assertEquals(expectedFuelTicks, fuelTicks);
        TestAssertions.assertEquals(expectedOreOutput, oreOutput);

        return String.format("grade = %s, castIntoIngotsFirst = %s, useCrucible = %s : fuelTicks = %d, oreOutput = %d, mBofOrePerFuelTick = %.3f", oreGrade, castIntoIngotsFirst, useCrucible, fuelTicks, oreOutput, (float) oreOutput / fuelTicks);
    }

    private void checkTicksSpentMoltenAfterPitKiln(GameTestHelper helper, int expectedTicks, boolean expectEmpty, boolean whileDraining, Metal.Default moltenMetal, ItemStack... contents)
    {
        final PitKilnBlockEntity pitKiln = pitKiln(helper);
        final IItemHandler pitKilnInventory = Helpers.getCapability(pitKiln, Capabilities.ITEM);

        final ItemStack vesselStack = vesselWithContents(contents);
        final VesselLike vessel = VesselLike.get(vesselStack);

        TestAssertions.assertNotNull(vessel);
        TestAssertions.assertNotNull(pitKilnInventory);

        try (CalendarTransaction tr = Calendars.SERVER.transaction())
        {
            pitKilnInventory.insertItem(0, vesselStack, false);

            tr.add(8000L);
            pitKiln.updateCache();
            pitKiln.cookContents();

            final ItemStack outputStack = pitKilnInventory.extractItem(0, 1, false);
            final VesselLike outputVessel = VesselLike.get(outputStack);

            // Assert the content is molten and at the expected temperature
            TestAssertions.assertNotNull(outputVessel);
            TestAssertions.assertTrue(outputVessel.isMolten());

            // Extract metal while molten
            for (int i = 0; i < expectedTicks; i++)
            {
                tr.add(1);
                TestAssertions.assertTrue(outputVessel.isMolten(), "Not molten after " + i + " ticks, temperature is " + outputVessel.getTemperature() + " and mode is " + outputVessel.mode());
                if (whileDraining)
                {
                    TestAssertions.assertEquals(new FluidStack(TFCFluids.METALS.get(moltenMetal).getSource(), 1), outputVessel.drain(1, IFluidHandler.FluidAction.EXECUTE));
                }
            }

            tr.add(1);
            if (expectEmpty && whileDraining)
            {
                TestAssertions.assertTrue(outputVessel.mode() == VesselLike.Mode.INVENTORY, "Vessel still contains metal, expected empty, at temperature " + outputVessel.getTemperature());
            }
            else
            {
                // Assert not molten anymore
                TestAssertions.assertTrue(outputVessel.mode() == VesselLike.Mode.SOLID_ALLOY, "Still molten after " + expectedTicks + " ticks, the temperature is " + outputVessel.getTemperature() + " with content " + outputVessel.mode());
            }
        }
    }

    private PitKilnBlockEntity pitKiln(GameTestHelper helper)
    {
        final BlockPos pos = helper.absolutePos(new BlockPos(0, 1, 0));
        final PitKilnBlockEntity pitKiln = new PitKilnBlockEntity(pos, TFCBlocks.PIT_KILN.get().defaultBlockState());
        pitKiln.setLevel(helper.getLevel());
        return pitKiln;
    }

    private ItemStack vesselWithContents(ItemStack... stacks)
    {
        final ItemStack vesselStack = new ItemStack(TFCItems.VESSEL.get());
        final VesselLike vessel = VesselLike.get(vesselStack);

        TestAssertions.assertNotNull(vessel);
        TestAssertions.assertTrue(stacks.length <= 4);

        for (int i = 0; i < stacks.length; i++)
        {
            vessel.insertItem(i, stacks[i].copy(), false);
        }

        return vesselStack;
    }

    private int ticksRequiredToMeltVessel(ItemStack... contents)
    {
        // Only works if all the vessel contents are the same (and have the same recipe)
        final ItemStack stack = vesselWithContents(contents);
        final IHeat heat = Helpers.getCapability(stack, HeatCapability.CAPABILITY);
        final HeatingRecipe recipe = HeatingRecipe.getRecipe(contents[0]);

        TestAssertions.assertNotNull(heat, "Heat missing for stack: " + heat);
        TestAssertions.assertNotNull(recipe, "Recipe missing for vessel content: " + stack);

        int ticks = 0;
        try (CalendarTransaction tr = Calendars.SERVER.transaction())
        {
            while (!recipe.isValidTemperature(heat.getTemperature()))
            {
                HeatCapability.addTemp(heat, Heat.maxVisibleTemperature());
                tr.add(1);
                ticks += 1;

                TestAssertions.assertNotEquals(ticks, 1_000_000, "Loop did not terminate with stack " + TestAssertions.wrap(stack) + " and recipe " + TestAssertions.wrap(recipe));
            }
        }

        return ticks;
    }

    private int ticksRequiredToMelt(ItemStack stack)
    {
        final IHeat heat = Helpers.getCapability(stack, HeatCapability.CAPABILITY);
        final HeatingRecipe recipe = HeatingRecipe.getRecipe(stack);

        TestAssertions.assertNotNull(heat, "Heat missing for stack: " + heat);
        TestAssertions.assertNotNull(recipe, "Recipe missing for stack: " + stack);

        int ticks = 0;
        try (CalendarTransaction tr = Calendars.SERVER.transaction())
        {
            while (!recipe.isValidTemperature(heat.getTemperature()))
            {
                HeatCapability.addTemp(heat, Heat.maxVisibleTemperature());
                tr.add(1);
                ticks += 1;

                TestAssertions.assertNotEquals(ticks, 1_000_000, "Loop did not terminate with stack " + TestAssertions.wrap(stack) + " and recipe " + TestAssertions.wrap(recipe));
            }
        }

        return ticks;
    }

    private int ticksRequiredToBeNotWorkable(ItemStack stack)
    {
        final IHeat heat = Helpers.getCapability(stack, HeatCapability.CAPABILITY);
        final HeatingRecipe recipe = HeatingRecipe.getRecipe(stack);

        TestAssertions.assertNotNull(heat, "Heat missing for stack: " + heat);
        TestAssertions.assertNotNull(recipe, "Recipe missing for stack: " + stack);

        int ticks = 0;
        try (CalendarTransaction tr = Calendars.SERVER.transaction())
        {
            heat.setTemperature(recipe.getTemperature()); // Assume melting > working
            while (heat.getWorkingTemperature() < heat.getTemperature())
            {
                tr.add(1);
                ticks += 1;

                TestAssertions.assertNotEquals(ticks, 1_000_000, "Loop did not terminate with stack " + TestAssertions.wrap(stack) + " and recipe " + TestAssertions.wrap(recipe));
            }
        }

        return ticks;
    }
}
