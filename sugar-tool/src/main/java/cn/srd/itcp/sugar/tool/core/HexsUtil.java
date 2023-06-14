package cn.srd.itcp.sugar.tool.core;

import cn.hutool.core.util.HexUtil;
import cn.srd.itcp.sugar.tool.core.object.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 十六进制工具
 *
 * @author wjm
 * @since 2022-07-06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HexsUtil extends HexUtil {

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data      Byte[]
     * @param needLower 是否需要转换为小写格式
     * @return 十六进制字符串
     */
    public static String hexToString(Byte[] data, boolean needLower) {
        return Objects.isNotNull((Object[]) data) ? hexToString(ArraysUtil.unWrap(data), needLower) : null;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data      byte[]
     * @param needLower 是否需要转换为小写格式
     * @return 十六进制字符串
     */
    public static String hexToString(byte[] data, boolean needLower) {
        return encodeHexStr(data, needLower);
    }

}