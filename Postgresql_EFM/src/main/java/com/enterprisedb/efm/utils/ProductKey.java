package com.enterprisedb.efm.utils;

import java.util.Date;

public final class ProductKey {
    public static final int LICENSE_FULL = 0;

    public static final int LICENSE_EXPRESS = 1;

    public static final int LICENSE_TRIAL = 2;

    private static final int BITS_PER_BYTE = 8;

    private static final int BASE32_BITS_PER_CHARACTER = 5;

    private static final int ENCODED_KEY_VERSION_BITS = 4;

    private static final int ENCODED_DATE_BITS = 12;

    private static final int ENCODED_LICENSE_TYPE_BITS = 2;

    private static final int ENCODED_PRODUCT_TYPE_BITS = 5;

    private static final int ENCODED_CUSTID_BITS = 48;

    private static final int ENCODED_PAD_BITS = 22;

    private static final int ENCODED_CHECKSUM_BITS = 32;

    private static final int ENCODED_BITS_TOTAL = 125;

    private static final int ENCODED_FORM_BYTES = 16;

    private static final int ENCODED_KEY_VERSION_OFF = 0;

    private static final int ENCODED_DATE_OFF = 4;

    private static final int ENCODED_LICENSE_TYPE_OFF = 16;

    private static final int ENCODED_PRODUCT_TYPE_OFF = 18;

    private static final int ENCODED_CUSTID_OFF = 23;

    private static final int ENCODED_PAD_OFF = 71;

    private static final int ENCODED_CHECKSUM_OFF = 93;

    private static final int PRODUCT_KEY_STRINGFORM_LENGTH = 29;

    private static final int LICENSE_EPOCH_SECONDS = 1167627600;

    private static final int SECONDS_PER_DAY = 86400;

    private static final int MILLISECONDS_PER_SECOND = 1000;

    public static String productKeyValidate(String stringForm) {
        if (stringForm.length() != 29)
            return "Invalid product key - " + stringForm;
        byte[] packedBits = new byte[16];
        try {
            base32Decode(packedBits, stringForm.getBytes("UTF-8"));
        } catch (Exception e) {
            return e.getMessage();
        }
        int expected = decodeBits(packedBits, 93, 32);
        encodeBits(packedBits, 93, 32, 0);
        int computed = computeCRC32(packedBits);
        if (computed != expected)
            return "Invalid checksum";
        return null;
    }

    public static Date productKeyExpirationDate(String stringForm) {
        byte[] packedBits = new byte[16];
        try {
            base32Decode(packedBits, stringForm.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
        int epoch = decodeBits(packedBits, 4, 12);
        if (epoch == 0)
            return null;
        long expirationDateInSeconds = (1167627600 + epoch * 86400);
        return new Date(expirationDateInSeconds * 1000L);
    }

    public static int productKeyLicenseType(String stringForm) {
        byte[] packedBits = new byte[16];
        try {
            base32Decode(packedBits, stringForm.getBytes("UTF-8"));
        } catch (Exception e) {
            return 1;
        }
        return decodeBits(packedBits, 16, 2);
    }

    private static int computeCRC32(byte[] buf) {
        int crc = -1;
        int len = buf.length;
        for (int i = 0; i < len; i++) {
            int index = (crc >> 24 ^ buf[i]) & 0xFF;
            crc = CRC_32_TABLE[index] ^ crc << 8;
        }
        return crc ^ 0xFFFFFFFF;
    }

    private static int decodeBits(byte[] buf, int bitOffset, int bitCount) {
        int result = 0;
        for (int i = bitCount - 1; i >= 0; i--)
            result += getBit(buf, bitOffset + i) << i;
        return result;
    }

    private static void encodeBits(byte[] buf, int bitOffset, int bitCount, int value) {
        for (int i = bitCount - 1; i >= 0; i--)
            setBit(buf, bitOffset + i, ((1 << i & value) == 0) ? 0 : 1);
    }

    private static int getBit(byte[] buf, int bitOffset) {
        int byteOffset = bitOffset / 8;
        int shiftCount = bitOffset % 8;
        if ((buf[byteOffset] & 1 << shiftCount) == 0)
            return 0;
        return 1;
    }

    private static void setBit(byte[] dst, int bitOffset, int newBit) {
        int byteOffset = bitOffset / 8;
        int shiftCount = bitOffset % 8;
        byte mask = (byte)(1 << shiftCount ^ 0xFF);
        byte shifted = (byte)(newBit << shiftCount);
        dst[byteOffset] = (byte)(dst[byteOffset] & mask | shifted);
    }

    private static void base32Decode(byte[] dst, byte[] src) throws Exception {
        int srcLen = src.length;
        int off = 0;
        for (int i = 0; i < srcLen; i++) {
            if (src[i] != 45) {
                byte bits = base32DecodeChar(src[i]);
                encodeBits(dst, off, 5, bits);
                off += 5;
            }
        }
        if (off % 8 != 0)
            encodeBits(dst, off, 8 - off % 8, 0);
    }

    private static byte base32DecodeChar(byte in) throws Exception {
        switch (in) {
            case 48:
                return 0;
            case 49:
                return 1;
            case 50:
                return 2;
            case 51:
                return 3;
            case 52:
                return 4;
            case 53:
                return 5;
            case 54:
                return 6;
            case 55:
                return 7;
            case 56:
                return 8;
            case 57:
                return 9;
            case 66:
                return 10;
            case 67:
                return 11;
            case 68:
                return 12;
            case 70:
                return 13;
            case 71:
                return 14;
            case 72:
                return 15;
            case 74:
                return 16;
            case 75:
                return 17;
            case 76:
                return 18;
            case 77:
                return 19;
            case 78:
                return 20;
            case 80:
                return 21;
            case 81:
                return 22;
            case 82:
                return 23;
            case 83:
                return 24;
            case 84:
                return 25;
            case 86:
                return 26;
            case 87:
                return 27;
            case 88:
                return 28;
            case 89:
                return 29;
            case 90:
                return 30;
            case 64:
                return 31;
        }
        throw new Exception("Invalid character in product key");
    }

    private static final int[] CRC_32_TABLE = new int[] {
            0, 1996959894, -301047508, -1727442502, 124634137, 1886057615, -379345611, -1637575261, 249268274, 2044508324,
            -522852066, -1747789432, 162941995, 2125561021, -407360249, -1866523247, 498536548, 1789927666, -205950648, -2067906082,
            450548861, 1843258603, -187386543, -2083289657, 325883990, 1684777152, -43845254, -1973040660, 335633487, 1661365465,
            -99664541, -1928851979, 997073096, 1281953886, -715111964, -1570279054, 1006888145, 1258607687, -770865667, -1526024853,
            901097722, 1119000684, -608450090, -1396901568, 853044451, 1172266101, -589951537, -1412350631, 651767980, 1373503546,
            -925412992, -1076862698, 565507253, 1454621731, -809855591, -1195530993, 671266974, 1594198024, -972236366, -1324619484,
            795835527, 1483230225, -1050600021, -1234817731, 1994146192, 31158534, -1731059524, -271249366, 1907459465, 112637215,
            -1614814043, -390540237, 2013776290, 251722036, -1777751922, -519137256, 2137656763, 141376813, -1855689577, -429695999,
            1802195444, 476864866, -2056965928, -228458418, 1812370925, 453092731, -2113342271, -183516073, 1706088902, 314042704,
            -1950435094, -54949764, 1658658271, 366619977, -1932296973, -69972891, 1303535960, 984961486, -1547960204, -725929758,
            1256170817, 1037604311, -1529756563, -740887301, 1131014506, 879679996, -1385723834, -631195440, 1141124467, 855842277,
            -1442165665, -586318647, 1342533948, 654459306, -1106571248, -921952122, 1466479909, 544179635, -1184443383, -832445281,
            1591671054, 702138776, -1328506846, -942167884, 1504918807, 783551873, -1212326853, -1061524307, -306674912, -1698712650,
            62317068, 1957810842, -355121351, -1647151185, 81470997, 1943803523, -480048366, -1805370492, 225274430, 2053790376,
            -468791541, -1828061283, 167816743, 2097651377, -267414716, -2029476910, 503444072, 1762050814, -144550051, -2140837941,
            426522225, 1852507879, -19653770, -1982649376, 282753626, 1742555852, -105259153, -1900089351, 397917763, 1622183637,
            -690576408, -1580100738, 953729732, 1340076626, -776247311, -1497606297, 1068828381, 1219638859, -670225446, -1358292148,
            906185462, 1090812512, -547295293, -1469587627, 829329135, 1181335161, -882789492, -1134132454, 628085408, 1382605366,
            -871598187, -1156888829, 570562233, 1426400815, -977650754, -1296233688, 733239954, 1555261956, -1026031705, -1244606671,
            752459403, 1541320221, -1687895376, -328994266, 1969922972, 40735498, -1677130071, -351390145, 1913087877, 83908371,
            -1782625662, -491226604, 2075208622, 213261112, -1831694693, -438977011, 2094854071, 198958881, -2032938284, -237706686,
            1759359992, 534414190, -2118248755, -155638181, 1873836001, 414664567, -2012718362, -15766928, 1711684554, 285281116,
            -1889165569, -127750551, 1634467795, 376229701, -1609899400, -686959890, 1308918612, 956543938, -1486412191, -799009033,
            1231636301, 1047427035, -1362007478, -640263460, 1088359270, 936918000, -1447252397, -558129467, 1202900863, 817233897,
            -1111625188, -893730166, 1404277552, 615818150, -1160759803, -841546093, 1423857449, 601450431, -1285129682, -1000256840,
            1567103746, 711928724, -1274298825, -1022587231, 1510334235, 755167117 };
}
