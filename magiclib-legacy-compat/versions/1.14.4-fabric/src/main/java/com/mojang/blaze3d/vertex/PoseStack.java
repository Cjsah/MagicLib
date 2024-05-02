//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mojang.blaze3d.vertex;

import com.google.common.collect.Queues;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import top.hendrixshen.magiclib.compat.minecraft.api.math.Matrix4fCompatApi;
import top.hendrixshen.magiclib.compat.minecraft.api.util.MthCompatApi;

import java.util.Deque;

// Code from mojang minecraft 1.18.2!
@Environment(EnvType.CLIENT)
public class PoseStack {
    private final Deque<Pose> poseStack = Util.make(Queues.newArrayDeque(), (arrayDeque) -> {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.setIdentity();
        arrayDeque.add(new Pose(matrix4f, matrix3f));
    });

    public PoseStack() {
    }

    public void translate(double d, double e, double f) {
        Pose pose = this.poseStack.getLast();
        pose.pose.multiplyWithTranslation((float) d, (float) e, (float) f);
    }

    public void scale(float f, float g, float h) {
        Pose pose = this.poseStack.getLast();
        pose.pose.multiply(Matrix4fCompatApi.createScaleMatrix(f, g, h));
        if (f == g && g == h) {
            if (f > 0.0F) {
                return;
            }

            pose.normal.mul(-1.0F);
        }

        float i = 1.0F / f;
        float j = 1.0F / g;
        float k = 1.0F / h;
        float l = MthCompatApi.fastInvCubeRoot(i * j * k);
        pose.normal.mul(Matrix3f.createScaleMatrix(l * i, l * j, l * k));
    }

    public void mulPose(Quaternion quaternion) {
        Pose pose = this.poseStack.getLast();
        pose.pose.multiply(quaternion);
        pose.normal.mul(quaternion);
    }

    public void pushPose() {
        Pose pose = this.poseStack.getLast();
        this.poseStack.addLast(new Pose(pose.pose.copy(), pose.normal.copy()));
    }

    public void popPose() {
        this.poseStack.removeLast();
    }

    public Pose last() {
        return this.poseStack.getLast();
    }

    public boolean clear() {
        return this.poseStack.size() == 1;
    }

    public void setIdentity() {
        Pose pose = this.poseStack.getLast();
        pose.pose.setIdentity();
        pose.normal.setIdentity();
    }

    public void mulPoseMatrix(Matrix4f matrix4f) {
        this.poseStack.getLast().pose.multiply(matrix4f);
    }

    @Environment(EnvType.CLIENT)
    public static final class Pose {
        final Matrix4f pose;
        final Matrix3f normal;

        Pose(Matrix4f matrix4f, Matrix3f matrix3f) {
            this.pose = matrix4f;
            this.normal = matrix3f;
        }

        public Matrix4f pose() {
            return this.pose;
        }

        public Matrix3f normal() {
            return this.normal;
        }
    }
}
