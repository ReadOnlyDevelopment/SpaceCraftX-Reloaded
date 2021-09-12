package fr.militario.spacex.models.items;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.Sys;

import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelFalcon9Item extends ModelTransformWrapper {

	public static long getMilliseconds() {
		return (Sys.getTime() * 1000L) / Sys.getTimerResolution();
	}

	public ModelFalcon9Item(IBakedModel modelToWrap) {
		super(modelToWrap);
	}

	@Override
	protected Matrix4f getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		if (cameraTransformType == ItemCameraTransforms.TransformType.GUI) {
			Vector3f trans = new Vector3f(-0.11F, 0.0F, -0.11F);
			Matrix4f ret = new Matrix4f();
			ret.setIdentity();
			Matrix4f mul = new Matrix4f();
			mul.setIdentity();
			Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30.0F, 225.0F, 0.0F));
			mul.setRotation(rot);
			ret.mul(mul);
			mul.setIdentity();
			mul.setScale(0.4F);
			ret.mul(mul);
			mul.setIdentity();
			mul.setTranslation(new Vector3f(-0.5F, -0.75F, -0.6F));
			ret.mul(mul);
			mul.setIdentity();
			mul.rotY(1.5707964F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotX(0.7853982F);
			ret.mul(mul);
			mul.setIdentity();
			mul.setTranslation(trans);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotY(ClientUtil.getClientTimeTotal() / 1000.0F);
			ret.mul(mul);
			mul.setIdentity();
			trans.scale(-1.0F);
			mul.setTranslation(trans);
			ret.mul(mul);
			mul.setIdentity();
			mul.setScale(0.28F);
			ret.mul(mul);
			return ret;
		}
		if ((cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) || (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)) {
			Vector3f trans = new Vector3f(0.5F, -6.0F, -2.6F);
			Matrix4f ret = new Matrix4f();
			ret.setIdentity();
			Matrix4f mul = new Matrix4f();
			mul.setIdentity();
			Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0.0F, 50.0F, 0.0F));
			mul.setRotation(rot);
			ret.mul(mul);
			mul.setIdentity();
			mul.setScale(0.5F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotX(1.5707964F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotZ(-0.7F);
			ret.mul(mul);
			mul.setIdentity();
			mul.setTranslation(trans);
			ret.mul(mul);
			return ret;
		}
		if ((cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) || (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)) {
			Vector3f trans = new Vector3f(-0.2F, -6.0F, 1.0F);
			Matrix4f ret = new Matrix4f();
			ret.setIdentity();
			Matrix4f mul = new Matrix4f();
			mul.setIdentity();
			Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(75.0F, 0.0F, 0.0F));
			mul.setRotation(rot);
			ret.mul(mul);
			mul.setIdentity();
			mul.setScale(0.5F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotZ(-1.5707964F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotY(1.5707964F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotX(0.3F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotZ(0.5F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotZ(-0.65F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotY(3.5F);
			mul.setTranslation(trans);
			ret.mul(mul);
			return ret;
		}
		if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND) {
			Matrix4f ret = new Matrix4f();
			ret.setIdentity();
			Matrix4f mul = new Matrix4f();
			mul.setIdentity();
			mul.setScale(0.1F);
			ret.mul(mul);
			mul.setIdentity();
			mul.setTranslation(new Vector3f(0.5F, 0.0F, 0.5F));
			ret.mul(mul);
			return ret;
		}
		if (cameraTransformType == ItemCameraTransforms.TransformType.FIXED) {
			Matrix4f ret = new Matrix4f();
			ret.setIdentity();
			Matrix4f mul = new Matrix4f();
			mul.setIdentity();
			mul.setScale(0.09F);
			ret.mul(mul);
			mul.setIdentity();
			mul.rotY(1.2F);
			ret.mul(mul);
			mul.setIdentity();
			mul.setTranslation(new Vector3f(0.5F, -5.5F, 0.5F));
			ret.mul(mul);
			return ret;
		}
		return null;
	}
}
