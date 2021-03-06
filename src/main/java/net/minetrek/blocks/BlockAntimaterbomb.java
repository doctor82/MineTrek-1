package net.minetrek.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minetrek.entities.explosives.EntityAntimaterTNT;

public class BlockAntimaterbomb extends Block {
	public BlockAntimaterbomb(){
		super(Material.tnt);
		
	}
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        if (worldIn.isBlockPowered(pos))
        {
            this.explode(worldIn, pos, (EntityLivingBase)null);
            worldIn.setBlockToAir(pos);
        }
    }
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (worldIn.isBlockPowered(pos))
        {
        	this.explode(worldIn, pos, (EntityLivingBase)null);
            worldIn.setBlockToAir(pos);
        }
    }
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        if (!worldIn.isRemote)
        {
            EntityAntimaterTNT entitytntprimed = new EntityAntimaterTNT(worldIn, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
            entitytntprimed.fuse = worldIn.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
            worldIn.spawnEntityInWorld(entitytntprimed);
        }
    }
    public void explode(World worldIn, BlockPos pos, EntityLivingBase igniter)
    {
        if (!worldIn.isRemote)
        {
        	System.out.println("debug: spawning antimaterbomb");
        	EntityAntimaterTNT entitytntprimed = new EntityAntimaterTNT(worldIn, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), igniter);
                worldIn.spawnEntityInWorld(entitytntprimed);
                //worldIn.playSoundAtEntity(entitytntprimed, "minetrek:alarm", 1.0F, 1.0F);
        }
    }
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (playerIn.getCurrentEquippedItem() != null)
        {
            Item item = playerIn.getCurrentEquippedItem().getItem();

            if (item == Items.flint_and_steel || item == Items.fire_charge)
            {
                this.explode(worldIn, pos, playerIn);
                worldIn.setBlockToAir(pos);

                if (item == Items.flint_and_steel)
                {
                    playerIn.getCurrentEquippedItem().damageItem(1, playerIn);
                }
                else if (!playerIn.capabilities.isCreativeMode)
                {
                    --playerIn.getCurrentEquippedItem().stackSize;
                }

                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;

            if (entityarrow.isBurning())
            {
                this.explode(worldIn, pos, entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }
    
}
