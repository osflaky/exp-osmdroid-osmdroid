package org.osmdroid.util;

/**
 * @author Fabrice Fontaine
 * @since 6.0.2
 */
public class TileSystemWebMercator extends TileSystem {

    public static final double MinLatitude = -85.05112877980658;
    public static final double MaxLatitude = 85.05112877980658;
    public static final double MinLongitude = -180;
    public static final double MaxLongitude = 180;

    @Override
    public double getX01FromLongitude(final double pLongitude) {
        return (pLongitude - getMinLongitude()) / (getMaxLongitude() - getMinLongitude());
    }

    @Override
    public double getY01FromLatitude(final double pLatitude) {
        final double sinus = Math.sin(pLatitude * Math.PI / 180);
        final double result = 0.5 - Math.log((1 + sinus) / (1 - sinus)) / (4 * Math.PI);
        // Math.sin/Math.log may round differently depending on the CPU (e.g. aarch64 intrinsics),
        // which can push the bounds just outside [0,1]: keep valid latitudes within [0,1]
        return isValidLatitude(pLatitude) ? Clip(result, 0, 1) : result;
    }

    @Override
    public double getLongitudeFromX01(final double pX01) {
        return getMinLongitude() + (getMaxLongitude() - getMinLongitude()) * pX01;
    }

    @Override
    public double getLatitudeFromY01(final double pY01) {
        return 90 - 360 * Math.atan(Math.exp((pY01 - 0.5) * 2 * Math.PI)) / Math.PI;
    }

    @Override
    public double getMinLatitude() {
        return MinLatitude;
    }

    @Override
    public double getMaxLatitude() {
        return MaxLatitude;
    }

    @Override
    public double getMinLongitude() {
        return MinLongitude;
    }

    @Override
    public double getMaxLongitude() {
        return MaxLongitude;
    }
}
