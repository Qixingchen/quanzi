package com.tizi.quanzi.tool;

/**
 * Created by qixingchen on 15/7/20.
 * 开发调试开关
 */
public class Statue {
    public final static class IsDebug {
        public final static int debug = 0;
        public final static int common = 1;
        public final static int now = 0;
    }

    public final static class IsDev {
        public final static int dev = 0;
        public final static int common = 1;
        public final static int now = 0;
        public final static boolean isDev = true;
    }
}
