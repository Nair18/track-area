package com.apogee.trackarea.helpers.util;

/**
 * This class is a port of the ruby file which can be found here:
 * http://gmaps-gis.googlecode.com/svn/trunk/lib/latlongutm.rb
 *
 * @author jotschi
 */
public class GeoConvert {

    /* Ellipsoid model constants (actual values here are for WGS84) */
    static double sm_a = 6378137.0;
    static double sm_b = 6356752.314;

    static double UTMScaleFactor = 0.9996;

    /*
     * DegToRad
     *
     * Converts degrees to radians.
     */
    public static double DegToRad(double deg) {
        return (deg / 180.0 * Math.PI);
    }


    /*
     * ArcLengthOfMeridian
     *
     * Computes the ellipsoidal distance from the equator to a point at a given
     * latitude.
     *
     * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
     * GPS: Theory and Practice, 3rd ed. New York: Springer-Verlag Wien, 1994.
     *
     * Inputs: phi - Latitude of the point, in radians.
     *
     * Globals: sm_a - Ellipsoid model major axis. sm_b - Ellipsoid model minor
     * axis.
     *
     * Returns: The ellipsoidal distance of the point from the equator, in
     * meters.
     */
    public static double ArcLengthOfMeridian(double phi) {
        double alpha, beta, gamma, delta, epsilon, n;
        double result;

        /* Precalculate n */
        n = (sm_a - sm_b) / (sm_a + sm_b);

        /* Precalculate alpha */
        alpha = ((sm_a + sm_b) / 2.0)
                * (1.0 + (Math.pow(n, 2.0) / 4.0) + (Math.pow(n, 4.0) / 64.0));

        /* Precalculate beta */
        beta = (-3.0 * n / 2.0) + (9.0 * Math.pow(n, 3.0) / 16.0)
                + (-3.0 * Math.pow(n, 5.0) / 32.0);

        /* Precalculate gamma */
        gamma = (15.0 * Math.pow(n, 2.0) / 16.0)
                + (-15.0 * Math.pow(n, 4.0) / 32.0);

        /* Precalculate delta */
        delta = (-35.0 * Math.pow(n, 3.0) / 48.0)
                + (105.0 * Math.pow(n, 5.0) / 256.0);

        /* Precalculate epsilon */
        epsilon = (315.0 * Math.pow(n, 4.0) / 512.0);

        /* Now calculate the sum of the series and return */
        result = alpha
                * (phi + (beta * Math.sin(2.0 * phi))
                + (gamma * Math.sin(4.0 * phi))
                + (delta * Math.sin(6.0 * phi)) + (epsilon * Math
                .sin(8.0 * phi)));

        return result;
    }

    /*
     * UTMCentralMeridian
     *
     * Determines the central meridian for the given UTM zone.
     *
     * Inputs: zone - An integer value designating the UTM zone, range [1,60].
     *
     * Returns: The central meridian for the given UTM zone, in radians, or zero
     * if the UTM zone parameter is outside the range [1,60]. Range of the
     * central meridian is the radian equivalent of [-177,+177].
     */
    public static double UTMCentralMeridian(int zone) {
        double cmeridian;

        cmeridian = DegToRad(-183.0 + (zone * 6.0));

        return cmeridian;
    }

    /*
     * FootpointLatitude
     *
     * Computes the footpoint latitude for use in converting transverse Mercator
     * coordinates to ellipsoidal coordinates.
     *
     * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
     * GPS: Theory and Practice, 3rd ed. New York: Springer-Verlag Wien, 1994.
     *
     * Inputs: lon - The UTM northing coordinate, in meters.
     *
     * Returns: The footpoint latitude, in radians.
     */

    /*
     * MapLatLonToXY
     *
     * Converts a latitude/longitude pair to lat and lon coordinates in the
     * Transverse Mercator projection. Note that Transverse Mercator is not the
     * same as UTM; a scale factor is required to convert between them.
     *
     * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
     * GPS: Theory and Practice, 3rd ed. New York: Springer-Verlag Wien, 1994.
     *
     * Inputs: phi - Latitude of the point, in radians. lambda - Longitude of
     * the point, in radians. lambda0 - Longitude of the central meridian to be
     * used, in radians.
     *
     * Outputs: xy - A 2-element array containing the lat and lon coordinates of the
     * computed point.
     *
     * Returns: The function does not return a value.
     */
    public static void MapLatLonToXY(double phi, double lambda, double lambda0,
                                     double[] xy) {
        double N, nu2, ep2, t, t2, l;
        double l3coef, l4coef, l5coef, l6coef, l7coef, l8coef;
        //double tmp;

        /* Precalculate ep2 */
        ep2 = (Math.pow(sm_a, 2.0) - Math.pow(sm_b, 2.0)) / Math.pow(sm_b, 2.0);

        /* Precalculate nu2 */
        nu2 = ep2 * Math.pow(Math.cos(phi), 2.0);

        /* Precalculate N */
        N = Math.pow(sm_a, 2.0) / (sm_b * Math.sqrt(1 + nu2));

        /* Precalculate t */
        t = Math.tan(phi);
        t2 = t * t;
        //tmp = (t2 * t2 * t2) - Math.pow(t, 6.0);

        /* Precalculate l */
        l = lambda - lambda0;

        /*
         * Precalculate coefficients for ln in the equations below so a normal
         * human being can read the expressions for easting and northing -- l1
         * and l2 have coefficients of 1.0
         */
        l3coef = 1.0 - t2 + nu2;

        l4coef = 5.0 - t2 + 9 * nu2 + 4.0 * (nu2 * nu2);

        l5coef = 5.0 - 18.0 * t2 + (t2 * t2) + 14.0 * nu2 - 58.0 * t2 * nu2;

        l6coef = 61.0 - 58.0 * t2 + (t2 * t2) + 270.0 * nu2 - 330.0 * t2 * nu2;

        l7coef = 61.0 - 479.0 * t2 + 179.0 * (t2 * t2) - (t2 * t2 * t2);

        l8coef = 1385.0 - 3111.0 * t2 + 543.0 * (t2 * t2) - (t2 * t2 * t2);

        /* Calculate easting (lat) */
        xy[0] = N
                * Math.cos(phi)
                * l
                + (N / 6.0 * Math.pow(Math.cos(phi), 3.0) * l3coef * Math.pow(
                l, 3.0))
                + (N / 120.0 * Math.pow(Math.cos(phi), 5.0) * l5coef * Math
                .pow(l, 5.0))
                + (N / 5040.0 * Math.pow(Math.cos(phi), 7.0) * l7coef * Math
                .pow(l, 7.0));

        /* Calculate northing (lon) */
        xy[1] = ArcLengthOfMeridian(phi)
                + (t / 2.0 * N * Math.pow(Math.cos(phi), 2.0) * Math
                .pow(l, 2.0))
                + (t / 24.0 * N * Math.pow(Math.cos(phi), 4.0) * l4coef * Math
                .pow(l, 4.0))
                + (t / 720.0 * N * Math.pow(Math.cos(phi), 6.0) * l6coef * Math
                .pow(l, 6.0))
                + (t / 40320.0 * N * Math.pow(Math.cos(phi), 8.0) * l8coef * Math
                .pow(l, 8.0));

        return;
    }

    /*
     * MapXYToLatLon
     *
     * Converts lat and lon coordinates in the Transverse Mercator projection to a
     * latitude/longitude pair. Note that Transverse Mercator is not the same as
     * UTM; a scale factor is required to convert between them.
     *
     * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
     * GPS: Theory and Practice, 3rd ed. New York: Springer-Verlag Wien, 1994.
     *
     * Inputs: lat - The easting of the point, in meters. lon - The northing of the
     * point, in meters. lambda0 - Longitude of the central meridian to be used,
     * in radians.
     *
     * Outputs: philambda - A 2-element containing the latitude and longitude in
     * radians.
     *
     * Returns: The function does not return a value.
     *
     * Remarks: The local variables Nf, nuf2, tf, and tf2 serve the same purpose
     * as N, nu2, t, and t2 in MapLatLonToXY, but they are computed with respect
     * to the footpoint latitude phif.
     *
     * x1frac, x2frac, x2poly, x3poly, etc. are to enhance readability and to
     * optimize computations.
     */

    /*
     * LatLonToUTMXY
     *
     * Converts a latitude/longitude pair to lat and lon coordinates in the
     * Universal Transverse Mercator projection.
     *
     * Inputs: lat - Latitude of the point, in radians. lon - Longitude of the
     * point, in radians. zone - UTM zone to be used for calculating values for
     * lat and lon. If zone is less than 1 or greater than 60, the routine will
     * determine the appropriate zone from the value of lon.
     *
     * Outputs: xy - A 2-element array where the UTM lat and lon values will be
     * stored.
     *
     * Returns: The UTM zone used for calculating the values of lat and lon.
     */
    public static int LatLonToUTMXY(double lat, double lon, int zone,
                                    double[] xy) {
        MapLatLonToXY(lat, lon, UTMCentralMeridian(zone), xy);

        /* Adjust easting and northing for UTM system. */
        xy[0] = xy[0] * UTMScaleFactor + 500000.0;
        xy[1] = xy[1] * UTMScaleFactor;
        if (xy[1] < 0.0)
            xy[1] = xy[1] + 10000000.0;

        return zone;
    }



    public static double[] toUtm(double lon, double lat) throws Exception {
        double[] xy = {0, 0};

        if ((lon < -180.0) || (180.0 <= lon)) {
            throw new Exception("The longitude you entered is out of range.  "
                    + "Please enter a number in the range [-180, 180).");

        }

        if ((lat < -90.0) || (90.0 < lat)) {
            throw new Exception("The latitude you entered is out of range.  "
                    + "Please enter a number in the range [-90, 90].");

        }

        // Compute the UTM zone.
        int zone = (int) Math.floor((lon + 180.0) / 6) + 1;
        LatLonToUTMXY(DegToRad(lat), DegToRad(lon), zone, xy);
        return xy;
    }


}