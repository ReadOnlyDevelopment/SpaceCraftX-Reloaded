package fr.militario.spacex.models;

import javax.vecmath.Vector3f;

import fr.militario.spacex.entity.EntityF9SecondStage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelF9SecondStage extends ModelBase {

	ModelRenderer Base01;
	ModelRenderer Base02;
	ModelRenderer Base03;
	ModelRenderer Base04;
	ModelRenderer Base05;
	ModelRenderer Base06;
	ModelRenderer Base07;
	ModelRenderer Base08;
	ModelRenderer Base09;
	ModelRenderer Base10;
	ModelRenderer Base11;
	ModelRenderer Base12;
	ModelRenderer Base13;
	ModelRenderer Base14;
	ModelRenderer Base15;
	ModelRenderer Base16;
	ModelRenderer Base17;
	ModelRenderer Base34;
	ModelRenderer Base35;
	ModelRenderer Base36;
	ModelRenderer Base37;
	ModelRenderer Base38;
	ModelRenderer Base39;
	ModelRenderer Base40;
	ModelRenderer Base41;
	ModelRenderer Base42;
	ModelRenderer Base62;
	ModelRenderer Base63;
	ModelRenderer Import_ImportMerlin01;
	ModelRenderer Import_ImportMerlin02;
	ModelRenderer Import_ImportMerlin03;
	ModelRenderer Import_ImportMerlin04;
	ModelRenderer Import_ImportMerlin05;
	ModelRenderer Import_ImportMerlin06;
	ModelRenderer Import_ImportMerlin07;
	ModelRenderer Import_ImportMerlin08;
	ModelRenderer Import_ImportMerlin09;
	ModelRenderer Import_ImportMerlin10;
	ModelRenderer Import_ImportMerlin11;
	ModelRenderer Import_ImportMerlin12;
	ModelRenderer Import_ImportMerlin13;
	ModelRenderer Import_ImportMerlin14;
	ModelRenderer Import_ImportMerlin15;
	ModelRenderer Import_ImportMerlin16;
	ModelRenderer Import_ImportMerlin17;
	ModelRenderer Box_0;
	ModelRenderer Box_1;
	ModelRenderer Box_2;
	ModelRenderer Box_3;
	ModelRenderer Box_4;
	ModelRenderer Box_5;
	ModelRenderer Box_6;
	ModelRenderer Box_7;
	ModelRenderer Box_8;
	ModelRenderer Box_9;
	ModelRenderer Box_10;
	ModelRenderer Box_11;
	ModelRenderer Box_12;
	ModelRenderer Box_13;
	ModelRenderer Box_14;
	ModelRenderer Box_15;
	ModelRenderer Box_16;
	ModelRenderer Box_17;
	ModelRenderer Box_18;
	ModelRenderer Box_19;

	public ModelF9SecondStage() {
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.Base01 = new ModelRenderer(this, 100, 20);
		this.Base01.addBox(-9.0F, 0.0F, 0.0F, 18, 60, 1);
		this.Base01.setRotationPoint(0.0F, -50.0F, -23.0F);
		this.Base01.setTextureSize(64, 32);
		this.Base01.mirror = true;
		setRotation(this.Base01, 0.0F, 0.0F, 0.0F);
		this.Base02 = new ModelRenderer(this, 100, 0);
		this.Base02.addBox(0.0F, 0.0F, -10.0F, 1, 60, 20);
		this.Base02.setRotationPoint(-16.0F, -50.0F, -16.0F);
		this.Base02.setTextureSize(64, 32);
		this.Base02.mirror = true;
		setRotation(this.Base02, 0.0F, -0.7853982F, 0.0F);
		this.Base03 = new ModelRenderer(this, 102, 2);
		this.Base03.addBox(0.0F, 0.0F, -9.0F, 1, 60, 18);
		this.Base03.setRotationPoint(-23.0F, -50.0F, 0.0F);
		this.Base03.setTextureSize(64, 32);
		this.Base03.mirror = true;
		setRotation(this.Base03, 0.0F, 0.0F, 0.0F);
		this.Base04 = new ModelRenderer(this, 100, 0);
		this.Base04.addBox(0.0F, 0.0F, -10.0F, 1, 60, 20);
		this.Base04.setRotationPoint(-16.0F, -50.0F, 16.0F);
		this.Base04.setTextureSize(64, 32);
		this.Base04.mirror = true;
		setRotation(this.Base04, 0.0F, 0.7853982F, 0.0F);
		this.Base05 = new ModelRenderer(this, 100, 20);
		this.Base05.addBox(-9.0F, 0.0F, 0.0F, 18, 60, 1);
		this.Base05.setRotationPoint(0.0F, -50.0F, 22.0F);
		this.Base05.setTextureSize(64, 32);
		this.Base05.mirror = true;
		setRotation(this.Base05, 0.0F, 0.0F, 0.0F);
		this.Base06 = new ModelRenderer(this, 100, 0);
		this.Base06.addBox(0.0F, 0.0F, -10.0F, 1, 60, 20);
		this.Base06.setRotationPoint(15.3F, -50.0F, 15.3F);
		this.Base06.setTextureSize(64, 32);
		this.Base06.mirror = true;
		setRotation(this.Base06, 0.0F, -0.7853982F, 0.0F);
		this.Base07 = new ModelRenderer(this, 100, 20);
		this.Base07.addBox(-9.0F, 0.0F, 0.0F, 18, 60, 1);
		this.Base07.setRotationPoint(22.0F, -50.0F, 0.0F);
		this.Base07.setTextureSize(64, 32);
		this.Base07.mirror = true;
		setRotation(this.Base07, 0.0F, 1.570796F, 0.0F);
		this.Base08 = new ModelRenderer(this, 100, 0);
		this.Base08.addBox(0.0F, 0.0F, -10.0F, 1, 60, 20);
		this.Base08.setRotationPoint(15.3F, -50.0F, -15.3F);
		this.Base08.setTextureSize(64, 32);
		this.Base08.mirror = true;
		setRotation(this.Base08, 0.0F, 0.7941248F, 0.0F);
		this.Base09 = new ModelRenderer(this, 145, 0);
		this.Base09.addBox(-1.0F, 0.0F, -9.0F, 14, 1, 18);
		this.Base09.setRotationPoint(-21.8F, -6.5F, 0.0F);
		this.Base09.setTextureSize(64, 32);
		this.Base09.mirror = true;
		setRotation(this.Base09, 0.0F, 0.0F, 0.2443461F);
		this.Base10 = new ModelRenderer(this, 145, 0);
		this.Base10.addBox(-9.0F, 0.0F, -14.0F, 18, 1, 14);
		this.Base10.setRotationPoint(0.0F, -6.9F, 22.7F);
		this.Base10.setTextureSize(64, 32);
		this.Base10.mirror = true;
		setRotation(this.Base10, 0.2792527F, 0.0F, 0.0F);
		this.Base11 = new ModelRenderer(this, 145, 0);
		this.Base11.addBox(-9.0F, 0.0F, -14.0F, 18, 1, 14);
		this.Base11.setRotationPoint(22.7F, -6.9F, 0.0F);
		this.Base11.setTextureSize(64, 32);
		this.Base11.mirror = true;
		setRotation(this.Base11, 0.2792527F, 1.570796F, 0.0F);
		this.Base12 = new ModelRenderer(this, 145, 0);
		this.Base12.addBox(-9.0F, 0.0F, -1.0F, 18, 1, 14);
		this.Base12.setRotationPoint(0.0F, -6.6F, -21.7F);
		this.Base12.setTextureSize(64, 32);
		this.Base12.mirror = true;
		setRotation(this.Base12, -0.2443461F, 0.0F, 0.0F);
		this.Base13 = new ModelRenderer(this, 145, 21);
		this.Base13.addBox(-9.0F, 0.0F, -1.0F, 18, 1, 14);
		this.Base13.setRotationPoint(15.0F, -6.6F, -14.7F);
		this.Base13.setTextureSize(64, 32);
		this.Base13.mirror = true;
		setRotation(this.Base13, -0.2268928F, -0.7853982F, 0.0F);
		this.Base14 = new ModelRenderer(this, 145, 21);
		this.Base14.addBox(-9.0F, 0.0F, -1.0F, 18, 1, 14);
		this.Base14.setRotationPoint(-15.0F, -6.6F, -15.0F);
		this.Base14.setTextureSize(64, 32);
		this.Base14.mirror = true;
		setRotation(this.Base14, -0.2094395F, 0.7853982F, 0.0F);
		this.Base15 = new ModelRenderer(this, 145, 21);
		this.Base15.addBox(-9.0F, 0.0F, -12.0F, 18, 1, 14);
		this.Base15.setRotationPoint(-14.2F, -6.6F, 14.2F);
		this.Base15.setTextureSize(64, 32);
		this.Base15.mirror = true;
		setRotation(this.Base15, 0.2443461F, -0.7853982F, 0.0F);
		this.Base16 = new ModelRenderer(this, 145, 21);
		this.Base16.addBox(-9.0F, 0.0F, -13.0F, 18, 1, 14);
		this.Base16.setRotationPoint(15.0F, -6.6F, 15.0F);
		this.Base16.setTextureSize(64, 32);
		this.Base16.mirror = true;
		setRotation(this.Base16, 0.2268928F, 0.7853982F, 0.0F);
		this.Base17 = new ModelRenderer(this, 138, 107);
		this.Base17.addBox(-9.0F, 0.0F, -9.0F, 20, 1, 20);
		this.Base17.setRotationPoint(-1.0F, -3.4F, -1.0F);
		this.Base17.setTextureSize(64, 32);
		this.Base17.mirror = true;
		setRotation(this.Base17, 0.0F, 0.0F, 0.0F);
		this.Base34 = new ModelRenderer(this, 138, 84);
		this.Base34.addBox(-9.0F, 0.0F, -9.0F, 20, 1, 20);
		this.Base34.setRotationPoint(-1.0F, -49.5F, -1.0F);
		this.Base34.setTextureSize(64, 32);
		this.Base34.mirror = true;
		setRotation(this.Base34, 0.0F, 0.0F, 0.0F);
		this.Base35 = new ModelRenderer(this, 145, 62);
		this.Base35.addBox(-9.0F, 0.0F, -13.0F, 18, 1, 14);
		this.Base35.setRotationPoint(15.0F, -46.7F, 15.0F);
		this.Base35.setTextureSize(64, 32);
		this.Base35.mirror = true;
		setRotation(this.Base35, -0.2268928F, 0.7853982F, 0.0F);
		this.Base36 = new ModelRenderer(this, 145, 39);
		this.Base36.addBox(-9.0F, 0.0F, -14.0F, 18, 1, 14);
		this.Base36.setRotationPoint(22.7F, -47.0F, 0.0F);
		this.Base36.setTextureSize(64, 32);
		this.Base36.mirror = true;
		setRotation(this.Base36, -0.2094395F, 1.570796F, 0.0F);
		this.Base37 = new ModelRenderer(this, 145, 62);
		this.Base37.addBox(-9.0F, 0.0F, -1.0F, 18, 1, 14);
		this.Base37.setRotationPoint(15.0F, -46.7F, -14.7F);
		this.Base37.setTextureSize(64, 32);
		this.Base37.mirror = true;
		setRotation(this.Base37, 0.2268928F, -0.7853982F, 0.0F);
		this.Base38 = new ModelRenderer(this, 145, 39);
		this.Base38.addBox(-9.0F, 0.0F, -1.0F, 18, 1, 14);
		this.Base38.setRotationPoint(0.0F, -46.7F, -21.7F);
		this.Base38.setTextureSize(64, 32);
		this.Base38.mirror = true;
		setRotation(this.Base38, 0.2443461F, 0.0F, 0.0F);
		this.Base39 = new ModelRenderer(this, 145, 39);
		this.Base39.addBox(-1.0F, 0.0F, -9.0F, 14, 1, 18);
		this.Base39.setRotationPoint(-21.8F, -46.6F, 0.0F);
		this.Base39.setTextureSize(64, 32);
		this.Base39.mirror = true;
		setRotation(this.Base39, 0.0F, 0.0F, -0.2443461F);
		this.Base40 = new ModelRenderer(this, 145, 39);
		this.Base40.addBox(-9.0F, 0.0F, -14.0F, 18, 1, 14);
		this.Base40.setRotationPoint(0.0F, -47.0F, 22.7F);
		this.Base40.setTextureSize(64, 32);
		this.Base40.mirror = true;
		setRotation(this.Base40, -0.2094395F, 0.0F, 0.0F);
		this.Base41 = new ModelRenderer(this, 145, 62);
		this.Base41.addBox(-9.0F, 0.0F, -1.0F, 18, 1, 14);
		this.Base41.setRotationPoint(-15.0F, -46.7F, -15.0F);
		this.Base41.setTextureSize(64, 32);
		this.Base41.mirror = true;
		setRotation(this.Base41, 0.2094395F, 0.7853982F, 0.0F);
		this.Base42 = new ModelRenderer(this, 145, 62);
		this.Base42.addBox(-9.0F, 0.0F, -12.0F, 18, 1, 14);
		this.Base42.setRotationPoint(-14.2F, -46.7F, 14.2F);
		this.Base42.setTextureSize(64, 32);
		this.Base42.mirror = true;
		setRotation(this.Base42, -0.2443461F, -0.7853982F, 0.0F);
		this.Base62 = new ModelRenderer(this, 102, 102);
		this.Base62.addBox(-2.5F, 0.0F, 0.0F, 5, 7, 3);
		this.Base62.setRotationPoint(-23.5F, 3.0F, -9.0F);
		this.Base62.setTextureSize(64, 32);
		this.Base62.mirror = true;
		setRotation(this.Base62, 0.0F, 1.186824F, 0.0F);
		this.Base63 = new ModelRenderer(this, 102, 117);
		this.Base63.addBox(-2.5F, 0.0F, 0.0F, 5, 3, 3);
		this.Base63.setRotationPoint(-23.5F, 3.0F, -9.0F);
		this.Base63.setTextureSize(64, 32);
		this.Base63.mirror = true;
		setRotation(this.Base63, 0.7853982F, 1.186824F, 0.0F);
		this.Import_ImportMerlin01 = new ModelRenderer(this, 0, 4);
		this.Import_ImportMerlin01.addBox(-2.5F, -5.0F, 0.0F, 5, 5, 1);
		this.Import_ImportMerlin01.setRotationPoint(0.5F, 18.5F, 5.0F);
		this.Import_ImportMerlin01.setTextureSize(64, 32);
		this.Import_ImportMerlin01.mirror = true;
		setRotation(this.Import_ImportMerlin01, 0.2792527F, 0.0F, 0.0F);
		this.Import_ImportMerlin02 = new ModelRenderer(this, 14, 4);
		this.Import_ImportMerlin02.addBox(-2.5F, 0.0F, -1.0F, 5, 5, 1);
		this.Import_ImportMerlin02.setRotationPoint(0.5F, 18.0F, -4.0F);
		this.Import_ImportMerlin02.setTextureSize(64, 32);
		this.Import_ImportMerlin02.mirror = true;
		setRotation(this.Import_ImportMerlin02, 0.0F, 0.0F, 0.0F);
		this.Import_ImportMerlin03 = new ModelRenderer(this, 14, 0);
		this.Import_ImportMerlin03.addBox(0.0F, 0.0F, -2.5F, 1, 5, 5);
		this.Import_ImportMerlin03.setRotationPoint(5.0F, 18.0F, 0.5F);
		this.Import_ImportMerlin03.setTextureSize(64, 32);
		this.Import_ImportMerlin03.mirror = true;
		setRotation(this.Import_ImportMerlin03, 0.0F, 0.0F, 0.0F);
		this.Import_ImportMerlin04 = new ModelRenderer(this, 14, 0);
		this.Import_ImportMerlin04.addBox(-1.0F, 0.0F, -2.5F, 1, 5, 5);
		this.Import_ImportMerlin04.setRotationPoint(-4.0F, 18.0F, 0.5F);
		this.Import_ImportMerlin04.setTextureSize(64, 32);
		this.Import_ImportMerlin04.mirror = true;
		setRotation(this.Import_ImportMerlin04, 0.0F, 0.0F, 0.0F);
		this.Import_ImportMerlin05 = new ModelRenderer(this, 0, 12);
		this.Import_ImportMerlin05.addBox(-1.0F, 0.0F, -2.5F, 1, 5, 4);
		this.Import_ImportMerlin05.setRotationPoint(-2.5F, 18.0F, 4.2F);
		this.Import_ImportMerlin05.setTextureSize(64, 32);
		this.Import_ImportMerlin05.mirror = true;
		setRotation(this.Import_ImportMerlin05, 0.0F, 0.7853982F, 0.0F);
		this.Import_ImportMerlin06 = new ModelRenderer(this, 0, 12);
		this.Import_ImportMerlin06.addBox(0.0F, 0.0F, -2.0F, 1, 5, 4);
		this.Import_ImportMerlin06.setRotationPoint(3.8F, 18.0F, -2.8F);
		this.Import_ImportMerlin06.setTextureSize(64, 32);
		this.Import_ImportMerlin06.mirror = true;
		setRotation(this.Import_ImportMerlin06, 0.0F, 0.7853982F, 0.0F);
		this.Import_ImportMerlin07 = new ModelRenderer(this, 0, 12);
		this.Import_ImportMerlin07.addBox(-1.0F, 0.0F, -2.0F, 1, 5, 4);
		this.Import_ImportMerlin07.setRotationPoint(-2.8F, 18.0F, -2.8F);
		this.Import_ImportMerlin07.setTextureSize(64, 32);
		this.Import_ImportMerlin07.mirror = true;
		setRotation(this.Import_ImportMerlin07, 0.0F, -0.7853982F, 0.0F);
		this.Import_ImportMerlin08 = new ModelRenderer(this, 0, 12);
		this.Import_ImportMerlin08.addBox(0.0F, 0.0F, -2.0F, 1, 5, 4);
		this.Import_ImportMerlin08.setRotationPoint(3.8F, 18.0F, 3.8F);
		this.Import_ImportMerlin08.setTextureSize(64, 32);
		this.Import_ImportMerlin08.mirror = true;
		setRotation(this.Import_ImportMerlin08, 0.0F, -0.7853982F, 0.0F);
		this.Import_ImportMerlin09 = new ModelRenderer(this, 0, 0);
		this.Import_ImportMerlin09.addBox(-1.0F, -5.0F, -2.5F, 1, 5, 5);
		this.Import_ImportMerlin09.setRotationPoint(-4.0F, 18.5F, 0.5F);
		this.Import_ImportMerlin09.setTextureSize(64, 32);
		this.Import_ImportMerlin09.mirror = true;
		setRotation(this.Import_ImportMerlin09, 0.0F, 0.0F, 0.2792527F);
		this.Import_ImportMerlin10 = new ModelRenderer(this, 0, 0);
		this.Import_ImportMerlin10.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5);
		this.Import_ImportMerlin10.setRotationPoint(5.0F, 18.5F, 0.5F);
		this.Import_ImportMerlin10.setTextureSize(64, 32);
		this.Import_ImportMerlin10.mirror = true;
		setRotation(this.Import_ImportMerlin10, 0.0F, 0.0F, -0.2792527F);
		this.Import_ImportMerlin11 = new ModelRenderer(this, 14, 4);
		this.Import_ImportMerlin11.addBox(-2.5F, 0.0F, 0.0F, 5, 5, 1);
		this.Import_ImportMerlin11.setRotationPoint(0.5F, 18.0F, 5.0F);
		this.Import_ImportMerlin11.setTextureSize(64, 32);
		this.Import_ImportMerlin11.mirror = true;
		setRotation(this.Import_ImportMerlin11, 0.0F, 0.0F, 0.0F);
		this.Import_ImportMerlin12 = new ModelRenderer(this, 0, 4);
		this.Import_ImportMerlin12.addBox(-2.5F, -5.0F, -1.0F, 5, 5, 1);
		this.Import_ImportMerlin12.setRotationPoint(0.5F, 18.5F, -4.0F);
		this.Import_ImportMerlin12.setTextureSize(64, 32);
		this.Import_ImportMerlin12.mirror = true;
		setRotation(this.Import_ImportMerlin12, -0.2792527F, 0.0F, 0.0F);
		this.Import_ImportMerlin13 = new ModelRenderer(this, 13, 12);
		this.Import_ImportMerlin13.addBox(-1.0F, -5.0F, -2.0F, 1, 5, 4);
		this.Import_ImportMerlin13.setRotationPoint(-2.8F, 18.5F, -2.8F);
		this.Import_ImportMerlin13.setTextureSize(64, 32);
		this.Import_ImportMerlin13.mirror = true;
		setRotation(this.Import_ImportMerlin13, 0.0F, -0.7853982F, 0.2792527F);
		this.Import_ImportMerlin14 = new ModelRenderer(this, 13, 12);
		this.Import_ImportMerlin14.addBox(0.0F, -5.0F, -2.0F, 1, 5, 4);
		this.Import_ImportMerlin14.setRotationPoint(3.8F, 18.5F, -2.8F);
		this.Import_ImportMerlin14.setTextureSize(64, 32);
		this.Import_ImportMerlin14.mirror = true;
		setRotation(this.Import_ImportMerlin14, 0.0349066F, 0.7853982F, -0.2792527F);
		this.Import_ImportMerlin15 = new ModelRenderer(this, 13, 12);
		this.Import_ImportMerlin15.addBox(0.0F, -5.0F, -2.0F, 1, 5, 4);
		this.Import_ImportMerlin15.setRotationPoint(3.8F, 18.5F, 3.8F);
		this.Import_ImportMerlin15.setTextureSize(64, 32);
		this.Import_ImportMerlin15.mirror = true;
		setRotation(this.Import_ImportMerlin15, 0.0F, -0.7853982F, -0.2792527F);
		this.Import_ImportMerlin16 = new ModelRenderer(this, 13, 12);
		this.Import_ImportMerlin16.addBox(-1.0F, -5.0F, -2.5F, 1, 5, 4);
		this.Import_ImportMerlin16.setRotationPoint(-2.5F, 18.5F, 4.2F);
		this.Import_ImportMerlin16.setTextureSize(64, 32);
		this.Import_ImportMerlin16.mirror = true;
		setRotation(this.Import_ImportMerlin16, 0.0F, 0.7853982F, 0.2792527F);
		this.Import_ImportMerlin17 = new ModelRenderer(this, 22, 34);
		this.Import_ImportMerlin17.addBox(-3.0F, 0.0F, -3.0F, 7, 1, 7);
		this.Import_ImportMerlin17.setRotationPoint(0.0F, 13.5F, 0.0F);
		this.Import_ImportMerlin17.setTextureSize(64, 32);
		this.Import_ImportMerlin17.mirror = true;
		setRotation(this.Import_ImportMerlin17, 0.0F, 0.0F, 0.0F);
		this.Box_0 = new ModelRenderer(this, 22, 45);
		this.Box_0.addBox(-3.0F, 0.0F, -3.0F, 5, 3, 5);
		this.Box_0.setRotationPoint(1.0F, 10.5F, 1.0F);
		this.Box_0.setTextureSize(64, 32);
		this.Box_0.mirror = true;
		setRotation(this.Box_0, 0.0F, 0.0F, 0.0F);
		this.Box_1 = new ModelRenderer(this, 24, 15);
		this.Box_1.addBox(-3.0F, 0.0F, -3.0F, 7, 11, 7);
		this.Box_1.setRotationPoint(0.0F, -0.5F, 0.0F);
		this.Box_1.setTextureSize(64, 32);
		this.Box_1.mirror = true;
		setRotation(this.Box_1, 0.0F, 0.0F, 0.0F);
		this.Box_2 = new ModelRenderer(this, 23, 57);
		this.Box_2.addBox(-3.0F, 0.0F, -3.0F, 5, 2, 5);
		this.Box_2.setRotationPoint(1.0F, -2.5F, 1.0F);
		this.Box_2.setTextureSize(64, 32);
		this.Box_2.mirror = true;
		setRotation(this.Box_2, 0.0F, 0.0F, 0.0F);
		this.Box_3 = new ModelRenderer(this, 0, 39);
		this.Box_3.addBox(-3.0F, 0.0F, -3.0F, 5, 9, 5);
		this.Box_3.setRotationPoint(1.0F, 0.5F, -6.0F);
		this.Box_3.setTextureSize(64, 32);
		this.Box_3.mirror = true;
		setRotation(this.Box_3, 0.0F, 0.0F, 0.0F);
		this.Box_4 = new ModelRenderer(this, 0, 54);
		this.Box_4.addBox(-3.0F, 0.0F, -3.0F, 3, 1, 3);
		this.Box_4.setRotationPoint(2.0F, 0.0F, -5.0F);
		this.Box_4.setTextureSize(64, 32);
		this.Box_4.mirror = true;
		setRotation(this.Box_4, 0.0F, 0.0F, 0.0F);
		this.Box_5 = new ModelRenderer(this, 0, 59);
		this.Box_5.addBox(-3.0F, 0.0F, -3.0F, 4, 1, 4);
		this.Box_5.setRotationPoint(1.5F, 9.5F, -5.5F);
		this.Box_5.setTextureSize(64, 32);
		this.Box_5.mirror = true;
		setRotation(this.Box_5, 0.0F, 0.0F, 0.0F);
		this.Box_6 = new ModelRenderer(this, 45, 0);
		this.Box_6.addBox(-3.0F, 0.0F, -3.0F, 2, 11, 2);
		this.Box_6.setRotationPoint(-2.5F, -0.5F, 2.0F);
		this.Box_6.setTextureSize(64, 32);
		this.Box_6.mirror = true;
		setRotation(this.Box_6, 0.0F, 0.0F, 0.0F);
		this.Box_7 = new ModelRenderer(this, 45, 0);
		this.Box_7.addBox(-3.0F, 0.0F, -3.0F, 2, 4, 2);
		this.Box_7.setRotationPoint(-2.5F, 9.5F, 2.0F);
		this.Box_7.setTextureSize(64, 32);
		this.Box_7.mirror = true;
		setRotation(this.Box_7, 0.0F, 0.0F, -0.3490659F);
		this.Box_8 = new ModelRenderer(this, 45, 0);
		this.Box_8.addBox(-3.0F, 0.0F, -3.0F, 2, 2, 5);
		this.Box_8.setRotationPoint(-2.5F, -3.5F, 0.0F);
		this.Box_8.setTextureSize(64, 32);
		this.Box_8.mirror = true;
		setRotation(this.Box_8, 0.0F, 0.0F, 0.0F);
		this.Box_9 = new ModelRenderer(this, 45, 0);
		this.Box_9.addBox(-3.0F, 0.0F, -3.0F, 2, 3, 2);
		this.Box_9.setRotationPoint(-1.5F, -0.5F, 2.0F);
		this.Box_9.setTextureSize(64, 32);
		this.Box_9.mirror = true;
		setRotation(this.Box_9, 0.0F, 0.0F, 0.8203047F);
		this.Box_10 = new ModelRenderer(this, 42, 0);
		this.Box_10.addBox(-3.0F, 0.0F, -3.0F, 9, 2, 2);
		this.Box_10.setRotationPoint(-1.5F, -3.5F, -2.0F);
		this.Box_10.setTextureSize(64, 32);
		this.Box_10.mirror = true;
		setRotation(this.Box_10, 0.0F, 0.0F, 0.0F);
		this.Box_11 = new ModelRenderer(this, 45, 0);
		this.Box_11.addBox(0.0F, 0.0F, -3.0F, 1, 3, 1);
		this.Box_11.setRotationPoint(-3.0F, -1.5F, -5.0F);
		this.Box_11.setTextureSize(64, 32);
		this.Box_11.mirror = true;
		setRotation(this.Box_11, 0.0F, 0.0F, 0.0F);
		this.Box_12 = new ModelRenderer(this, 45, 0);
		this.Box_12.addBox(0.0F, 0.0F, -3.0F, 1, 1, 3);
		this.Box_12.setRotationPoint(-3.0F, -2.5F, -5.0F);
		this.Box_12.setTextureSize(64, 32);
		this.Box_12.mirror = true;
		setRotation(this.Box_12, 0.3490659F, 0.0F, 0.0F);
		this.Box_13 = new ModelRenderer(this, 45, 0);
		this.Box_13.addBox(-3.0F, 0.0F, -3.0F, 2, 9, 2);
		this.Box_13.setRotationPoint(7.5F, -4.0F, -3.0F);
		this.Box_13.setTextureSize(64, 32);
		this.Box_13.mirror = true;
		setRotation(this.Box_13, 0.7853982F, 0.0F, 0.0F);
		this.Box_14 = new ModelRenderer(this, 45, 0);
		this.Box_14.addBox(-3.0F, 0.0F, -3.0F, 2, 2, 2);
		this.Box_14.setRotationPoint(7.0F, 2.5F, 4.0F);
		this.Box_14.setTextureSize(64, 32);
		this.Box_14.mirror = true;
		setRotation(this.Box_14, 0.0F, 0.0F, 0.0F);
		this.Box_15 = new ModelRenderer(this, 45, 0);
		this.Box_15.addBox(0.0F, 0.0F, -3.0F, 1, 6, 1);
		this.Box_15.setRotationPoint(-3.5F, -1.5F, -2.5F);
		this.Box_15.setTextureSize(64, 32);
		this.Box_15.mirror = true;
		setRotation(this.Box_15, 0.0F, 0.0F, 0.0F);
		this.Box_16 = new ModelRenderer(this, 45, 0);
		this.Box_16.addBox(0.0F, 0.0F, -3.0F, 2, 1, 1);
		this.Box_16.setRotationPoint(-3.0F, 3.5F, -2.5F);
		this.Box_16.setTextureSize(64, 32);
		this.Box_16.mirror = true;
		setRotation(this.Box_16, 0.0F, 0.0F, 0.4886922F);
		this.Box_17 = new ModelRenderer(this, 45, 0);
		this.Box_17.addBox(-3.0F, 0.0F, -3.0F, 1, 1, 3);
		this.Box_17.setRotationPoint(-2.0F, -3.0F, 5.0F);
		this.Box_17.setTextureSize(64, 32);
		this.Box_17.mirror = true;
		setRotation(this.Box_17, 0.0F, 0.0F, 0.0F);
		this.Box_18 = new ModelRenderer(this, 45, 0);
		this.Box_18.addBox(0.0F, 0.0F, -3.0F, 3, 1, 1);
		this.Box_18.setRotationPoint(-4.0F, -3.0F, 7.0F);
		this.Box_18.setTextureSize(64, 32);
		this.Box_18.mirror = true;
		setRotation(this.Box_18, 0.0F, 0.0F, 0.0F);
		this.Box_19 = new ModelRenderer(this, 45, 0);
		this.Box_19.addBox(0.0F, 0.0F, -3.0F, 1, 3, 1);
		this.Box_19.setRotationPoint(-2.0F, -3.0F, 6.5F);
		this.Box_19.setTextureSize(64, 32);
		this.Box_19.mirror = true;
		setRotation(this.Box_19, 0.0F, 0.0F, 0.0F);
	}

	private Vector3f ConvertEulerYzxToZyx(Vector3f eulerYzx) {
		float a = MathHelper.cos(eulerYzx.x);
		float b = MathHelper.sin(eulerYzx.x);
		float c = MathHelper.cos(eulerYzx.y);
		float d = MathHelper.sin(eulerYzx.y);
		float e = MathHelper.cos(eulerYzx.z);
		float f = MathHelper.sin(eulerYzx.z);
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = c * e;
		matrix.m01 = (b * d) - (a * c * f);
		matrix.m02 = (b * c * f) + (a * d);
		matrix.m10 = f;
		matrix.m11 = a * e;
		matrix.m12 = -b * e;
		matrix.m20 = -d * e;
		matrix.m21 = (a * d * f) + (b * c);
		matrix.m22 = (a * c) - (b * d * f);
		matrix.m33 = 1.0F;
		Vector3f eulerZyx = new Vector3f();
		eulerZyx.y = (float) Math.asin(MathHelper.clamp(-matrix.m20, -1.0F, 1.0F));
		if (MathHelper.abs(matrix.m20) < 0.99999D) {
			eulerZyx.x = (float) Math.atan2(matrix.m21, matrix.m22);
			eulerZyx.z = (float) Math.atan2(matrix.m10, matrix.m00);
		} else {
			eulerZyx.x = 0.0F;
			eulerZyx.z = (float) Math.atan2(-matrix.m01, matrix.m11);
		}
		return eulerZyx;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		EntityF9SecondStage rocket = (EntityF9SecondStage) entity;
		GlStateManager.pushMatrix();
		GlStateManager.rotate(rocket.getGroundAngle(), 0.0F, 0.0F, 1.0F);
		this.Base01.render(f5);
		this.Base02.render(f5);
		this.Base03.render(f5);
		this.Base04.render(f5);
		this.Base05.render(f5);
		this.Base06.render(f5);
		this.Base07.render(f5);
		this.Base08.render(f5);
		this.Base09.render(f5);
		this.Base10.render(f5);
		this.Base11.render(f5);
		this.Base12.render(f5);
		this.Base13.render(f5);
		this.Base14.render(f5);
		this.Base15.render(f5);
		this.Base16.render(f5);
		this.Base17.render(f5);
		this.Base34.render(f5);
		this.Base35.render(f5);
		this.Base36.render(f5);
		this.Base37.render(f5);
		this.Base38.render(f5);
		this.Base39.render(f5);
		this.Base40.render(f5);
		this.Base41.render(f5);
		this.Base42.render(f5);
		this.Base62.render(f5);
		this.Base63.render(f5);
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.5F, 1.0F, 1.5F);
		this.Import_ImportMerlin01.render(f5);
		this.Import_ImportMerlin02.render(f5);
		this.Import_ImportMerlin03.render(f5);
		this.Import_ImportMerlin04.render(f5);
		this.Import_ImportMerlin05.render(f5);
		this.Import_ImportMerlin06.render(f5);
		this.Import_ImportMerlin07.render(f5);
		this.Import_ImportMerlin08.render(f5);
		this.Import_ImportMerlin09.render(f5);
		this.Import_ImportMerlin10.render(f5);
		this.Import_ImportMerlin11.render(f5);
		this.Import_ImportMerlin12.render(f5);
		this.Import_ImportMerlin13.render(f5);
		this.Import_ImportMerlin14.render(f5);
		this.Import_ImportMerlin15.render(f5);
		this.Import_ImportMerlin16.render(f5);
		this.Import_ImportMerlin17.render(f5);
		this.Box_0.render(f5);
		this.Box_1.render(f5);
		this.Box_2.render(f5);
		this.Box_3.render(f5);
		this.Box_4.render(f5);
		this.Box_5.render(f5);
		this.Box_6.render(f5);
		this.Box_7.render(f5);
		this.Box_8.render(f5);
		this.Box_9.render(f5);
		this.Box_10.render(f5);
		this.Box_11.render(f5);
		this.Box_12.render(f5);
		this.Box_13.render(f5);
		this.Box_14.render(f5);
		this.Box_15.render(f5);
		this.Box_16.render(f5);
		this.Box_17.render(f5);
		this.Box_18.render(f5);
		this.Box_19.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		Vector3f eulerYzx = new Vector3f(x, y, z);
		Vector3f eulerZyx = ConvertEulerYzxToZyx(eulerYzx);
		model.rotateAngleX = eulerZyx.x;
		model.rotateAngleY = eulerZyx.y;
		model.rotateAngleZ = eulerZyx.z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
