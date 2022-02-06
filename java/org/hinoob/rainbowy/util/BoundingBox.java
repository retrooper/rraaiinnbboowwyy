package org.hinoob.rainbowy.util;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BoundingBox {

    public double minX, maxX;
    public double minY, maxY;
    public double minZ, maxZ;

    public BoundingBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ){
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public BoundingBox(Vector loc){
        this.minX = loc.getX() - 0.3f;
        this.maxX = loc.getX() + 0.3f;
        this.minY = loc.getY();
        this.maxY = loc.getY() + 1.8f;
        this.minZ = loc.getZ() - 0.3f;
        this.maxZ = loc.getZ() + 0.3f;
    }

    public BoundingBox expand(double value){
        this.minX -= value;
        this.minY -= value;
        this.minZ -= value;
        this.maxX += value;
        this.maxY += value;
        this.maxZ += value;
        return this;
    }

    public boolean isVecInside(Vector vec) {
        return vec.getX() > minX && vec.getX() < maxX && (vec.getY() > minY && vec.getY() < maxY && vec.getZ() > minZ && vec.getZ() < maxZ);
    }


    public Vector calculateIntercept(Vector origin, Vector end) {
        Vector minX = getIntermediateWithXValue(origin, end, this.minX);
        Vector maxX = getIntermediateWithXValue(origin, end, this.maxX);
        Vector minY = getIntermediateWithYValue(origin, end, this.minY);
        Vector maxY = getIntermediateWithYValue(origin, end, this.maxY);
        Vector minZ = getIntermediateWithZValue(origin, end, this.minZ);
        Vector maxZ = getIntermediateWithZValue(origin, end, this.maxZ);

        if (!isVecInYZ(minX)) {
            minX = null;
        }

        if (!isVecInYZ(maxX)) {
            maxX = null;
        }

        if (!isVecInXZ(minY)) {
            minY = null;
        }

        if (!isVecInXZ(maxY)) {
            maxY = null;
        }

        if (!isVecInXY(minZ)) {
            minZ = null;
        }

        if (!isVecInXY(maxZ)) {
            maxZ = null;
        }

        Vector best = null;

        if (minX != null) {
            best = minX;
        }

        if (maxX != null && (best == null || origin.distanceSquared(maxX) < origin.distanceSquared(best))) {
            best = maxX;
        }

        if (minY != null && (best == null || origin.distanceSquared(minY) < origin.distanceSquared(best))) {
            best = minY;
        }

        if (maxY != null && (best == null || origin.distanceSquared(maxY) < origin.distanceSquared(best))) {
            best = maxY;
        }

        if (minZ != null && (best == null || origin.distanceSquared(minZ) < origin.distanceSquared(best))) {
            best = minZ;
        }

        if (maxZ != null && (best == null || origin.distanceSquared(maxZ) < origin.distanceSquared(best))) {
            best = maxZ;
        }

        return best;
    }

    /**
     * Returns a new vector with x value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vector getIntermediateWithXValue(Vector self, Vector other, double x) {
        double d0 = other.getX() - self.getX();
        double d1 = other.getY() - self.getY();
        double d2 = other.getZ() - self.getZ();

        if (d0 * d0 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d3 = (x - self.getX()) / d0;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vector(self.getX() + d0 * d3, self.getY() + d1 * d3, self.getZ() + d2 * d3) : null;
        }
    }

    /**
     * Returns a new vector with y value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vector getIntermediateWithYValue(Vector self, Vector other, double y) {
        double d0 = other.getX() - self.getX();
        double d1 = other.getY() - self.getY();
        double d2 = other.getZ() - self.getZ();

        if (d1 * d1 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d3 = (y - self.getY()) / d1;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vector(self.getX() + d0 * d3, self.getY() + d1 * d3, self.getZ() + d2 * d3) : null;
        }
    }

    /**
     * Returns a new vector with z value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vector getIntermediateWithZValue(Vector self, Vector other, double z) {
        double d0 = other.getX() - self.getX();
        double d1 = other.getY() - self.getY();
        double d2 = other.getZ() - self.getZ();

        if (d2 * d2 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d3 = (z - self.getZ()) / d2;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vector(self.getX() + d0 * d3, self.getY() + d1 * d3, self.getZ() + d2 * d3) : null;
        }
    }

    private boolean isVecInYZ(Vector vec) {
        return vec != null && vec.getY() >= minY && vec.getY() <= maxY && vec.getZ() >= minZ && vec.getZ() <= maxZ;
    }

    private boolean isVecInXZ(Vector vec) {
        return vec != null && vec.getX() >= minX && vec.getX() <= maxX && vec.getZ() >= minZ && vec.getZ() <= maxZ;
    }

    private boolean isVecInXY(Vector vec) {
        return vec != null && vec.getX() >= minX && vec.getX() <= maxX && vec.getY() >= minY && vec.getY() <= maxY;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", minZ=" + minZ +
                ", maxZ=" + maxZ +
                '}';
    }
}
