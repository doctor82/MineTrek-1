package net.minetrek.blocks.machines;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minetrek.MineTrek;
import net.minetrek.client.gui.GuiHandler;

public class ParticleAccelerator extends BlockContainer {
	public ParticleAccelerator(){
		super(Material.iron);
		this.setHarvestLevel("pickaxe", 2);
		GameRegistry.registerTileEntity(ParticleAcceleratorTileEntity.class, "particleAcceleratorTileEntity");
	}
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new ParticleAcceleratorTileEntity();
	}
    public int getRenderType()
    {
        return 3;
    }
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if (!worldIn.isRemote) {
			FMLNetworkHandler.openGui(playerIn, MineTrek.instance, GuiHandler.PARTICLEACCELERATOR_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
    }
}
