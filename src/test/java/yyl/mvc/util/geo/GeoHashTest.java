package yyl.mvc.util.geo;

public class GeoHashTest {

    public static void main(String[] args) {
        Location location = GeoHashUtil.decode("9rj2gn5chwnr");
        System.out.println(location);
        String hash = GeoHashUtil.encode(new Location(39.54, -116.23));
        System.out.println(hash);
    }
}
